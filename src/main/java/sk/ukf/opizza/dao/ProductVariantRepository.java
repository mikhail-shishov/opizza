package sk.ukf.opizza.dao;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sk.ukf.opizza.entity.ProductVariant;
import java.util.List;

@Repository
public interface ProductVariantRepository extends JpaRepository<ProductVariant, Integer> {
    List<ProductVariant> findByProductProductId(int productId);

    @Transactional
    void deleteByProductProductId(int productId);
    
    long countBySizeId(int sizeId);
}