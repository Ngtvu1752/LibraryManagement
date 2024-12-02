package org.example.Database.DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.example.Database.IssueBookDBHistory;
import org.example.Database.User;
import org.example.DatabaseHelper;
import org.example.SessionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class IssueBookDBHistoryDAO implements DAO<IssueBookDBHistory> {
    private final DatabaseHelper dbHelper;
    private static IssueBookDBHistoryDAO instance;
    private static final String TABLE_NAME = "select * from IssueBook";
    private static final String ISSUE_TABLE =
            "select borrow_id, issuebook.ISBN, TITLE, due_date, is_returned\n" +
                    "from issuebook join BOOK B on B.ISBN = issuebook.ISBN\n" +
                    "where student_id = ?";
    private static final String INSERT = "INSERT INTO issuebook(student_id,ISBN,borrow_date,due_date) VALUES ( ?, ?, ?,?)";

    public IssueBookDBHistoryDAO() {
        this.dbHelper = DatabaseHelper.getInstance();
    }

    public static IssueBookDBHistoryDAO getInstance() {
        if (instance == null) {
            synchronized (IssueBookDBHistoryDAO.class) {
                if (instance == null) {
                    instance = new IssueBookDBHistoryDAO();
                }
            }
        }
        return instance;
    }

    public List<IssueBookDBHistory> getAll() {
        List<IssueBookDBHistory> issueBookDBHistories = new ArrayList<IssueBookDBHistory>();
        try (Connection conn = dbHelper.connect();
             PreparedStatement pstmt = conn.prepareStatement(TABLE_NAME)) {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("borrower_id");
                int studentId = rs.getInt("student_id");
                int bookId = rs.getInt("isbn");
                String isbn = rs.getString("isbn");
                Date borrowedDate = rs.getDate("borrowed_date");
                Date returnedDate = rs.getDate("returned_date");
                Date dueDate = rs.getDate("due_date");
                String status = rs.getString("status");
                Double price = rs.getDouble("late_fee");
                issueBookDBHistories.add(new IssueBookDBHistory(id, studentId, isbn, borrowedDate, returnedDate, dueDate, status, price));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return issueBookDBHistories;
    }

    public ObservableList<IssueBookDBHistory> getObservableList() {
        ObservableList<IssueBookDBHistory> issueBookDBHistories = FXCollections.observableArrayList();
        try (Connection conn = dbHelper.connect();
             PreparedStatement stmt = conn.prepareStatement(ISSUE_TABLE)) {
            User currentUser = SessionManager.getCurrentUser();
            if (currentUser == null) {
                throw new IllegalStateException("No user is currently logged in.");
            }
            stmt.setInt(1, currentUser.getId());
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("borrow_id");
                String isbn = rs.getString("isbn");
                String title = rs.getString("title");
                Date dueDate = rs.getDate("due_date");
                String status = rs.getString("is_returned");

                issueBookDBHistories.add(new IssueBookDBHistory(id, isbn, title, dueDate, status));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return issueBookDBHistories;
    }

    public boolean save(IssueBookDBHistory issueBookDBHistory) {
        try (Connection conn = dbHelper.connect();
             PreparedStatement pstmt = conn.prepareStatement(INSERT)) {
            pstmt.setInt(1, issueBookDBHistory.getStudentId());
            pstmt.setString(2, issueBookDBHistory.getIsbn());
            pstmt.setDate(3, issueBookDBHistory.getBorrowDate());
            pstmt.setDate(4, issueBookDBHistory.getDueDate());
            pstmt.executeUpdate();
            System.out.println("Issue Book sucessfully saved.");
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public boolean delete(IssueBookDBHistory issueBookDBHistory) {
        return false;
    }


    public boolean update(IssueBookDBHistory issueBookDBHistory) {
        return false;
    }
}
