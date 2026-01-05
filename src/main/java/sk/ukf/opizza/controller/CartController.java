package sk.ukf.opizza.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import sk.ukf.opizza.config.UserPrincipal;
import sk.ukf.opizza.entity.Cart;
import sk.ukf.opizza.entity.User;
import sk.ukf.opizza.service.CartService;

@Controller
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;

    @Autowired
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    // show cart content
    @GetMapping
    public String viewCart(Model model, Authentication authentication) {
        User user = ((UserPrincipal) authentication.getPrincipal()).getUser();
        Cart cart = cartService.getCartByUser(user);

        model.addAttribute("cart", cart);
        model.addAttribute("totalPrice", cartService.getTotalPrice(cart));
        return "cart/index";
    }

    @PostMapping("/add")
    public String addToCart(@RequestParam("variantId") int variantId, @RequestParam("quantity") int quantity, Authentication authentication) {
        User user = ((UserPrincipal) authentication.getPrincipal()).getUser();
        cartService.addItemToCart(user, variantId, quantity);
        return "redirect:/cart";
    }

    @PostMapping("/remove/{itemId}")
    public String removeFromCart(@PathVariable("itemId") int itemId) {
        cartService.removeItemFromCart(itemId);
        return "redirect:/cart";
    }
}