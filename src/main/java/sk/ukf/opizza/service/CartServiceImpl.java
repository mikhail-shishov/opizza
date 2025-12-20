package sk.ukf.opizza.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sk.ukf.opizza.dao.CartItemRepository;
import sk.ukf.opizza.dao.CartRepository;
import sk.ukf.opizza.dao.ProductVariantRepository;
import sk.ukf.opizza.entity.*;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductVariantRepository variantRepository;

    @Autowired
    public CartServiceImpl(CartRepository cartRepository, CartItemRepository cartItemRepository, ProductVariantRepository variantRepository) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.variantRepository = variantRepository;
    }

    @Override
    @Transactional
    public Cart getCartByUser(User user) {
        // looking for cart - if no found, create new
        return cartRepository.findByUser(user).orElseGet(() -> {
            Cart newCart = new Cart();
            newCart.setUser(user);
            newCart.setItems(new ArrayList<>());
            return cartRepository.save(newCart);
        });
    }

    @Override
    @Transactional
    public void addItemToCart(User user, int productVariantId, int quantity) {
        Cart cart = getCartByUser(user);
        ProductVariant variant = variantRepository.findById(productVariantId).orElseThrow(() -> new RuntimeException("Variant produktu nebol nájdený"));

        // checking if there's existing item in the cart, which is the same as the new one
        Optional<CartItem> existingItem = cart.getItems().stream().filter(item -> item.getProductVariant().getVariantId() == productVariantId).findFirst();

        if (existingItem.isPresent()) {
            // +1
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + quantity);
        } else {
            // make new item in cart
            CartItem newItem = new CartItem();
            newItem.setCart(cart);
            newItem.setProductVariant(variant);
            newItem.setQuantity(quantity);
            cart.getItems().add(newItem);
        }

        cartRepository.save(cart);
    }

    @Override
    @Transactional
    public void removeItemFromCart(int cartItemId) {
        cartItemRepository.deleteById(cartItemId);
    }

    @Override
    @Transactional
    public void clearCart(User user) {
        Cart cart = getCartByUser(user);
        cart.getItems().clear();
        cartRepository.save(cart);
    }

    @Override
    public double getTotalPrice(Cart cart) {
        // sum
        return cart.getItems().stream().mapToDouble(item -> item.getQuantity() * item.getProductVariant().getPrice()).sum();
    }
}