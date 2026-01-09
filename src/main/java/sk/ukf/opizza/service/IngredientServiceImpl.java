package sk.ukf.opizza.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sk.ukf.opizza.dao.IngredientRepository;
import sk.ukf.opizza.entity.Ingredient;
import java.util.List;

@Service
public class IngredientServiceImpl implements IngredientService {
    @Autowired
    private IngredientRepository ingredientRepository;

    @Override public List<Ingredient> getAllIngredients() { return ingredientRepository.findAll(); }
    @Override public Ingredient getIngredientById(int id) { return ingredientRepository.findById(id).orElse(null); }
    @Override public void saveIngredient(Ingredient ingredient) { ingredientRepository.save(ingredient); }
    @Override public void deleteIngredient(int id) { ingredientRepository.deleteById(id); }
}