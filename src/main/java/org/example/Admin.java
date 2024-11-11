package org.example;

public class Admin extends User {
    public static final String role = "admin";

    public Admin(String username, String password, String name, String securityQuestion, String securityAnswer) {
        super(username, password, name, securityQuestion, securityAnswer);
    }

    public String getRole() {
        return role;
    }
}
