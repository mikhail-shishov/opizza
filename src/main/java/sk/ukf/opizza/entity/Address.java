package sk.ukf.opizza.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "addresses")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "addresses_id")
    private int id;

    @NotBlank(message = "Mesto je povinné")
    @Size(max = 30)
    private String city;

    @NotBlank(message = "PSČ je povinné")
    @Pattern(regexp = "\\d{3} ?\\d{2}", message = "PSČ musí byť v tvare 123 45 alebo 12345")
    @Size(max = 6)
    private String zipCode;

    @NotBlank(message = "Ulica je povinná")
    @Size(max = 100)
    private String street;

    @NotBlank(message = "Číslo domu je povinné")
    @Size(max = 10)
    private String house;

    @Size(max = 5, message = "Číslo bytu môže mať max 5 znakov")
    private String flat;

    @Size(max = 64)
    private String type;

    private boolean isDefault;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getHouse() {
        return house;
    }

    public void setHouse(String house) {
        this.house = house;
    }

    public String getFlat() {
        return flat;
    }

    public void setFlat(String flat) {
        this.flat = flat;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}