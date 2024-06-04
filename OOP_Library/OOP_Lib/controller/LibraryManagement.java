package controller;
import model.Book;
import model.Borrower;
import model.EBook;

import java.util.ArrayList;



interface LibraryManagement {
    public void login();
    public void register(Borrower Borrower);
    public void logout();

    void addBook(Book book);

    void addEBook(EBook ebook);
    void removeBook(String bookId, int quantityToRemove);

    void borrowBook(String bookId, Borrower borrower, int quantityToBorrow);

    void borrowEBook(String bookId, Borrower borrower);

    void returnBook(String bookId, int borrowerId, int quantityToReturn);
    public void displayBooks();


    ArrayList<Book> searchBook(String query);

    void saveAllToDatabase();

    void loadAllFromDatabase();
}