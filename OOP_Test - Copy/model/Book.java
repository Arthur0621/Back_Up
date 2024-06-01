package model;

import model.LibraryItem;

public class Book extends LibraryItem {
    private String author;
    private int yearPublished;
    private boolean borrowed;
    private int quantity;
    public Book() {
        super();
    }

    public Book(int itemId, String title, String author, int yearPublished) {
        super(itemId, title);
        this.author = author;
        this.yearPublished = yearPublished;
        this.borrowed = false;
    }

    public Book(int itemId, String title, String author, int yearPublished, int quantity) {
        super(itemId, title);
        this.author = author;
        this.yearPublished = yearPublished;
        this.borrowed = false;
        this.quantity = quantity;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAuthor() {
        return author;
    }

    public int getYearPublished() {
        return yearPublished;
    }

    public void setYearPublished(int yearPublished) {
        this.yearPublished = yearPublished;
    }

    public boolean isBorrowed() {
        return borrowed;
    }

    public void setBorrowed(boolean borrowed) {
        this.borrowed = borrowed;
    }

    public int getQuantity() {
        return quantity;
    }
    
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
