package sk.ukf.opizza.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sk.ukf.opizza.dao.ProductVariantRepository;
import sk.ukf.opizza.entity.*;
import sk.ukf.opizza.service.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final OrderService orderService;
    private final PizzaService pizzaService;
    private final CategoryService categoryService;
    private final TagService tagService;
    private final SizeService sizeService;
    private final ProductVariantRepository variantRepository;

    @Autowired
    public AdminController(OrderService orderService, PizzaService pizzaService, CategoryService categoryService, TagService tagService, SizeService sizeService, ProductVariantRepository variantRepository) {
        this.orderService = orderService;
        this.pizzaService = pizzaService;
        this.categoryService = categoryService;
        this.tagService = tagService;
        this.sizeService = sizeService;
        this.variantRepository = variantRepository;
    }

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
        model.addAttribute("products", pizzaService.getAllPizzas());
        return "admin/products-list";
    }

    @GetMapping("/products/add")
    public String showAddProductForm(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("allTags", tagService.getAllTags());
        model.addAttribute("allSizes", sizeService.getAllSizes());
        return "admin/product-form";
    }

    @PostMapping("/products/save")
    public String saveProduct(
            @RequestParam("productId") int productId,
            @RequestParam("name") String name,
            @RequestParam("category") Integer categoryId,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "slug", required = false) String slug,
            @RequestParam(value = "available", defaultValue = "false") boolean available,
            @RequestParam(value = "tagData", required = false) String tagData,
            @RequestParam(value = "imageFiles", required = false) MultipartFile[] imageFiles,
            @RequestParam(value = "existingImageIdsData", required = false) String existingImageIdsData,
            @RequestParam(value = "mainImageIndex", defaultValue = "0") int mainIndex,
            @RequestParam(value = "variantData", required = false) String variantData) {

        List<Integer> existingImageIds = new ArrayList<>();
        if (existingImageIdsData != null && !existingImageIdsData.isEmpty()) {
            for (String id : existingImageIdsData.split(",")) {
                try {
                    existingImageIds.add(Integer.parseInt(id));
                } catch (NumberFormatException ignored) {}
            }
        }

        List<Integer> sizeIds = new ArrayList<>();
        List<Double> prices = new ArrayList<>();
        if (variantData != null && !variantData.isEmpty()) {
            String[] pairs = variantData.split(",");
            for (String pair : pairs) {
                String[] parts = pair.split(":");
                if (parts.length == 2) {
                    try {
                        sizeIds.add(Integer.parseInt(parts[0]));
                        prices.add(Double.parseDouble(parts[1]));
                    } catch (NumberFormatException ignored) {}
                }
            }
        }

        List<Integer> tagIds = new ArrayList<>();
        if (tagData != null && !tagData.isEmpty()) {
            for (String id : tagData.split(",")) {
                try {
                    tagIds.add(Integer.parseInt(id));
                } catch (NumberFormatException ignored) {}
            }
        }

        Product product = new Product();
        product.setProductId(productId);
        product.setName(name);
        product.setDescription(description);
        product.setAvailable(available);
        product.setSlug(slug != null && !slug.isEmpty() ? slug : generateSlug(name));

        Category cat = new Category();
        cat.setId(categoryId);
        product.setCategory(cat);

        if (tagIds != null) {
            List<Tag> tags = tagIds.stream().map(id -> {
                Tag t = new Tag();
                t.setId(id);
                return t;
            }).toList();
            product.setTags(tags);
        }

        List<String> finalUrls = new ArrayList<>();
        if (imageFiles != null) {
            for (MultipartFile file : imageFiles) {
                if (!file.isEmpty()) {
                    try {
                        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename().replaceAll("\\s+", "_");
                        Path uploadPath = Paths.get("user-pics", "products");
                        if (!Files.exists(uploadPath)) Files.createDirectories(uploadPath);
                        Files.write(uploadPath.resolve(fileName), file.getBytes());
                        finalUrls.add("/uploads/products/" + fileName);
                    } catch (IOException e) { e.printStackTrace(); }
                }
            }
        }

        pizzaService.saveProductFull(product, finalUrls, existingImageIds, mainIndex, sizeIds, prices);

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
    public String saveCategory(@RequestParam(required = false) Integer id, @RequestParam String name) {
        Category cat = (id != null) ? categoryService.getCategoryById(id) : new Category();
        cat.setName(name);
        categoryService.saveCategory(cat);
        return "redirect:/admin/categories";
    }

    @PostMapping("/categories/delete/{id}")
    public String deleteCategory(@PathVariable int id) {
        categoryService.deleteCategory(id);
        return "redirect:/admin/categories";
    }

    @GetMapping("/tags")
    public String listTags(Model model) {
        model.addAttribute("tags", tagService.getAllTags());
        return "admin/tags";
    }

    @PostMapping("/tags/save")
    public String saveTag(@RequestParam(required = false) Integer id, @RequestParam String name, @RequestParam(required = false) String colorHex) {
        Tag tag = (id != null) ? tagService.getTagById(id) : new Tag();
        tag.setName(name);
        tag.setColorHex(colorHex != null ? colorHex : "#808080");
        tagService.saveTag(tag);
        return "redirect:/admin/tags";
    }

    @PostMapping("/tags/delete/{id}")
    public String deleteTag(@PathVariable int id) {
        tagService.deleteTag(id);
        return "redirect:/admin/tags";
    }

    @GetMapping("/products/edit/{id}")
    public String showEditProductForm(@PathVariable("id") int id, Model model) {
        Product product = pizzaService.getPizzaById(id);
        model.addAttribute("product", product);
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("allTags", tagService.getAllTags());
        model.addAttribute("allSizes", sizeService.getAllSizes());
        return "admin/product-form";
    }

    @GetMapping("/sizes")
    public String listSizes(Model model) {
        model.addAttribute("sizes", sizeService.getAllSizes());
        return "admin/sizes"; // We will create this HTML file next
    }

    @PostMapping("/sizes/save")
    public String saveSize(@RequestParam(required = false) Integer id, @RequestParam String name, @RequestParam int weightGrams) {
        Size size = (id != null) ? sizeService.getSizeById(id) : new Size();
        size.setName(name);
        size.setWeightGrams(weightGrams);
        sizeService.saveSize(size);
        return "redirect:/admin/sizes";
    }

    private String generateSlug(String name) {
        if (name == null || name.isEmpty()) return "";

        return name.toLowerCase().replace('ľ', 'l').replace('š', 's').replace('č', 'c').replace('ť', 't').replace('ž', 'z').replace('ý', 'y').replace('á', 'a').replace('í', 'i').replace('é', 'e').replace('ď', 'd').replace('ň', 'n').replace('ó', 'o').replace('ô', 'o').replace('ŕ', 'r').replace('ĺ', 'l').replaceAll("[^a-z0-9\\s]", "").replaceAll("\\s+", "-").replaceAll("-+", "-").trim();
    }
}