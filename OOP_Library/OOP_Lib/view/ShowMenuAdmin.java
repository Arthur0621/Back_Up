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

public class ShowMenuAdmin extends JFrame implements ActionListener {
    //private JLabel noBooksLabel;
    private JButton searchButton, displayButton, borrowButton, returnButton,
            addbookButton,editbookButton,removeButton,saveButton,loadButton, logoutButton, exitButton;
    private JTextField searchField;
    private static Library library;
    private ArrayList<Book> books;
    private ArrayList<EBook> ebooks;
    private ArrayList<Borrower> borrowers;
    private ArrayList<Borrowing> borrowings;
    private ArrayList<EBorrowing> eborrowings;

    public ShowMenuAdmin() {
        library = new Library();


        setTitle("Library Menu Admin");
        setSize(300, 600);
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
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        searchButton = new JButton("Search Book");
        searchButton.addActionListener(this);
        mainPanel.add(searchButton, gbc);

        // Display Button
        gbc.gridx = 0;
        gbc.gridy = 1;
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

        // Add Book Button
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        addbookButton = new JButton("Add Book");
        addbookButton.addActionListener(this);
        mainPanel.add(addbookButton, gbc);

        // Edit Book Button
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        editbookButton = new JButton("Edit Book");
        editbookButton.addActionListener(this);
        mainPanel.add(editbookButton, gbc);

        // Remove Book Button
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        removeButton = new JButton("Remove Book");
        removeButton.addActionListener(this);
        mainPanel.add(removeButton, gbc);

        // Save Button
        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.gridwidth = 2;
        saveButton = new JButton("Save To DataBase");
        saveButton.addActionListener(this);
        mainPanel.add(saveButton, gbc);

        // Load Button
        gbc.gridx = 0;
        gbc.gridy = 9;
        gbc.gridwidth = 2;
        loadButton = new JButton("Load From DataBase");
        loadButton.addActionListener(this);
        mainPanel.add(loadButton, gbc);

        // Logout Button
        gbc.gridx = 0;
        gbc.gridy = 10;
        gbc.gridwidth = 2;
        logoutButton = new JButton("Logout");
        logoutButton.addActionListener(this);
        mainPanel.add(logoutButton, gbc);

        // Exit Button
        gbc.gridx = 0;
        gbc.gridy = 11;
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
        } else if (e.getSource() == addbookButton) {
            addBook();
        } else if (e.getSource() == editbookButton) {
            editBook();
        } else if (e.getSource() == removeButton) {
            removeBook();
        } else if (e.getSource() == saveButton) {
            saveDatabase();
        } else if (e.getSource() == loadButton) {
            loadDatabase();
        } else if (e.getSource() == logoutButton) {
            library.logout();
        } else if (e.getSource() == exitButton) {
            int response = JOptionPane.showConfirmDialog(this, "Are you sure you want to exit?", "Confirm Exit", JOptionPane.YES_NO_OPTION);
            if (response == JOptionPane.YES_OPTION) {
                System.exit(0); // Đóng chương trình nếu người dùng chọn YES
            }
        }
    }

    public void main(String[] args) {
        books.add(new Book("B001", "Book Title 1", "Author 1", 2020, 10));
        books.add(new Book("B002", "Book Title 2", "Author 2", 2021, 5));
        ebooks.add(new EBook("E001", "Book Title 3", "Author 3", 2022, 3.2, "PDF"));

        ShowMenuAdmin menuAdmin = new ShowMenuAdmin();
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
            // Show message in a designated GUI element (e.g., a label)
            //noBooksLabel.setText("No books available.");
        } else {
            // Display book details in a suitable GUI component (e.g., a text area)
            StringBuilder bookList = new StringBuilder("List of Books:\n");
            for (Book book : books) {
                bookList.append("ID: ").append(book.getItemId()).append(", Title: ")
                        .append(book.getTitle()).append(", Author: ")
                        .append(book.getAuthor()).append(", Year Published: ")
                        .append(book.getYearPublished()).append(", Quantity: ")
                        .append(book.getQuantity()).append("\n");
            }
            for (EBook ebook : ebooks) {
                bookList.append("ID: ").append(ebook.getItemId()).append(", Title: ")
                        .append(ebook.getTitle()).append(", Author: ")
                        .append(ebook.getAuthor()).append(", Year Published: ")
                        .append(ebook.getYearPublished()).append(", Size: ")
                        .append(ebook.getSize()).append(", Format: ")
                        .append(ebook.getFormat()).append("\n");
            }
//            Label bookDisplayArea = null;
//            bookDisplayArea.setText(bookList.toString());
        }
    }


    public void borrowBook() {
        JTextField bookIdField = new JTextField();
        JTextField quantityField = new JTextField();

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

    public void addBook() {
        JTextField bookIdField = new JTextField();
        JTextField titleField = new JTextField();
        JTextField authorField = new JTextField();
        JTextField yearField = new JTextField();
        JTextField quantityField = new JTextField();
        JTextField sizeField = new JTextField();
        JTextField formatField = new JTextField();

        Object[] message = {
                "Book ID:", bookIdField,
                "Title:", titleField,
                "Author:", authorField,
                "Year Published:", yearField,
                "Quantity (for physical books):", quantityField,
                "Size (for eBooks):", sizeField,
                "Format (for eBooks):", formatField
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Add Book", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String bookId = bookIdField.getText().trim();
            String title = titleField.getText().trim();
            String author = authorField.getText().trim();

            // Year Published Validation
            int yearPublished;
            try {
                yearPublished = Integer.parseInt(yearField.getText());
                if (yearPublished < 0) {
                    JOptionPane.showMessageDialog(this, "Year published cannot be negative.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Invalid year published format.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (bookId.startsWith("B")) {
                // Handle Quantity for physical books
                int quantity;
                try {
                    quantity = Integer.parseInt(quantityField.getText());
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, "Invalid quantity format.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Book newBook = new Book(bookId, title, author, yearPublished, quantity);
                library.addBook(newBook);
            } else if (bookId.startsWith("E")) {
                // Handle Size for eBooks
                double size;
                try {
                    size = Double.parseDouble(sizeField.getText());
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, "Invalid size format.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String format = formatField.getText().trim();
                EBook newEBook = new EBook(bookId, title, author, yearPublished, size, format);
                library.addEBook(newEBook);
            } else {
                JOptionPane.showMessageDialog(this, "Invalid book ID format. Book ID must start with 'B' for physical books or 'E' for eBooks.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void editBook() {
        JTextField bookIdField = new JTextField();
        Object[] message = {
                "Enter the ID of the book you want to edit:", bookIdField,
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Edit Book", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String bookId = bookIdField.getText().trim();
            Book bookToEdit = library.findBookById(bookId);
            if (bookToEdit != null) {
                JTextField titleField = new JTextField(bookToEdit.getTitle());
                JTextField authorField = new JTextField(bookToEdit.getAuthor());
                JTextField yearField = new JTextField(String.valueOf(bookToEdit.getYearPublished()));
                JTextField quantityField = new JTextField(String.valueOf(bookToEdit.getQuantity()));

                Object[] editMessage = {
                        "Current information of the book:",
                        "ID: " + bookToEdit.getItemId(),
                        "Title:", titleField,
                        "Author:", authorField,
                        "Year Published:", yearField,
                        "Quantity:", quantityField,
                };

                int editOption = JOptionPane.showConfirmDialog(this, editMessage, "Edit Book", JOptionPane.OK_CANCEL_OPTION);
                if (editOption == JOptionPane.OK_OPTION) {
                    String newTitle = titleField.getText().trim();
                    String newAuthor = authorField.getText().trim();
                    int newYear;
                    int newQuantity;

                    try {
                        newYear = Integer.parseInt(yearField.getText().trim());
                        if (newYear < 0) {
                            JOptionPane.showMessageDialog(this, "Year published cannot be negative.", "Error", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(this, "Invalid year published format.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    try {
                        newQuantity = Integer.parseInt(quantityField.getText().trim());
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(this, "Invalid quantity format.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    bookToEdit.setTitle(newTitle);
                    bookToEdit.setAuthor(newAuthor);
                    bookToEdit.setYearPublished(newYear);
                    bookToEdit.setQuantity(newQuantity);

                    JOptionPane.showMessageDialog(this, "Book information updated successfully.");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Book with ID " + bookId + " not found.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void removeBook() {
        JTextField bookIdField = new JTextField();
        JTextField quantityField = new JTextField();

        Object[] message = {
                "Enter the Book ID to remove:", bookIdField,
                "Enter the quantity to remove:", quantityField
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Remove Book", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String bookId = bookIdField.getText().trim();
            int quantityToRemove;

            try {
                quantityToRemove = Integer.parseInt(quantityField.getText().trim());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Invalid quantity format.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            boolean success = library.removeBookGUI(bookId, quantityToRemove);
            if (success) {
                JOptionPane.showMessageDialog(this, "Book removed successfully.");
            } else {
                JOptionPane.showMessageDialog(this, "Failed to remove the book. Please check the Book ID and quantity.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void saveDatabase() {
        library.saveAllToDatabase();
            JOptionPane.showMessageDialog(this, "Library successfully saved to database!", "Success", JOptionPane.INFORMATION_MESSAGE);
//        } else {
//            JOptionPane.showMessageDialog(this, "An error occurred while saving to database!", "Error", JOptionPane.ERROR_MESSAGE);
//        }
    }

    public void loadDatabase() {
        library.loadAllFromDatabase();
            JOptionPane.showMessageDialog(this, "Library successfully loaded from database!", "Success", JOptionPane.INFORMATION_MESSAGE);
//        } else {
//            JOptionPane.showMessageDialog(this, "An error occurred while loading from database!", "Error", JOptionPane.ERROR_MESSAGE);
//        }
    }


}

