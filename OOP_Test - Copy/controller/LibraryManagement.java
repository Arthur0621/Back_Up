package controller;
import model.Book;
import model.Borrower;

import java.util.ArrayList;



interface LibraryManagement {
    public void login();
    public void register(Borrower Borrower);
    public void logout();
    public void addBook(Book book);
    public void borrowBook(int bookId, Borrower borrower, int quantityToBorrow);
    public void returnBook(int bookId, int quantityToReturn);
    public void displayBooks();
    ArrayList<Book> searchBook(String query);

    void saveToDatabase();

    void loadFromDatabase();
}