package org.example.Database;

public class Student extends User {
    public static final String role = "student";
    private String classroom;
    private String school;

    /**
     * Constructor 1.
     *
     * @param id       ID của sinh viên.
     * @param username tên người dùng của sinh viên.
     * @param name     tên của sinh viên.
     */
    public Student(int id, String username, String name) {
        super(id, username, name);
    }

    /**
     * Constructor 2.
     *
     * @param id               ID của sinh viên.
     * @param username         tên người dùng của sinh viên.
     * @param password         mật khẩu của sinh viên.
     * @param name             tên của sinh viên.
     * @param securityQuestion câu hỏi bảo mật của sinh viên.
     * @param securityAnswer   câu trả lời cho câu hỏi bảo mật của sinh viên.
     */
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
