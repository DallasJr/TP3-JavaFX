package com.example.tp3;

import javafx.application.Application;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;

public class TicketPersistenceApp extends Application {

    private TextField titleField;
    private TextField customerField;
    private ComboBox<String> priorityBox;
    private ComboBox<String> statusBox;
    private DatePicker datePicker;
    private TextArea descriptionArea;
    private CheckBox urgentCheck;
    private Label statusLabel;
    private TextField searchField;
    private TableView<SupportTicket> table;
    private TicketPersistenceService service;

    @Override
    public void start(Stage primaryStage) {
        try {
            DatabaseManager.initializeDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        service = new TicketPersistenceService();

        titleField = new TextField();
        customerField = new TextField();
        priorityBox = new ComboBox<>();
        priorityBox.getItems().addAll("Low", "Medium", "High");
        statusBox = new ComboBox<>();
        statusBox.getItems().addAll("Open", "In Progress", "Closed");
        datePicker = new DatePicker();
        descriptionArea = new TextArea();
        urgentCheck = new CheckBox("Urgent");
        statusLabel = new Label();
        searchField = new TextField();
        searchField.setPromptText("Search by title or customer...");

        table = new TableView<>();
        TableColumn<SupportTicket, Number> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(data -> new SimpleLongProperty(data.getValue().getId()));
        TableColumn<SupportTicket, String> titleCol = new TableColumn<>("Title");
        titleCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTitle()));
        TableColumn<SupportTicket, String> customerCol = new TableColumn<>("Customer");
        customerCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCustomerName()));
        TableColumn<SupportTicket, String> priorityCol = new TableColumn<>("Priority");
        priorityCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getPriority()));
        TableColumn<SupportTicket, String> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCreatedAt().toString()));
        TableColumn<SupportTicket, Boolean> urgentCol = new TableColumn<>("Urgent");
        urgentCol.setCellValueFactory(data -> new SimpleBooleanProperty(data.getValue().isUrgent()));
        TableColumn<SupportTicket, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getStatus()));

        table.getColumns().addAll(idCol, titleCol, customerCol, priorityCol, dateCol, urgentCol, statusCol);
        table.setItems(service.getTickets());

        Button addButton = new Button("Add");
        Button updateButton = new Button("Update");
        Button deleteButton = new Button("Delete");
        Button resetButton = new Button("Reset");
        Button reloadButton = new Button("Reload");
        Button exportButton = new Button("Export to CSV");

        VBox form = new VBox(10,
                new Label("Search:"), searchField,
                new Label("Title:"), titleField,
                new Label("Customer:"), customerField,
                new Label("Priority:"), priorityBox,
                new Label("Status:"), statusBox,
                new Label("Date:"), datePicker,
                new Label("Description:"), descriptionArea,
                urgentCheck,
                statusLabel
        );
        form.setPadding(new Insets(10));

        HBox buttons = new HBox(10, addButton, updateButton, deleteButton, resetButton, reloadButton, exportButton);
        buttons.setPadding(new Insets(10));

        BorderPane root = new BorderPane();
        root.setLeft(form);
        root.setCenter(table);
        root.setBottom(buttons);

        addButton.setOnAction(e -> {
            if (validateForm()) {
                LocalDate createdAt = datePicker.getValue();
                SupportTicket ticket = new SupportTicket(
                        titleField.getText(),
                        customerField.getText(),
                        priorityBox.getValue(),
                        descriptionArea.getText(),
                        createdAt,
                        urgentCheck.isSelected(),
                        statusBox.getValue()
                );
                service.createTicket(ticket);
                clearForm();
                statusLabel.setText("Ticket added");
            }
        });

        updateButton.setOnAction(e -> {
            SupportTicket selected = table.getSelectionModel().getSelectedItem();
            if (selected != null && validateForm()) {
                LocalDate createdAt = datePicker.getValue();
                SupportTicket updated = new SupportTicket(
                        selected.getId(),
                        titleField.getText(),
                        customerField.getText(),
                        priorityBox.getValue(),
                        descriptionArea.getText(),
                        createdAt,
                        urgentCheck.isSelected(),
                        statusBox.getValue()
                );
                service.updateTicket(updated);
                statusLabel.setText("Ticket updated");
            }
        });

        deleteButton.setOnAction(e -> {
            SupportTicket selected = table.getSelectionModel().getSelectedItem();
            if (selected != null) {
                service.deleteTicket(selected.getId());
                statusLabel.setText("Ticket deleted");
            }
        });

        resetButton.setOnAction(e -> {
            clearForm();
            table.getSelectionModel().clearSelection();
            statusLabel.setText("");
        });

        reloadButton.setOnAction(e -> {
            service.refresh();
            statusLabel.setText("Reloaded");
        });

        exportButton.setOnAction(e -> {
            try {
                TicketExporter.exportToCsv(service.getTickets(), "tickets.csv");
                statusLabel.setText("Exported to tickets.csv");
            } catch (IOException ex) {
                statusLabel.setText("Export failed: " + ex.getMessage());
            }
        });

        table.getSelectionModel().selectedItemProperty().addListener((obs, old, newVal) -> {
            if (newVal != null) {
                fillForm(newVal);
            }
        });

        searchField.textProperty().addListener((obs, old, newVal) -> {
            if (newVal == null || newVal.trim().isEmpty()) {
                table.setItems(service.getTickets());
            } else {
                table.setItems(FXCollections.observableArrayList(service.search(newVal.trim())));
            }
        });

        Scene scene = new Scene(root, 1000, 700);
        scene.getStylesheets().add(getClass().getResource("ticket-persistence.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.setTitle("Ticket Persistence App");
        primaryStage.show();
    }

    private void clearForm() {
        titleField.clear();
        customerField.clear();
        priorityBox.setValue(null);
        statusBox.setValue(null);
        datePicker.setValue(null);
        descriptionArea.clear();
        urgentCheck.setSelected(false);
    }

    private void fillForm(SupportTicket ticket) {
        titleField.setText(ticket.getTitle());
        customerField.setText(ticket.getCustomerName());
        priorityBox.setValue(ticket.getPriority());
        statusBox.setValue(ticket.getStatus());
        datePicker.setValue(ticket.getCreatedAt());
        descriptionArea.setText(ticket.getDescription());
        urgentCheck.setSelected(ticket.isUrgent());
    }

    private boolean validateForm() {
        if (titleField.getText().trim().isEmpty() ||
            customerField.getText().trim().isEmpty() ||
            priorityBox.getValue() == null ||
            statusBox.getValue() == null ||
            datePicker.getValue() == null ||
            descriptionArea.getText().trim().isEmpty()) {
            statusLabel.setText("Please fill all fields");
            return false;
        }
        return true;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
