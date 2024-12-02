import org.example.Database.Admin;
import org.example.Database.User;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class UserTest {

    @Test
    public void testUserConstructorAndGetters() {
        // Tạo một AdminUser instance
        User user = new Admin("john_doe", "password123", "John Doe", "What is your pet's name?", "Fluffy");

        // Test the constructor and getters
        assertEquals("john_doe", user.getUsername());
        assertEquals("password123", user.getPassword());
        assertEquals("John Doe", user.getName());
        assertEquals("What is your pet's name?", user.getSecurityQuestion());
        assertEquals("Fluffy", user.getAnswer());
    }

    @Test
    public void testSetters() {
        // Tạo một AdminUser instance
        User user = new Admin("john_doe", "password123", "John Doe", "What is your pet's name?", "Fluffy");

        // Set giá trị mới
        user.setUsername("jane_doe");
        user.setPassword("newpassword");
        user.setName("Jane Doe");
        user.setSecurityQuestion("What is your favorite color?");
        user.setAnswer("Blue");

        // Kiểm tra xem các setter đã cập nhật giá trị đúng cách chưa.
        assertEquals("jane_doe", user.getUsername());
        assertEquals("newpassword", user.getPassword());
        assertEquals("Jane Doe", user.getName());
        assertEquals("What is your favorite color?", user.getSecurityQuestion());
        assertEquals("Blue", user.getAnswer());
    }
}
