package view;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LibraryMenuGUIAdmin extends JFrame implements ActionListener {
    private JButton searchButton, displayButton, borrowButton, returnButton, addButton, deleteButton, editButton, saveButton, loadButton;
    private JTextField searchField;

    public LibraryMenuGUIAdmin() {
        setTitle("Library Management System - Admin");
        setSize(400, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        JMenu adminMenu = new JMenu("Admin");
        menuBar.add(adminMenu);

        JMenuItem searchItem = new JMenuItem("Search Book");
        JMenuItem displayItem = new JMenuItem("Display Books");
        JMenuItem borrowItem = new JMenuItem("Borrow Book");
        JMenuItem returnItem = new JMenuItem("Return Book");
        JMenuItem addItem = new JMenuItem("Add Book");
        JMenuItem deleteItem = new JMenuItem("Delete Book");
        JMenuItem editItem = new JMenuItem("Edit Book");
        JMenuItem saveItem = new JMenuItem("Save to Database");
        JMenuItem loadItem = new JMenuItem("Load from Database");
        adminMenu.add(searchItem);
        adminMenu.add(displayItem);
        adminMenu.add(borrowItem);
        adminMenu.add(returnItem);
        adminMenu.add(addItem);
        adminMenu.add(deleteItem);
        adminMenu.add(editItem);
        adminMenu.add(saveItem);
        adminMenu.add(loadItem);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(8, 1));

        searchButton = new JButton("Search Book");
        displayButton = new JButton("Display Books");
        borrowButton = new JButton("Borrow Book");
        returnButton = new JButton("Return Book");
        addButton = new JButton("Add Book");
        deleteButton = new JButton("Delete Book");
        editButton = new JButton("Edit Book");
        saveButton = new JButton("Save to Database");
        loadButton = new JButton("Load from Database");
        searchField = new JTextField();

        searchButton.addActionListener(this);
        displayButton.addActionListener(this);
        borrowButton.addActionListener(this);
        returnButton.addActionListener(this);
        addButton.addActionListener(this);
        deleteButton.addActionListener(this);
        editButton.addActionListener(this);
        saveButton.addActionListener(this);
        loadButton.addActionListener(this);

        panel.add(searchField);
        panel.add(searchButton);
        panel.add(displayButton);
        panel.add(borrowButton);
        panel.add(returnButton);
        panel.add(addButton);
        panel.add(deleteButton);
        panel.add(editButton);
        panel.add(saveButton);
        panel.add(loadButton);

        add(panel);
        setVisible(true);
    }

    public static void main(String[] args) {
        new LibraryMenuGUIAdmin();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == searchButton) {
            String searchText = searchField.getText();
            JOptionPane.showMessageDialog(this, "You selected to search for a book with keyword: " + searchText);
        } else if (e.getSource() == displayButton) {
            displayBooks();
        } else if (e.getSource() == borrowButton) {
            borrowBook();
        } else if (e.getSource() == returnButton) {
            returnBook();
        } else if (e.getSource() == addButton) {
            addBook();
        } else if (e.getSource() == deleteButton) {
            deleteBook();
        } else if (e.getSource() == editButton) {
            editBook();
        } else if (e.getSource() == saveButton) {
            JOptionPane.showMessageDialog(this, "You selected to save to the database.");
        } else if (e.getSource() == loadButton) {
            JOptionPane.showMessageDialog(this, "You selected to load from the database.");
        }
    }

    private void displayBooks() {
        String[] bookList = {
            "ID: 1, Title: 1984, Author: George Orwell, Year: 1949, Quantity: 10",
            "ID: 2, Title: To Kill a Mockingbird, Author: Harper Lee, Year: 1960, Quantity: 5",
            "ID: 3, Title: The Great Gatsby, Author: F. Scott Fitzgerald, Year: 1925, Quantity: 7"
        };
        StringBuilder booksDisplay = new StringBuilder();
        for (String book : bookList) {
            booksDisplay.append(book).append("\n");
        }
        JTextArea textArea = new JTextArea(booksDisplay.toString());
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(380, 200));
        JOptionPane.showMessageDialog(this, scrollPane, "Book List", JOptionPane.INFORMATION_MESSAGE);
    }

    private void borrowBook() {
        JTextField bookIdField = new JTextField();
        JTextField quantityField = new JTextField();
        Object[] message = {
            "Book ID:", bookIdField,
            "Quantity to Borrow:", quantityField
        };
        int option = JOptionPane.showConfirmDialog(this, message, "Borrow Book", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String bookId = bookIdField.getText();
            String quantity = quantityField.getText();
            JOptionPane.showMessageDialog(this, "You selected to borrow book with ID: " + bookId + ", Quantity: " + quantity);
        }
    }

    private void returnBook() {
        JTextField bookIdField = new JTextField();
        Object[] message = {
            "Book ID to Return:", bookIdField
        };
        int option = JOptionPane.showConfirmDialog(this, message, "Return Book", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String bookId = bookIdField.getText();
            JOptionPane.showMessageDialog(this, "You selected to return book with ID: " + bookId);
        }
    }

    private void addBook() {
        JTextField idField = new JTextField();
        JTextField titleField = new JTextField();
        JTextField authorField = new JTextField();
        JTextField yearField = new JTextField();
        JTextField quantityField = new JTextField();
        Object[] message = {
            "Book ID:", idField,
            "Title:", titleField,
            "Author:", authorField,
            "Year Published:", yearField,
            "Quantity:", quantityField
        };
        int option = JOptionPane.showConfirmDialog(this, message, "Add Book", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String id = idField.getText();
            String title = titleField.getText();
            String author = authorField.getText();
            String year = yearField.getText();
            String quantity = quantityField.getText();
            JOptionPane.showMessageDialog(this, "You added a book with ID: " + id + ", Title: " + title + ", Author: " + author + ", Year Published: " + year + ", Quantity: " + quantity);
        }
    }

    private void deleteBook() {
        JTextField bookIdField = new JTextField();
        Object[] message = {
            "Book ID to Delete:", bookIdField
        };
        int option = JOptionPane.showConfirmDialog(this, message, "Delete Book", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String bookId = bookIdField.getText();
            JOptionPane.showMessageDialog(this, "You deleted a book with ID: " + bookId);
        }
    }

    private void editBook() {
        JTextField bookIdField = new JTextField();
        JTextField titleField = new JTextField();
        JTextField authorField = new JTextField();
        JTextField yearField = new JTextField();
        JTextField quantityField = new JTextField();
        Object[] message = {
            "Book ID to Edit:", bookIdField,
            "New Title:", titleField,
            "New Author:", authorField,
            "New Year Published:", yearField,
            "Update Quantity:", quantityField
        };
        int option = JOptionPane.showConfirmDialog(this, message, "Edit Book", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String bookId = bookIdField.getText();
            String newTitle = titleField.getText();
            String newAuthor = authorField.getText();
            String newYear = yearField.getText();
            String newQuantity = quantityField.getText();
            JOptionPane.showMessageDialog(this, "You edited book with ID: " + bookId + " to Title: " + newTitle + ", Author: " + newAuthor + ", Year: " + newYear + ", Update Quantity: " + newQuantity);
        }
    }
}
