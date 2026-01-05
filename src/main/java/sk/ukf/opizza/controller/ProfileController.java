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
    public String updateProfile(@ModelAttribute("user") User updatedUser, @AuthenticationPrincipal UserPrincipal principal) {
        updatedUser.setId(principal.getUser().getId());

        userService.saveUser(updatedUser);

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