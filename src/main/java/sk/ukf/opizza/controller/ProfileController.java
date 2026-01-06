package sk.ukf.opizza.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import sk.ukf.opizza.config.UserPrincipal;
import sk.ukf.opizza.entity.Address;
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
        return "profile/index";
    }

    @PostMapping("/update")
    public String updateProfile(@ModelAttribute("user") User formUser, @RequestParam(value = "avatarFile", required = false) org.springframework.web.multipart.MultipartFile avatarFile, @AuthenticationPrincipal UserPrincipal principal) {
        User existingUser = userService.getUserById(principal.getUser().getId());

        if (avatarFile != null && !avatarFile.isEmpty()) {
            try {
                String fileName = "user_" + existingUser.getId() + "_" + avatarFile.getOriginalFilename();
                String uploadDir = "src/main/resources/static/uploads/avatars/";
                java.nio.file.Path path = java.nio.file.Paths.get(uploadDir + fileName);

                java.nio.file.Files.createDirectories(path.getParent());
                java.nio.file.Files.write(path, avatarFile.getBytes());

                existingUser.setAvatarUrl("/uploads/avatars/" + fileName);
            } catch (java.io.IOException e) {
                e.printStackTrace();
            }
        }

        existingUser.setFirstName(formUser.getFirstName());
        existingUser.setLastName(formUser.getLastName());
        existingUser.setPhone(formUser.getPhone());

        if (formUser.getPassword() != null && !formUser.getPassword().trim().isEmpty()) {
            existingUser.setPassword(formUser.getPassword());
        }

        userService.saveUser(existingUser);
        return "redirect:/profile?success";
    }

    @PostMapping("/set-default-address")
    public String setDefaultAddress(@RequestParam("addressId") int addressId, @AuthenticationPrincipal UserPrincipal principal) {
        User user = userService.getUserById(principal.getUser().getId());

        Address selectedAddress = user.getAllAddresses().stream().filter(a -> a.getId() == addressId).findFirst().orElseThrow(() -> new RuntimeException("Adresa nebola nájdená"));

        user.setDefaultAddress(selectedAddress);

        userService.saveUser(user);

        return "redirect:/profile?success";
    }

    @GetMapping("/add-address")
    public String showAddAddressForm(Model model) {
        model.addAttribute("address", new Address());
        return "profile/add-address";
    }

    @PostMapping("/add-address")
    public String saveAddress(@ModelAttribute("address") Address address, @AuthenticationPrincipal UserPrincipal principal) {
        User user = userService.getUserById(principal.getUser().getId());

        address.setUser(user);

        if (user.getDefaultAddress() == null) {
            address.setDefault(true);
            user.setDefaultAddress(address);
        }

        user.getAllAddresses().add(address);
        userService.saveUser(user);

        return "redirect:/profile?addressAdded";
    }
}