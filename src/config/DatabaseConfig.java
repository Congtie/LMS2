package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConfig {
    private static final String DB_URL = "jdbc:h2:./data/library";
    private static final String DB_USER = "sa";
    private static final String DB_PASSWORD = "";
    
    private static DatabaseConfig instance;
    private Connection connection;
    
    private DatabaseConfig() {
        try {
            Class.forName("org.h2.Driver");
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            initializeTables();
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException("Failed to initialize database", e);
        }
    }
    
    public static synchronized DatabaseConfig getInstance() {
        if (instance == null) {
            instance = new DatabaseConfig();
        }
        return instance;
    }
    
    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get database connection", e);
        }
        return connection;
    }
    
    private void initializeTables() {
        try (Statement stmt = connection.createStatement()) {
            // Authors table
            stmt.execute("CREATE TABLE IF NOT EXISTS authors (" +
                "id INT PRIMARY KEY, " +
                "name VARCHAR(255) NOT NULL, " +
                "biography TEXT, " +
                "created_at VARCHAR(50)" +
                ")");
            
            // Books table
            stmt.execute("CREATE TABLE IF NOT EXISTS books (" +
                "id INT PRIMARY KEY, " +
                "title VARCHAR(255) NOT NULL, " +
                "author_id INT, " +
                "genre VARCHAR(50), " +
                "publication_year INT, " +
                "available BOOLEAN DEFAULT TRUE, " +
                "book_type VARCHAR(20) DEFAULT 'BOOK', " +
                "format VARCHAR(20), " +
                "size_in_mb INT, " +
                "created_at VARCHAR(50), " +
                "FOREIGN KEY (author_id) REFERENCES authors(id)" +
                ")");
            
            // Members table
            stmt.execute("CREATE TABLE IF NOT EXISTS members (" +
                "id INT PRIMARY KEY, " +
                "first_name VARCHAR(100) NOT NULL, " +
                "last_name VARCHAR(100) NOT NULL, " +
                "email VARCHAR(255), " +
                "phone VARCHAR(20), " +
                "street VARCHAR(255), " +
                "city VARCHAR(100), " +
                "country VARCHAR(100), " +
                "postal_code VARCHAR(20), " +
                "created_at VARCHAR(50)" +
                ")");
            
            // Borrowings table
            stmt.execute("CREATE TABLE IF NOT EXISTS borrowings (" +
                "id INT PRIMARY KEY, " +
                "book_id INT, " +
                "member_id INT, " +
                "borrow_date DATE, " +
                "due_date DATE, " +
                "return_date DATE, " +
                "created_at VARCHAR(50), " +
                "FOREIGN KEY (book_id) REFERENCES books(id), " +
                "FOREIGN KEY (member_id) REFERENCES members(id)" +
                ")");
            
        } catch (SQLException e) {
            throw new RuntimeException("Failed to initialize database tables", e);
        }
    }
}