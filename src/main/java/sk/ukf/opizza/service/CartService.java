package sk.ukf.opizza.service;

import sk.ukf.opizza.entity.Cart;
import sk.ukf.opizza.entity.User;

public interface CartService {
    Cart getCartByUser(User user);

    void addItemToCart(User user, int productVariantId, int quantity);

    void removeItemFromCart(int cartItemId);

    void clearCart(User user);

    double getTotalPrice(Cart cart);
}