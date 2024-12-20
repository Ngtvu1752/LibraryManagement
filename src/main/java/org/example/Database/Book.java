package org.example.Database;

public class Book {
    private String isbn;
    private String title;
    private String author;
    private String language;
    private int quantity;
    private int borrowed;
    private String imageUrl;

    /**
     * Constructor 1.
     *
     * @param ISPN     ISBN của cuốn sách.
     * @param title    tiêu đề của cuốn sách.
     * @param author   tác giả của cuốn sách.
     * @param language ngôn ngữ của cuốn sách.
     * @param quantity tổng số lượng của cuốn sách.
     * @param borrowed số cuốn sách đã mượn.
     */
    public Book(String ISPN, String title, String author, String language, int quantity, int borrowed) {
        this.isbn = ISPN;
        this.title = title;
        this.author = author;
        this.language = language;
        this.quantity = quantity;
        this.borrowed = borrowed;
        this.imageUrl = null;
    }

    /**
     * Constructor 2.
     *
     * @param ISPN     ISBN của cuốn sách.
     * @param title    tiêu đề của cuốn sách.
     * @param author   tác giả của cuốn sách.
     * @param language ngôn ngữ của cuốn sách.
     * @param quantity tổng số lượng của cuốn sách.
     */
    public Book(String ISPN, String title, String author, String language, int quantity) {
        this.isbn = ISPN;
        this.title = title;
        this.author = author;
        this.language = language;
        this.quantity = quantity;
        this.imageUrl = imageUrl;
    }

    /**
     * Constructor 3.
     *
     * @param ISPN     ISBN của cuốn sách.
     * @param title    tiêu đề của cuốn sách.
     * @param author   tác giả của cuốn sách.
     * @param language ngôn ngữ của cuốn sách.
     * @param imageUrl URL hình ảnh của cuốn sách.
     * @param quantity tổng số lượng của cuốn sách.
     */
    public Book(String ISPN, String title, String author, String language, String imageUrl, int quantity) {
        this.isbn = ISPN;
        this.title = title;
        this.author = author;
        this.language = language;
        this.quantity = quantity;
        this.imageUrl = imageUrl;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getLanguage() {
        return language;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getBorrowed() {
        return borrowed;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
