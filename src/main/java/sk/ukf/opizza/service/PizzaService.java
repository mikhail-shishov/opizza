package sk.ukf.opizza.service;

import sk.ukf.opizza.entity.Product;
import java.util.List;

public interface PizzaService {
    List<Product> getAllActivePizzas();
    List<Product> getAllPizzas();
    List<Product> searchPizzas(String query);
    List<Product> getPizzasByCategory(int categoryId);
    Product getPizzaById(int id);
    void savePizza(Product pizza);
    void softDeletePizza(int id);
    void saveProductFull(Product product, List<String> imageUrls, List<Integer> existingImageIds, int mainIndex, List<Integer> sizeIds, List<Double> prices);
    Product getPizzaBySlug(String slug);
}