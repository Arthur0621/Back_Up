package controller;
import model.Book;
import model.EBook;
import model.Borrower;
import model.Borrowing;
import model.EBorrowing;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Objects;
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
    private ArrayList<EBook> ebooks;
    private ArrayList<Borrower> borrowers;
    private ArrayList<Borrowing> borrowings;
    private ArrayList<EBorrowing> eborrowings;
    private static final String DB_URL = "jdbc:mysql://localhost:3306/library";
    private static final String USER = "root";
    private static final String PASS = "binh1234";
    private static int borrowerCounter = 0;
    private static int borrowerIdPre = 24040001;
    private static Borrower currentUser;

    public Library() {
        books = new ArrayList<>();
        ebooks = new ArrayList<>();
        borrowers = new ArrayList<>();
        borrowings = new ArrayList<>();
        eborrowings = new ArrayList<>();
        this.borrowers.add(new Borrower(24040000, "Admin", "Admin Address", "987654321", "admin", "1234"));
    }


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

    public void login() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your username or ID:");
        String userInput = scanner.nextLine().trim();

        System.out.println("Enter your password:");
        String password = scanner.nextLine().trim();

        for (Borrower borrower : borrowers) {
            if ((borrower.getUsername().equals(userInput) || borrower.getBorrowerId() == Integer.parseInt(userInput)) && borrower.getPassword().equals(password)) {
                currentUser = borrower;
                return;
            }
        }
        System.out.println("Invalid username/ID or password. Please try again.");
    }

    public int loginGUI(String username, String password) {
        for (Borrower borrower : borrowers) {
            if ((borrower.getUsername().equals(username) || String.valueOf(borrower.getBorrowerId()).equals(username)) && borrower.getPassword().equals(password)) {
                currentUser = borrower;
                return 1;
            }
        }
        System.out.println("Invalid username/ID or password. Please try again.");
        return 0;
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

        borrowerIdPre += (borrowerCounter++);
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

    public int registerGUI(Borrower borrower) {
        JOptionPane JOptionPane = null;
        for (Borrower existingBorrower : borrowers) {
            if (existingBorrower.getUsername().equals(borrower.getUsername())) {
                //JOptionPane.showMessageDialog(null, "Username already exists. Registration failed.");
                return 0;
            }
        }

        borrowers.add(borrower);
        return 1;
        //JOptionPane.showMessageDialog(null, "Registration successful! Your Borrower ID is: " + newBorrowerId);
    }

//    public int generateBorrowerId() {
//        int newBorrowerId = borrowerIdPre + (borrowerCounter++);
//        return newBorrowerId;
//    }
public int generateBorrowerId() {
    return borrowerIdPre + borrowerCounter;
}

    public void incrementBorrowerCounter() {
        borrowerCounter++;
    }

    public void logout() {
        currentUser = null;
        System.out.println("LogOut successful!");
    }

    public void setCurrentUser(Borrower borrower) {
        this.currentUser = borrower;
    }


    public Borrower getCurrentUser() {
        return currentUser;
    }
    public Integer getCurrentUserId() {
        if (currentUser != null) {
            return currentUser.getBorrowerId();
        } else {
            throw new IllegalStateException("No current user is set.");
        }
    }

    public int checkRole() {
        if (currentUser != null) {
            if ("admin".equals(currentUser.getUsername()) && "1234".equals(currentUser.getPassword())) {
                return 1; // Admin
            } else {
                return 0; // Normal user
            }
        } else {
            throw new IllegalStateException("No current user is set.");
        }
    }

    @Override
    public void addBook(Book book) {
        Scanner scanner = new Scanner(System.in);
         String bookId = book.getItemId();
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

    }

    @Override
    public void addEBook(EBook ebook) {
        Scanner scanner = new Scanner(System.in);
        String bookId = ebook.getItemId();
            if (isEBookIdDuplicate(bookId)) {
                System.out.println("Book ID " + bookId + " already exists.)");
            }
            else {
                ebooks.add(ebook);
            }

        System.out.println("Book added successfully.");
    }

    public boolean isBookIdDuplicate(String bookId) {
        for (Book book : books) {
            if (book.getItemId().equals(bookId)) {
                return true;
            }
        }
        return false;
    }

    public boolean isEBookIdDuplicate(String bookId) {
        for (EBook ebook : ebooks) {
            if (ebook.getItemId().equals(bookId)) {
                return true;
            }
        }
        return false;
    }

    public void updateBookQuantity(String bookId, int quantity) {
        for (Book book : books) {
            if (book.getItemId().equals(bookId)) {
                book.setQuantity(book.getQuantity() + quantity);
                return;
            }
        }
    }

    public Book findBookById(String bookId) {
        for (Book book : books) {
            if (book.getItemId().equals(bookId)) {
                return book;
            }
        }
        return null;
    }

    public EBook findEBookById(String bookId) {
        for (EBook ebook : ebooks) {
            if (ebook.getItemId().equals(bookId)) {
                return ebook;
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

    public ArrayList<EBook> findEBooksByTitle(String title) {
        ArrayList<EBook> foundEBooks = new ArrayList<>();
        for (EBook ebook : ebooks) {
            if (ebook.getTitle().equalsIgnoreCase(title)) {
                foundEBooks.add(ebook);
            }
        }
        return foundEBooks;
    }

    public ArrayList<Book> searchBook(String query) {
        ArrayList<Book> foundBooks = new ArrayList<>();
        try {
            Book foundById = findBookById(query);
            if (foundById != null) {
                foundBooks.add(foundById);
            }
        } catch (NumberFormatException e) {
            ArrayList<Book> foundByTitle = findBooksByTitle(query);
            foundBooks.addAll(foundByTitle);
        }
        return foundBooks;
    }

    public ArrayList<EBook> searchEBook(String query) {
        ArrayList<EBook> foundEBooks = new ArrayList<>();
        try {
            EBook foundById = findEBookById(query);
            if (foundById != null) {
                foundEBooks.add(foundById);
            }
        } catch (NumberFormatException e) {
            ArrayList<EBook> foundByTitle = findEBooksByTitle(query);
            foundEBooks.addAll(foundByTitle);
        }
        return foundEBooks;
    }

    @Override
    public void removeBook(String bookId, int quantityToRemove) {
        Book bookToRemove = findBookById(bookId);
        if (bookToRemove != null) {
            int currentQuantity = bookToRemove.getQuantity();
            if (currentQuantity > quantityToRemove) {
                bookToRemove.setQuantity(currentQuantity - quantityToRemove);
                System.out.println("Quantity of book with ID " + bookId + " has been reduced by " + quantityToRemove + ". Remaining quantity: " + bookToRemove.getQuantity());
            } else {
                books.remove(bookToRemove);
                System.out.println("Book with ID " + bookId + " has been completely removed from the library.");
            }
        } else {
            System.out.println("Book with ID " + bookId + " not found.");
        }
    }
    public boolean removeBookGUI(String bookId, int quantityToRemove) {
        Book bookToRemove = findBookById(bookId);
        if (bookToRemove != null) {
            int currentQuantity = bookToRemove.getQuantity();
            if (currentQuantity > quantityToRemove) {
                bookToRemove.setQuantity(currentQuantity - quantityToRemove);
                System.out.println("Quantity of book with ID " + bookId + " has been reduced by " + quantityToRemove + ". Remaining quantity: " + bookToRemove.getQuantity());
            } else {
                books.remove(bookToRemove);
                System.out.println("Book with ID " + bookId + " has been completely removed from the library.");
            }
        } else {
            System.out.println("Book with ID " + bookId + " not found.");
        }
        return false;
    }
    public boolean removeEBookGUI(String bookId) {
        EBook ebookToRemove = findEBookById(bookId);
        if (ebookToRemove != null) {
                ebooks.remove(ebookToRemove);
                System.out.println("Book with ID " + bookId + " has been completely removed from the library.");
        } else {
            System.out.println("Book with ID " + bookId + " not found.");
        }
        return false;
    }

    @Override
    public void borrowBook(String bookId, Borrower borrower, int quantityToBorrow) {
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
    public void borrowEBook(String bookId, Borrower borrower) {
        EBook EbookToBorrow = (EBook) findEBookById(bookId);
        if (EbookToBorrow == null) {
            System.out.println("Book with ID " + bookId + " not found.");
            return;
        }

        int borrowerId = borrower.getBorrowerId();
        EBorrowing newEBorrowing = new EBorrowing(bookId, borrowerId, new Date(System.currentTimeMillis()), null);
        eborrowings.add(newEBorrowing);

        System.out.println("Book borrowed successfully." + " Expiration Date: 7 days later." );
    }

    @Override
    public void returnBook(String bookId, int borrowerId, int quantityToReturn) {
        Book returnedBook = findBookById(String.valueOf(bookId));
        if (returnedBook != null) {
            Borrowing borrowing = findBorrowingByBookIdAndBorrowerId(bookId, borrowerId);
            if (borrowing != null) {
                int currentQuantity = borrowing.getQuantityBorrow();
                if (quantityToReturn < currentQuantity) {
                    System.out.println("Please return the borrowed books in full!");
                } else {
                    borrowing.setQuantityBorrow(quantityToReturn);
                    borrowing.setReturnDate(new Date(System.currentTimeMillis()));
                    returnedBook.setQuantity(returnedBook.getQuantity() + quantityToReturn);
                    System.out.println("Book '" + returnedBook.getTitle() + "' returned successfully in " + borrowing.getReturnDate() );
                }
            } else {
                System.out.println("Borrowing record for book with ID " + bookId + " and borrower with ID " + borrowerId + " not found.");
            }
        } else {
            System.out.println("Book with ID " + bookId + " not found.");
        }
    }


    public Borrowing findBorrowingByBookIdAndBorrowerId(String bookId, int borrowerId) {
        for (Borrowing borrowing : borrowings) {
            if (borrowing.getBookId().equals(bookId) && borrowing.getBorrowerId() == borrowerId && borrowing.getReturnDate() == null) {
                return borrowing;
            }
        }
        return null;
    }

    @Override
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

    public void saveAllToDatabase() {
        saveBooksToDatabase();
        saveEBooksToDatabase();
        saveBorrowersToDatabase();
        saveBorrowingsToDatabase();
        saveEBorrowingsToDatabase();
    }

    private void saveBooksToDatabase() {
        String insertBookQuery = "INSERT INTO Books (bookId, title, author, yearPublished, quantity) VALUES (?, ?, ?, ?, ?)";
        String checkBookQuery = "SELECT * FROM Books WHERE bookId = ?";
        String updateBookQuery = "UPDATE Books SET title = ?, author = ?, yearPublished = ?, quantity = ? WHERE bookId = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement pstmtInsert = conn.prepareStatement(insertBookQuery);
             PreparedStatement pstmtCheck = conn.prepareStatement(checkBookQuery);
             PreparedStatement pstmtUpdate = conn.prepareStatement(updateBookQuery)) {

            for (Book book : books) {
                pstmtCheck.setString(1, book.getItemId());
                ResultSet resultSet = pstmtCheck.executeQuery();

                if (resultSet.next()) {
                    // Update existing book
                    pstmtUpdate.setString(1, book.getTitle());
                    pstmtUpdate.setString(2, book.getAuthor());
                    pstmtUpdate.setInt(3, book.getYearPublished());
                    pstmtUpdate.setInt(4, book.getQuantity());
                    pstmtUpdate.setString(5, book.getItemId());
                    pstmtUpdate.executeUpdate();
                    System.out.println("Book with ID " + book.getItemId() + " updated successfully.");
                } else {
                    // Insert new book
                    pstmtInsert.setString(1, book.getItemId());
                    pstmtInsert.setString(2, book.getTitle());
                    pstmtInsert.setString(3, book.getAuthor());
                    pstmtInsert.setInt(4, book.getYearPublished());
                    pstmtInsert.setInt(5, book.getQuantity());
                    pstmtInsert.executeUpdate();
                    System.out.println("Book with ID " + book.getItemId() + " saved to the database successfully.");
                }
            }
            System.out.println("All books processed successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private void saveEBooksToDatabase() {
        String insertBookQuery = "INSERT INTO EBooks (EbookId, title, author, yearPublished, size, format) VALUES (?, ?, ?, ?, ?, ?)";
        String checkBookQuery = "SELECT * FROM EBooks WHERE EbookId = ?";
        String updateBookQuery = "UPDATE EBooks SET title = ?, author = ?, yearPublished = ?, size = ?, format = ? WHERE EbookId = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement pstmtInsert = conn.prepareStatement(insertBookQuery);
             PreparedStatement pstmtCheck = conn.prepareStatement(checkBookQuery);
             PreparedStatement pstmtUpdate = conn.prepareStatement(updateBookQuery)) {

            for (EBook ebook : ebooks) {
                pstmtCheck.setString(1, ebook.getItemId());
                ResultSet resultSet = pstmtCheck.executeQuery();

                if (resultSet.next()) {
                    // Update existing eBook
                    pstmtUpdate.setString(1, ebook.getTitle());
                    pstmtUpdate.setString(2, ebook.getAuthor());
                    pstmtUpdate.setInt(3, ebook.getYearPublished());
                    pstmtUpdate.setDouble(4, ebook.getSize());
                    pstmtUpdate.setString(5, ebook.getFormat());
                    pstmtUpdate.setString(6, ebook.getItemId());
                    pstmtUpdate.executeUpdate();
                    System.out.println("eBook with ID " + ebook.getItemId() + " updated successfully.");
                } else {
                    // Insert new eBook
                    pstmtInsert.setString(1, ebook.getItemId());
                    pstmtInsert.setString(2, ebook.getTitle());
                    pstmtInsert.setString(3, ebook.getAuthor());
                    pstmtInsert.setInt(4, ebook.getYearPublished());
                    pstmtInsert.setDouble(5, ebook.getSize());
                    pstmtInsert.setString(6, ebook.getFormat());
                    pstmtInsert.executeUpdate();
                    System.out.println("eBook with ID " + ebook.getItemId() + " saved to the database successfully.");
                }
            }
            System.out.println("All eBooks processed successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveBorrowersToDatabase() {
        for (Borrower borrower : borrowers) {
            saveBorrowerToDatabase(borrower);
        }
    }

    private void saveBorrowerToDatabase(Borrower borrower) {
        String checkBorrowerQuery = "SELECT * FROM Borrowers WHERE borrowerId = ?";
        String insertBorrowerQuery = "INSERT INTO Borrowers (borrowerId, name, address, phoneNumber, username, password) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement pstmtCheck = conn.prepareStatement(checkBorrowerQuery);
             PreparedStatement pstmtInsert = conn.prepareStatement(insertBorrowerQuery)) {

            pstmtCheck.setInt(1, borrower.getBorrowerId());
            ResultSet resultSet = pstmtCheck.executeQuery();

            if (resultSet.next()) {
                System.out.println("Borrower with ID " + borrower.getBorrowerId() + " already exists in the database. Skipping...");
                return;
            }

            pstmtInsert.setInt(1, borrower.getBorrowerId());
            pstmtInsert.setString(2, borrower.getName());
            pstmtInsert.setString(3, borrower.getAddress());
            pstmtInsert.setString(4, borrower.getPhoneNumber());
            pstmtInsert.setString(5, borrower.getUsername());
            pstmtInsert.setString(6, borrower.getPassword());

            pstmtInsert.executeUpdate();
            System.out.println("New borrower saved to the database successfully.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveBorrowingsToDatabase() {
        for (Borrowing borrowing : borrowings) {
            saveBorrowingToDatabase(borrowing);
        }
    }

    private void saveBorrowingToDatabase(Borrowing borrowing) {
        String checkBorrowingQuery = "SELECT * FROM Borrowings WHERE bookId = ? AND borrowerId = ? AND returnDate IS NULL";
        String insertBorrowingQuery = "INSERT INTO Borrowings (bookId, borrowerId, quantityBorrow, borrowDate, returnDate) VALUES (?, ?, ?, ?, ?)";
        String updateBorrowingQuery = "UPDATE Borrowings SET quantityBorrow = ?, borrowDate = ?, returnDate = ? WHERE bookId = ? AND borrowerId = ? AND returnDate IS NULL";

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement pstmtCheck = conn.prepareStatement(checkBorrowingQuery);
             PreparedStatement pstmtInsert = conn.prepareStatement(insertBorrowingQuery);
             PreparedStatement pstmtUpdate = conn.prepareStatement(updateBorrowingQuery)) {

            pstmtCheck.setString(1, borrowing.getBookId());
            pstmtCheck.setInt(2, borrowing.getBorrowerId());
            ResultSet resultSet = pstmtCheck.executeQuery();

            if (resultSet.next()) {
                // Bản ghi đã tồn tại với returnDate là null, tiến hành cập nhật
                pstmtUpdate.setInt(1, borrowing.getQuantityBorrow());
                pstmtUpdate.setDate(2, borrowing.getBorrowDate());
                pstmtUpdate.setDate(3, borrowing.getReturnDate());
                pstmtUpdate.setString(4, borrowing.getBookId());
                pstmtUpdate.setInt(5, borrowing.getBorrowerId());
                pstmtUpdate.executeUpdate();
                System.out.println("Borrowing record updated successfully.");
            } else {
                // Không tồn tại bản ghi với returnDate là null, tiến hành chèn mới
                pstmtInsert.setString(1, borrowing.getBookId());
                pstmtInsert.setInt(2, borrowing.getBorrowerId());
                pstmtInsert.setInt(3, borrowing.getQuantityBorrow());
                pstmtInsert.setDate(4, borrowing.getBorrowDate());
                pstmtInsert.setDate(5, borrowing.getReturnDate());
                pstmtInsert.executeUpdate();
                System.out.println("Borrowing record saved to the database successfully.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void saveEBorrowingsToDatabase() {
        for (EBorrowing eborrowing : eborrowings) {
            saveEBorrowingToDatabase(eborrowing);
        }
    }

    private void saveEBorrowingToDatabase(EBorrowing eborrowing) {
        String checkEBorrowingQuery = "SELECT * FROM EBorrowings WHERE EbookId = ? AND borrowerId = ? AND returnDate IS NULL";
        String insertEBorrowingQuery = "INSERT INTO EBorrowings (EbookId, borrowerId, borrowDate, returnDate) VALUES (?, ?, ?, ?)";
        String updateEBorrowingQuery = "UPDATE EBorrowings SET borrowDate = ?, returnDate = ? WHERE EbookId = ? AND borrowerId = ? AND returnDate IS NULL";

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement pstmtCheck = conn.prepareStatement(checkEBorrowingQuery);
             PreparedStatement pstmtInsert = conn.prepareStatement(insertEBorrowingQuery);
             PreparedStatement pstmtUpdate = conn.prepareStatement(updateEBorrowingQuery)) {

            pstmtCheck.setString(1, eborrowing.getBookId());
            pstmtCheck.setInt(2, eborrowing.getBorrowerId());
            ResultSet resultSet = pstmtCheck.executeQuery();

            if (resultSet.next()) {
                // Update existing eBorrowing
                pstmtUpdate.setDate(1, eborrowing.getBorrowDate());
                pstmtUpdate.setDate(2, eborrowing.getReturnDate());
                pstmtUpdate.setString(3, eborrowing.getBookId());
                pstmtUpdate.setInt(4, eborrowing.getBorrowerId());
                pstmtUpdate.executeUpdate();
                System.out.println("EBorrowing record updated successfully.");
            } else {
                // Insert new eBorrowing
                pstmtInsert.setString(1, eborrowing.getBookId());
                pstmtInsert.setInt(2, eborrowing.getBorrowerId());
                pstmtInsert.setDate(3, eborrowing.getBorrowDate());
                pstmtInsert.setDate(4, eborrowing.getReturnDate());
                pstmtInsert.executeUpdate();
                System.out.println("EBorrowing record saved to the database successfully.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void loadAllFromDatabase() {
        loadBooksFromDatabase();
        loadEBooksFromDatabase();
        loadBorrowersFromDatabase();
        loadBorrowingsFromDatabase();
        loadEBorrowingsFromDatabase();

    }

    private void loadBooksFromDatabase() {
        String selectBooksQuery = "SELECT * FROM Books";
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(selectBooksQuery)) {

            books.clear(); // Xóa dữ liệu cũ trước khi tải dữ liệu mới

            while (rs.next()) {
                String bookId = rs.getString("bookId");
                String title = rs.getString("title");
                String author = rs.getString("author");
                int yearPublished = rs.getInt("yearPublished");
                int quantity = rs.getInt("quantity");

                Book book = new Book(bookId, title, author, yearPublished, quantity);
                books.add(book);
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

            borrowers.clear(); // Xóa dữ liệu cũ trước khi tải dữ liệu mới

            while (rs.next()) {
                int borrowerId = rs.getInt("borrowerId");
                String name = rs.getString("name");
                String address = rs.getString("address");
                String phoneNumber = rs.getString("phoneNumber");
                String username = rs.getString("username");
                String password = rs.getString("password");

                Borrower borrower = new Borrower(borrowerId, name, address, phoneNumber);
                borrower.setUsername(username);
                borrower.setPassword(password);
                borrowers.add(borrower);
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

            borrowings.clear(); // Xóa dữ liệu cũ trước khi tải dữ liệu mới

            while (rs.next()) {
                String bookId = rs.getString("bookId");
                int borrowerId = rs.getInt("borrowerId");
                int quantityBorrow = rs.getInt("quantityBorrow");
                Date borrowDate = rs.getDate("borrowDate");
                Date returnDate = rs.getDate("returnDate");

                Borrowing borrowing = new Borrowing(bookId, borrowerId, quantityBorrow,borrowDate, returnDate );
                borrowings.add(borrowing);
            }
            System.out.println("Borrowings loaded from the database successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadEBooksFromDatabase() {
        String selectEBooksQuery = "SELECT * FROM EBooks";
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(selectEBooksQuery)) {

            ebooks.clear(); // Xóa dữ liệu cũ trước khi tải dữ liệu mới

            while (rs.next()) {
                String ebookId = rs.getString("ebookId");
                String title = rs.getString("title");
                String author = rs.getString("author");
                int yearPublished = rs.getInt("yearPublished");
                double size = rs.getDouble("size");
                String format = rs.getString("format");

                EBook ebook = new EBook(ebookId, title, author, yearPublished, 1, format, size);
                ebooks.add(ebook);
            }
            System.out.println("All eBooks loaded from the database successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadEBorrowingsFromDatabase() {
        String selectEBorrowingsQuery = "SELECT * FROM EBorrowings";
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(selectEBorrowingsQuery)) {

            eborrowings.clear(); // Xóa dữ liệu cũ trước khi tải dữ liệu mới

            while (rs.next()) {
                String ebookId = rs.getString("ebookId");
                int borrowerId = rs.getInt("borrowerId");
                Date borrowDate = rs.getDate("borrowDate");
                Date returnDate = rs.getDate("returnDate");

                EBorrowing eborrowing = new EBorrowing(ebookId, borrowerId, borrowDate, returnDate);
                eborrowings.add(eborrowing);
            }
            System.out.println("All eBook borrowings loaded from the database successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



}
