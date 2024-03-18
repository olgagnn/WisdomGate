package ece.wisdomgate.MyObjects;

import java.io.Serializable;
public class Category implements Serializable {

    private String category;

    // Constructor
    public Category(String category) {
        this.category = category;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return category;
    }
}