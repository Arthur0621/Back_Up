package view;

import controller.Library;
import model.Borrower;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LibraryMenuGUI extends JFrame implements ActionListener {
    private JButton searchButton, displayButton, borrowButton, returnButton, loginButton, registerButton, logoutButton, exitButton;
    private JTextField searchField, usernameField;
    private JPasswordField passwordField;
    private static Library library;

    public LibraryMenuGUI() {
        library = new Library();
        setTitle("Library Management System");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        // Username Label
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(new JLabel("Username:"), gbc);

        // Username Field
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        usernameField = new JTextField(20);
        mainPanel.add(usernameField, gbc);

        // Password Label
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        mainPanel.add(new JLabel("Password:"), gbc);

        // Password Field
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        passwordField = new JPasswordField(20);
        mainPanel.add(passwordField, gbc);

        // Buttons Panel
        JPanel buttonPanel = new JPanel(new GridLayout(1, 3, 10, 10));

        // Login Button
        loginButton = new JButton("Login");
        loginButton.addActionListener(this);
        buttonPanel.add(loginButton);

        // Register Button
        registerButton = new JButton("Register");
        registerButton.addActionListener(this);
        buttonPanel.add(registerButton);

        exitButton = new JButton("Exit");
        exitButton.addActionListener(this);
        buttonPanel.add(exitButton);

        // Add button panel to main panel
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(buttonPanel, gbc);

        add(mainPanel);
        setVisible(true);

    }

    public static void main(String[] args) {
        library = new Library();
        library.loadAllFromDatabase();
        LibraryMenuGUI menuGUI = new LibraryMenuGUI();
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == loginButton) {
            login();
        } else if (e.getSource() == registerButton) {
            register();
        } else if (e.getActionCommand().equals("Logout")) {
            library.logout();
        } else if (e.getActionCommand().equals("Exit")) {
            int response = JOptionPane.showConfirmDialog(this, "Are you sure you want to exit?", "Confirm Exit", JOptionPane.YES_NO_OPTION);
            if (response == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        }
    }

    private void login() {
        int loginResult = library.loginGUI(usernameField.getText(), new String(passwordField.getPassword()));
        if (loginResult == 1) {
            int role = library.checkRole();
            System.out.println("hehe " + role);
            if (role == 0) {
                ShowMenu showMenu = new ShowMenu(library);
            } else if (role == 1) {
                ShowMenuAdmin showMenuAdmin = new ShowMenuAdmin(library);
            }

        }
    }

    private void register() {
        JTextField nameField = new JTextField();
        JTextField addressField = new JTextField();
        JTextField phoneField = new JTextField();
        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        JPasswordField confirmPasswordField = new JPasswordField();

        boolean passwordsMatch = false;
        int newBorrowerId = library.generateBorrowerId();

        while (!passwordsMatch) {
            Object[] message = {
                    "Your Borrower ID:", newBorrowerId,
                    "Name:", nameField,
                    "Address:", addressField,
                    "Phone Number:", phoneField,
                    "Username:", usernameField,
                    "Password:", passwordField,
                    "Confirm Password:", confirmPasswordField
            };

            int option = JOptionPane.showConfirmDialog(this, message, "Register", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                String name = nameField.getText();
                String address = addressField.getText();
                String phone = phoneField.getText();
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                String confirmPassword = new String(confirmPasswordField.getPassword());

                if (name.isEmpty() || address.isEmpty() || phone.isEmpty() || username.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "All fields must be filled out.");
                } else if (password.equals(confirmPassword)) {
                    passwordsMatch = true;

                    Borrower newBorrower = new Borrower();
                    newBorrower.setBorrowerId(newBorrowerId);
                    newBorrower.setName(name);
                    newBorrower.setAddress(address);
                    newBorrower.setPhoneNumber(phone);
                    newBorrower.setUsername(username);
                    newBorrower.setPassword(password);

                    // Register the new borrower
                    library.registerGUI(newBorrower);
                    library.incrementBorrowerCounter();
                    library.saveBorrowersToDatabase();
                    JOptionPane.showMessageDialog(null, "Registration successful! Your Borrower ID is: " + newBorrowerId);
                } else {
                    JOptionPane.showMessageDialog(this, "Passwords do not match. Please try again.");
                }
            } else {
                // User chose to cancel, exit the method
                return;
            }
        }
    }



}

