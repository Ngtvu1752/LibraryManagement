package org.example;

public class Book {
    private String isbn;
    private String title;
    private String author;
    private String language;
    private int quantity;
    private int borrowed;

    public Book (String ISPN, String title, String author, String language, int quantity, int borrowed) {
        this.isbn  = ISPN;
        this.title = title;
        this.author = author;
        this.language = language;
        this.quantity = quantity;
        this.borrowed = borrowed;
    }

    public Book(String ISPN, String title, String author, String language, int quantity) {
        this.isbn = ISPN;
        this.title = title;
        this.author = author;
        this.language = language;
        this.quantity = quantity;
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
}
