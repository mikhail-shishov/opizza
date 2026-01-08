package sk.ukf.opizza.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

    @GetMapping("/api/notifications/count")
    @ResponseBody
    public ResponseEntity<Integer> getOrderNotificationCount(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.ok(0);
        }

        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        String role = principal.getUser().getRole();

        int count = 0;
        if ("ADMIN".equalsIgnoreCase(role) || "COOK".equalsIgnoreCase(role)) {
            count = orderService.countByStatus("PENDING");
        } else if ("COURIER".equalsIgnoreCase(role)) {
            count = orderService.countByStatus("READY");
        }

        return ResponseEntity.ok(count);
    }

    @PostMapping("/cancel")
    public String cancelOrder(@RequestParam("orderId") int orderId, Authentication authentication) {
        User user = ((UserPrincipal) authentication.getPrincipal()).getUser();
        Order order = orderService.getAllOrders().stream().filter(o -> o.getOrderId() == orderId).findFirst().orElseThrow(() -> new RuntimeException("Objednávka sa nenašla"));

        if (order.getUser().getId() == user.getId() && "PENDING".equals(order.getStatus())) {
            orderService.updateStatus(orderId, "CANCELED");
            return "redirect:/orders/history?canceled";
        }

        return "redirect:/orders/history?error";
    }
}