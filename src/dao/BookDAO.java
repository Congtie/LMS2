package dao;

import models.*;
import config.DatabaseConfig;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BookDAO implements GenericDAO<Book> {
    private static BookDAO instance;
    private final Connection connection;
    private final AuthorDAO authorDAO;
    
    private BookDAO() {
        this.connection = DatabaseConfig.getInstance().getConnection();
        this.authorDAO = AuthorDAO.getInstance();
    }
    
    public static synchronized BookDAO getInstance() {
        if (instance == null) {
            instance = new BookDAO();
        }
        return instance;
    }
    
    @Override
    public Book save(Book book) {
        String sql = "INSERT INTO books (id, title, author_id, genre, publication_year, available, book_type, format, size_in_mb, created_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, book.getId());
            stmt.setString(2, book.getTitle());
            stmt.setInt(3, book.getAuthor().getId());
            stmt.setString(4, book.getGenre().name());
            stmt.setInt(5, book.getYear());
            stmt.setBoolean(6, book.isAvailable());
            
            if (book instanceof EBook) {
                EBook ebook = (EBook) book;
                stmt.setString(7, "EBOOK");
                stmt.setString(8, ebook.getFormat());
                stmt.setInt(9, ebook.getSizeInMB());
            } else {
                stmt.setString(7, "BOOK");
                stmt.setNull(8, Types.VARCHAR);
                stmt.setNull(9, Types.INTEGER);
            }
            
            stmt.setString(10, book.getCreatedAt());
            stmt.executeUpdate();
            return book;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to save book", e);
        }
    }
    
    @Override
    public Optional<Book> findById(int id) {
        String sql = "SELECT * FROM books WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapResultSetToBook(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find book by id", e);
        }
        return Optional.empty();
    }
    
    @Override
    public List<Book> findAll() {
        String sql = "SELECT * FROM books ORDER BY title";
        List<Book> books = new ArrayList<>();
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                books.add(mapResultSetToBook(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find all books", e);
        }
        return books;
    }
    
    @Override
    public Book update(Book book) {
        String sql = "UPDATE books SET title = ?, author_id = ?, genre = ?, publication_year = ?, available = ?, format = ?, size_in_mb = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, book.getTitle());
            stmt.setInt(2, book.getAuthor().getId());
            stmt.setString(3, book.getGenre().name());
            stmt.setInt(4, book.getYear());
            stmt.setBoolean(5, book.isAvailable());
            
            if (book instanceof EBook) {
                EBook ebook = (EBook) book;
                stmt.setString(6, ebook.getFormat());
                stmt.setInt(7, ebook.getSizeInMB());
            } else {
                stmt.setNull(6, Types.VARCHAR);
                stmt.setNull(7, Types.INTEGER);
            }
            
            stmt.setInt(8, book.getId());
            stmt.executeUpdate();
            return book;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update book", e);
        }
    }
    
    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM books WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete book", e);
        }
    }
    
    public List<Book> findByTitle(String title) {
        String sql = "SELECT * FROM books WHERE LOWER(title) LIKE ? ORDER BY title";
        List<Book> books = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, "%" + title.toLowerCase() + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                books.add(mapResultSetToBook(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find books by title", e);
        }
        return books;
    }
    
    public List<Book> findAvailable() {
        String sql = "SELECT * FROM books WHERE available = TRUE ORDER BY title";
        List<Book> books = new ArrayList<>();
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                books.add(mapResultSetToBook(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find available books", e);
        }
        return books;
    }
    
    private Book mapResultSetToBook(ResultSet rs) throws SQLException {
        int authorId = rs.getInt("author_id");
        Author author = authorDAO.findById(authorId)
                .orElseThrow(() -> new RuntimeException("Author not found for book"));
        
        String bookType = rs.getString("book_type");
        
        if ("EBOOK".equals(bookType)) {
            return new EBook(
                rs.getInt("id"),
                rs.getString("title"),
                author,
                Genre.valueOf(rs.getString("genre")),
                rs.getInt("publication_year"),
                rs.getString("format"),
                rs.getInt("size_in_mb")
            );
        } else {
            Book book = new Book(
                rs.getInt("id"),
                rs.getString("title"),
                author,
                Genre.valueOf(rs.getString("genre")),
                rs.getInt("publication_year")
            );
            book.setAvailable(rs.getBoolean("available"));
            return book;
        }
    }
}