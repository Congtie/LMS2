package dao;

import models.Member;
import models.Address;
import config.DatabaseConfig;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MemberDAO implements GenericDAO<Member> {
    private static MemberDAO instance;
    private final Connection connection;
    
    private MemberDAO() {
        this.connection = DatabaseConfig.getInstance().getConnection();
    }
    
    public static synchronized MemberDAO getInstance() {
        if (instance == null) {
            instance = new MemberDAO();
        }
        return instance;
    }
    
    @Override
    public Member save(Member member) {
        String sql = "INSERT INTO members (id, first_name, last_name, email, phone, street, city, country, postal_code, created_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, member.getId());
            stmt.setString(2, member.getFirstName());
            stmt.setString(3, member.getLastName());
            stmt.setString(4, member.getEmail());
            stmt.setString(5, member.getPhone());
            stmt.setString(6, member.getAddress().getStreet());
            stmt.setString(7, member.getAddress().getCity());
            stmt.setString(8, member.getAddress().getCountry());
            stmt.setString(9, member.getAddress().getPostalCode());
            stmt.setString(10, member.getCreatedAt());
            stmt.executeUpdate();
            return member;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to save member", e);
        }
    }
    
    @Override
    public Optional<Member> findById(int id) {
        String sql = "SELECT * FROM members WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapResultSetToMember(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find member by id", e);
        }
        return Optional.empty();
    }
    
    @Override
    public List<Member> findAll() {
        String sql = "SELECT * FROM members ORDER BY last_name, first_name";
        List<Member> members = new ArrayList<>();
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                members.add(mapResultSetToMember(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find all members", e);
        }
        return members;
    }
    
    @Override
    public Member update(Member member) {
        String sql = "UPDATE members SET first_name = ?, last_name = ?, email = ?, phone = ?, street = ?, city = ?, country = ?, postal_code = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, member.getFirstName());
            stmt.setString(2, member.getLastName());
            stmt.setString(3, member.getEmail());
            stmt.setString(4, member.getPhone());
            stmt.setString(5, member.getAddress().getStreet());
            stmt.setString(6, member.getAddress().getCity());
            stmt.setString(7, member.getAddress().getCountry());
            stmt.setString(8, member.getAddress().getPostalCode());
            stmt.setInt(9, member.getId());
            stmt.executeUpdate();
            return member;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update member", e);
        }
    }
    
    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM members WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete member", e);
        }
    }
    
    private Member mapResultSetToMember(ResultSet rs) throws SQLException {
        Address address = new Address(
            rs.getString("street"),
            rs.getString("city"),
            rs.getString("country"),
            rs.getString("postal_code")
        );
        
        return new Member(
            rs.getInt("id"),
            rs.getString("first_name"),
            rs.getString("last_name"),
            address,
            rs.getString("email"),
            rs.getString("phone")
        );
    }
}