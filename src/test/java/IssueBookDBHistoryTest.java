import org.example.Database.IssueBookDBHistory;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.sql.Date;

public class IssueBookDBHistoryTest {
    private IssueBookDBHistory issueBookDBHistory;
    private Date borrowDate;
    private Date returnDate;
    private Date dueDate;

    @Before
    public void setUp() {
        // Initialize sample dates and IssueBookDBHistory object for testing
        borrowDate = Date.valueOf("2024-11-01");
        returnDate = Date.valueOf("2024-11-15");
        dueDate = Date.valueOf("2024-11-14");

        issueBookDBHistory = new IssueBookDBHistory(1, 101, "978-3-16-148410-0", borrowDate, returnDate, dueDate, "Returned", 5.0);
    }

    @Test
    public void testIssueBookID() {
        // Test the get and set for issueBookID
        issueBookDBHistory.setIssueBookID(2);
        assertEquals(2, issueBookDBHistory.getIssueBookID());
    }

    @Test
    public void testIsbn() {
        // Test the get and set for ISBN
        issueBookDBHistory.setIsbn("978-1-234-56789-0");
        assertEquals("978-1-234-56789-0", issueBookDBHistory.getIsbn());
    }

    @Test
    public void testStudentId() {
        // Test the get and set for studentId
        issueBookDBHistory.setStudentId(102);
        assertEquals(102, issueBookDBHistory.getStudentId());
    }

    @Test
    public void testTitle() {
        // Test the get and set for title
        issueBookDBHistory.setTitle("The Great Book");
        assertEquals("The Great Book", issueBookDBHistory.getTitle());
    }

    @Test
    public void testBorrowDate() {
        // Test the get and set for borrowDate
        issueBookDBHistory.setBorrowDate(Date.valueOf("2024-10-01"));
        assertEquals(Date.valueOf("2024-10-01"), issueBookDBHistory.getBorrowDate());
    }

    @Test
    public void testReturnDate() {
        // Test the get and set for returnDate
        issueBookDBHistory.setReturnDate(Date.valueOf("2024-11-10"));
        assertEquals(Date.valueOf("2024-11-10"), issueBookDBHistory.getReturnDate());
    }

    @Test
    public void testDueDate() {
        // Test the get and set for dueDate
        issueBookDBHistory.setDueDate(Date.valueOf("2024-11-12"));
        assertEquals(Date.valueOf("2024-11-12"), issueBookDBHistory.getDueDate());
    }

    @Test
    public void testStatus() {
        // Test the get and set for status
        issueBookDBHistory.setStatus("Overdue");
        assertEquals("Overdue", issueBookDBHistory.getStatus());
    }

    @Test
    public void testLateFee() {
        // Test the get and set for lateFee
        issueBookDBHistory.setLateFee(10.0);
        assertEquals(10.0, issueBookDBHistory.getLateFee(), 0.01);
    }

    @Test
    public void testConstructorWithAllFields() {
        // Test the constructor with all fields
        IssueBookDBHistory issueBook = new IssueBookDBHistory(2, 103, "978-1-234-56789-0", Date.valueOf("2024-10-10"), Date.valueOf("2024-10-20"), Date.valueOf("2024-10-15"), "Returned", 3.5);
        assertEquals(2, issueBook.getIssueBookID());
        assertEquals(103, issueBook.getStudentId());
        assertEquals("978-1-234-56789-0", issueBook.getIsbn());
        assertEquals("Returned", issueBook.getStatus());
        assertEquals(3.5, issueBook.getLateFee(), 0.01);
    }

    @Test
    public void testConstructorWithPartialFields() {
        // Test the constructor with some fields (status and dueDate)
        IssueBookDBHistory issueBook = new IssueBookDBHistory(3, "978-1-567-89012-3", "Another Book", Date.valueOf("2024-12-01"), "Issued");
        assertEquals(3, issueBook.getIssueBookID());
        assertEquals("Another Book", issueBook.getTitle());
        assertEquals("978-1-567-89012-3", issueBook.getIsbn());
        assertEquals(Date.valueOf("2024-12-01"), issueBook.getDueDate());
        assertEquals("Issued", issueBook.getStatus());
    }
}
