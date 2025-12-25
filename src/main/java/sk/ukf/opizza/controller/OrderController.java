package sk.ukf.opizza.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import sk.ukf.opizza.config.UserPrincipal;
import sk.ukf.opizza.entity.Order;
import sk.ukf.opizza.entity.User;
import sk.ukf.opizza.service.AddressService;
import sk.ukf.opizza.service.OrderService;

import java.util.List;

@Controller
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;
    private final AddressService addressService;

    @Autowired
    public OrderController(OrderService orderService, AddressService addressService) {
        this.orderService = orderService;
        this.addressService = addressService;
    }

    // create order
    @PostMapping("/create")
    public String createOrder(@RequestParam("addressId") int addressId, @RequestParam("note") String note, Authentication authentication) {
        User user = ((UserPrincipal) authentication.getPrincipal()).getUser();
        orderService.createOrder(user, note, addressId);
        return "redirect:/orders/history?success";
    }

    // GET checkout
    @GetMapping("/checkout")
    public String checkout(Model model, Authentication authentication) {
        User user = ((UserPrincipal) authentication.getPrincipal()).getUser();
        model.addAttribute("addresses", addressService.getAddressesByUser(user));
        return "order/checkout";
    }

    // history
    @GetMapping("/history")
    public String viewOrderHistory(Model model, Authentication authentication) {
        User user = ((UserPrincipal) authentication.getPrincipal()).getUser();
        List<Order> history = orderService.getUserOrderHistory(user);
        model.addAttribute("orders", history);
        return "order/history";
    }
}