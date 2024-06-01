package controller;
import model.Book;
import model.Borrower;
import model.Borrowing;

import java.util.ArrayList;
import java.util.Scanner;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;

public class Library implements LibraryManagement {
    private ArrayList<Book> books;
    private ArrayList<Borrower> borrowers;
    private ArrayList<Borrowing> borrowings;
    private static final String DB_URL = "jdbc:mysql://localhost:3306/library";
    private static final String USER = "root";
    private static final String PASS = "binh1234";
    private static int borrowerCounter = 0;
    private static int borrowerIdPre = 24040000;
    private static Borrower currentUser;

    public Library() {
        books = new ArrayList<>();
        borrowers = new ArrayList<>();
        borrowings = new ArrayList<>();
    }

    public ArrayList<Book> getBooks() {
        return books;
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

    public void login() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your username or ID:");
        String userInput = scanner.nextLine().trim();

        System.out.println("Enter your password:");
        String password = scanner.nextLine().trim();

        for (Borrower borrower : borrowers) {
            if ((borrower.getUsername().equals(userInput) || borrower.getBorrowerId() == Integer.parseInt(userInput)) && borrower.getPassword().equals(password)) {
                currentUser = borrower;
                System.out.println("Login successful!");
                return;
            }
        }
        System.out.println("Invalid username/ID or password. Please try again.");
    }

    public void register(Borrower borrower) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter your username: ");
        String username = scanner.nextLine().trim();

        for (Borrower existingBorrower : borrowers) {
            if (existingBorrower.getUsername().equals(username)) {
                System.out.println("Username already exists. Registration failed.");
                return;
            }
        }

        System.out.print("Enter your password: ");
        String password = scanner.nextLine().trim();
        System.out.print("Confirm your password: ");
        String confirmPassword = scanner.nextLine().trim();

        if (!password.equals(confirmPassword)) {
            System.out.println("Passwords do not match. Registration failed.");
            return;
        }

        borrowerIdPre += (++borrowerCounter);
        int newBorrowerId = borrowerIdPre;
        borrower.setBorrowerId(newBorrowerId);
        System.out.printf("Your ID: %d\n", newBorrowerId);

        System.out.print("Enter borrower's name: ");
        String name = scanner.nextLine().trim();
        borrower.setName(name);

        System.out.print("Enter borrower's address: ");
        String address = scanner.nextLine().trim();
        borrower.setAddress(address);

        System.out.print("Enter borrower's phone number: ");
        String phoneNumber = scanner.nextLine().trim();
        borrower.setPhoneNumber(phoneNumber);

        borrower.setUsername(username);
        borrower.setPassword(password);

        borrowers.add(borrower);

