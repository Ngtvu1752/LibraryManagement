package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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

    public void insertUser(String username, String name, String password, String question, String answer) {
        String sql = "INSERT INTO users(username, name, password, question, answer) VALUES(?,?,?,?,?)";

        try (Connection conn = this.connect();
        PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, name);
            ps.setString(3, password);
            ps.setString(4, question);
            ps.setString(5, answer);
            ps.executeUpdate();
            System.out.println("Add them user thanh cong");
        } catch (SQLException e)
        {
            System.out.println(e.getMessage());
        }

    }
    public static void main(String[] args) {
        DatabaseHelper helper = new DatabaseHelper();
        helper.insertUser("VU", "YeuQH", "12345", "Qh la cua ai","Cua bo may");
    }
}
