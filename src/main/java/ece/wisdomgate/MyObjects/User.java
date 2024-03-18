package ece.wisdomgate.MyObjects;

import java.io.Serializable;

public class User implements Serializable {

    private String username, password, first_name, last_name, ID_number, email;
    private Integer num_books_borrowed;

    // Constructor with additional fields
    public User(String username, String password, String first_name, String last_name,
                String ID_number, String email, Integer num_books_borrowed) {
        this.username = username;
        this.password = password;
        this.first_name = first_name;
        this.last_name = last_name;
        this.ID_number = ID_number;
        this.email = email;
        this.num_books_borrowed = num_books_borrowed;
    }

    public User(String username, String password, String first_name, String last_name,
                String ID_number, String email) {
        this.username = username;
        this.password = password;
        this.first_name = first_name;
        this.last_name = last_name;
        this.ID_number = ID_number;
        this.email = email;
        this.num_books_borrowed = 0;
    }

    public User(String username, String ID_number){
        this.username = username;
        this.ID_number = ID_number;
    }

    public User(String username){
        this.username = username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void setFirstName(String first_name) {
        this.first_name = first_name;
    }
    public void setLastName(String last_name) {
        this.last_name = last_name;
    }
    public void setIDNumber(String ID_number) {
        this.ID_number = ID_number;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setNumBooksBorrowed(Integer num_books_borrowed) {
        this.num_books_borrowed = num_books_borrowed;
    }

    public String getUsername() {
        return this.username;
    }
    public String getPassword() {
        return this.password;
    }
    public String getFirstName() {
        return this.first_name;
    }
    public String getLastName() {
        return this.last_name;
    }
    public String getIDNumber() {
        return this.ID_number;
    }
    public String getEmail() {
        return this.email;
    }
    public Integer getNumBooksBorrowed() { return this.num_books_borrowed; }

    // Override toString for better representation

    @Override
    public String toString() {
        return String.format("%s,%s,%s,%s,%s,%s,%s",
                username, password, first_name, last_name, ID_number, email);
    }

}
