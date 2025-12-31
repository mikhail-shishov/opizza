package sk.ukf.opizza.config;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import sk.ukf.opizza.entity.User;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

// logged in user, in session
public class UserPrincipal implements UserDetails {

    private final User user;

    public UserPrincipal(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String dbRole = user.getRole().toLowerCase();
        return List.of(new SimpleGrantedAuthority("ROLE_" + dbRole));
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail(); // username = email in this project
    }

    // set true to make account active
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    // TODO optional authentication
    public String getFirstName() {
        return user.getFirstName();
    }

    public String getLastName() {
        return user.getLastName();
    }

    // first letter of name and surname in circle
    public String getInitials() {
        String firstName = user.getFirstName();
        String lastName = user.getLastName();
        return (firstName.substring(0, 1) + lastName.substring(0, 1)).toUpperCase();
    }

    public String getEmail() {
        return user.getEmail();
    }

    // get user entity
    public User getUser() {
        return user;
    }
}