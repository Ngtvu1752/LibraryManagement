package org.example;

import java.sql.Date;

public class IssueBookDBHistory {
    private int issueBookID;
    private String isbn;
    private int studentId;
    private String studentName;
    private String className;
    private String schoolName;
    private String title;
    private Date borrowDate;
    private Date returnDate;
    private Date dueDate;
    private String status;
    private double lateFee;

    public IssueBookDBHistory(int issueBookID, int studentId, String isbn, Date borrowDate, Date returnDate, Date dueDate, String status, double lateFee) {
        this.issueBookID = issueBookID;
        this.studentId = studentId;
        this.isbn = isbn;
        this.borrowDate = borrowDate;
        this.returnDate = returnDate;
        this.dueDate = dueDate;
        this.status = status;
        this.lateFee = lateFee;
    }

    public IssueBookDBHistory(int issueBookID, String isbn, String title, Date dueDate, String status) {
        this.isbn = isbn;
        this.issueBookID = issueBookID;
        this.title = title;
        this.dueDate = dueDate;
        this.status = status;
    }

    public IssueBookDBHistory(int id, String name, String className, String schoolName, String isbn, Date dueDate, String status, double lateFee) {
        this.studentId = id;
        this.studentName = name;
        this.className = className;
        this.schoolName = schoolName;
        this.isbn = isbn;
        this.dueDate = dueDate;
        this.status = status;
        this.lateFee = lateFee;
    }

    public int getIssueBookID() {
        return issueBookID;
    }

    public void setIssueBookID(int issueBookID) {
        this.issueBookID = issueBookID;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getBorrowDate() {
        return borrowDate;
    }

    public void setBorrowDate(Date borrowDate) {
        this.borrowDate = borrowDate;
    }

    public Date getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getLateFee() {
        return lateFee;
    }

    public void setLateFee(double lateFee) {
        this.lateFee = lateFee;
    }
}
