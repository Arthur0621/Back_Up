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
import java.util.Random;

public class ShowMenu extends JFrame implements ActionListener {
    private JButton searchButton, displayButton, borrowButton, returnButton, logoutButton, exitButton;
    private JTextField searchField;
    static Library library;
    private static ArrayList<Book> books;
    private static ArrayList<EBook> ebooks;
    private static ArrayList<Borrower> borrowers;
    private static ArrayList<Borrowing> borrowings;
    private static ArrayList<EBorrowing> eborrowings;

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

    public ShowMenu(Library library) {
        ShowMenu.library = new Library();
        this.library = library;
        this.books = ShowMenu.library.getBooks();  // Get book list from Library
        this.ebooks = ShowMenu.library.getEBooks();

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
                System.exit(0);
            }
        }
    }

    public static void main(String[] args) {
        library = new Library();
        //ShowMenuAdmin menuAdmin = new ShowMenuAdmin();
        SwingUtilities.invokeLater(() -> new ShowMenu(library));
    }

    private void searchBook() {
        String query = searchField.getText().trim();
        ArrayList<Book> foundBooks = new ArrayList<>();

        if (query.startsWith("B")) {
            Book foundBook = library.findBookById(query);
            if (foundBook != null) {
                foundBooks.add(foundBook);
            }
        } else if (query.startsWith("E")) {
            EBook foundEBook = library.findEBookById(query);
            if (foundEBook != null) {
                foundBooks.add(foundEBook);
                foundBooks.addAll(library.searchEBook(query));
            }
        } else {
            foundBooks = library.searchBook(query);
        }

        displaySearchResults(foundBooks);
    }

    private void displaySearchResults(ArrayList<Book> foundBooks) {
        StringBuilder bookList = new StringBuilder();

        if (foundBooks.isEmpty()) {
            bookList.append("No books found for the given query.");
        } else {
            for (Book book : foundBooks) {
                if (book instanceof EBook) {
                    EBook ebook = (EBook) book;
                    bookList.append("ID: ").append(ebook.getItemId())
                            .append(", Title: ").append(ebook.getTitle())
                            .append(", Author: ").append(ebook.getAuthor())
                            .append(", Year Published: ").append(ebook.getYearPublished())
                            .append(", Size: ").append(ebook.getSize())
                            .append(", Format: ").append(ebook.getFormat()).append("\n");
                } else {
                    bookList.append("ID: ").append(book.getItemId())
                            .append(", Title: ").append(book.getTitle())
                            .append(", Author: ").append(book.getAuthor())
                            .append(", Year Published: ").append(book.getYearPublished())
                            .append(", Quantity: ").append(book.getQuantity()).append("\n");
                }
            }
        }

        JTextArea bookTextArea = new JTextArea(bookList.toString());
        bookTextArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(bookTextArea);
        scrollPane.setPreferredSize(new Dimension(400, 200));
        JOptionPane.showMessageDialog(this, scrollPane, "Search Results", JOptionPane.INFORMATION_MESSAGE);
    }


    private void displayBooks() {
        StringBuilder bookList = new StringBuilder();
        appendBooksToList(bookList, books, "Books");
        appendEBooksToList(bookList, ebooks, "EBooks");

        if (bookList.isEmpty()) {
            bookList.append("No books available.");
        }

        JTextArea bookTextArea = new JTextArea(bookList.toString());
        JScrollPane scrollPane = new JScrollPane(bookTextArea);
        JOptionPane.showMessageDialog(null, scrollPane, "Books", JOptionPane.PLAIN_MESSAGE);
    }

    private void appendBooksToList(StringBuilder list, ArrayList<Book> books, String type) {
        if (!books.isEmpty()) {
            list.append(type).append(":\n");
            for (Book book : books) {
                list.append("ID: ").append(book.getItemId())
                        .append(", Title: ").append(book.getTitle())
                        .append(", Author: ").append(book.getAuthor())
                        .append(", Year Published: ").append(book.getYearPublished())
                        .append(", Quantity: ").append(book.getQuantity())
                        .append("\n");
            }
            list.append("\n");
        }
    }

    private void appendEBooksToList(StringBuilder list, ArrayList<EBook> ebooks, String type) {
        if (!ebooks.isEmpty()) {
            list.append(type).append(":\n");
            for (EBook ebook : ebooks) {
                list.append("ID: ").append(ebook.getItemId())
                        .append(", Title: ").append(ebook.getTitle())
                        .append(", Author: ").append(ebook.getAuthor())
                        .append(", Year Published: ").append(ebook.getYearPublished())
                        .append(", Size: ").append(ebook.getSize())
                        .append(", Format: ").append(ebook.getFormat())
                        .append("\n");
            }
            list.append("\n");
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
            library.saveBorrowingsToDatabase();
        } else if (bookId.startsWith("E")) {
            library.borrowEBook(bookId, library.getCurrentUser());
            library.saveEBorrowingsToDatabase();
            String randomCode = generateRandomCode();
            JOptionPane.showMessageDialog(this, "EBook borrowed successfully. Your code: " + randomCode);
        }
    }

    public String generateRandomCode() {
        Random random = new Random();
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            code.append(random.nextInt(10));
        }
        return code.toString();
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
            library.saveBorrowingsToDatabase();
            JOptionPane.showMessageDialog(this, "You have returned " + quantityToReturn + " copy/copies of the book with ID: " + bookId);
        }
    }

}
