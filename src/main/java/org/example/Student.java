package org.example;

public class Student extends User {
    public static final String role = "student";
    private String classroom;
    private String school;
    public Student(int id, String username, String name) {
        super(id, username, name);
    }

    public Student(int id, String username, String password, String name, String securityQuestion, String securityAnswer) {
        super(id, username, password, name, securityQuestion, securityAnswer);
    }

    public String getClassroom() {
        return classroom;
    }

    public void setClassroom(String classroom) {
        this.classroom = classroom;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public Student(String username, String password, String name, String securityQuestion, String securityAnswer) {
        super(username, password, name, securityQuestion, securityAnswer);
    }

    public String getRole() {
        return role;
    }
}
