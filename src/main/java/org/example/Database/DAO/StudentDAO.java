package org.example.Database.DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.example.Database.Student;
import org.example.DatabaseHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class StudentDAO implements DAO<Student> {
    private final DatabaseHelper dbHelper;
    private static final String DISPLAY_STUDENTS = "SELECT id, username, name FROM student";

    public StudentDAO() {
        this.dbHelper = DatabaseHelper.getInstance();
    }

    @Override
    public ObservableList<Student> getObservableList() {
        ObservableList<Student> students = FXCollections.observableArrayList();
        try (Connection conn = dbHelper.connect();
             PreparedStatement pstmt = conn.prepareStatement(DISPLAY_STUDENTS)) {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String username = rs.getString("username");
                String name = rs.getString("name");
                Student student = new Student(id, username, name);
                students.add(student);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return students;
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

    /**
     * Tìm kiếm sinh viên theo ID.
     *
     * @param id ID của sinh viên cần tìm.
     * @return Một Optional chứa sinh viên nếu tìm thấy hoặc một Optional rỗng nếu không tìm thấy.
     */
    public Optional<Student> findById(int id) {
        String sql = "SELECT * FROM users WHERE id = ? AND role = 'student'";
        try (Connection conn = dbHelper.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
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

    /**
     * Kiểm tra xem tên người dùng có tồn tại trong database không.
     *
     * @param username Tên người dùng.
     * @return True nếu tên người dùng tồn tại, nếu không thì trả về false.
     */
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

    /**
     * Kiểm tra xem ID sinh viên có tồn tại trong database không.
     *
     * @param studentId ID sinh viên.
     * @return True nếu ID sinh viên tồn tại, nếu không thì trả về false.
     */
    public boolean isStudentIdExists(int studentId) {
        String checkStudentIdQuery = "SELECT COUNT(*) FROM users WHERE id = ?";
        try (Connection conn = dbHelper.connect();
             PreparedStatement stmt = conn.prepareStatement(checkStudentIdQuery)) {
            stmt.setInt(1, studentId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                return true;
            }
        } catch (SQLException e) {
            System.out.println("Error checking student ID: " + e.getMessage());
        }
        return false;
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

    /**
     * Lưu một sinh viên mới vào cơ sở dữ liệu.
     * Kiểm tra xem tên người dùng đã tồn tại chưa trước khi lưu.
     *
     * @param student sinh viên.
     * @return True nếu lưu thành công, nếu không thì trả về false.
     */
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

    /**
     * Xóa một sinh viên khỏi database.
     *
     * @param student sinh viên cần xóa.
     * @return True nếu xóa thành công, nếu không thì trả về false.
     */
    public boolean delete(Student student) {
        String sql = "DELETE FROM users WHERE id = ? AND role = 'student'";

        try (Connection conn = dbHelper.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, student.getId());
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Student deleted successfully.");
                return true;
            } else {
                System.out.println("Student not found or cannot be deleted.");
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Error while deleting student: " + e.getMessage());
            return false;
        }
    }

    /**
     * Cập nhật thông tin sinh viên tồn tại trong database.
     *
     * @param student sinh viên.
     * @return True nếu cập nhật thành công, nếu không thì trả về false.
     */
    public boolean update(Student student) {
        String updateQuery = "UPDATE users SET name = ?, class = ?, school = ? WHERE id = ?";
        try (Connection conn = dbHelper.connect();
             PreparedStatement stmt = conn.prepareStatement(updateQuery)) {
            stmt.setString(1, student.getName());
            stmt.setString(2, student.getClassroom());
            stmt.setString(3, student.getSchool());
            stmt.setInt(4, student.getId());
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.out.println("Error updating student: " + e.getMessage());
            return false;
        }
    }


}