        System.out.println("Registration successful!");
    }

    public void logout() {
        currentUser = null;
        System.out.println("LogOut successful!");
    }

    public Borrower getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(Borrower borrower) {
        this.currentUser = borrower;
    }

    @Override
    public void addBook(Book book) {
        Scanner scanner = new Scanner(System.in);
        int bookId = book.getItemId();
        if (isBookIdDuplicate(bookId)) {
            System.out.println("Book ID " + bookId + " already exists. Do you want to add more copies? (y/n)");
            String choice = scanner.nextLine().trim().toLowerCase();
            if (choice.equals("y")) {
                System.out.println("Enter the quantity to add: ");
                int quantity = scanner.nextInt();
                scanner.nextLine();
                updateBookQuantity(bookId, quantity);
                System.out.println("Quantity of Book ID " + bookId + " updated successfully.");
                return;
            } else {
                System.out.println("Exiting book addition process.");
                return;
            }
        }
        books.add(book);
        System.out.println("Book added successfully.");
    }

    public boolean isBookIdDuplicate(int bookId) {
        for (Book book : books) {
            if (book.getItemId() == bookId) {
                return true;
            }
        }
        return false;
    }

    public void updateBookQuantity(int bookId, int quantity) {
        for (Book book : books) {
            if (book.getItemId() == bookId) {
                book.setQuantity(book.getQuantity() + quantity);
                return;
            }
        }
    }

    public Book findBookById(int bookId) {
        for (Book book : books) {
            if (book.getItemId() == bookId) {
                return book;
            }
        }
        return null;
    }

    public ArrayList<Book> findBooksByTitle(String title) {
        ArrayList<Book> foundBooks = new ArrayList<>();
        for (Book book : books) {
            if (book.getTitle().equalsIgnoreCase(title)) {
                foundBooks.add(book);
            }
        }
        return foundBooks;
    }

    public ArrayList<Book> searchBook(String query) {
        ArrayList<Book> foundBooks = new ArrayList<>();
        try {
            int bookId = Integer.parseInt(query);
            Book foundById = findBookById(bookId);
            if (foundById != null) {
                foundBooks.add(foundById);
            }
        } catch (NumberFormatException e) {
            ArrayList<Book> foundByTitle = findBooksByTitle(query);
            foundBooks.addAll(foundByTitle);
        }
        return foundBooks;
    }

    public void borrowBook(int bookId, Borrower borrower, int quantityToBorrow) {
        Book bookToBorrow = findBookById(bookId);
        if (bookToBorrow == null) {
            System.out.println("Book with ID " + bookId + " not found.");
            return;
        }

        if (quantityToBorrow > bookToBorrow.getQuantity()) {
            System.out.println("Not enough copies of the book available for borrowing.");
            return;
        }

        int borrowerId = borrower.getBorrowerId();
        Borrowing newBorrowing = new Borrowing(bookId, borrowerId, quantityToBorrow, new Date(System.currentTimeMillis()), null);
        borrowings.add(newBorrowing);
        int updatedQuantity = bookToBorrow.getQuantity() - quantityToBorrow;
        bookToBorrow.setQuantity(updatedQuantity);

        System.out.println("Book borrowed successfully.");
    }

    @Override
    public void returnBook(int bookId, int quantityToReturn) {
        Book returnedBook = findBookById(bookId);
        if (returnedBook != null) {
            returnedBook.setQuantity(returnedBook.getQuantity() + quantityToReturn);
            System.out.println("Book '" + returnedBook.getTitle() + "' returned successfully.");
        } else {
            System.out.println("Book with ID " + bookId + " not found.");
        }
    }

    @Override
    public void displayBooks() {
        if (books.isEmpty()) {
            System.out.println("No books available.");
        } else {
            System.out.println("List of Books:");
            for (Book book : books) {
                System.out.println("ID: " + book.getItemId() + ", Title: " + book.getTitle() + ", Author: " + book.getAuthor() + ", Year Published: " + book.getYearPublished() + ", Quantity: " + book.getQuantity());
            }
        }
    }

    public int generateBorrowerId() {
        borrowerCounter++;
        return borrowerIdPre + borrowerCounter;
    }

    public Borrower findBorrowerById(int borrowerId) {
        for (Borrower borrower : borrowers) {
            if (borrower.getBorrowerId() == borrowerId) {
                return borrower;
            }
        }
        return null;
    }
    @Override
    public void saveToDatabase() {
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement stmtBooks = conn.prepareStatement("INSERT INTO books (itemId, title, author, yearPublished, quantity) VALUES (?, ?, ?, ?, ?)");
             PreparedStatement stmtBorrowers = conn.prepareStatement("INSERT INTO borrowers (borrowerId, name, address, phoneNumber, username, password) VALUES (?, ?, ?, ?, ?, ?)");
             PreparedStatement stmtBorrowings = conn.prepareStatement("INSERT INTO borrowings (bookId, borrowerId, quantity, borrowDate, returnDate) VALUES (?, ?, ?, ?, ?)")) {

            // Save books
            for (Book book : books) {
                stmtBooks.setInt(1, book.getItemId());
                stmtBooks.setString(2, book.getTitle());
                stmtBooks.setString(3, book.getAuthor());
                stmtBooks.setInt(4, book.getYearPublished());
                stmtBooks.setInt(5, book.getQuantity());
                stmtBooks.executeUpdate();
            }
            System.out.println("Books saved to database.");

            // Save borrowers
            for (Borrower borrower : borrowers) {
                stmtBorrowers.setInt(1, borrower.getBorrowerId());
                stmtBorrowers.setString(2, borrower.getName());
                stmtBorrowers.setString(3, borrower.getAddress());
                stmtBorrowers.setString(4, borrower.getPhoneNumber());
                stmtBorrowers.setString(5, borrower.getUsername());
                stmtBorrowers.setString(6, borrower.getPassword());
                stmtBorrowers.executeUpdate();
            }
            System.out.println("Borrowers saved to database.");

            // Save borrowings
            for (Borrowing borrowing : borrowings) {
                stmtBorrowings.setInt(1, borrowing.getBookId());
                stmtBorrowings.setInt(2, borrowing.getBorrowerId());
                stmtBorrowings.setInt(3, borrowing.getQuantity());
                stmtBorrowings.setDate(4, new java.sql.Date(borrowing.getBorrowDate().getTime()));
                stmtBorrowings.setDate(5, borrowing.getReturnDate() != null ? new java.sql.Date(borrowing.getReturnDate().getTime()) : null);
                stmtBorrowings.executeUpdate();
            }
            System.out.println("Borrowings saved to database.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void loadFromDatabase() {
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             Statement stmt = conn.createStatement()) {

            // Load books
            ResultSet rsBooks = stmt.executeQuery("SELECT * FROM books");
            books.clear();
            while (rsBooks.next()) {
                Book book = new Book();
                book.setItemId(rsBooks.getInt("itemId"));
                book.setTitle(rsBooks.getString("title"));
                book.setAuthor(rsBooks.getString("author"));
                book.setYearPublished(rsBooks.getInt("yearPublished"));
                book.setQuantity(rsBooks.getInt("quantity"));
                books.add(book);
            }
            System.out.println("Books loaded from database.");

            // Load borrowers
            ResultSet rsBorrowers = stmt.executeQuery("SELECT * FROM borrowers");
            borrowers.clear();
            while (rsBorrowers.next()) {
                Borrower borrower = new Borrower();
                borrower.setBorrowerId(rsBorrowers.getInt("borrowerId"));
                borrower.setName(rsBorrowers.getString("name"));
                borrower.setAddress(rsBorrowers.getString("address"));
                borrower.setPhoneNumber(rsBorrowers.getString("phoneNumber"));
                borrower.setUsername(rsBorrowers.getString("username"));
                borrower.setPassword(rsBorrowers.getString("password"));
                borrowers.add(borrower);
            }
            System.out.println("Borrowers loaded from database.");

            // Load borrowings
            ResultSet rsBorrowings = stmt.executeQuery("SELECT * FROM borrowings");
            borrowings.clear();
            while (rsBorrowings.next()) {
                Borrowing borrowing = new Borrowing();
                borrowing.setBookId(rsBorrowings.getInt("bookId"));
                borrowing.setBorrowerId(rsBorrowings.getInt("borrowerId"));
                borrowing.setQuantityBorrow(rsBorrowings.getInt("quantity"));
                borrowing.setBorrowDate(rsBorrowings.getDate("borrowDate"));
                borrowing.setReturnDate(rsBorrowings.getDate("returnDate"));
                borrowings.add(borrowing);
            }
            System.out.println("Borrowings loaded from database.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


//    public void saveToAllDatabase() {
//        saveBooksToDatabase();
//        saveBorrowersToDatabase();
//        saveBorrowingsToDatabase();
//    }

    private void saveBooksToDatabase() {
        String insertBookQuery = "INSERT INTO Books (bookId, title, author, yearPublished, quantity) VALUES (?, ?, ?, ?, ?)";
        String checkBookQuery = "SELECT * FROM Books WHERE bookId = ?";
        String updateBookQuery = "UPDATE Books SET quantity = ? WHERE bookId = ?";
        
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement pstmtInsert = conn.prepareStatement(insertBookQuery, Statement.RETURN_GENERATED_KEYS);
             PreparedStatement pstmtCheck = conn.prepareStatement(checkBookQuery);
             PreparedStatement pstmtUpdate = conn.prepareStatement(updateBookQuery)) {
            for (Book book : books) {
                pstmtCheck.setInt(1, book.getItemId());
                ResultSet resultSet = pstmtCheck.executeQuery();
                if (resultSet.next()) {
                    int existingQuantity = resultSet.getInt("quantity");
                    int newQuantity = book.getQuantity();
                    if (existingQuantity != newQuantity) {
                        pstmtUpdate.setInt(1, newQuantity);
                        pstmtUpdate.setInt(2, book.getItemId());
                        pstmtUpdate.executeUpdate();
                        System.out.println("Quantity for Book with ID " + book.getItemId() + " updated successfully.");
                    }
                    System.out.println("Book with ID " + book.getItemId() + " already exists in the database. Skipping...");
                    continue;
                }
                pstmtInsert.setInt(1, book.getItemId());
                pstmtInsert.setString(2, book.getTitle());
                pstmtInsert.setString(3, book.getAuthor());
                pstmtInsert.setInt(4, book.getYearPublished());
                pstmtInsert.setInt(5, book.getQuantity());
    
                pstmtInsert.executeUpdate();
                System.out.println("Book with ID " + book.getItemId() + " saved to the database successfully.");
            }
            System.out.println("All books saved to the database successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
          
    private void saveBorrowersToDatabase() {
        for (Borrower borrower : borrowers) {
            saveBorrowerToDatabase(borrower);
        }
    }
    
    private void saveBorrowerToDatabase(Borrower borrower) {
        String checkBorrowerQuery = "SELECT * FROM Borrowers WHERE borrowerId = ?";
        String insertBorrowerQuery = "INSERT INTO Borrowers (borrowerId, name, address, phoneNumber, username, password) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
            for (Borrower borrowerCheck : borrowers) {
                try (PreparedStatement pstmtCheck = conn.prepareStatement(checkBorrowerQuery)) {
                    pstmtCheck.setInt(1, borrowerCheck.getBorrowerId());
                    ResultSet resultSet = pstmtCheck.executeQuery();
                    if (resultSet.next()) {
                        System.out.println("Borrower with ID " + borrowerCheck.getBorrowerId() + " already exists in the database. Skipping...");
                        continue;
                    }
                }
                try (PreparedStatement pstmt = conn.prepareStatement(insertBorrowerQuery, Statement.RETURN_GENERATED_KEYS)) {
                    pstmt.setInt(1, borrowerCheck.getBorrowerId());
                    pstmt.setString(2, borrowerCheck.getName());
                    pstmt.setString(3, borrowerCheck.getAddress());
                    pstmt.setString(4, borrowerCheck.getPhoneNumber());
                    pstmt.setString(5, borrowerCheck.getUsername());
                    pstmt.setString(6, borrowerCheck.getPassword());
                    pstmt.executeUpdate();
                    System.out.println("New borrower saved to the database successfully.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private void saveBorrowingToDatabase(Borrowing borrowing) {
        String checkBorrowingQuery = "SELECT * FROM Borrowings WHERE bookId = ? AND borrowerId = ?";
        String insertBorrowingQuery = "INSERT INTO Borrowings (bookId, borrowerId, quantityBorrow, borrowDate, returnDate) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
            try (PreparedStatement pstmtCheck = conn.prepareStatement(checkBorrowingQuery)) {
                pstmtCheck.setInt(1, borrowing.getBookId());
                pstmtCheck.setInt(2, borrowing.getBorrowerId());
                ResultSet resultSet = pstmtCheck.executeQuery();
                if (resultSet.next()) {
                    System.out.println("Borrowing record for Book ID " + borrowing.getBookId() + " and Borrower ID " + borrowing.getBorrowerId() + " already exists in the database. Skipping...");
                    return;
                }
            }
            try (PreparedStatement pstmt = conn.prepareStatement(insertBorrowingQuery)) {
                pstmt.setInt(1, borrowing.getBookId());
                pstmt.setInt(2, borrowing.getBorrowerId());
                pstmt.setInt(3, borrowing.getQuantityBorrow());
                pstmt.setDate(4, borrowing.getBorrowDate());
                pstmt.setDate(5, borrowing.getReturnDate());
                pstmt.executeUpdate();
                System.out.println("Borrowing record saved to the database successfully.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
   
    public void loadAllFromDatabase() {
        loadBooksFromDatabase();
        loadBorrowersFromDatabase();
        loadBorrowingsFromDatabase();
    }
    
    private void loadBooksFromDatabase() {
        String selectBooksQuery = "SELECT * FROM Books";
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(selectBooksQuery)) {
            while (rs.next()) {
                int bookId = rs.getInt("bookId");
                String title = rs.getString("title");
                String author = rs.getString("author");
                int yearPublished = rs.getInt("yearPublished");
                boolean borrowed = rs.getString("borrowed").equals("Yes");
                boolean exists = false;

                for (Book book : books) {
                    if (book.getItemId()==bookId && book.getTitle()==title && 
                        book.getAuthor()==author && book.getYearPublished()==yearPublished) {
                        exists = true;
                        break;
                    }
                }
                if (!exists) {
                    Book book = new Book(bookId, title, author, yearPublished);
                    book.setBorrowed(borrowed);
                    books.add(book);
                }
            }
            System.out.println("Books loaded from the database successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private void loadBorrowersFromDatabase() {
        String selectBorrowersQuery = "SELECT * FROM Borrowers";
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(selectBorrowersQuery)) {
            while (rs.next()) {
                int borrowerId = rs.getInt("borrowerId");
                String name = rs.getString("name");
                String address = rs.getString("address");
                String phoneNumber = rs.getString("phoneNumber");
                boolean exists = false;

                for (Borrower borrower : borrowers) {
                    if (borrower.getBorrowerId()==borrowerId && borrower.getName()==name &&
                        borrower.getAddress()==address && borrower.getPhoneNumber()==phoneNumber) {
                        exists = true;
                        break;
                    }
                }
                if (!exists) {
                    Borrower borrower = new Borrower(borrowerId, name, address, phoneNumber);
                    borrowers.add(borrower);
                }
            }
            System.out.println("Borrowers loaded from the database successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private void loadBorrowingsFromDatabase() {
        String selectBorrowingsQuery = "SELECT * FROM Borrowings";
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(selectBorrowingsQuery)) {
            while (rs.next()) {
                int bookId = rs.getInt("bookId");
                int borrowerId = rs.getInt("borrowerId");
                Date borrowDate = rs.getDate("borrowDate");
                Date returnDate = rs.getDate("returnDate");
                boolean exists = false;

                for (Borrowing borrowing : borrowings) {
                    if (borrowing.getBookId() == bookId && borrowing.getBorrowerId() == borrowerId &&
                        borrowing.getBorrowDate()==borrowDate && borrowing.getReturnDate()==returnDate) {
                        exists = true;
                        break;
                    }
                }
                if (!exists) {
                    Borrowing borrowing = new Borrowing(bookId, borrowerId, borrowDate, returnDate);
                    borrowings.add(borrowing);
                }
            }
            System.out.println("Borrowings loaded from the database successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
}
