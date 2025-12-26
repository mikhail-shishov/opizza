package sk.ukf.opizza.service;

import sk.ukf.opizza.entity.User;

public interface UserService {
    User saveUser(User user);
    User getUserById(int id);
    User getUserByEmail(String email);
    void resetPassword(String email, String newPassword);
    void createPasswordResetToken(String email);
    void updatePasswordByToken(String token, String newPassword);
}

