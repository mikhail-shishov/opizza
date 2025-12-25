package sk.ukf.opizza.service;

import sk.ukf.opizza.entity.Order;
import sk.ukf.opizza.entity.User;
import java.util.List;

public interface OrderService {
    Order createOrder(User user, String note, int addressId);
    List<Order> getUserOrderHistory(User user);
    List<Order> getAllOrders(); // Pre admina/kuchára
    void updateStatus(int orderId, String status);
}