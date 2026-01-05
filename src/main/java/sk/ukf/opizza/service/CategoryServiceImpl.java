package sk.ukf.opizza.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sk.ukf.opizza.dao.CategoryRepository;
import sk.ukf.opizza.dao.ProductRepository;
import sk.ukf.opizza.entity.Category;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAllByOrderBySortOrderAsc();
    }

    @Override
    public Category getCategoryById(int id) {
        return categoryRepository.findById(id).orElse(null);
    }

    @Override
    public void saveCategory(Category cat) {
        categoryRepository.save(cat);
    }

    @Override
    public void deleteCategory(int id) {
        if (productRepository.countByCategoryId(id) > 0) {
            return;
        }
        categoryRepository.deleteById(id);
    }
}