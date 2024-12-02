package org.example.Database;

public class Admin extends User {
    public static final String role = "admin";

    /**
     * Constructor.
     *
     * @param username         tên người dùng của admin.
     * @param password         mật khẩu của admin.
     * @param name             tên admin.
     * @param securityQuestion câu hỏi bảo mật cho admin.
     * @param securityAnswer   câu trả lời cho câu hỏi bảo mật của admin.
     */
    public Admin(String username, String password, String name, String securityQuestion, String securityAnswer) {
        super(username, password, name, securityQuestion, securityAnswer);
    }

    public String getRole() {
        return role;
    }
}
