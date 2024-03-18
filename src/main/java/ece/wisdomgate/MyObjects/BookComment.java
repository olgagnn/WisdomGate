package ece.wisdomgate.MyObjects;

import java.io.Serializable;

public class BookComment implements Serializable {

    private User user;
    private Book book;
    private String comment;

    public BookComment(User user, Book book, String comment) {
        this.user = user;
        this.book = book;
        this.comment = comment;
    }

    // Add getters for user, book, and comment
    public User getUser() {
        return user;
    }

    public Book getBook() {
        return book;
    }
    public String getComment() {
        return comment;
    }
}
