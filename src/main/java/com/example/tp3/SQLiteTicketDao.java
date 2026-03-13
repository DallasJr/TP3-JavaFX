package com.example.tp3;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SQLiteTicketDao implements TicketDao {

    private static SupportTicket mapRow(ResultSet rs) throws SQLException {
        long id = rs.getLong("id");
        String title = rs.getString("title");
        String customerName = rs.getString("customer_name");
        String priority = rs.getString("priority");
        String createdAt = rs.getString("created_at");
        String description = rs.getString("description");
        boolean urgent = rs.getInt("urgent") != 0;
        String status = rs.getString("status");

        return new SupportTicket(
                id,
                title,
                customerName,
                priority,
                description,
                LocalDate.parse(createdAt),
                urgent,
                status
        );
    }

    @Override
    public SupportTicket insert(SupportTicket ticket) {
        final String sql = "INSERT INTO support_tickets " +
                "(title, customer_name, priority, created_at, description, urgent, status) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, ticket.getTitle());
            stmt.setString(2, ticket.getCustomerName());
            stmt.setString(3, ticket.getPriority());
            stmt.setString(4, ticket.getCreatedAt().toString());
            stmt.setString(5, ticket.getDescription());
            stmt.setInt(6, ticket.isUrgent() ? 1 : 0);
            stmt.setString(7, ticket.getStatus());

            stmt.executeUpdate();

            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    long generatedId = keys.getLong(1);
                    return ticket.withId(generatedId);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to insert ticket", e);
        }

        throw new RuntimeException("Failed to retrieve generated key for ticket insert");
    }

    @Override
    public List<SupportTicket> findAll() {
        final String sql = "SELECT * FROM support_tickets ORDER BY id DESC";
        List<SupportTicket> tickets = new ArrayList<>();

        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                tickets.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to read tickets", e);
        }

        return tickets;
    }

    @Override
    public Optional<SupportTicket> findById(long id) {
        final String sql = "SELECT * FROM support_tickets WHERE id = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find ticket by id", e);
        }

        return Optional.empty();
    }

    @Override
    public List<SupportTicket> searchByTitleOrCustomer(String keyword) {
        final String sql = "SELECT * FROM support_tickets " +
                "WHERE title LIKE ? OR customer_name LIKE ? " +
                "ORDER BY id DESC";
        List<SupportTicket> tickets = new ArrayList<>();
        String pattern = "%" + keyword + "%";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, pattern);
            stmt.setString(2, pattern);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    tickets.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to search tickets", e);
        }

        return tickets;
    }

    @Override
    public void update(SupportTicket ticket) {
        final String sql = "UPDATE support_tickets SET " +
                "title = ?, customer_name = ?, priority = ?, created_at = ?, " +
                "description = ?, urgent = ?, status = ? " +
                "WHERE id = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, ticket.getTitle());
            stmt.setString(2, ticket.getCustomerName());
            stmt.setString(3, ticket.getPriority());
            stmt.setString(4, ticket.getCreatedAt().toString());
            stmt.setString(5, ticket.getDescription());
            stmt.setInt(6, ticket.isUrgent() ? 1 : 0);
            stmt.setString(7, ticket.getStatus());
            stmt.setLong(8, ticket.getId());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update ticket", e);
        }
    }

    @Override
    public void deleteById(long id) {
        final String sql = "DELETE FROM support_tickets WHERE id = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete ticket", e);
        }
    }

    @Override
    public void deleteAll() {
        final String sql = "DELETE FROM support_tickets";

        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete all tickets", e);
        }
    }
}
