package sk.ukf.opizza.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import sk.ukf.opizza.service.UserService;

@Controller
@RequestMapping("/admin/users")
public class AdminUserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public String listUsers(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "admin/users-list";
    }

    @PostMapping("/update-role")
    public String updateRole(@RequestParam int userId, @RequestParam String role) {
        userService.updateUserRole(userId, role);
        return "redirect:/admin/users";
    }

    @PostMapping("/delete/{id}")
    public String deleteUser(@PathVariable int id) {
        userService.softDeleteUser(id);
        return "redirect:/admin/users";
    }
}