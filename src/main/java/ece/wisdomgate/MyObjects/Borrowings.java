package ece.wisdomgate.MyObjects;

import java.time.LocalDate;

public class Borrowings {
    private User user;
    private Book book;
    private LocalDate borrowDate;
    private LocalDate returnDate;

    public Borrowings(User user, Book book, LocalDate borrowDate, LocalDate returnDate) {
        this.user = user;
        this.book = book;
        this.borrowDate = borrowDate;
        this.returnDate = returnDate;
    }

    // Setters and Getters
    public void setUser(User user) {
        this.user = user;
    }
    public void setBook(Book book) {
        this.book = book;
    }
    public void setBorrowDate(LocalDate borrowDate) {
        this.borrowDate = borrowDate;
    }
    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public User getUser() { return this.user;}
    public Book getBook() {
        return this.book;
    }
    public LocalDate getBorrowDate() {
        return this.borrowDate;
    }
    public LocalDate getReturnDate() {
        return this.returnDate;
    }

}
