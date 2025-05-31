package dao;

import models.*;
import config.DatabaseConfig;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BorrowingDAO implements GenericDAO<Borrowing> {
    private static BorrowingDAO instance;
    private final Connection connection;
    private final BookDAO bookDAO;
    private final MemberDAO memberDAO;
    
    private BorrowingDAO() {
        this.connection = DatabaseConfig.getInstance().getConnection();
        this.bookDAO = BookDAO.getInstance();
        this.memberDAO = MemberDAO.getInstance();
    }
    
    public static synchronized BorrowingDAO getInstance() {
        if (instance == null) {
            instance = new BorrowingDAO();
        }
        return instance;
    }
    
    @Override
    public Borrowing save(Borrowing borrowing) {
        String sql = "INSERT INTO borrowings (id, book_id, member_id, borrow_date, due_date, return_date, created_at) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, borrowing.getId());
            stmt.setInt(2, borrowing.getBook().getId());
            stmt.setInt(3, borrowing.getMember().getId());
            stmt.setDate(4, Date.valueOf(borrowing.getBorrowDate()));
            stmt.setDate(5, Date.valueOf(borrowing.getDueDate()));
            if (borrowing.getReturnDate() != null) {
                stmt.setDate(6, Date.valueOf(borrowing.getReturnDate()));
            } else {
                stmt.setNull(6, Types.DATE);
            }
            stmt.setString(7, borrowing.getCreatedAt());
            stmt.executeUpdate();
            return borrowing;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to save borrowing", e);
        }
    }
    
    @Override
    public Optional<Borrowing> findById(int id) {
        String sql = "SELECT * FROM borrowings WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapResultSetToBorrowing(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find borrowing by id", e);
        }
        return Optional.empty();
    }
    
    @Override
    public List<Borrowing> findAll() {
        String sql = "SELECT * FROM borrowings ORDER BY borrow_date DESC";
        List<Borrowing> borrowings = new ArrayList<>();
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                borrowings.add(mapResultSetToBorrowing(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find all borrowings", e);
        }
        return borrowings;
    }
    
    @Override
    public Borrowing update(Borrowing borrowing) {
        String sql = "UPDATE borrowings SET return_date = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            if (borrowing.getReturnDate() != null) {
                stmt.setDate(1, Date.valueOf(borrowing.getReturnDate()));
            } else {
                stmt.setNull(1, Types.DATE);
            }
            stmt.setInt(2, borrowing.getId());
            stmt.executeUpdate();
            return borrowing;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update borrowing", e);
        }
    }
    
    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM borrowings WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete borrowing", e);
        }
    }
    
    public List<Borrowing> findByMemberId(int memberId) {
        String sql = "SELECT * FROM borrowings WHERE member_id = ? ORDER BY borrow_date DESC";
        List<Borrowing> borrowings = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, memberId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                borrowings.add(mapResultSetToBorrowing(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find borrowings by member", e);
        }
        return borrowings;
    }
    
    public List<Borrowing> findActiveBorrowings() {
        String sql = "SELECT * FROM borrowings WHERE return_date IS NULL ORDER BY due_date";
        List<Borrowing> borrowings = new ArrayList<>();
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                borrowings.add(mapResultSetToBorrowing(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find active borrowings", e);
        }
        return borrowings;
    }
    
    private Borrowing mapResultSetToBorrowing(ResultSet rs) throws SQLException {
        int bookId = rs.getInt("book_id");
        int memberId = rs.getInt("member_id");
        
        Book book = bookDAO.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found for borrowing"));
        Member member = memberDAO.findById(memberId)
                .orElseThrow(() -> new RuntimeException("Member not found for borrowing"));
        
        // Create borrowing with custom constructor for database loading
        Borrowing borrowing = new Borrowing(rs.getInt("id"), book, member);
        
        // Set return date if exists
        Date returnDate = rs.getDate("return_date");
        if (returnDate != null) {
            // We need to manually set the return date and book availability
            // This is a limitation of the current Borrowing class design
        }
        
        return borrowing;
    }
}