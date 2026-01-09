package sk.ukf.opizza.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sk.ukf.opizza.dao.ProductRepository;
import sk.ukf.opizza.dao.ProductVariantRepository;
import sk.ukf.opizza.dao.SizeRepository;
import sk.ukf.opizza.dao.TagRepository;
import sk.ukf.opizza.dao.IngredientRepository;
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

    @Autowired
    private IngredientRepository ingredientRepository;

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

    @Autowired
    private TagRepository tagRepository;

    @Transactional
    @Override
    public void saveProductFull(Product product, List<String> imageUrls, List<Integer> existingImageIds, int mainIndex, List<Integer> sizeIds, List<Double> prices) {
        Product managedProduct;
        if (product.getProductId() != 0) {
            managedProduct = productRepository.findById(product.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            managedProduct.setName(product.getName());
            managedProduct.setDescription(product.getDescription());
            managedProduct.setAvailable(product.isAvailable());
            managedProduct.setSlug(product.getSlug());

            if (managedProduct.getImageUrl() == null) {
                managedProduct.setImageUrl("/img/pizza_pictures/placeholder.webp");
            }

            Category cat = new Category();
            cat.setId(product.getCategory().getId());
            managedProduct.setCategory(cat);

            if (product.getTags() != null) {
                managedProduct.getTags().clear();
                for (Tag detachedTag : product.getTags()) {
                    managedProduct.getTags().add(tagRepository.findById(detachedTag.getId()).orElseThrow());
                }
            } else {
                managedProduct.getTags().clear();
            }
        } else {
            managedProduct = product;
            if (managedProduct.getImageUrl() == null) {
                managedProduct.setImageUrl("/img/pizza_pictures/placeholder.webp");
            }
            if (product.getTags() != null) {
                List<Tag> attachedTags = new ArrayList<>();
                for (Tag detachedTag : product.getTags()) {
                    attachedTags.add(tagRepository.findById(detachedTag.getId()).orElseThrow());
                }
                managedProduct.setTags(attachedTags);
            }
        }

        if (managedProduct.getProductId() != 0) {
            if (existingImageIds == null || existingImageIds.isEmpty()) {
                managedProduct.getImages().clear();
            } else {
                managedProduct.getImages().removeIf(img -> !existingImageIds.contains(img.getId()));
            }
        }

        if (imageUrls != null) {
            for (String url : imageUrls) {
                ProductImage img = new ProductImage();
                img.setUrl(url);
                img.setProduct(managedProduct);
                managedProduct.getImages().add(img);
            }
        }

        for (int i = 0; i < managedProduct.getImages().size(); i++) {
            managedProduct.getImages().get(i).setMain(i == mainIndex);
        }

        if (product.getIngredients() != null) {
            managedProduct.getIngredients().clear();
            for (Ingredient detachedIng : product.getIngredients()) {
                managedProduct.getIngredients().add(ingredientRepository.findById(detachedIng.getId()).orElseThrow());
            }
        } else {
            managedProduct.getIngredients().clear();
        }

        if (sizeIds != null && prices != null) {
            for (int i = 0; i < sizeIds.size(); i++) {
                int sizeId = sizeIds.get(i);
                Double price = prices.get(i);

                ProductVariant variant = managedProduct.getVariants().stream()
                        .filter(v -> v.getSize().getId() == sizeId)
                        .findFirst()
                        .orElse(null);

                if (price != null && price > 0) {
                    if (variant == null) {
                        variant = new ProductVariant();
                        variant.setProduct(managedProduct);
                        variant.setSize(sizeRepository.findById(sizeId).orElseThrow());
                        managedProduct.getVariants().add(variant);
                    }
                    variant.setPrice(price);
                    variant.setActive(true);
                } else if (variant != null) {
                    variant.setActive(false);
                }
            }
        }

        productRepository.save(managedProduct);
    }

    @Override
    public Product getPizzaBySlug(String slug) {
        return productRepository.findBySlugWithVariants(slug).orElse(null);
    }

    @Override
    public List<Product> getAllPizzas() {
        return productRepository.findAll();
    }
}