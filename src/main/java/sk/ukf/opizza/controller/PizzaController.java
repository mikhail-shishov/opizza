package sk.ukf.opizza.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import sk.ukf.opizza.entity.Product;
import sk.ukf.opizza.service.CategoryService;
import sk.ukf.opizza.service.PizzaService;

import java.util.List;

@Controller
public class PizzaController {

    private final PizzaService pizzaService;
    private final CategoryService categoryService;

    @Autowired
    public PizzaController(PizzaService pizzaService, CategoryService categoryService) {
        this.pizzaService = pizzaService;
        this.categoryService = categoryService;
    }

    // homepage and search
    @GetMapping("/")
    public String viewMenu(@RequestParam(value = "search", required = false) String query, @RequestParam(value = "category", required = false) Integer categoryId, Model model) {

        List<Product> pizzas;

        // filter and search
        if (query != null && !query.isEmpty()) {
            pizzas = pizzaService.searchPizzas(query);
            model.addAttribute("searchQuery", query);
        } else if (categoryId != null) {
            pizzas = pizzaService.getPizzasByCategory(categoryId);
            model.addAttribute("selectedCategory", categoryId);
        } else {
            pizzas = pizzaService.getAllActivePizzas();
        }

        model.addAttribute("pizzas", pizzas);
        model.addAttribute("categories", categoryService.getAllCategories());

        return "home";
    }

    @GetMapping("/pizza/{slug}")
    public String viewPizzaDetail(@PathVariable("slug") String slug, Model model) {
        Product pizza = pizzaService.getPizzaBySlug(slug);

        if (pizza == null) {
            return "redirect:/";
        }

        model.addAttribute("pizza", pizza);
        return "detail";
    }
}