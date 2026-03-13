package com.example.tp3;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;

public final class TicketExporter {

    public static void exportToCsv(Collection<SupportTicket> tickets, String filePath) throws IOException {
        Path path = Path.of(filePath);
        if (path.getParent() != null) {
            Files.createDirectories(path.getParent());
        }
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write("ID,Title,Customer Name,Priority,Created At,Description,Urgent,Status\n");
            for (SupportTicket ticket : tickets) {
                writer.write(ticket.getId() + ",");
                writer.write("\"" + ticket.getTitle().replace("\"", "\"\"") + "\",");
                writer.write("\"" + ticket.getCustomerName().replace("\"", "\"\"") + "\",");
                writer.write("\"" + ticket.getPriority().replace("\"", "\"\"") + "\",");
                writer.write("\"" + ticket.getCreatedAt().toString().replace("\"", "\"\"") + "\",");
                writer.write("\"" + ticket.getDescription().replace("\"", "\"\"") + "\",");
                writer.write(ticket.isUrgent() + ",");
                writer.write("\"" + ticket.getStatus().replace("\"", "\"\"") + "\"\n");
            }
        }
    }
}
