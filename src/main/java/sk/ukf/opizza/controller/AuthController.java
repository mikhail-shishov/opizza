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

@Controller
@RequestMapping("/auth") // Všetky URL v tomto kontroléri začínajú na /auth
public class AuthController {

    private final UserService userService;

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    // Zobrazí prihlasovaciu stránku (Thymeleaf šablóna: src/main/resources/templates/auth/login.html)
    @GetMapping("/login")
    public String showLoginPage() {
        return "auth/login";
    }

    // Pripraví prázdny objekt User a zobrazí registračný formulár
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User()); // Pridanie objektu do modelu pre Thymeleaf (th:object)
        return "auth/register";
    }

    // Spracuje dáta z registračného formulára
    @PostMapping("/register")
    public String createUser(@ModelAttribute("user") User user, BindingResult bindingResult) {
        try {
            user.setId(0); // Zabezpečenie, že sa vytvorí nový záznam (id=0 vynúti INSERT v Hibernate)
            userService.saveUser(user);
            return "redirect:/auth/login?register"; // Presmerovanie po úspechu s parametrom v URL
        } catch (IllegalArgumentException e) {
            // Ak email už existuje, vrátime chybu priamo k poľu "email" vo formulári
            bindingResult.rejectValue("email", "email.exists", e.getMessage());
            return "auth/register";
        }
    }

    // Zobrazenie všeobecnej chybovej stránky
    @GetMapping("/error")
    public String showErrorPage() {
        return "error";
    }
}