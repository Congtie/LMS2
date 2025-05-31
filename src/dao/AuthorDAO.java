package dao;

import models.Author;
import config.DatabaseConfig;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AuthorDAO implements GenericDAO<Author> {
    private static AuthorDAO instance;
    private final Connection connection;
    
    private AuthorDAO() {
        this.connection = DatabaseConfig.getInstance().getConnection();
    }
    
    public static synchronized AuthorDAO getInstance() {
        if (instance == null) {
            instance = new AuthorDAO();
        }
        return instance;
    }
    
    @Override
    public Author save(Author author) {
        String sql = "INSERT INTO authors (id, name, biography, created_at) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, author.getId());
            stmt.setString(2, author.getName());
            stmt.setString(3, author.getBiography());
            stmt.setString(4, author.getCreatedAt());
            stmt.executeUpdate();
            return author;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to save author", e);
        }
    }
    
    @Override
    public Optional<Author> findById(int id) {
        String sql = "SELECT * FROM authors WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapResultSetToAuthor(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find author by id", e);
        }
        return Optional.empty();
    }
    
    @Override
    public List<Author> findAll() {
        String sql = "SELECT * FROM authors ORDER BY name";
        List<Author> authors = new ArrayList<>();
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                authors.add(mapResultSetToAuthor(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find all authors", e);
        }
        return authors;
    }
    
    @Override
    public Author update(Author author) {
        String sql = "UPDATE authors SET name = ?, biography = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, author.getName());
            stmt.setString(2, author.getBiography());
            stmt.setInt(3, author.getId());
            stmt.executeUpdate();
            return author;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update author", e);
        }
    }
    
    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM authors WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete author", e);
        }
    }
    
    private Author mapResultSetToAuthor(ResultSet rs) throws SQLException {
        Author author = new Author(
            rs.getInt("id"),
            rs.getString("name"),
            rs.getString("biography")
        );
        return author;
    }
}