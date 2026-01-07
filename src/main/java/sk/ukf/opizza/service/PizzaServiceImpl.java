package sk.ukf.opizza.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sk.ukf.opizza.dao.ProductRepository;
import sk.ukf.opizza.dao.ProductVariantRepository;
import sk.ukf.opizza.dao.SizeRepository;
import sk.ukf.opizza.entity.*;

import java.util.ArrayList;
import java.util.List;

@Service
public class PizzaServiceImpl implements PizzaService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductVariantRepository variantRepository;

    @Autowired
    private SizeRepository sizeRepository;

    @Override
    public List<Product> getAllActivePizzas() {
        return productRepository.findByIsAvailableTrue();
    }

    @Override
    public List<Product> searchPizzas(String query) {
        if (query == null || query.trim().isEmpty()) return getAllActivePizzas();
        return productRepository.searchByName(query);
    }

    @Override
    public List<Product> getPizzasByCategory(int categoryId) {
        return productRepository.findByCategoryId(categoryId);
    }

    @Override
    public Product getPizzaById(int id) {
        return productRepository.findById(id).orElseThrow(() -> new RuntimeException("Pizza sa nenašla"));
    }

    @Override
    public void softDeletePizza(int id) {
        Product pizza = getPizzaById(id);
        pizza.setAvailable(false);
        productRepository.save(pizza);
    }

    @Override
    public void savePizza(Product pizza) {
        productRepository.save(pizza);
    }

    @Transactional
    @Override
    public void saveProductFull(Product product, List<String> imageUrls, int mainIndex, List<Integer> sizeIds, List<Double> prices) {
        Product existingProduct;
        if (product.getProductId() != 0) {
            existingProduct = productRepository.findById(product.getProductId()).orElse(product);
            existingProduct.setName(product.getName());
            existingProduct.setDescription(product.getDescription());
            existingProduct.setCategory(product.getCategory());
            existingProduct.setSlug(product.getSlug());
            existingProduct.setAvailable(product.isAvailable());
        } else {
            existingProduct = product;
        }

        if (imageUrls != null) {
            existingProduct.getImages().clear();
            for (int i = 0; i < imageUrls.size(); i++) {
                if (imageUrls.get(i) == null || imageUrls.get(i).trim().isEmpty()) continue;
                ProductImage img = new ProductImage();
                img.setUrl(imageUrls.get(i));
                img.setMain(i == mainIndex);
                img.setProduct(existingProduct);
                existingProduct.getImages().add(img);
            }
        }

        Product savedProduct = productRepository.save(existingProduct);

        if (sizeIds != null && prices != null) {
            List<ProductVariant> currentDbVariants = variantRepository.findByProductProductId(savedProduct.getProductId());

            for (int i = 0; i < sizeIds.size(); i++) {
                Double price = prices.get(i);
                int currentSizeId = sizeIds.get(i);

                ProductVariant variant = currentDbVariants.stream().filter(v -> v.getSize().getId() == currentSizeId).findFirst().orElse(new ProductVariant());

                if (price != null && price > 0) {
                    variant.setProduct(savedProduct);
                    variant.setPrice(price);
                    variant.setSize(sizeRepository.findById(currentSizeId).orElseThrow());
                    variant.setActive(true);
                    variantRepository.save(variant);
                } else if (variant.getVariantId() != 0) {
                    variant.setActive(false);
                    variantRepository.save(variant);
                }
            }
        }
    }

    @Override
    public Product getPizzaBySlug(String slug) {
        return productRepository.findBySlugWithVariants(slug).orElse(null);
    }
}