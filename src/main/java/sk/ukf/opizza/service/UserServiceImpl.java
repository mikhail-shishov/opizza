package sk.ukf.opizza.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import sk.ukf.opizza.dao.UserRepository;
import sk.ukf.opizza.entity.User;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public User saveUser(User incoming) {
        // new registration
        if (incoming.getId() == 0) {
            // check if email exists
            if (userRepository.existsByEmail(incoming.getEmail())) {
                throw new IllegalArgumentException("Používateľ s týmto emailom už existuje.");
            }

            incoming.setPassword(passwordEncoder.encode(incoming.getPassword()));

            // set default role
            if (incoming.getRole() == null) {
                incoming.setRole("GUEST");
            }

            incoming.setActive(true);
            return userRepository.save(incoming);
        }

        //update of user
        User existing = userRepository.findById(incoming.getId()).orElseThrow(() -> new RuntimeException("Používateľ nebol nájdený"));

        existing.setFirstName(incoming.getFirstName());
        existing.setLastName(incoming.getLastName());
        existing.setPhone(incoming.getPhone());
        existing.setAvatarUrl(incoming.getAvatarUrl());

        // if default address is changed
        if (incoming.getDefaultAddress() != null) {
            existing.setDefaultAddress(incoming.getDefaultAddress());
        }

        return userRepository.save(existing);
    }

    @Override
    public User getUserById(int id) {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("Používateľ nebol nájdený"));
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Používateľ s emailom " + email + " neexistuje"));
    }
}