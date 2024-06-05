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
            addbookButton, editbookButton, removeButton, saveButton, loadButton, logoutButton, exitButton;
    private JTextField searchField;
    static Library library;
    private static ArrayList<Book> books;
    private static ArrayList<EBook> ebooks;
    private static ArrayList<Borrower> borrowers;
    private static ArrayList<Borrowing> borrowings;
    private static ArrayList<EBorrowing> eborrowings;
    private JTextArea bookTextArea;

    public ShowMenuAdmin(Library library) {
        ShowMenuAdmin.library = new Library();
        books = new ArrayList<>();
        ebooks = new ArrayList<>();
        borrowers = new ArrayList<>();
        borrowings = new ArrayList<>();
        eborrowings = new ArrayList<>();
        this.library = library;
        this.books = ShowMenuAdmin.library.getBooks();  // Get book list from Library
        this.ebooks = ShowMenuAdmin.library.getEBooks();

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
                library.saveAllToDatabase();
                System.exit(0);
            }
        }
    }

    public static void main(String[] args) {
        library = new Library();
        //ShowMenuAdmin menuAdmin = new ShowMenuAdmin();
        SwingUtilities.invokeLater(() -> new ShowMenuAdmin(library));
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
                "Enter the ID of the book/ebook you want to edit:", bookIdField,
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Edit Book/EBook", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String bookId = bookIdField.getText().trim();

            if (bookId.startsWith("B")) {
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
            } else if (bookId.startsWith("E")) {
                EBook ebookToEdit = library.findEBookById(bookId);
                if (ebookToEdit != null) {
                    JTextField titleField = new JTextField(ebookToEdit.getTitle());
                    JTextField authorField = new JTextField(ebookToEdit.getAuthor());
                    JTextField yearField = new JTextField(String.valueOf(ebookToEdit.getYearPublished()));
                    JTextField sizeField = new JTextField(String.valueOf(ebookToEdit.getSize()));
                    JTextField formatField = new JTextField(ebookToEdit.getFormat());

                    Object[] editMessage = {
                            "Current information of the ebook:",
                            "ID: " + ebookToEdit.getItemId(),
                            "Title:", titleField,
                            "Author:", authorField,
                            "Year Published:", yearField,
                            "Size:", sizeField,
                            "Format:", formatField,
                    };

                    int editOption = JOptionPane.showConfirmDialog(this, editMessage, "Edit EBook", JOptionPane.OK_CANCEL_OPTION);
                    if (editOption == JOptionPane.OK_OPTION) {
                        String newTitle = titleField.getText().trim();
                        String newAuthor = authorField.getText().trim();
                        int newYear;
                        double newSize;
                        String newFormat = formatField.getText().trim();

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
                            newSize = Double.parseDouble(sizeField.getText().trim());
                            if (newSize < 0) {
                                JOptionPane.showMessageDialog(this, "Size cannot be negative.", "Error", JOptionPane.ERROR_MESSAGE);
                                return;
                            }
                        } catch (NumberFormatException e) {
                            JOptionPane.showMessageDialog(this, "Invalid size format.", "Error", JOptionPane.ERROR_MESSAGE);
                            return;
                        }

                        ebookToEdit.setTitle(newTitle);
                        ebookToEdit.setAuthor(newAuthor);
                        ebookToEdit.setYearPublished(newYear);
                        ebookToEdit.setSize(newSize);
                        ebookToEdit.setFormat(newFormat);

                        JOptionPane.showMessageDialog(this, "EBook information updated successfully.");
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "EBook with ID " + bookId + " not found.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Invalid ID format. Please enter a valid book or ebook ID.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }


    public void removeBook() {
        JTextField bookIdField = new JTextField();
        JTextField quantityField = new JTextField();

        Object[] message = {
                "Enter the Book/EBook ID to remove:", bookIdField,
                "Enter the quantity to remove (for physical books only):", quantityField
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Remove Book/EBook", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String bookId = bookIdField.getText().trim();
            int quantityToRemove = 0;

            if (bookId.startsWith("B")) {
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
            } else if (bookId.startsWith("E")) {
                boolean success = library.removeEBookGUI(bookId);
                if (success) {
                    JOptionPane.showMessageDialog(this, "EBook removed successfully.");
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to remove the ebook. Please check the EBook ID.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Invalid ID format. Please enter a valid book or ebook ID.", "Error", JOptionPane.ERROR_MESSAGE);
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

