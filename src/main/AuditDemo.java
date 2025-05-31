package main;

import models.*;
import services.PersistentLibraryService;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class AuditDemo {
    public static void main(String[] args) {
        PersistentLibraryService service = PersistentLibraryService.getInstance();
        
        System.out.println("=== Library Management System - Audit Demo ===\n");
        
        // Perform various operations to generate audit entries
        System.out.println("1. Performing operations to generate audit logs...");
        
        // Create Author
        Author author = service.createAuthor("Test Author", "Test Biography");
        
        // Create Book
        Book book = service.createBook("Test Book", author, Genre.FICTION, 2023);
        
        // Create Member
        Address address = new Address("Test St", "Test City", "Test Country", "12345");
        Member member = service.createMember("Test", "User", address, "test@example.com", "123-456-7890");
        
        // Read operations
        service.getAuthorById(author.getId());
        service.getBookById(book.getId());
        service.getMemberById(member.getId());
        
        // List operations
        service.getAllAuthors();
        service.getAllBooks();
        service.getAllMembers();
        service.getAvailableBooks();
        
        // Search operation
        service.searchBooksByTitle("Test");
        
        // Create borrowing
        Borrowing borrowing = service.createBorrowing(book, member);
        
        // Borrowing operations
        service.getAllBorrowings();
        service.getActiveBorrowings();
        service.getMemberBorrowingHistory(member.getId());
        
        // Return book
        service.returnBook(borrowing.getId());
        
        // Popular books
        service.getMostPopularBooks();
        service.getBorrowedBooks();
        
        // Update operations
        author.setBiography("Updated biography");
        service.updateAuthor(author);
        
        member.setEmail("updated@example.com");
        service.updateMember(member);
        
        System.out.println("Operations completed!\n");
        
        // Display audit log
        System.out.println("2. Audit Log Contents:");
        System.out.println("=" .repeat(50));
        displayAuditLog();
        
        System.out.println("\n=== Audit Demo Complete ===");
    }
    
    private static void displayAuditLog() {
        try (BufferedReader reader = new BufferedReader(new FileReader("audit.csv"))) {
            String line;
            int lineNumber = 0;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                if (lineNumber == 1) {
                    // Header line
                    System.out.println("| " + String.format("%-30s", "Action") + " | " + 
                                     String.format("%-20s", "Timestamp") + " |");
                    System.out.println("|" + "-".repeat(32) + "|" + "-".repeat(22) + "|");
                } else {
                    String[] parts = line.split(",", 2);
                    if (parts.length == 2) {
                        System.out.println("| " + String.format("%-30s", parts[0]) + " | " + 
                                         String.format("%-20s", parts[1]) + " |");
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading audit log: " + e.getMessage());
        }
    }
}