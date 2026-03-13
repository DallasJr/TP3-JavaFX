package com.example.tp3;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

public class TicketPersistenceService {

    private final TicketDao dao = new SQLiteTicketDao();
    private final ObservableList<SupportTicket> tickets = FXCollections.observableArrayList();

    public TicketPersistenceService() {
        refresh();
    }

    public ObservableList<SupportTicket> getTickets() {
        return tickets;
    }

    public void refresh() {
        tickets.clear();
        tickets.addAll(dao.findAll());
    }

    public SupportTicket createTicket(SupportTicket ticket) {
        SupportTicket created = dao.insert(ticket);
        refresh();
        return created;
    }

    public void updateTicket(SupportTicket ticket) {
        dao.update(ticket);
        refresh();
    }

    public void deleteTicket(long id) {
        dao.deleteById(id);
        refresh();
    }

    public void deleteAllTickets() {
        dao.deleteAll();
        refresh();
    }

    public List<SupportTicket> search(String keyword) {
        return dao.searchByTitleOrCustomer(keyword);
    }
}
