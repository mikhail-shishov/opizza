package sk.ukf.opizza.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private UserDetailsServiceImpl userDetailsService; // getting users from db

    // hashing
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        // not logged in
                        .requestMatchers(
                                "/",
                                "/auth/login",
                                "/auth/register",
                                "/item/**",
                                "/error",
                                "/auth/forgot-password",
                                "/auth/reset-password"
                        ).permitAll()
                        // excluding technical files
                        .requestMatchers("/css/**", "/js/**", "/img/**", "/webjars/**").permitAll()

                        .requestMatchers("/error").permitAll()

                        // safety for admin panel
                        .requestMatchers("/admin/**").hasAnyAuthority("ROLE_admin")

                        // other roles
                        // .requestMatchers("/employer/**").hasAnyAuthority("ROLE_cook")

                        // other requests
                        .anyRequest().authenticated())
                // login form config
                .formLogin(formLogin -> formLogin.loginPage("/auth/login") // thymeleaf template here
                        .loginProcessingUrl("/log-in-post") // send POST data here!
                        .defaultSuccessUrl("/", true) // when login success
                        .failureUrl("/auth/login?error=true") // when login with error
                        .permitAll())
                // logout config
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/auth/login?logout=true")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll())
                // error check
                .exceptionHandling(exceptionHandling -> exceptionHandling.accessDeniedPage("/auth/error") // 403
                        .authenticationEntryPoint((request, response, authException) -> {
                            // redirect, if user is not logged in
                            response.sendRedirect("/auth/login");
                        }));

        return http.build();
    }

    // AuthenticationManager helps control identity
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
        return authenticationManagerBuilder.build();
    }
}