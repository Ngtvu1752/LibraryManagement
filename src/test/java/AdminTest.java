import org.example.Admin;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class AdminTest {
    private Admin admin;

    @Test
    public void testGetRole() {
        // Khẳng định rằng vai trò là "admin".
        admin = new Admin("adminUsername", "adminPassword", "Admin Name", "What is your pet's name?", "Fluffy");
        assertEquals("admin", admin.getRole());
    }

    @Test
    public void testAdminConstructor() {
        // Khẳng định rằng hàm khởi tạo hoạt động bằng cách kiểm tra các giá trị trong lớp User (lớp kế thừa).
        admin = new Admin("adminUsername", "adminPassword", "Admin Name", "What is your pet's name?", "Fluffy");
        assertEquals("adminUsername", admin.getUsername());
        assertEquals("adminPassword", admin.getPassword());
        assertEquals("Admin Name", admin.getName());
        assertEquals("What is your pet's name?", admin.getSecurityQuestion());
        assertEquals("Fluffy", admin.getAnswer());
    }

    @Test
    public void testConstructorWithNullValues() {
        Admin adminWithNullValues = new Admin(null, null, null, null, null);
        assertNull(adminWithNullValues.getUsername());
        assertNull(adminWithNullValues.getPassword());
        assertNull(adminWithNullValues.getName());
        assertNull(adminWithNullValues.getSecurityQuestion());
        assertNull(adminWithNullValues.getAnswer());
    }

    @Test
    public void testConstructorWithEmptyStrings() {
        Admin adminWithEmptyStrings = new Admin("", "", "", "", "");
        assertEquals("", adminWithEmptyStrings.getUsername());
        assertEquals("", adminWithEmptyStrings.getPassword());
        assertEquals("", adminWithEmptyStrings.getName());
        assertEquals("", adminWithEmptyStrings.getSecurityQuestion());
        assertEquals("", adminWithEmptyStrings.getAnswer());
    }
}

