package ca.mcgill.ecse321.GameShop.dto;

public class ManagerRequestDto {
    // Attributes
    private String email;
    private String password;

    // Constructors
    public ManagerRequestDto() {}

    public ManagerRequestDto(String email, String password) {
        this.email = email;
        this.password = password;
    }

    // Getters and Setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
