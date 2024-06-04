
import java.util.ArrayList;
import java.util.Scanner;

import controller.Library;
import model.*;

public class Library_App {
    private static Library library;

    public static void main(String[] args) {
        library = new Library();
        Scanner scanner = new Scanner(System.in);
        System.out.println("\n==== Library Management System ====");
        System.out.println("1. Login");
        System.out.println("2. Register");
        System.out.println("3. Exit");
        System.out.print("Enter your choice: ");
        int choice = scanner.nextInt();
        scanner.nextLine();
        switch (choice) {
            case 1:
                library.login();
                break;
            case 2:
                register(scanner);
                break;
            case 3:
                System.out.println("Exiting...");
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
        }

        if(library.checkRole() == 0) {
            while (true) {
                System.out.println("\n==== Library Management System ====");
                System.out.println("1. Search Book by Id/Title");
                System.out.println("2. Display Books");
                System.out.println("3. Borrow Book");
                System.out.println("4. Return Book");
                System.out.println("5. Log out: ");
                System.out.println("6. Exit");
                System.out.print("Enter your choice: ");
                int choice1 = scanner.nextInt();
                scanner.nextLine();

                switch (choice1) {
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
                        library.logout();
                        return;
                    case 6:
                        System.out.println("Exiting...");
                        return;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            }
        }
        if (library.checkRole() == 1) {

            while (true) {
                System.out.println("\n==== Library Management System ====");
                System.out.println("1. Search Book by Id/Title");
                System.out.println("2. Display Books");
                System.out.println("3. Borrow Book");
                System.out.println("4. Return Book");
                System.out.println("5. Add Book");
                System.out.println("6. Edit Book");
                System.out.println("7. Delete Book");
                System.out.println("8. Save Library to Database");
                System.out.println("9. Load From Database");
                System.out.println("10. Log out");
                System.out.println("11. Exit");
                System.out.print("Enter your choice: ");
                int choice2 = scanner.nextInt();
                scanner.nextLine();

                switch (choice2) {
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
                        editBook(scanner);
                        break;
                    case 7:
                        removeBook(scanner);
                    case 8:
                        library.saveAllToDatabase();
                        break;
                    case 9:
                        library.loadAllFromDatabase();
                        break;
                    case 10:
                        library.logout();
                        break;
                    case 11:
                        System.out.println("Exiting...");
                        return;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
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
        String bookId = scanner.nextLine();
        if (bookId.startsWith("B")) {
            System.out.print("Enter the quantity to borrow: ");
            int quantityToBorrow = scanner.nextInt();
            scanner.nextLine();
            library.borrowBook(bookId, library.getCurrentUser(), quantityToBorrow);
        } else if (bookId.startsWith("E")) {
            library.borrowEBook(bookId, library.getCurrentUser());
        }
    }

    public static void returnBook(Scanner scanner) {
        if (library.getCurrentUser() == null) {
            System.out.println("You need to log in before returning books.");
            return;
        }
        Integer borrowerId = library.getCurrentUserId();
        if (borrowerId == null) {
            System.out.println("Invalid user ID.");
            return;
        }
        System.out.print("Enter the book ID to return: ");
        String bookId = scanner.nextLine();
        scanner.nextLine();

        System.out.print("Enter the quantity to return: ");
        int quantityToReturn = scanner.nextInt();
        scanner.nextLine();

        library.returnBook(bookId, borrowerId, quantityToReturn);
    }

    public static void addBook(Scanner scanner) {
        Book newBook = new Book();
        EBook newEBook = new EBook();
        System.out.print("Enter book ID: ");
        String bookId = scanner.nextLine();

        System.out.print("Enter book title: ");
        String title = scanner.nextLine().trim();

        System.out.print("Enter book author: ");
        String author = scanner.nextLine().trim();

        System.out.print("Enter year published: ");
        int yearPublished = scanner.nextInt();
        scanner.nextLine();
        if (bookId.startsWith("B")){
            System.out.print("Enter quantity: ");
            int quantity = scanner.nextInt();

            newBook.setItemId(String.valueOf(bookId));
            newBook.setTitle(title);
            newBook.setAuthor(author);
            newBook.setYearPublished(yearPublished);
            newBook.setQuantity(quantity);

            library.addBook(newBook);
        }

        if (bookId.startsWith("E")){
            System.out.print("Enter Size: ");
            double size = scanner.nextDouble();
            scanner.nextLine();

            System.out.print("Enter Format: ");
            String format = scanner.nextLine().trim();


            newEBook.setItemId(String.valueOf(bookId));
            newEBook.setTitle(title);
            newEBook.setAuthor(author);
            newEBook.setYearPublished(yearPublished);
            newEBook.setSize(size);
            newEBook.setFormat(format);

            library.addEBook(newEBook);

        }
    }

    public static void editBook(Scanner scanner) {
        System.out.print("Enter the ID of the book you want to edit: ");
        String bookId = scanner.nextLine();
        Book bookToEdit = library.findBookById(String.valueOf(bookId));
        if (bookToEdit != null) {
            System.out.println("Current information of the book:");
            System.out.println("ID: " + bookToEdit.getItemId() + ", Title: " + bookToEdit.getTitle() + ", Author: " + bookToEdit.getAuthor() +
                    ", Year Published: " + bookToEdit.getYearPublished() + ", Quantity: " + bookToEdit.getQuantity());
            System.out.print("Enter new title: ");
            String newTitle = scanner.nextLine();
            System.out.print("Enter new author: ");
            String newAuthor = scanner.nextLine();
            System.out.print("Enter new year published: ");
            int newYear = scanner.nextInt();
            System.out.print("Enter new quantity: ");
            int newQuantity = scanner.nextInt();

            bookToEdit.setTitle(newTitle);
            bookToEdit.setAuthor(newAuthor);
            bookToEdit.setYearPublished(newYear);
            bookToEdit.setQuantity(newQuantity);

            System.out.println("Book information updated successfully.");
        } else {
            System.out.println("Book with ID " + bookId + " not found.");
        }
    }

    public static void removeBook(Scanner scanner) {
        System.out.print("Enter the Book ID to remove: ");
        String bookId = scanner.nextLine();
        System.out.print("Enter the quantity to remove: ");
        int quantityToRemove = scanner.nextInt();
        scanner.nextLine(); // consume the newline

        library.removeBook(bookId, quantityToRemove);
    }

    public static void register(Scanner scanner) {
            Borrower newBorrower = new Borrower();
        library.register(newBorrower);
    }
}
