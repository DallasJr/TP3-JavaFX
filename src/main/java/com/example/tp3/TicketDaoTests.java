package com.example.tp3;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class TicketDaoTests {

    public static void main(String[] args) {
        try {
            DatabaseManager.initializeDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        TicketDao dao = new SQLiteTicketDao();

        dao.deleteAll();

        SupportTicket ticket = new SupportTicket(
                "Test Ticket",
                "John Doe",
                "High",
                "This is a test description",
                LocalDate.now(),
                true,
                "Open"
        );
        SupportTicket inserted = dao.insert(ticket);

        if (inserted.getId() == 0) {
            System.err.println("FAIL: ID not generated");
            return;
        }
        System.out.println("PASS: Ticket inserted with ID " + inserted.getId());

        List<SupportTicket> all = dao.findAll();
        if (all.size() < 1) {
            System.err.println("FAIL: findAll returned no tickets");
            return;
        }
        System.out.println("PASS: findAll returned " + all.size() + " ticket(s)");

        SupportTicket updated = new SupportTicket(
                inserted.getId(),
                "Updated Test Ticket",
                "Jane Doe",
                "Low",
                "Updated description",
                inserted.getCreatedAt(),
                false,
                "Closed"
        );
        dao.update(updated);
        SupportTicket found = dao.findById(inserted.getId()).orElse(null);
        if (found == null || !found.getTitle().equals("Updated Test Ticket")) {
            System.err.println("FAIL: Update not reflected");
            return;
        }
        System.out.println("PASS: Ticket updated successfully");

        dao.deleteById(inserted.getId());
        all = dao.findAll();
        if (all.size() != 0) {
            System.err.println("FAIL: Table not empty after delete");
            return;
        }
        System.out.println("PASS: Ticket deleted, table is empty");

        System.out.println("All tests passed!");
    }
}
