package org.example;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BookDAO implements DAO<Book> {
    private final DatabaseHelper dbHelper;
    private static BookDAO instance;
    private static final String INSERT = "insert into book values(?,?,?,?,?,?,?,0,0)";
    private static final String SELECT_ALL = "select * from book";
    private static final String DISPLAY_ALL_BOOKS = "select isbn,title, author, language, quantity from book";
    private static final String DELETE_BOOK = "delete from book where ISBN = ?";
    private static final String FIND_BY_TITLE = "select * from book where title like ?";
    private static final String query = "SELECT image_url FROM BOOK WHERE ISBN = ?";
    private static final String UPDATE_RATING_SCORE = "UPDATE book SET ratingScore = ratingScore + ? WHERE ISBN = ?";
    private static final String UPDATE_RATING_COUNT = "UPDATE book SET ratingCount = ratingCount + 1 WHERE ISBN = ?";
    public BookDAO() {
        this.dbHelper = DatabaseHelper.getInstance();
    }

    public static BookDAO getInstance() {
        if (instance == null) {
            synchronized (BookDAO.class) {
                if (instance == null) {
                    instance = new BookDAO();
                }
            }
        }
        return instance;
    }

    public List<Book> getAll() {
        List<Book> books = new ArrayList<Book>();
        try (Connection conn = dbHelper.connect();
             PreparedStatement pstmt = conn.prepareStatement(SELECT_ALL)) {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String isbn = rs.getString("ISBN");
                String title = rs.getString("TITLE");
                String author = rs.getString("AUTHOR");
                String language = rs.getString("LANGUAGE");
                int quantity = rs.getInt("QUANTITY");
                int borrowed = rs.getInt("Borrowed");
                books.add(new Book(isbn, title, author, language, quantity, borrowed));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return books;
    }

    @Override
    public ObservableList<Book> getObservableList() {
        ObservableList<Book> books = FXCollections.observableArrayList();
        try (Connection conn = dbHelper.connect();
             PreparedStatement stmt = conn.prepareStatement(DISPLAY_ALL_BOOKS)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String isbn = rs.getString("ISBN");
                String title = rs.getString("TITLE");
                String author = rs.getString("AUTHOR");
                String language = rs.getString("LANGUAGE");
                int quantity = rs.getInt("QUANTITY");

                Book book = new Book(isbn, title, author, language, quantity);
                books.add(book);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("Database query failed");
        }
        return books;
    }

    public Optional<Book> findByTitle(String title) {
        try (Connection conn = dbHelper.connect();
             PreparedStatement pstmt = conn.prepareStatement(FIND_BY_TITLE)) {
            pstmt.setString(1, title);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String isbn = rs.getString("ISBN");
                String author = rs.getString("AUTHOR");
                String language = rs.getString("LANGUAGE");
                int quantity = rs.getInt("QUANTITY");
                int borrowed = rs.getInt("Borrowed");
                return Optional.of(new Book(isbn, title, author, language, quantity, borrowed));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return Optional.empty();
    }

    public boolean save(Book book) {
        try (Connection conn = dbHelper.connect();
             PreparedStatement pstmt = conn.prepareStatement(INSERT)) {
            pstmt.setString(1, book.getIsbn());
            pstmt.setString(2, book.getTitle());
            pstmt.setString(3, book.getAuthor());
            pstmt.setString(4, book.getLanguage());
            pstmt.setInt(5, book.getQuantity());
            pstmt.setInt(6, book.getBorrowed());
            pstmt.setString(7, book.getImageUrl());
            pstmt.executeUpdate();
            System.out.println("Add book successfully");
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public String getImageUrl(String isbn) {
        try (Connection conn = dbHelper.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, isbn);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getString("image_url");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int getRatingCount(String isbn) {
        int ratingCount = 0;
        String str2 = "SELECT ratingCount FROM book WHERE ISBN = ?";
        try (Connection conn = dbHelper.connect();
             PreparedStatement pstmt = conn.prepareStatement(str2)) {
            pstmt.setString(1, isbn);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                ratingCount = rs.getInt("ratingCount");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ratingCount;
    }
    public int getRatingScore(String isbn) {
        String str2 = "SELECT ratingScore FROM book WHERE ISBN = ?";
        int ratingScore = 0;
        try (Connection conn = dbHelper.connect();
             PreparedStatement pstmt = conn.prepareStatement(str2)) {
            pstmt.setString(1, isbn);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                ratingScore = rs.getInt("ratingScore");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ratingScore;
    }
    public boolean delete(Book book) {
        // Kiểm tra xem sách có đang được mượn hay không.
        String checkBorrowedSql = "SELECT Borrowed FROM book WHERE ISBN = ?";
        try (Connection conn = dbHelper.connect();
             PreparedStatement checkStmt = conn.prepareStatement(checkBorrowedSql)) {
            checkStmt.setString(1, book.getIsbn());
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next()) {
                int borrowed = rs.getInt("Borrowed");
                if (borrowed > 0) {
                    // Nếu có được mượn, không thể xóa.
                    System.out.println("Cannot delete the book. It is currently borrowed.");
                    return false;
                }
            } else {
                // Sách không tìm thấy trong database.
                System.out.println("Book not found in the database.");
                return false;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
        // Nếu không được mượn, xóa sách
        try (Connection conn = dbHelper.connect();
             PreparedStatement deleteStmt = conn.prepareStatement(DELETE_BOOK)) {
            deleteStmt.setString(1, book.getIsbn());
            int rowsAffected = deleteStmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Book deleted successfully.");
                return true;  // cuốn sách đã bị xóa.
            } else {
                System.out.println("No rows affected. Book not found or already deleted.");
                return false;  // sách không được tìm thấy
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
    // Cập nhật ratingCount mỗi khi người dùng bấm vào Rating
    public boolean incrementRatingCount(String isbn) {
        try (Connection conn = dbHelper.connect();
             PreparedStatement pstmt = conn.prepareStatement(UPDATE_RATING_COUNT)) {
            pstmt.setString(1, isbn);  // Sử dụng ISBN để xác định sách
            int rowsAffected = pstmt.executeUpdate();  // Cập nhật ratingCount
            return rowsAffected > 0;  // Nếu có ít nhất 1 dòng được cập nhật, trả về true
        } catch (SQLException e) {
            System.out.println("Error updating rating count: " + e.getMessage());
            return false;
        }
    }
    // Cộng điểm vào ratingScore
    public boolean addRatingScore(String isbn, int rating, int userId) {
        // Kiểm tra xem người dùng đã đánh giá chưa
        String ratedUsers = getRatedUsers(isbn);

        if (ratedUsers != null && ratedUsers.contains(String.valueOf(userId))) {
            // Nếu người dùng đã đánh giá, không thực hiện cập nhật
            System.out.println("User " + userId + " has already rated this book.");
            return false;
        }

        // Nếu người dùng chưa đánh giá, cập nhật ratingScore
        try (Connection conn = dbHelper.connect();
             PreparedStatement pstmt = conn.prepareStatement(UPDATE_RATING_SCORE)) {
            pstmt.setInt(1, rating);  // Thêm điểm đánh giá vào ratingScore
            pstmt.setString(2, isbn);  // Sử dụng ISBN để xác định sách
            int rowsAffected = pstmt.executeUpdate();  // Cập nhật ratingScore
            incrementRatingCount(isbn);
            if (rowsAffected > 0) {
                // Sau khi cập nhật ratingScore, thêm user vào danh sách đã đánh giá
                addRatedUser(isbn, userId); // Cập nhật RatedUser
                return true;  // Nếu có ít nhất 1 dòng được cập nhật, trả về true
            }
        } catch (SQLException e) {
            System.out.println("Error updating rating score: " + e.getMessage());
        }
        return false;
    }

    public String getRatedUsers(String isbn) {
        String s = "SELECT RatedUser FROM book WHERE ISBN = ?";
        try (Connection conn = dbHelper.connect();
             PreparedStatement pstmt = conn.prepareStatement(s)) {
            pstmt.setString(1, isbn);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getString("RatedUser");
            }
        } catch (SQLException e) {
            System.out.println("Error fetching RatedUser: " + e.getMessage());
        }
        return null;
    }
    public boolean updateRatedUsers(String isbn, String updatedRatedUsers) {
        String s = "UPDATE book SET RatedUser = ? WHERE ISBN = ?";
        try (Connection conn = dbHelper.connect();
             PreparedStatement pstmt = conn.prepareStatement(s)) {
            pstmt.setString(1, updatedRatedUsers); // Cập nhật danh sách RatedUser
            pstmt.setString(2, isbn); // Xác định sách qua ISBN
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0; // Nếu có ít nhất 1 dòng được cập nhật, trả về true
        } catch (SQLException e) {
            System.out.println("Error updating RatedUser: " + e.getMessage());
            return false;
        }
    }
    public boolean addRatedUser(String isbn, int userId) {String ratedUsers = getRatedUsers(isbn);

        if (ratedUsers != null && ratedUsers.contains(String.valueOf(userId))) {
            // Nếu người dùng đã đánh giá sách, không cho phép đánh giá lại
            System.out.println("User " + userId + " has already rated this book.");
            return false;
        }

        // Nếu danh sách trống, thêm userId vào
        if (ratedUsers == null || ratedUsers.isEmpty()) {
            ratedUsers = String.valueOf(userId);
        } else {
            ratedUsers += "," + userId;
        }
        return updateRatedUsers(isbn, ratedUsers);
    }

}
