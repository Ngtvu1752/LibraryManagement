package org.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BorrowService {
    private DatabaseHelper dbHelper = DatabaseHelper.getInstance();

    public boolean borrowBook(int userId, String isbn) {
        String checkAvailabilitySql = "Select QUANTITY, Borrowed from BOOK where isbn = ?";
        String insertBorrowSql = "INSERT INTO BORROW(student_id, isbn, borrow_date, is_returned) VALUES (?, ?, ?, ?)";
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
            try(PreparedStatement insertStmt = conn.prepareStatement(insertBorrowSql)) {
                insertStmt.setInt(1, userId);
                insertStmt.setString(2, isbn);
                insertStmt.setDate(3, new java.sql.Date(System.currentTimeMillis()));
                insertStmt.setBoolean(4, false);
                insertStmt.executeUpdate();
            }
            try(PreparedStatement updateStmt = conn.prepareStatement(updateBookSql)) {
                updateStmt.setString(1, isbn);
                updateStmt.executeQuery();
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean returnBook(int userId, String isbn) {
        return false;
    }

}
