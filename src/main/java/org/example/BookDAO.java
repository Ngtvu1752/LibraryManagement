package org.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BookDAO implements DAO<Book> {
    private final DatabaseHelper dbHelper;
    private static final String INSERT = "insert into book values(?,?,?,?,?,?,?)";
    private static final String SELECT_ALL = "select * from book";
    private static final String DISPLAY_ALL_BOOKS = "select title, author, publisher, subject, language from book";
    private static final String DELETE_BOOK = "delete from book where ISBN = ?";
    private static final String SEARCH_BOOK = "select * from book where title like ?";
    private static final String FIND_BY_TITLE = "select * from book where title like ?";
    public BookDAO() {
        this.dbHelper = DatabaseHelper.getInstance();
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
                String subject = rs.getString("SUBJECT");
                String language = rs.getString("LANGUAGE");
                int quantity = rs.getInt("QUANTITY");
                int isAvailable = rs.getInt("isAvail");
                int borrowed = rs.getInt("Borrowed");
                books.add(new Book(isbn, title, author, subject, language, quantity, isAvailable, borrowed));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
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
                String bookTitle = rs.getString("TITLE");
                String author = rs.getString("AUTHOR");
                String subject = rs.getString("SUBJECT");
                String language = rs.getString("LANGUAGE");
                int quantity = rs.getInt("QUANTITY");
                int isAvailable = rs.getInt("isAvail");
                int borrowed = rs.getInt("Borrowed");
                return Optional.of(new Book(isbn, title, author, subject, language, quantity, isAvailable, borrowed));
            }
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return Optional.empty();
    }
    public boolean save(Book book) {
        try (Connection conn = dbHelper.connect();
         PreparedStatement pstmt = conn.prepareStatement(INSERT)) {
            pstmt.setString(1, book.getISPN());
            pstmt.setString(2, book.getTitle());
            pstmt.setString(3, book.getAuthor());
            pstmt.setString(5, book.getSubject());
            pstmt.setString(6, book.getLanguage());
            pstmt.setInt(7, book.getQuantity());
            pstmt.executeQuery();
            System.out.println("Add book successfully");
            return true;
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
}
