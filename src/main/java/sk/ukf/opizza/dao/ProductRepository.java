package sk.ukf.opizza.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sk.ukf.opizza.entity.Product;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    @Query("SELECT p FROM Product p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :query, '%')) AND p.isAvailable = true")
    List<Product> searchByName(@Param("query") String query);

    List<Product> findByIsAvailableTrue();

    List<Product> findByCategoryId(int categoryId);

    @Query("SELECT p FROM Product p JOIN p.tags t WHERE t.id = :tagId")
    List<Product> findByTagId(@Param("tagId") int tagId);
}