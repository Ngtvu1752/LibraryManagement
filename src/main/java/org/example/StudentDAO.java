package org.example;

import javafx.fxml.FXML;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class StudentDAO implements DAO<Student> {
    private final DatabaseHelper dbHelper;

    public StudentDAO() {
        this.dbHelper = DatabaseHelper.getInstance();
    }

    @Override
    public List<Student> getAll() {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT * FROM users WHERE role = ?";

        try (Connection conn = dbHelper.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, Student.role);  // Lọc theo vai trò "student"
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String username = rs.getString("username");
                String password = rs.getString("password");
                String name = rs.getString("name");
                students.add(new Student(username, password, name, null, null));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return students;
    }

    @Override
    public Optional<Student> findById(int id) {
        String sql = "SELECT * FROM users WHERE id = ? AND role = 'student'";
        try (Connection conn = dbHelper.connect();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if(rs.next()) {
                String username = rs.getString("username");
                String password = rs.getString("password");
                String name = rs.getString("name");
                return Optional.of(new Student(username, password, name, null, null));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public boolean usernameExists(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";
        try (Connection conn = dbHelper.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public boolean userExists(String username, String password) {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (Connection conn = dbHelper.connect();
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

    public boolean save(Student student) {
        String sql = "INSERT INTO users(username, name, password, question, answer, role) VALUES(?,?,?,?,?,?)";

        // Kiểm tra nếu username đã tồn tại
        if (usernameExists(student.getUsername())) {
            System.out.println("Username already exists, please choose another name");
            return false;
        } else {
            try (Connection conn = dbHelper.connect();
                 PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, student.getUsername());
                ps.setString(2, student.getName());
                ps.setString(3, student.getPassword());
                ps.setString(4, student.getSecurityQuestion());
                ps.setString(5, student.getAnswer());
                ps.setString(6, student.getRole());
                ps.executeUpdate();
                System.out.println("Student registered successfully.");
                return true;
            } catch (SQLException e) {
                System.out.println("Error while saving student: " + e.getMessage());
                return false;
            }
        }
    }


}
    