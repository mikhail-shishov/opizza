package sk.ukf.opizza.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "ingredients")
public class Ingredient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ingredient_id")
    private Integer id;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(name = "is_spicy")
    private Boolean isSpicy = false;

    @Column(name = "is_fish")
    private Boolean isFish = false;

    @Column(name = "is_meat")
    private Boolean isMeat = false;

    @ManyToMany(mappedBy = "ingredients")
    private List<Product> products;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getIsSpicy() {
        return isSpicy;
    }

    public void setIsSpicy(Boolean isSpicy) {
        this.isSpicy = isSpicy;
    }

    public Boolean getIsFish() {
        return isFish;
    }

    public void setIsFish(Boolean isFish) {
        this.isFish = isFish;
    }

    public Boolean getIsMeat() {
        return isMeat;
    }

    public void setIsMeat(Boolean isMeat) {
        this.isMeat = isMeat;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }
}