package com.example.tp3;

import javafx.collections.ObservableList;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class TicketPersistenceServiceTests {

    public static void main(String[] args) {
        try {
            DatabaseManager.initializeDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        TicketPersistenceService service = new TicketPersistenceService();

        service.deleteAllTickets();

        SupportTicket ticket1 = new SupportTicket(
                "Issue with login",
                "Alice Smith",
                "High",
                "Cannot log in to the system",
                LocalDate.now(),
                true,
                "Open"
        );
        SupportTicket ticket2 = new SupportTicket(
                "Feature request",
                "Bob Johnson",
                "Medium",
                "Add dark mode",
                LocalDate.now().minusDays(1),
                false,
                "In Progress"
        );
        service.createTicket(ticket1);
        service.createTicket(ticket2);

        ObservableList<SupportTicket> tickets = service.getTickets();
        if (tickets.size() != 2) {
            System.err.println("FAIL: Expected 2 tickets, got " + tickets.size());
            return;
        }
        System.out.println("PASS: ObservableList contains " + tickets.size() + " tickets");

        List<SupportTicket> searchResults = service.search("Alice");
        if (searchResults.size() != 1 || !searchResults.get(0).getCustomerName().equals("Alice Smith")) {
            System.err.println("FAIL: Search for 'Alice' failed");
            return;
        }
        System.out.println("PASS: Search for 'Alice' returned 1 result");

        service.deleteAllTickets();
        if (service.getTickets().size() != 0) {
            System.err.println("FAIL: deleteAllTickets did not clear the list");
            return;
        }
        System.out.println("PASS: All tickets deleted");

        System.out.println("All service tests passed!");
    }
}
