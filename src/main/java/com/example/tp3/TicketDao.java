package com.example.tp3;

import java.util.List;
import java.util.Optional;

public interface TicketDao {

    SupportTicket insert(SupportTicket ticket);

    List<SupportTicket> findAll();

    Optional<SupportTicket> findById(long id);

    List<SupportTicket> searchByTitleOrCustomer(String keyword);

    void update(SupportTicket ticket);

    void deleteById(long id);

    void deleteAll();
}
