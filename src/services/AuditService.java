package services;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AuditService {
    private static AuditService instance;
    private static final String AUDIT_FILE = "audit.csv";
    private static final DateTimeFormatter TIMESTAMP_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    private AuditService() {
        initializeAuditFile();
    }
    
    public static synchronized AuditService getInstance() {
        if (instance == null) {
            instance = new AuditService();
        }
        return instance;
    }
    
    private void initializeAuditFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(AUDIT_FILE, true))) {
            // Check if file is empty and add header
            if (new java.io.File(AUDIT_FILE).length() == 0) {
                writer.println("nume_actiune,timestamp");
            }
        } catch (IOException e) {
            System.err.println("Failed to initialize audit file: " + e.getMessage());
        }
    }
    
    public synchronized void logAction(String actionName) {
        String timestamp = LocalDateTime.now().format(TIMESTAMP_FORMAT);
        try (PrintWriter writer = new PrintWriter(new FileWriter(AUDIT_FILE, true))) {
            writer.printf("%s,%s%n", actionName, timestamp);
            writer.flush();
        } catch (IOException e) {
            System.err.println("Failed to log action: " + actionName + " - " + e.getMessage());
        }
    }
    
    // Predefined action constants
    public static final String CREATE_AUTHOR = "CREATE_AUTHOR";
    public static final String READ_AUTHOR = "READ_AUTHOR";
    public static final String UPDATE_AUTHOR = "UPDATE_AUTHOR";
    public static final String DELETE_AUTHOR = "DELETE_AUTHOR";
    public static final String LIST_ALL_AUTHORS = "LIST_ALL_AUTHORS";
    
    public static final String CREATE_BOOK = "CREATE_BOOK";
    public static final String CREATE_EBOOK = "CREATE_EBOOK";
    public static final String READ_BOOK = "READ_BOOK";
    public static final String UPDATE_BOOK = "UPDATE_BOOK";
    public static final String DELETE_BOOK = "DELETE_BOOK";
    public static final String LIST_ALL_BOOKS = "LIST_ALL_BOOKS";
    public static final String LIST_AVAILABLE_BOOKS = "LIST_AVAILABLE_BOOKS";
    public static final String SEARCH_BOOKS_BY_TITLE = "SEARCH_BOOKS_BY_TITLE";
    
    public static final String CREATE_MEMBER = "CREATE_MEMBER";
    public static final String READ_MEMBER = "READ_MEMBER";
    public static final String UPDATE_MEMBER = "UPDATE_MEMBER";
    public static final String DELETE_MEMBER = "DELETE_MEMBER";
    public static final String LIST_ALL_MEMBERS = "LIST_ALL_MEMBERS";
    
    public static final String CREATE_BORROWING = "CREATE_BORROWING";
    public static final String READ_BORROWING = "READ_BORROWING";
    public static final String UPDATE_BORROWING = "UPDATE_BORROWING";
    public static final String DELETE_BORROWING = "DELETE_BORROWING";
    public static final String LIST_ALL_BORROWINGS = "LIST_ALL_BORROWINGS";
    public static final String LIST_ACTIVE_BORROWINGS = "LIST_ACTIVE_BORROWINGS";
    public static final String RETURN_BOOK = "RETURN_BOOK";
    public static final String GET_MEMBER_BORROWING_HISTORY = "GET_MEMBER_BORROWING_HISTORY";
    
    public static final String GET_MOST_POPULAR_BOOKS = "GET_MOST_POPULAR_BOOKS";
    public static final String GET_BORROWED_BOOKS = "GET_BORROWED_BOOKS";
}