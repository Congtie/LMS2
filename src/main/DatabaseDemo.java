package main;

import models.*;
import services.PersistentLibraryService;
import java.util.List;
import java.util.Optional;

public class DatabaseDemo {
    public static void main(String[] args) {
        PersistentLibraryService service = PersistentLibraryService.getInstance();
        
        System.out.println("=== Library Management System - Database Demo ===");
        System.out.println("Note: All operations are being logged to audit.csv file\n");
        
        // Create Authors
        System.out.println("1. Creating Authors:");
        Author author1 = service.createAuthor("George Orwell", "English novelist and essayist");
        Author author2 = service.createAuthor("J.K. Rowling", "British author of Harry Potter series");
        Author author3 = service.createAuthor("Agatha Christie", "Detective fiction writer");
        System.out.println("Created: " + author1);
        System.out.println("Created: " + author2);
        System.out.println("Created: " + author3);
        
        // Create Books
        System.out.println("\n2. Creating Books:");
        Book book1 = service.createBook("1984", author1, Genre.FICTION, 1949);
        Book book2 = service.createBook("Animal Farm", author1, Genre.FICTION, 1945);
        EBook ebook1 = service.createEBook("Harry Potter and the Philosopher's Stone", 
                                          author2, Genre.FANTASY, 1997, "PDF", 15);
        Book book3 = service.createBook("Murder on the Orient Express", author3, Genre.MYSTERY, 1934);
        System.out.println("Created: " + book1);
        System.out.println("Created: " + book2);
        System.out.println("Created: " + ebook1);
        System.out.println("Created: " + book3);
        
        // Create Members
        System.out.println("\n3. Creating Members:");
        Address address1 = new Address("123 Main St", "New York", "USA", "10001");
        Address address2 = new Address("456 Park Ave", "London", "UK", "SW1A 1AA");
        Member member1 = service.createMember("John", "Doe", address1, "john@example.com", "123-456-7890");
        Member member2 = service.createMember("Jane", "Smith", address2, "jane@example.com", "987-654-3210");
        System.out.println("Created: " + member1);
        System.out.println("Created: " + member2);
        
        // Create Borrowings
        System.out.println("\n4. Creating Borrowings:");
        Borrowing borrowing1 = service.createBorrowing(book1, member1);
        Borrowing borrowing2 = service.createBorrowing(ebook1, member2);
        System.out.println("Created: " + borrowing1);
        System.out.println("Created: " + borrowing2);
        
        // Read Operations
        System.out.println("\n5. Reading Data:");
        System.out.println("All Authors:");
        service.getAllAuthors().forEach(System.out::println);
        
        System.out.println("\nAll Books:");
        service.getAllBooks().forEach(System.out::println);
        
        System.out.println("\nAvailable Books:");
        service.getAvailableBooks().forEach(System.out::println);
        
        System.out.println("\nAll Members:");
        service.getAllMembers().forEach(System.out::println);
        
        // Update Operations
        System.out.println("\n6. Update Operations:");
        author1.setBiography("Updated: Famous English author of dystopian fiction");
        Author updatedAuthor = service.updateAuthor(author1);
        System.out.println("Updated Author: " + updatedAuthor);
        
        member1.setEmail("john.doe.updated@example.com");
        Member updatedMember = service.updateMember(member1);
        System.out.println("Updated Member: " + updatedMember);
        
        // Return a book
        System.out.println("\n7. Returning Books:");
        boolean returned = service.returnBook(borrowing1.getId());
        System.out.println("Book returned: " + returned);
        
        System.out.println("Available books after return:");
        service.getAvailableBooks().forEach(System.out::println);
        
        // Search Operations
        System.out.println("\n8. Search Operations:");
        List<Book> harryPotterBooks = service.searchBooksByTitle("Harry");
        System.out.println("Books with 'Harry' in title:");
        harryPotterBooks.forEach(System.out::println);
        
        // Popular books
        System.out.println("\n9. Most Popular Books:");
        service.getMostPopularBooks().forEach((book, count) -> 
            System.out.println(book.getTitle() + " - Borrowed " + count + " times"));
        
        // Member borrowing history
        System.out.println("\n10. Member Borrowing History:");
        List<Borrowing> memberHistory = service.getMemberBorrowingHistory(member1.getId());
        memberHistory.forEach(System.out::println);
        
        System.out.println("\n=== Demo Complete ===");
        System.out.println("Check the 'audit.csv' file to see all logged operations.");
    }
}