package sk.ukf.opizza.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sk.ukf.opizza.dao.ProductRepository;
import sk.ukf.opizza.entity.Product;
import java.util.List;

@Service
public class PizzaServiceImpl implements PizzaService {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public List<Product> getAllActivePizzas() {
        return productRepository.findByIsAvailableTrue();
    }

    @Override
    public List<Product> searchPizzas(String query) {
        if (query == null || query.trim().isEmpty()) {
            return getAllActivePizzas();
        }
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
        pizza.setAvailable(false); // soft delete
        productRepository.save(pizza);
    }

    @Override
    public void savePizza(Product pizza) {
        productRepository.save(pizza);
    }
}