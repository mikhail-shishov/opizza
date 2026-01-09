package sk.ukf.opizza.service;

import sk.ukf.opizza.entity.Ingredient;
import java.util.List;

public interface IngredientService {
    List<Ingredient> getAllIngredients();
    Ingredient getIngredientById(int id);
    void saveIngredient(Ingredient ingredient);
    void deleteIngredient(int id);
}