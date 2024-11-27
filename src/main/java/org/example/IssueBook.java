package org.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;

public class IssueBook {
    private final DatabaseHelper dbHelper = DatabaseHelper.getInstance();
    private final IssueBookDBHistoryDAO issueBookDBHistoryDAO = IssueBookDBHistoryDAO.getInstance();
    public boolean borrowBook(int userId, String isbn) {
        String checkAvailabilitySql = "Select QUANTITY, Borrowed from BOOK where isbn = ?";
        String insertIssueBooksql = "INSERT INTO issuebook(student_id,ISBN,borrow_date,due_date) VALUES ( ?, ?, ?,?)";
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
    public long returnBook(int userId, String isbn) {
        String selectIssueBookSql = "SELECT borrow_id, borrow_date, due_date, is_returned FROM issuebook WHERE student_id = ? AND isbn = ? AND is_returned = 0";
        String updateIssueBookSql = "UPDATE issuebook SET return_date = ?, is_returned = 'Yes', late_fee = ? WHERE borrow_id = ?";
        String updateBookSql = "UPDATE BOOK SET Borrowed = Borrowed - 1 WHERE ISBN = ?";

        try (Connection conn = dbHelper.connect()) {
            // Lấy bản ghi từ bảng issuebook.
            try (PreparedStatement selectStmt = conn.prepareStatement(selectIssueBookSql)) {
                selectStmt.setInt(1, userId);
                selectStmt.setString(2, isbn);
                ResultSet rs = selectStmt.executeQuery();

                // Nếu không tìm thấy hoặc cuốn sách đã được trả lại.
                if (!rs.next()) {
                    return -1;
                }

                //Lấy ngày mượn và ngày đến hạn
                LocalDate borrowDate = rs.getDate("borrow_date").toLocalDate();
                LocalDate dueDate = rs.getDate("due_date").toLocalDate();
                int borrowId = rs.getInt("borrow_id");

                // Tính phí trễ hạn nếu trả lại sau ngày đáo hạn
                LocalDate returnDate = LocalDate.now(); // Giả sử ngày trả là ngày hiện tại.
                long daysLate = ChronoUnit.DAYS.between(dueDate, returnDate);

                // Nếu cuốn sách được trả muộn, hãy tính phí trễ.
                long lateFee = 0;
                if (daysLate > 0) {
                    lateFee = daysLate * 5000; // 5000 VND/ngày
                }

                //Cập nhật bảng issuebook với ngày trả, phí trễ, và đánh dấu là đã trả.
                try (PreparedStatement updateStmt = conn.prepareStatement(updateIssueBookSql)) {
                    updateStmt.setDate(1, java.sql.Date.valueOf(returnDate));
                    updateStmt.setLong(2, lateFee);
                    updateStmt.setInt(3, borrowId);
                    updateStmt.executeUpdate();
                }

                // Cập nhật bảng sách để giảm số lượng sách đã mượn.
                try (PreparedStatement updateBookStmt = conn.prepareStatement(updateBookSql)) {
                    updateBookStmt.setString(1, isbn);
                    updateBookStmt.executeUpdate();
                }

                return lateFee;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            return -1;
        }
    }

}
