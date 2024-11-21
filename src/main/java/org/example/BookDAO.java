package org.example;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BookDAO implements DAO<Book> {
    private final DatabaseHelper dbHelper;
    private static final String INSERT = "insert into book values(?,?,?,?,?,?)";
    private static final String SELECT_ALL = "select * from book";
    private static final String DISPLAY_ALL_BOOKS = "select isbn,title, author, language, quantity from book";
    private static final String DELETE_BOOK = "delete from book where ISBN = ?";
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
        try(Connection conn = dbHelper.connect();
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
        }
        catch (SQLException e) {
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
            pstmt.executeUpdate();
            System.out.println("Add book successfully");
            return true;
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public void delete(Book book) {
    }
}
