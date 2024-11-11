package org.example;

import java.sql.*;

public class DatabaseHelper {
    private static final String URL = "jdbc:mysql://localhost:3306/library_db"; // Địa chỉ database
    private static final String USERNAME = "root"; // Tên người dùng MySQL
    private static final String PASSWORD = "23021752"; // Mật khẩu MySQL
    private static DatabaseHelper instance;

    private DatabaseHelper() {
    }

    public static DatabaseHelper getInstance() {
        if (instance == null) {
            instance = new DatabaseHelper();
        }
        return instance;
    }

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
}
