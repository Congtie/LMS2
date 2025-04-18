package main;

import models.*;
import services.LibraryService;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        LibraryService libraryService = new LibraryService();

        Author author1 = libraryService.addAuthor("George Orwell", "English novelist and essayist.");
        Author author2 = libraryService.addAuthor("J.K. Rowling", "British author best known for the Harry Potter series.");
        Author author3 = libraryService.addAuthor("Agatha Christie", "English writer known for her detective novels.");

        Book book1 = libraryService.addBook("1984", author1, Genre.FICTION, 1949);
        Book book2 = libraryService.addBook("Animal Farm", author1, Genre.FICTION, 1945);
        Book book3 = libraryService.addBook("Harry Potter and the Philosopher's Stone", author2, Genre.FANTASY, 1997);
        Book book4 = libraryService.addEBook("Murder on the Orient Express", author3, Genre.MYSTERY, 1934, "PDF", 5);
        Book book5 = libraryService.addBook("Death on the Nile", author3, Genre.MYSTERY, 1937);

        Address address1 = new Address("123 Main St", "New York", "USA", "10001");
        Address address2 = new Address("456 Park Ave", "London", "UK", "SW1A 1AA");

        Member member1 = libraryService.addMember("John", "Doe", address1, "john.doe@example.com", "123-456-7890");
        Member member2 = libraryService.addMember("Jane", "Smith", address2, "jane.smith@example.com", "987-654-3210");

        System.out.println("All Books:");
        for (Book book : libraryService.getAllBooks()) {
            System.out.println(book);
        }

        System.out.println("\nAll Members:");
        for (Member member : libraryService.getAllMembers()) {
            System.out.println(member);
        }

        Borrowing borrowing1 = libraryService.borrowBook(book1, member1);
        Borrowing borrowing2 = libraryService.borrowBook(book3, member1);
        Borrowing borrowing3 = libraryService.borrowBook(book5, member2);

        System.out.println("\nBorrowed Books:");
        for (Book book : libraryService.getBorrowedBooks()) {
            System.out.println(book);
        }

        System.out.println("\nAvailable Books:");
        for (Book book : libraryService.getAvailableBooks()) {
            System.out.println(book);
        }

        libraryService.returnBook(borrowing1);

        System.out.println("\nAfter returning a book - Borrowed Books:");
        for (Book book : libraryService.getBorrowedBooks()) {
            System.out.println(book);
        }

        System.out.println("\nMember Borrowing History:");
        for (Borrowing borrowing : libraryService.getMemberBorrowingHistory(member1)) {
            System.out.println(borrowing);
        }

        System.out.println("\nSearch books by title 'Harry':");
        for (Book book : libraryService.searchBooksByTitle("Harry")) {
            System.out.println(book);
        }

        System.out.println("\nSearch books by author 'Christie':");
        for (Book book : libraryService.searchBooksByAuthor("Christie")) {
            System.out.println(book);
        }

        System.out.println("\nMost Popular Books:");
        Map<Book, Integer> popularBooks = libraryService.getMostPopularBooks();
        for (Map.Entry<Book, Integer> entry : popularBooks.entrySet()) {
            System.out.println(entry.getKey().getTitle() + " - Borrowed " + entry.getValue() + " times");
        }
    }
}