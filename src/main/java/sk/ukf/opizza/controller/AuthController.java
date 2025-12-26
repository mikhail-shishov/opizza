package sk.ukf.opizza.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import sk.ukf.opizza.entity.User;
import sk.ukf.opizza.service.UserService;
import sk.ukf.opizza.service.EmailService;

@Controller
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
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
    public String createUser(@ModelAttribute("user") User user, BindingResult bindingResult) {
        try {
            userService.saveUser(user);

            emailService.sendEmail(user.getEmail(), "Vitajte v Opizza!", "Dobrý deň " + user.getFirstName() + ",\n\n" + "Vaša registrácia v aplikácii Opizza prebehla úspešne. " + "Teraz sa môžete prihlásiť a objednať si pizzu.\n\n" + "Tím Opizza");

            return "redirect:/auth/login?register";
        } catch (IllegalArgumentException e) {
            bindingResult.rejectValue("email", "email.exists", e.getMessage());
            return "auth/register";
        }
    }

    @GetMapping("/error")
    public String showErrorPage() {
        return "error";
    }
}