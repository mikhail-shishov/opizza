package sk.ukf.opizza.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sk.ukf.opizza.dao.OrderRepository;
import sk.ukf.opizza.entity.*;
import sk.ukf.opizza.service.EmailService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private CartService cartService;
    @Autowired
    private AddressService addressService;
    @Autowired
    private EmailService emailService;

    @Override
    @Transactional
    public Order createOrder(User user, String note, int addressId) {
        Cart cart = cartService.getCartByUser(user);
        Address address = addressService.getAddressById(addressId);

        Order order = new Order();
        order.setUser(user);
        order.setDeliveryAddress(address);
        order.setNote(note);
        order.setOrderTime(LocalDateTime.now());
        order.setStatus("PENDING");
        order.setTotalPrice(cartService.getTotalPrice(cart));

        List<OrderItem> items = new ArrayList<>();
        for (CartItem ci : cart.getItems()) {
            OrderItem oi = new OrderItem();
            oi.setOrder(order);
            oi.setProductVariant(ci.getProductVariant());
            oi.setQuantity(ci.getQuantity());
            oi.setPriceAtOrder(ci.getProductVariant().getPrice());
            oi.setProductNameAtOrder(ci.getProductVariant().getProduct().getName());
            items.add(oi);
        }
        order.setOrderItems(items);

        List<OrderStatusHistory> historyList = new ArrayList<>();
        OrderStatusHistory initialHistory = new OrderStatusHistory();
        initialHistory.setOrder(order);
        initialHistory.setStatus("PENDING");
        initialHistory.setChangedAt(LocalDateTime.now());
        historyList.add(initialHistory);
        order.setStatusHistory(historyList);

        Order saved = orderRepository.save(order);

        emailService.sendEmail(user.getEmail(), "Potvrdenie objednávky č. " + saved.getOrderId(),"Vaša objednavka bola prijatá! Celková suma je " + String.format("%.2f", saved.getTotalPrice()) + " €");

        cartService.clearCart(user);
        return saved;
    }

    @Override
    public List<Order> getUserOrderHistory(User user) {
        return orderRepository.findByUserIdOrderByOrderTimeDesc(user.getId());
    }

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    @Transactional
    public void updateStatus(int id, String status) {
        Order o = orderRepository.findById(id).orElseThrow();
        o.setStatus(status);

        OrderStatusHistory newEntry = new OrderStatusHistory();
        newEntry.setOrder(o);
        newEntry.setStatus(status);
        newEntry.setChangedAt(LocalDateTime.now());

        if (o.getStatusHistory() == null) o.setStatusHistory(new ArrayList<>());
        o.getStatusHistory().add(newEntry);

        orderRepository.save(o);
    }

    @Override
    public int countByStatus(String status) {
        return orderRepository.countByStatus(status);
    }
}