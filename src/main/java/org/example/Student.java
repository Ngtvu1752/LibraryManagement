package org.example;

public class Student extends User {
    public static final String role = "student";

    public Student(int id, String username, String password, String name, String securityQuestion, String securityAnswer) {
        super(id, username, password, name, securityQuestion, securityAnswer);
    }

    public Student(String username, String password, String name, String securityQuestion, String securityAnswer) {
        super(username, password, name, securityQuestion, securityAnswer);
    }

    public String getRole() {
        return role;
    }
}
