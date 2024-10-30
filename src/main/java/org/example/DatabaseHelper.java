package org.example;

import java.sql.*;

public class DatabaseHelper {
    private static final String URL = "jdbc:mysql://localhost:3306/library_db"; // Địa chỉ database
    private static final String USERNAME = "root"; // Tên người dùng MySQL
    private static final String PASSWORD = "23021752"; // Mật khẩu MySQL

    public Connection connect() {
        Connection connection = null;
        try {
            // Nạp driver MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            System.out.println("Kết nối thành công!");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("Driver không tìm thấy.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Kết nối thất bại.");
        }
        return connection;
    }

    public boolean insertUser(String username, String name, String password, String question, String answer) {
        String sql = "INSERT INTO users(username, name, password, question, answer) VALUES(?,?,?,?,?)";
        if (usernameExists(username)) {
            System.out.println("Username already exists, please choose another name");
            return false;
        } else {
            try (Connection conn = this.connect();
                 PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setString(1, username);
                ps.setString(2, name);
                ps.setString(3, password);
                ps.setString(4, question);
                ps.setString(5, answer);
                ps.executeUpdate();
                System.out.println("Add them user thanh cong");
                return true;
            } catch (SQLException e) {
                System.out.println(e.getMessage());
                return false;
            }
        }
    }

    public boolean userExists(String username, String password) {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public boolean usernameExists(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public static void main(String[] args) {
        DatabaseHelper helper = new DatabaseHelper();
        helper.insertUser("VU", "YeuQH", "12345", "Qh la cua ai",
                "Cua bo may");
        boolean exists = helper.usernameExists("Dmm");
        if (exists) {
            System.out.println("Username already exists, please choose another name");
        } else {
            System.out.println("account created successfully");
        }
    }
}
