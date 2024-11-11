import org.example.DatabaseHelper;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class DatabaseHelperTest {

    @Test
    public void testConnect() {
        DatabaseHelper dbHelper = DatabaseHelper.getInstance();
        assertNotNull("Kết nối với cơ sở dữ liệu thất bại" ,dbHelper.connect() );
    }
}