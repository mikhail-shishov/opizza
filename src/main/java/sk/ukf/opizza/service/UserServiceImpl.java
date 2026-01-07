package sk.ukf.opizza.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import sk.ukf.opizza.config.ValidationConfig;
import sk.ukf.opizza.dao.UserRepository;
import sk.ukf.opizza.entity.User;
import sk.ukf.opizza.service.EmailService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, EmailService emailService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    @Override
    @Transactional
    public User saveUser(User incoming) {
        // new registration
        if (incoming.getId() == 0) {
            if (userRepository.existsByEmail(incoming.getEmail())) {
                throw new IllegalArgumentException("Používateľ s týmto emailom už existuje.");
            }

            if (!ValidationConfig.isValidPassword(incoming.getPassword())) {
                throw new IllegalArgumentException("Heslo musí mať aspoň 8 znakov, jedno veľké písmeno a jedno číslo");
            }

            incoming.setPassword(passwordEncoder.encode(incoming.getPassword()));

            if (incoming.getRole() == null) incoming.setRole("GUEST");
            incoming.setActive(true);
            return userRepository.save(incoming);
        }

        User existing = userRepository.findById(incoming.getId()).orElseThrow(() -> new RuntimeException("Používateľ nebol nájdený"));

        existing.setFirstName(incoming.getFirstName());
        existing.setLastName(incoming.getLastName());
        existing.setPhone(incoming.getPhone());
        existing.setAvatarUrl(incoming.getAvatarUrl());

        if (incoming.getPassword() != null && !incoming.getPassword().isEmpty() && !passwordEncoder.matches(incoming.getPassword(), existing.getPassword())) {
            if (!ValidationConfig.isValidPassword(incoming.getPassword())) {
                throw new IllegalArgumentException("Nové heslo nespĺňa požiadavky.");
            }
            existing.setPassword(passwordEncoder.encode(incoming.getPassword()));
        }

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
        return userRepository.findByEmail(email.trim()).orElseThrow(() -> new RuntimeException("Používateľ s emailom " + email + " neexistuje"));
    }

    @Override
    @Transactional
    public void resetPassword(String email, String newPassword) {
        User user = userRepository.findByEmail(email.trim()).orElseThrow(() -> new RuntimeException("Používateľ nebol nájdený"));

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void createPasswordResetToken(String email) {
        User user = userRepository.findByEmail(email.trim()).orElseThrow(() -> new RuntimeException("Používateľ neexistuje"));

        String token = UUID.randomUUID().toString();
        user.setResetToken(token);
        user.setTokenExpiry(LocalDateTime.now().plusMinutes(10));
        userRepository.save(user);

        String link = "http://localhost:8080/auth/reset-password?token=" + token;

        emailService.sendEmail(email, "Reset hesla - Opizza", "Kliknite na nasledujúci odkaz pre zmenu hesla: " + link);
    }

    @Override
    @Transactional
    public void updatePasswordByToken(String token, String newPassword) {
        User user = userRepository.findByResetToken(token).orElseThrow(() -> new RuntimeException("Neplatný alebo expirovaný odkaz"));

        if (user.getTokenExpiry().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Odkaz vypršal, požiadajte o nový");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setResetToken(null);
        user.setTokenExpiry(null);
        userRepository.save(user);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    @Transactional
    public void updateUserRole(int userId, String role) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("Používateľ nebol nájdený"));
        user.setRole(role.toUpperCase());
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void softDeleteUser(int userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("Používateľ nebol nájdený"));
        user.setActive(false);
        userRepository.save(user);
    }
}