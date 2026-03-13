package com.example.tp3;

import java.time.LocalDate;

public class SupportTicket {
    
    private long id;
    private String title;
    private String customerName;
    private String priority;
    private LocalDate createdAt;
    private String description;
    private boolean urgent;
    private String status;

    public SupportTicket(long id, String title, String customerName, String priority, String description, LocalDate createdAt, boolean urgent, String status) {
        this.id = id;
        this.title = title;
        this.customerName = customerName;
        this.priority = priority;
        this.description = description;
        this.createdAt = createdAt;
        this.urgent = urgent;
        this.status = status;
    }

    public SupportTicket(String title, String customerName, String priority, String description, LocalDate createdAt, boolean urgent, String status) {
        this.title = title;
        this.customerName = customerName;
        this.priority = priority;
        this.description = description;
        this.createdAt = createdAt;
        this.urgent = urgent;
        this.status = status;
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getPriority() {
        return priority;
    }

    public boolean isUrgent() {
        return urgent;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "SupportTicket{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", customerName='" + customerName + '\'' +
                ", priority='" + priority + '\'' +
                ", description='" + description + '\'' +
                ", createdAt=" + createdAt +
                ", urgent=" + urgent +
                ", status='" + status + '\'' +
                '}';
    }

    public SupportTicket withId(long id) {
        this.id = id;
        return this;
    }

}
