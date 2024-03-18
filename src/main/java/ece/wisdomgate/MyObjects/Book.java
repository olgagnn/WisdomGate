package ece.wisdomgate.MyObjects;

import java.io.Serializable;

public class Book implements Serializable {

    private String title, author, publisher, category, ISBN;
    private Integer year_published, number_of_copies, rating = 0, users_rated = 0;

    // 1 constructor with the default value 0 for rating by the Admin
    public Book(String title, String author, String publisher, String category,
                String ISBN, Integer year_published, Integer number_of_copies) {

        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.category = category;
        this.ISBN = ISBN;
        this.year_published = year_published;
        this.number_of_copies = number_of_copies;
        this.rating = 0;
        this.users_rated = 0;
    }

    public Book(String title, String author, String publisher, String category,
                String ISBN, Integer year_published, Integer number_of_copies, Integer rating, Integer users_rated) {

        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.category = category;
        this.ISBN = ISBN;
        this.year_published = year_published;
        this.number_of_copies = number_of_copies;
        this.rating = rating;
        this.users_rated = users_rated;
    }

    public Book(String title, String ISBN, String category) {
        this.title = title;
        this.ISBN = ISBN;
        this.category = category;
    }

    public Book(String ISBN) {
        this.ISBN = ISBN;
    }

    // Methods for modifications by the Admin
    public void setTitle(String title) {
        this.title = title;
    }
    public void setAuthor(String author) {
        this.author = author;
    }
    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }
    public void setCategory(String category) {
        this.category = category;
    }
    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }
    public void setYearPublished(Integer yearPublished) {
        this.year_published = yearPublished;
    }
    public void setNumberOfCopies(Integer numberOfCopies) {
        this.number_of_copies = numberOfCopies;
    }
    public String getTitle() {
        return this.title;
    }
    public String getAuthor() {
        return this.author;
    }
    public String getPublisher() {
        return this.publisher;
    }
    public String getCategory() {
        return this.category;
    }
    public String getISBN() {
        return this.ISBN;
    }
    public Integer getYearPublished() {
        return this.year_published;
    }
    public Integer getNumberOfCopies() {
        return this.number_of_copies;
    }
    public Integer getRating() {
        return this.rating;
    }
    public Integer getUsersRated() {
        return this.users_rated;
    }

    // Override toString() method to provide a custom string representation
    @Override
    public String toString() {
        return String.format("%s,%s,%s,%s,%s,%d,%d,%d",
                title, author, publisher, category, ISBN, year_published, number_of_copies, rating);
    }

    public void rateBook(Integer userRating) {
        // Update the total rating and the number of users who rated
        this.rating = (this.rating + userRating) / (this.users_rated + 1);

        // Increment the number of users who rated
        this.users_rated++;
    }

}