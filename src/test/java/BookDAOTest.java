import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import javafx.collections.ObservableList;
import org.example.Book;
import org.example.BookDAO;
import org.example.DatabaseHelper;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.List;
import java.util.Optional;

public class BookDAOTest {

    @Mock private DatabaseHelper mockDbHelper;  // Mock the DatabaseHelper
    @Mock private Connection mockConnection;    // Mock the Database connection
    @Mock private PreparedStatement mockPreparedStatement; // Mock the PreparedStatement
    @Mock private ResultSet mockResultSet;      // Mock the ResultSet

    private BookDAO bookDAO;  // The BookDAO instance to test

    @Before
    public void setUp() throws SQLException, NoSuchFieldException, IllegalAccessException {
        // Initialize mocks
        MockitoAnnotations.initMocks(this);

        // Mock the behavior of DatabaseHelper and Connection
        when(mockDbHelper.connect()).thenReturn(mockConnection);  // Mock database connection
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);  // Mock PreparedStatement creation

        // Initialize BookDAO
        bookDAO = new BookDAO();

        // Use reflection to inject the mock DatabaseHelper into the BookDAO
        setDbHelperUsingReflection(bookDAO, mockDbHelper);
    }

    private void setDbHelperUsingReflection(BookDAO bookDAO, DatabaseHelper dbHelper) throws NoSuchFieldException, IllegalAccessException {
        // Access the 'dbHelper' field in BookDAO class
        Field dbHelperField = BookDAO.class.getDeclaredField("dbHelper");
        dbHelperField.setAccessible(true); // Allow access to private fields
        dbHelperField.set(bookDAO, dbHelper); // Inject the mock dbHelper
    }

    @Test
    public void testGetAll() throws SQLException {
        // Arrange
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true).thenReturn(false); // Simulate one result

        // Mock the data returned by the ResultSet
        when(mockResultSet.getString("ISBN")).thenReturn("1234567890");
        when(mockResultSet.getString("TITLE")).thenReturn("Book Title");
        when(mockResultSet.getString("AUTHOR")).thenReturn("Author Name");
        when(mockResultSet.getString("LANGUAGE")).thenReturn("English");
        when(mockResultSet.getInt("QUANTITY")).thenReturn(10);
        when(mockResultSet.getInt("Borrowed")).thenReturn(2);

        // Act
        List<Book> books = bookDAO.getAll();

        // Assert
        assertEquals(1, books.size());
        Book book = books.get(0);
        assertEquals("1234567890", book.getIsbn());
        assertEquals("Book Title", book.getTitle());
        assertEquals("Author Name", book.getAuthor());
        assertEquals("English", book.getLanguage());
        assertEquals(10, book.getQuantity());
        assertEquals(2, book.getBorrowed());
    }

    @Test
    public void testFindByTitle_Found() throws SQLException {
        // Arrange
        String searchTitle = "Book Title";
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true).thenReturn(false); // Simulate one result

        // Mock ResultSet return data
        when(mockResultSet.getString("ISBN")).thenReturn("1234567890");
        when(mockResultSet.getString("TITLE")).thenReturn("Book Title");
        when(mockResultSet.getString("AUTHOR")).thenReturn("Author Name");
        when(mockResultSet.getString("LANGUAGE")).thenReturn("English");
        when(mockResultSet.getInt("QUANTITY")).thenReturn(10);
        when(mockResultSet.getInt("Borrowed")).thenReturn(2);

        // Act
        Optional<Book> bookOpt = bookDAO.findByTitle(searchTitle);

        // Assert
        assertTrue(bookOpt.isPresent());
        Book book = bookOpt.get();
        assertEquals("1234567890", book.getIsbn());
        assertEquals("Book Title", book.getTitle());
    }

    @Test
    public void testFindByTitle_NotFound() throws SQLException {
        // Arrange
        String searchTitle = "Nonexistent Title";
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false); // No result

        // Act
        Optional<Book> bookOpt = bookDAO.findByTitle(searchTitle);

        // Assert
        assertFalse(bookOpt.isPresent());
    }

    @Test
    public void testSave() throws SQLException {
        // Arrange
        Book bookToSave = new Book("1234567890", "Book Title", "Author", "English", 10, 2);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1); // Simulate success

        // Act
        boolean result = bookDAO.save(bookToSave);

        // Assert
        assertTrue(result);
        verify(mockPreparedStatement).setString(1, "1234567890");
        verify(mockPreparedStatement).setString(2, "Book Title");
    }

    @Test
    public void testSave_Failure() throws SQLException {
        // Arrange
        Book bookToSave = new Book("1234567890", "Book Title", "Author", "English", 10, 2);
        when(mockPreparedStatement.executeUpdate()).thenThrow(new SQLException("SQL error")); // Simulate failure

        // Act
        boolean result = bookDAO.save(bookToSave);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testGetObservableList() throws SQLException {
        // Arrange
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true).thenReturn(false); // Mock one result

        // Mock the data returned by the ResultSet
        when(mockResultSet.getString("ISBN")).thenReturn("1234567890");
        when(mockResultSet.getString("TITLE")).thenReturn("Book Title");
        when(mockResultSet.getString("AUTHOR")).thenReturn("Author Name");
        when(mockResultSet.getString("LANGUAGE")).thenReturn("English");
        when(mockResultSet.getInt("QUANTITY")).thenReturn(10);

        // Act
        ObservableList<Book> books = bookDAO.getObservableList();

        // Assert
        assertEquals(1, books.size());
        Book book = books.get(0);
        assertEquals("1234567890", book.getIsbn());
        assertEquals("Book Title", book.getTitle());
    }
}
