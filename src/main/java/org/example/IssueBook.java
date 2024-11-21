package org.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;

public class IssueBook {
    private DatabaseHelper dbHelper = DatabaseHelper.getInstance();

    public boolean borrowBook(int userId, String isbn) {
        String checkAvailabilitySql = "Select QUANTITY, Borrowed from BOOK where isbn = ?";
        String insertIssueBooksql = "INSERT INTO issuebook(student_id,ISBN,borrow_date,due_date,is_returned) VALUES ( ?, ?, ?,?,?)";
        String updateBookSql = "UPDATE BOOK SET Borrowed = Borrowed + 1 WHERE ISBN = ?";

        try (Connection conn = dbHelper.connect()) {
            //ktra còn sách không
            try (PreparedStatement checkStmt = conn.prepareStatement(checkAvailabilitySql)) {
                checkStmt.setString(1, isbn);
                ResultSet rs = checkStmt.executeQuery();
                if(rs.next()) {
                    int available = rs.getInt("Quantity") - rs.getInt("Borrowed");
                    if(available <= 0) {
                        return false;
                    }
                } else {
                    return false;
                }
            }
            try (PreparedStatement insertStmt = conn.prepareStatement(insertIssueBooksql)) {
                insertStmt.setInt(1, userId);
                insertStmt.setString(2, isbn);
                insertStmt.setDate(3, new java.sql.Date(System.currentTimeMillis()));  // Set borrow date to current date

                // Set return date to 1 month after borrow date
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(System.currentTimeMillis());
                calendar.add(Calendar.MONTH, 12);  // Add 1 month to the current date
                Date dueDate = new java.sql.Date(calendar.getTimeInMillis());
                insertStmt.setDate(4, (java.sql.Date) dueDate);
                insertStmt.setBoolean(5, false);  // Not yet returned
                insertStmt.executeUpdate();
            }

            try(PreparedStatement updateStmt = conn.prepareStatement(updateBookSql)) {
                updateStmt.setString(1, isbn);
                updateStmt.executeUpdate();
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            return false;
        }
    }

    public boolean returnBook(int userId, String isbn) {
        return false;
    }

}
