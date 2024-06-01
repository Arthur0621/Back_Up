
import java.util.ArrayList;
import java.util.Scanner;

import controller.Library;
import model.*;

public class Library_App {
    private static Library library;

    public static void main(String[] args) {
        library = new Library();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n==== Library Management System ====");
            System.out.println("1. Search Book by Id/Title");
            System.out.println("2. Display Books");
            System.out.println("3. Borrow Book");
            System.out.println("4. Return Book");
            System.out.println("5. Add Book");
            System.out.println("6. Save Library to Database");
            System.out.println("7. Load From Database");
            System.out.println("8. Exit");
            System.out.println("9. Login");
            System.out.println("10. Register");
            System.out.println("11. Log out");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    searchBook(scanner);
                    break;
                case 2:
                    library.displayBooks();
                    break;
                case 3:
                    borrowBook(scanner);
                    break;
                case 4:
                    returnBook(scanner);
                    break;
                case 5:
                    addBook(scanner);
                    break;
                case 6:
                    library.saveToDatabase();
                    break;
                case 7:
                    library.loadFromDatabase();
                    break;
                case 8:
                    System.out.println("Exiting...");
                    return;
                case 9:
                    library.login();
                    break;
                case 10:
                    register(scanner);
                    break;
                case 11:
                    library.logout();
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    public static void searchBook(Scanner scanner) {
        System.out.print("Enter the book ID or title to search: ");
        String query = scanner.nextLine().trim();
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

    public static void borrowBook(Scanner scanner) {
        if (library.getCurrentUser() == null) {
            System.out.println("You need to log in before borrowing books.");
            return;
        }

        System.out.print("Enter the book ID to borrow: ");
        int bookId = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Enter the quantity to borrow: ");
        int quantityToBorrow = scanner.nextInt();
        scanner.nextLine();

        library.borrowBook(bookId, library.getCurrentUser(), quantityToBorrow);
    }

    public static void returnBook(Scanner scanner) {
        if (library.getCurrentUser() == null) {
            System.out.println("You need to log in before returning books.");
            return;
        }

        System.out.print("Enter the book ID to return: ");
        int bookId = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Enter the quantity to return: ");
        int quantityToReturn = scanner.nextInt();
        scanner.nextLine();

        library.returnBook(bookId, quantityToReturn);
    }

    public static void addBook(Scanner scanner) {
        Book newBook = new Book();

        System.out.print("Enter book ID: ");
        int bookId = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Enter book title: ");
        String title = scanner.nextLine().trim();

        System.out.print("Enter book author: ");
        String author = scanner.nextLine().trim();

        System.out.print("Enter year published: ");
        int yearPublished = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Enter quantity: ");
        int quantity = scanner.nextInt();
        scanner.nextLine();

        newBook.setItemId(bookId);
        newBook.setTitle(title);
        newBook.setAuthor(author);
        newBook.setYearPublished(yearPublished);
        newBook.setQuantity(quantity);

        library.addBook(newBook);
    }

    public static void register(Scanner scanner) {
        Borrower newBorrower = new Borrower();

        System.out.print("Enter your username: ");
        String username = scanner.nextLine().trim();

        for (Borrower borrower : library.getBorrowers()) {
            if (borrower.getUsername().equals(username)) {
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

        newBorrower.setUsername(username);
        newBorrower.setPassword(password);

        System.out.print("Enter borrower's name: ");
        String name = scanner.nextLine().trim();
        newBorrower.setName(name);

        System.out.print("Enter borrower's address: ");
        String address = scanner.nextLine().trim();
        newBorrower.setAddress(address);

        System.out.print("Enter borrower's phone number: ");
        String phoneNumber = scanner.nextLine().trim();
        newBorrower.setPhoneNumber(phoneNumber);

        library.register(newBorrower);
    }
}
