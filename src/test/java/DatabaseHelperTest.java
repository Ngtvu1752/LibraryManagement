import org.example.DatabaseHelper;
import org.example.PasswordUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class DatabaseHelperTest {
    private static final String URL = "jdbc:mysql://localhost:3306/library_dbTest";
    private static final String USERNAME = "root"; // Tên người dùng MySQL
    private static final String PASSWORD = ""; // Mật khẩu MySQL
    private DatabaseHelper databaseHelper;
    private Connection connection;

    @BeforeEach
    public void setUp() throws SQLException {
        // Kết nối đến cơ sở dữ liệu MySQL
        connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        databaseHelper = DatabaseHelper.getInstance();

        // Đảm bảo bảng 'users' có dữ liệu
        Statement stmt = connection.createStatement();
        stmt.executeUpdate("DROP TABLE IF EXISTS users;");
        stmt.executeUpdate("CREATE TABLE users (" + "id INT AUTO_INCREMENT PRIMARY KEY," + "username VARCHAR(50) NOT NULL," + "name VARCHAR(100) NOT NULL," + "password VARCHAR(100) NOT NULL," + "question VARCHAR(100) NOT NULL," + "answer VARCHAR(100) NOT NULL," + "role VARCHAR(100) NOT NULL" + ");");

        // Chèn dữ liệu mẫu vào bảng users
        stmt.executeUpdate("INSERT INTO users (username, name, password, question, answer, role) " + "VALUES ('testUser', 'Test User', '" + "testPassword" + "', 'What was the name of your first pet?', 'Dog', 'user')");
        stmt.executeUpdate("INSERT INTO users (username, name, password, question, answer, role) " + "VALUES ('anotherUser', 'Another User', '" + "anotherPassword" + "', 'What was the name of your first pet?', 'Cat', 'admin')");
    }

    @Test
    public void testUserExists_UserFound() throws SQLException {
        // Gọi phương thức kiểm tra với người dùng tồn tại và mật khẩu chính xác
        boolean result = databaseHelper.userExists("testUser", "testPassword");

        // Kiểm tra kết quả trả về là true (người dùng tồn tại)
        assertTrue(result);
    }

    @Test
    public void testUserExists_UserNotFound() throws SQLException {
        // Gọi phương thức kiểm tra với người dùng không tồn tại
        boolean result = databaseHelper.userExists("nonExistentUser", "testPassword");

        // Kiểm tra kết quả trả về là false (người dùng không tồn tại)
        assertFalse(result);
    }

    @Test
    public void testUserExists_IncorrectPassword() throws SQLException {
        // Gọi phương thức kiểm tra với tên người dùng đúng nhưng mật khẩu sai
        boolean result = databaseHelper.userExists("testUser", "wrongPassword");

        // Kiểm tra kết quả trả về là false (mật khẩu sai)
        assertFalse(result);
    }

    @Test
    public void testConnect() throws SQLException {
        // Kiểm tra kết nối cơ sở dữ liệu
        Connection conn = databaseHelper.connect();
        assertNotNull(conn, "Database connection should not be null.");
    }

    @AfterEach
    public void tearDown() throws SQLException {
        // Dọn dẹp bảng users sau khi kiểm tra
        Statement stmt = connection.createStatement();
        stmt.executeUpdate("DROP TABLE IF EXISTS users;");
        connection.close();
    }
}