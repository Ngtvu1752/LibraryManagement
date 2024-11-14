package org.example;

public class Book {
    private String ISPN;
    private String title;
    private String author;
    private String subject;
    private String language;
    private int quantity;
    private int isAvail;
    private int borrowed;

    public Book (String ISPN, String title, String author, String subject, String language, int quantity, int isAvail, int borrowed) {
        this.ISPN = ISPN;
        this.title = title;
        this.author = author;
        this.subject = subject;
        this.language = language;
        this.quantity = quantity;
        this.isAvail = isAvail;
        this.borrowed = borrowed;
    }

    public Book(String ISPN, String title, String author, String subject, String language, int quantity) {
        this.ISPN = ISPN;
        this.title = title;
        this.author = author;
        this.subject = subject;
        this.language = language;
        this.quantity = quantity;
    }

    public String getISPN() {
        return ISPN;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getSubject() {
        return subject;
    }

    public String getLanguage() {
        return language;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getIsAvail() {
        return isAvail;
    }

    public int getBorrowed() {
        return borrowed;
    }
}
