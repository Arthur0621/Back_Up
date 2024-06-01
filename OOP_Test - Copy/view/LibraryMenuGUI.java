package view;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LibraryMenuGUI extends JFrame implements ActionListener {
    private JButton searchButton, displayButton, borrowButton, returnButton;
    private JTextField searchField;

    public LibraryMenuGUI() {
        setTitle("Library Menu");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 1));

        searchButton = new JButton("Search Book");
        displayButton = new JButton("Display Books");
        borrowButton = new JButton("Borrow Book");
        returnButton = new JButton("Return Book");
        searchField = new JTextField();

        searchButton.addActionListener(this);
        displayButton.addActionListener(this);
        borrowButton.addActionListener(this);
        returnButton.addActionListener(this);

        panel.add(searchField);
        panel.add(searchButton);
        panel.add(displayButton);
        panel.add(borrowButton);
        panel.add(returnButton);

        add(panel);
        setVisible(true);
    }

    public static void main(String[] args) {
        new LibraryMenuGUI();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == searchButton) {
            String searchText = searchField.getText();
            // Handle search book button click
            JOptionPane.showMessageDialog(this, "You selected to search for a book with keyword: " + searchText);
        } else if (e.getSource() == displayButton) {
            // Handle display books button click
            // Call method to display books
            displayBooks();
        } else if (e.getSource() == borrowButton) {
            // Handle borrow book button click
            // Call method to borrow book
            borrowBook();
        } else if (e.getSource() == returnButton) {
            // Handle return book button click
            // Call method to return book
            returnBook();
        }
    }

    private void displayBooks() {
        // Display demo books in the library
        String[] bookList = {
            "ID: 1, Title: 1984, Author: George Orwell, Year: 1949, Quantity: 4",
            "ID: 2, Title: To Kill a Mockingbird, Author: Harper Lee, Year: 1960, Quantity: 4",
            "ID: 3, Title: The Great Gatsby, Author: F. Scott Fitzgerald, Year: 1925, Quantity: 4"
        };
        StringBuilder booksDisplay = new StringBuilder();
        for (String book : bookList) {
            booksDisplay.append(book).append("\n");
        }
        JTextArea textArea = new JTextArea(booksDisplay.toString());
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(460, 200));
        JOptionPane.showMessageDialog(this, scrollPane, "Book List", JOptionPane.INFORMATION_MESSAGE);
    }

    private void borrowBook() {
        // Display interface to borrow a book
        JTextField bookIdField = new JTextField();
        JTextField quantityField = new JTextField();
        Object[] message = {
            "Book ID/ Title", bookIdField,
            //"Quantity to Borrow:", quantityField
        };
        int option = JOptionPane.showConfirmDialog(this, message, "Borrow Book", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String bookId = bookIdField.getText();
            String quantity = quantityField.getText();
            // Handle borrowing book information
            JOptionPane.showMessageDialog(this, "You selected to borrow book with ID: " + bookId + ", Quantity: " + quantity);
        }
    }

    private void returnBook() {
        // Display interface to return a book
        JTextField bookIdField = new JTextField();
        Object[] message = {
            "Book ID to Return:", bookIdField
        };
        int option = JOptionPane.showConfirmDialog(this, message, "Return Book", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String bookId = bookIdField.getText();
            // Handle returning book information
            JOptionPane.showMessageDialog(this, "You selected to return book with ID: " + bookId);
        }
    }
}
