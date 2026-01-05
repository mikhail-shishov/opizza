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

    @Override
    @Transactional
    public void savePizzaWithImages(Product product, List<String> imageUrls, int mainIndex) {
    }

    @Override
    @Transactional
    public void saveProductFull(Product product, List<String> imageUrls, int mainIndex, List<Integer> sizeIds, List<Double> prices) {
        Product savedProduct = productRepository.save(product);

        if (savedProduct.getImages() == null) {
            savedProduct.setImages(new ArrayList<>());
        } else {
            savedProduct.getImages().clear();
        }

        if (imageUrls != null) {
            for (int i = 0; i < imageUrls.size(); i++) {
                String url = imageUrls.get(i);
                if (url != null && !url.trim().isEmpty()) {
                    ProductImage img = new ProductImage();
                    img.setUrl(url.trim());
                    img.setProduct(savedProduct);
                    img.setMain(i == mainIndex);
                    savedProduct.getImages().add(img);
                }
            }
        }

        variantRepository.deleteByProductProductId(savedProduct.getProductId());

        if (sizeIds != null && prices != null) {
            for (int i = 0; i < sizeIds.size(); i++) {
                if (i < prices.size() && prices.get(i) != null && prices.get(i) > 0) {
                    ProductVariant v = new ProductVariant();
                    v.setProduct(savedProduct);
                    v.setPrice(prices.get(i));
                    v.setSize(sizeRepository.findById(sizeIds.get(i)).orElse(null));
                    variantRepository.save(v);
                }
            }
        }

        productRepository.save(savedProduct);
    }

    @Override
    public Product getPizzaBySlug(String slug) {
        return productRepository.findBySlugWithVariants(slug).orElse(null);
    }
}