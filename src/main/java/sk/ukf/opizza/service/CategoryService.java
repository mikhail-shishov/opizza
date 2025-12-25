package sk.ukf.opizza.service;

import sk.ukf.opizza.entity.Category;
import java.util.List;

public interface CategoryService {
    List<Category> getAllCategories();
    Category getCategoryById(int id);
}