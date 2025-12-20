package sk.ukf.opizza.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sk.ukf.opizza.entity.Order;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    @Query("SELECT o FROM Order o WHERE o.user.id = :userId ORDER BY o.orderTime DESC")
    List<Order> findAllByUserId(@Param("userId") int userId);
    List<Order> findByStatus(String status);

    @Query("SELECT o FROM Order o WHERE o.status = 'READY' OR o.status = 'DELIVERING'")
    List<Order> findActiveDeliveries();
}