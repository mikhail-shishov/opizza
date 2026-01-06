package sk.ukf.opizza.dto;

import jakarta.validation.constraints.*;

public class UserRegistration {

    @NotBlank(message = "Meno nesmie byť prázdne")
    private String firstName;

    @NotBlank(message = "Priezvisko nesmie byť prázdne")
    private String lastName;

    @NotBlank(message = "Email nesmie byť prázdny")
    @Email(message = "Zadajte platnú emailovú adresu")
    private String email;

    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Neplatný formát telefónneho čísla (+421...)")
    private String phone;

    @NotBlank(message = "Heslo je povinné")
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*\\d).{8,}$",
            message = "Heslo musí mať aspoň 8 znakov, jedno veľké písmeno a jedno číslo"
    )
    private String password;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}