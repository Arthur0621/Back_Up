package view;


import controller.Library;
import model.Book;
import model.Borrower;
import model.Borrowing;
import model.EBook;
import model.EBorrowing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Scanner;

public class ShowMenu extends JFrame implements ActionListener {
    private JButton searchButton, displayButton, borrowButton, returnButton, logoutButton, exitButton;
    private JTextField searchField;
    private static Library library;
    private ArrayList<Book> books;
    private ArrayList<EBook> ebooks;
    private ArrayList<Borrower> borrowers;
    private ArrayList<Borrowing> borrowings;
    private ArrayList<EBorrowing> eborrowings;

    public ArrayList<Book> getBooks() {
        return books;
    }

    public ArrayList<EBook> getEBooks() {
        return ebooks;
    }

    public void setBooks(ArrayList<Book> books) {
        this.books = books;
    }

    public ArrayList<Borrower> getBorrowers() {
        return borrowers;
    }

    public void setBorrowers(ArrayList<Borrower> borrowers) {
        this.borrowers = borrowers;
    }

    public ShowMenu() {
        library = new Library();

        setTitle("Library Menu");
        setSize(300, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        // Search Field
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        searchField = new JTextField(20);
        mainPanel.add(searchField, gbc);

        // Search Button
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        searchButton = new JButton("Search Book");
        searchButton.addActionListener(this);
        mainPanel.add(searchButton, gbc);

        // Display Button
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        displayButton = new JButton("Display Books");
        displayButton.addActionListener(this);
        mainPanel.add(displayButton, gbc);

        // Borrow Button
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        borrowButton = new JButton("Borrow Book");
        borrowButton.addActionListener(this);
        mainPanel.add(borrowButton, gbc);

        // Return Button
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        returnButton = new JButton("Return Book");
        returnButton.addActionListener(this);
        mainPanel.add(returnButton, gbc);

        // Logout Button
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        logoutButton = new JButton("Logout");
        logoutButton.addActionListener(this);
        mainPanel.add(logoutButton, gbc);

        // Exit Button
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        exitButton = new JButton("Exit");
        exitButton.addActionListener(this);
        mainPanel.add(exitButton, gbc);

        add(mainPanel);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == searchButton) {
            searchBook();
        } else if (e.getSource() == displayButton) {
            displayBooks();
        } else if (e.getSource() == borrowButton) {
            borrowBook();
        } else if (e.getSource() == returnButton) {
            returnBook();
        } else if (e.getSource() == logoutButton) {
            library.logout();
        } else if (e.getSource() == exitButton) {
            int response = JOptionPane.showConfirmDialog(this, "Are you sure you want to exit?", "Confirm Exit", JOptionPane.YES_NO_OPTION);
            if (response == JOptionPane.YES_OPTION) {
                System.exit(0); // Đóng chương trình nếu người dùng chọn YES
            }
        }
    }

    public static void main(String[] args) {
        ShowMenu menu = new ShowMenu();
        library.loadAllFromDatabase();
    }

    private void searchBook() {
        String query = String.valueOf(library.searchBook(searchField.getText()));
        ArrayList<Book> foundBooks = library.searchBook(query);

        if (foundBooks.isEmpty()) {
            System.out.println("No books found for the given query.");
        } else {
            System.out.println("Books found:");
            for (Book book : foundBooks) {
                System.out.println("ID: " + book.getItemId() + ", Title: " + book.getTitle() + ", Author: " + book.getAuthor() + ", Year Published: " + book.getYearPublished() + ", Quantity: " + book.getQuantity());
            }
        }
    }

    public void displayBooks() {
        if (books.isEmpty() && ebooks.isEmpty()) {
            System.out.println("No books available.");
        } else {
            System.out.println("List of Books:");
            for (Book book : books) {
                System.out.println("ID: " + book.getItemId() + ", Title: " + book.getTitle() + ", Author: " + book.getAuthor() + ", Year Published: " + book.getYearPublished() + ", Quantity: " + book.getQuantity());
            }
            for (EBook ebook : ebooks) {
                System.out.println("ID: " + ebook.getItemId() + ", Title: " + ebook.getTitle() + ", Author: " + ebook.getAuthor() + ", Year Published: " + ebook.getYearPublished() + ", Size " + ebook.getSize() + ", Format: " + ebook.getFormat());
            }
            for (Borrowing borrowing : borrowings) {
                System.out.println("BookId: " + borrowing.getBookId() + ", BorrowerId: " + borrowing.getBorrowerId()+ ", Quantity: " + borrowing.getQuantityBorrow());
            }
            for (EBorrowing eborrowing : eborrowings) {
                System.out.println("BookId: " + eborrowing.getBookId() + ", BorrowerId: ");
            }
        }
    }

    public void borrowBook() {
        JTextField bookIdField = new JTextField();
        JTextField quantityField = new JTextField();
        int newBorrowerId = library.generateBorrowerId();

            Object[] message = {
                    "Book ID/ Title", bookIdField,
                    "Quantity to Borrow", quantityField,
            };

            JOptionPane.showConfirmDialog(this, message, "Register", JOptionPane.OK_CANCEL_OPTION);
            String bookId = bookIdField.getText();
            String quantity = quantityField.getText();

        if (bookId.startsWith("B")) {
            library.borrowBook(bookId, library.getCurrentUser(), Integer.parseInt(quantity));
        } else if (bookId.startsWith("E")) {
            library.borrowEBook(bookId, library.getCurrentUser());
        }
    }

    private void returnBook() {
        JTextField bookIdField = new JTextField();
        JTextField quantityField = new JTextField();

        Object[] message = {
                "Book ID to Return:", bookIdField,
                "Quantity to Return:", quantityField
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Return Book", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String bookId = bookIdField.getText();
            int quantityToReturn = Integer.parseInt(quantityField.getText());
            library.returnBook(bookId, library.getCurrentUser().getBorrowerId(), quantityToReturn);
            JOptionPane.showMessageDialog(this, "You have returned " + quantityToReturn + " copy/copies of the book with ID: " + bookId);
        }
    }

}
