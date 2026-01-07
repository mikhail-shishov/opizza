package sk.ukf.opizza.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sk.ukf.opizza.entity.Product;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    @Query("SELECT p FROM Product p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :query, '%')) AND p.isAvailable = true")
    List<Product> searchByName(@Param("query") String query);

    List<Product> findByIsAvailableTrue();

    long countByCategoryId(int categoryId);

    List<Product> findByCategoryId(int categoryId);

    Optional<Product> findBySlug(String slug);

    @Query("SELECT p FROM Product p " + "LEFT JOIN FETCH p.variants v " + "LEFT JOIN FETCH v.size " + "WHERE p.slug = :slug AND (v IS NULL OR v.isActive = true)")
    Optional<Product> findBySlugWithVariants(@Param("slug") String slug);

    @Query("SELECT p FROM Product p JOIN p.tags t WHERE t.id = :tagId")
    List<Product> findByTagId(@Param("tagId") int tagId);
}