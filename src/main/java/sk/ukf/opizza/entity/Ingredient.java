package sk.ukf.opizza.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "ingredients")
public class Ingredient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ingredient_id")
    private int id;

    @Column(nullable = false, length = 50)
    private String name;

    private boolean isSpicy;
    private Boolean isFish;
    private Boolean isMeat;

    @ManyToMany(mappedBy = "ingredients")
    private List<Product> products;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSpicy() {
        return isSpicy;
    }

    public void setSpicy(boolean spicy) {
        isSpicy = spicy;
    }

    public Boolean getFish() {
        return isFish;
    }

    public void setFish(Boolean fish) {
        isFish = fish;
    }

    public Boolean getMeat() {
        return isMeat;
    }

    public void setMeat(Boolean meat) {
        isMeat = meat;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }
}