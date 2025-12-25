package sk.ukf.opizza.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import sk.ukf.opizza.dao.UserRepository;
import sk.ukf.opizza.entity.User;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository; // access to db via JPA

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // looking for user via email
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Používateľ s emailom " + email + " nebol nájdený"));

        return new UserPrincipal(user);
    }
}