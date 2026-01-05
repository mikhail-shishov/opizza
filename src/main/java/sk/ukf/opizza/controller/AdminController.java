package sk.ukf.opizza.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import sk.ukf.opizza.entity.Order;
import sk.ukf.opizza.entity.Product;
import sk.ukf.opizza.entity.Category;
import sk.ukf.opizza.service.OrderService;
import sk.ukf.opizza.service.PizzaService;
import sk.ukf.opizza.service.CategoryService; // Potrebný import

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final OrderService orderService;
    private final PizzaService pizzaService;
    private final CategoryService categoryService;

    @Autowired
    public AdminController(OrderService orderService, PizzaService pizzaService, CategoryService categoryService) {
        this.orderService = orderService;
        this.pizzaService = pizzaService;
        this.categoryService = categoryService;
    }

    // --- OBJEDNÁVKY ---
    @GetMapping("/orders")
    public String viewAllOrders(Model model) {
        model.addAttribute("orders", orderService.getAllOrders());
        return "admin/orders-list";
    }

    @PostMapping("/orders/update-status")
    public String updateStatus(@RequestParam("orderId") int orderId, @RequestParam("status") String status) {
        orderService.updateStatus(orderId, status);
        return "redirect:/admin/orders";
    }

    @GetMapping("/products")
    public String listProducts(Model model) {
        model.addAttribute("products", pizzaService.getAllActivePizzas());
        return "admin/products-list";
    }

    @GetMapping("/products/add")
    public String showAddProductForm(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("categories", categoryService.getAllCategories());
        return "admin/product-form";
    }

    @PostMapping("/products/save")
    public String saveProduct(@ModelAttribute("product") Product product, @RequestParam(value = "imageUrls", required = false) List<String> imageUrls, @RequestParam(value = "mainImageIndex", defaultValue = "0") int mainIndex) {
        if (product.getSlug() == null || product.getSlug().trim().isEmpty()) {
            product.setSlug(generateSlug(product.getName()));
        } else {
            product.setSlug(generateSlug(product.getSlug()));
        }
        pizzaService.savePizzaWithImages(product, imageUrls, mainIndex);
        return "redirect:/admin/products";
    }

    @PostMapping("/products/delete/{id}")
    public String deleteProduct(@PathVariable("id") int id) {
        pizzaService.softDeletePizza(id);
        return "redirect:/admin/products";
    }

    @GetMapping("/categories")
    public String listCategories(Model model) {
        model.addAttribute("categories", categoryService.getAllCategories());
        return "admin/categories";
    }

    @PostMapping("/categories/save")
    public String saveCategory(@RequestParam String name) {
        Category cat = new Category();
        cat.setName(name);
        categoryService.saveCategory(cat);
        return "redirect:/admin/categories";
    }

    @PostMapping("/categories/delete/{id}")
    public String deleteCategory(@PathVariable int id) {
        categoryService.deleteCategory(id);
        return "redirect:/admin/categories";
    }

    @GetMapping("/products/edit/{id}")
    public String showEditProductForm(@PathVariable("id") int id, Model model) {
        Product product = pizzaService.getPizzaById(id);
        model.addAttribute("product", product);
        model.addAttribute("categories", categoryService.getAllCategories());
        return "admin/product-form";
    }

    private String generateSlug(String name) {
        if (name == null || name.isEmpty()) return "";

        return name.toLowerCase()
                .replace('ľ', 'l').replace('š', 's').replace('č', 'c')
                .replace('ť', 't').replace('ž', 'z').replace('ý', 'y')
                .replace('á', 'a').replace('í', 'i').replace('é', 'e')
                .replace('ď', 'd').replace('ň', 'n').replace('ó', 'o')
                .replace('ô', 'o').replace('ŕ', 'r').replace('ĺ', 'l')
                .replaceAll("[^a-z0-9\\s]", "") // Odstráni špeciálne znaky
                .replaceAll("\\s+", "-")        // Nahradí medzery pomlčkou
                .replaceAll("-+", "-")          // Odstráni duplicitné pomlčky
                .trim();
    }
}