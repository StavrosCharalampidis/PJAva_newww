package app.Scene;

import app.Main;

import java.util.List;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import app.MoviesPackage.Tickets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class MovieSceneCreator extends SceneCreator implements EventHandler<MouseEvent> {
    BorderPane rootBorderPane;
    Button cancelButton, addTicketButton, modifyButton, goback;
    TextField movieField, typeField, seatField, priceField;
    Label movieLabel, typeLabel, seatLabel, priceLabel;
    TableView<Tickets> ticketTableView;
    List <Tickets> ticketList;

    public MovieSceneCreator(double width, double height) {
        // SceneCreator (width και height)
        super(width, height);
        
        // Add all components  
        rootBorderPane = new BorderPane();
        ticketTableView = new TableView<>();
        ticketList = new ArrayList<>();
        
        movieLabel = new Label("Movie: ");
        typeLabel = new Label("Type: ");
        seatLabel = new Label("Seat: ");
        priceLabel = new Label("Price: ");
        
        movieField = new TextField();
        typeField = new TextField();
        seatField = new TextField();
        priceField = new TextField();
        
        // All Button Components
        addTicketButton = new Button("Add Ticket");  
        cancelButton = new Button("cancel");
        modifyButton = new Button("modifyButton");
        goback = new Button("go back");
        
        // Border Pane layout
        rootBorderPane.setPadding(new Insets(10));
       
        // Create columns
        TableColumn<Tickets, String> codeColumn = new TableColumn<>("Code");
        TableColumn<Tickets, String> movieColumn = new TableColumn<>("Movies");
        TableColumn<Tickets, String> issueDateColumn = new TableColumn<>("Issue Date");
        TableColumn<Tickets, String> typeColumn = new TableColumn<>("Type");
        TableColumn<Tickets, String> seatColumn = new TableColumn<>("Seat");
        TableColumn<Tickets, Double> priceColumn = new TableColumn<>("Price");

        // Set column cell value factories
        codeColumn.setCellValueFactory(new PropertyValueFactory<>("code"));
        movieColumn.setCellValueFactory(new PropertyValueFactory<>("screening"));
        issueDateColumn.setCellValueFactory(new PropertyValueFactory<>("dateOfIssue"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        seatColumn.setCellValueFactory(new PropertyValueFactory<>("seat"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        // Add columns to the table
        ticketTableView.getColumns().addAll(codeColumn, movieColumn, issueDateColumn, typeColumn, seatColumn, priceColumn);
        
        // Add to Left of Border Pane
        rootBorderPane.setLeft(ticketTableView);

        // Create input area for new ticket
        VBox MovieTicketInputBox = new VBox(10);
        MovieTicketInputBox.setPadding(new Insets(10));
        
        // All Vbox Children
        MovieTicketInputBox.getChildren().addAll(movieLabel, movieField,typeLabel, typeField,seatLabel, seatField,priceLabel, priceField, addTicketButton, cancelButton, modifyButton,  goback);
        //MovieTicketInputBox.getChildren().addAll(movieLabel, typeField
        // Add to Right of Border Pane
        rootBorderPane.setRight(MovieTicketInputBox);
        
        addTicketButton.setOnMouseClicked(this);
        cancelButton.setOnMouseClicked(this);
        modifyButton.setOnMouseClicked(this);
        goback.setOnMouseClicked(this);
    }
    
    @Override
    public Scene createScene() {
        return new Scene(rootBorderPane, width, height);            
    }
    


        
    @Override
    public void handle(MouseEvent event) {
        if (event.getSource() == addTicketButton) {
            String movie = movieField.getText();
            String type = typeField.getText();
            String seat = seatField.getText();
            double price = Double.parseDouble(priceField.getText());
            //Create New Ticket
            NewCreateTicket(movie, type, seat, price);
            
            tableSync();
            // Clear the text fields
            clearTextFields();
        }
        
        if (event.getSource() == modifyButton) {
            String Movie = movieField.getText();
            String type = typeField.getText();
            String seat = seatField.getText();
            double price = Double.parseDouble(priceField.getText());
            //Create New Ticket
            modifyTicket(Movie, type, seat, price);
            
            tableSync();
            // Clear the text fields
            clearTextFields();
        }
        
        if (event.getSource() == cancelButton) {
            CancelTicket(movieField.getText(), typeField.getText(), seatField.getText(), Double.parseDouble(priceField.getText()));
    
            tableSync();
            clearTextFields();
        }
        
        if (event.getSource() == goback) {
            //Set the Stage to UserScene
            Main.primaryStage.setScene(Main.UserScene);
            Main.primaryStage.setTitle("User Scene");
            
        }
        
    }
    

    
    public void modifyTicket(String Movie, String type, String seat, double price) {
        LocalDateTime screeningDateTime = LocalDateTime.parse(Movie, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        LocalDateTime currentDateTime = LocalDateTime.now();
        Duration duration = Duration.between(currentDateTime, screeningDateTime);
        Tickets modifyTicket;
        String dateOfIssue = "2024-12-18", code = "T001";
        
        if (duration.toHours() <= 2) {

            modifyTicket = new Tickets(code, Movie, dateOfIssue, type, seat, price);
        }
        
        else {
            System.out.println("Ticket modification is not allowed at this time.");
        }
    }
    
    public void CancelTicket(String screening, String type, String seat, double price) {
        Tickets ticketToRemove = null;

        for (Tickets ticket : ticketList) {
            if (ticket.getScreening().equals(screening)
                    && ticket.getType().equals(type)
                    && ticket.getSeat().equals(seat)
                    && ticket.getPrice() == price) {
                ticketToRemove = ticket;
                break;
            }
        }

        if (ticketToRemove != null) {
            ticketList.remove(ticketToRemove);
            System.out.println("Ticket canceled successfully.");
            } 
        else {
            System.out.println("Ticket not found. No cancellation performed.");
            }
        }

    

    
    public void NewCreateTicket(String movie, String type, String seat, double price){
         Tickets newTicket = new Tickets("T001", movie, "2024-12-18", type, seat, price);
         ticketList.add(newTicket);
    }
    
        
    public void tableSync() {
        List<Tickets> items = ticketTableView.getItems();
        for (Tickets ticket : ticketList) {
            if (ticket instanceof Tickets) {
                items.add(ticket);
            }
        }
    }
    
    public void clearTextFields() {
            // Clear the text fields
            movieField.clear();
            typeField.clear();
            seatField.clear();
            priceField.clear();
     }
    
}