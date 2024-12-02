package org.example;

public class Book {
    private String isbn;
    private String title;
    private String author;
    private String language;
    private int quantity;
    private int borrowed;
    private String imageUrl;

    public Book (String ISPN, String title, String author, String language, int quantity, int borrowed) {
        this.isbn  = ISPN;
        this.title = title;
        this.author = author;
        this.language = language;
        this.quantity = quantity;
        this.borrowed = borrowed;
        this.imageUrl = null;
    }

    public Book(String ISPN, String title, String author, String language, int quantity) {
        this.isbn = ISPN;
        this.title = title;
        this.author = author;
        this.language = language;
        this.quantity = quantity;
        this.imageUrl = imageUrl;
    }

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

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
