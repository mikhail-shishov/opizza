package sk.ukf.opizza.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import sk.ukf.opizza.entity.User;
import sk.ukf.opizza.service.UserService;
import sk.ukf.opizza.service.EmailService;

@Controller
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;

    private final EmailService emailService;

    @Autowired
    public AuthController(UserService userService, EmailService emailService) {
        this.userService = userService;
        this.emailService = emailService;
    }

    @GetMapping("/login")
    public String showLoginPage() {
        return "auth/login";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "auth/register";
    }

    @PostMapping("/register")
    public String createUser(@ModelAttribute("user") User user, BindingResult bindingResult, jakarta.servlet.http.HttpServletRequest request) {
        try {
            String plainPassword = user.getPassword();
            userService.saveUser(user);

            try {
                emailService.sendEmail(user.getEmail(), "Vitajte v Opizza!", "Dobrý deň " + user.getFirstName() + ",\n\n" + "Vaša registrácia v aplikácii Opizza prebehla úspešne. " + "Teraz sa môžete prihlásiť a objednať si pizzu.\n\n" + "Tím Opizza");
            } catch(Exception e) {
                System.err.println("Chyba na strane email-servisu " + e.getMessage());
            }


            request.login(user.getEmail(), plainPassword);

            return "redirect:/";
        } catch (Exception e) {
            if (e instanceof IllegalArgumentException) {
                bindingResult.rejectValue("email", "email.exists", e.getMessage());
            }
            return "auth/register";
        }
    }

    @GetMapping("/error")
    public String showErrorPage() {
        return "error";
    }

    @GetMapping("/forgot-password")
    public String showForgotPasswordForm() {
        return "auth/forgot-password";
    }

    @PostMapping("/forgot-password")
    public String processForgotPassword(@RequestParam("email") String email) {
        try {
            String cleanEmail = email.trim();
            userService.createPasswordResetToken(email);
            return "redirect:/auth/login?sent";
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/auth/forgot-password?error";
        }
    }

    @GetMapping("/reset-password")
    public String showResetPasswordForm(@RequestParam("token") String token, Model model) {
        model.addAttribute("token", token);
        return "auth/reset-password";
    }

    @PostMapping("/reset-password")
    public String handleResetPassword(@RequestParam("token") String token, @RequestParam("newPassword") String newPassword) {
        try {
            userService.updatePasswordByToken(token, newPassword);
            return "redirect:/auth/login?resetSuccess";
        } catch (Exception e) {
            return "redirect:/error?msg=NeplatnyToken";
        }
    }
}