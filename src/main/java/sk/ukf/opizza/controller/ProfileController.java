package sk.ukf.opizza.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import sk.ukf.opizza.config.UserPrincipal;
import sk.ukf.opizza.entity.User;
import sk.ukf.opizza.service.UserService;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    private final UserService userService;

    @Autowired
    public ProfileController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String showProfile(@AuthenticationPrincipal UserPrincipal principal, Model model) {
        User currentUser = userService.getUserById(principal.getUser().getId());
        model.addAttribute("user", currentUser);
        return "profile";
    }

    @PostMapping("/update")
    public String updateProfile(@ModelAttribute("user") User updatedUser, @AuthenticationPrincipal UserPrincipal principal) {
        updatedUser.setId(principal.getUser().getId());

        userService.saveUser(updatedUser);

        return "redirect:/profile?success";
    }
}