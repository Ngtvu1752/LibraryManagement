import org.example.Book;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class BookTest {

    private Book bookWithAllAttributes;
    private Book bookWithQuantityOnly;

    @Before
    public void setUp() {
        // Initialize the book objects before each test
        bookWithAllAttributes = new Book("978-0134685991", "Effective Java", "Joshua Bloch", "English", 10, 2);
        bookWithQuantityOnly = new Book("978-0134685991", "Effective Java", "Joshua Bloch", "English", 10);
    }

    @Test
    public void testBookConstructorWithAllAttributes() {
        // Test constructor with all attributes (including borrowed)
        assertEquals("978-0134685991", bookWithAllAttributes.getIsbn());
        assertEquals("Effective Java", bookWithAllAttributes.getTitle());
        assertEquals("Joshua Bloch", bookWithAllAttributes.getAuthor());
        assertEquals("English", bookWithAllAttributes.getLanguage());
        assertEquals(10, bookWithAllAttributes.getQuantity());
        assertEquals(2, bookWithAllAttributes.getBorrowed());
    }

    @Test
    public void testBookConstructorWithQuantityOnly() {
        // Test constructor with quantity only (borrowed should default to 0)
        assertEquals("978-0134685991", bookWithQuantityOnly.getIsbn());
        assertEquals("Effective Java", bookWithQuantityOnly.getTitle());
        assertEquals("Joshua Bloch", bookWithQuantityOnly.getAuthor());
        assertEquals("English", bookWithQuantityOnly.getLanguage());
        assertEquals(10, bookWithQuantityOnly.getQuantity());
        assertEquals(0, bookWithQuantityOnly.getBorrowed());  // Default value for borrowed
    }

    @Test
    public void testGetters() {
        // Test all getter methods for the book object
        assertEquals("978-0134685991", bookWithAllAttributes.getIsbn());
        assertEquals("Effective Java", bookWithAllAttributes.getTitle());
        assertEquals("Joshua Bloch", bookWithAllAttributes.getAuthor());
        assertEquals("English", bookWithAllAttributes.getLanguage());
        assertEquals(10, bookWithAllAttributes.getQuantity());
        assertEquals(2, bookWithAllAttributes.getBorrowed());
    }

    @Test
    public void testGettersForBookWithQuantityOnly() {
        // Test getter methods for the book object with only quantity
        assertEquals("978-0134685991", bookWithQuantityOnly.getIsbn());
        assertEquals("Effective Java", bookWithQuantityOnly.getTitle());
        assertEquals("Joshua Bloch", bookWithQuantityOnly.getAuthor());
        assertEquals("English", bookWithQuantityOnly.getLanguage());
        assertEquals(10, bookWithQuantityOnly.getQuantity());
        assertEquals(0, bookWithQuantityOnly.getBorrowed());  // borrowed is 0 by default
    }
}
