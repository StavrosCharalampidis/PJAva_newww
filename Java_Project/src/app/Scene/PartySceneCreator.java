package app.Scene;

import app.Main;
import app.PartyPackage.PartyEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.Label;
import javafx.scene.control.cell.PropertyValueFactory;

public class PartySceneCreator extends SceneCreator implements EventHandler<MouseEvent> {
    private BorderPane rootBorderPane;
    private Button cancelButton, addPartyButton, modifyButton, goBackButton;
    private TextField codeField, dateField, detailsField, priceField;
    private TableView<PartyEvent> partyTableView;
    private List<PartyEvent> partyList;
    private Label codeLabel, dateLabel, detailsLabel, priceLabel;
    public PartySceneCreator(double width, double height) {
        super(width, height);

        rootBorderPane = new BorderPane();
        partyTableView = new TableView<>();
        partyList = new ArrayList<>();
        
        codeLabel = new Label("Code: ");
        dateLabel = new Label("Date: ");
        detailsLabel = new Label("Details: ");
        priceLabel = new Label("Price: ");
        
        codeField = new TextField();
        dateField = new TextField();
        detailsField = new TextField();
        priceField = new TextField();

        addPartyButton = new Button("Add Party ");
        cancelButton = new Button("Cancel Party");
        modifyButton = new Button("Modify Party");
        goBackButton = new Button("Go Back");

        rootBorderPane.setPadding(new Insets(10));

        TableColumn<PartyEvent, String> codeColumn = new TableColumn<>("Code");
        TableColumn<PartyEvent, String> dateColumn = new TableColumn<>("Date");
        TableColumn<PartyEvent, String> detailsColumn = new TableColumn<>("Details");
        TableColumn<PartyEvent, Double> priceColumn = new TableColumn<>("Price");

        codeColumn.setCellValueFactory(new PropertyValueFactory<>("code"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        detailsColumn.setCellValueFactory(new PropertyValueFactory<>("details"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        partyTableView.getColumns().addAll(codeColumn, dateColumn, detailsColumn, priceColumn);

        rootBorderPane.setLeft(partyTableView);

        VBox partyInputBox = new VBox(10);
        partyInputBox.setPadding(new Insets(10));

        addPartyButton.setOnMouseClicked(this);
        cancelButton.setOnMouseClicked(this);
        modifyButton.setOnMouseClicked(this);
        goBackButton.setOnMouseClicked(this);

        partyInputBox.getChildren().addAll(codeLabel, codeField, dateLabel, dateField, detailsLabel, detailsField, priceLabel, priceField,
                addPartyButton,
                cancelButton,
                modifyButton,
                goBackButton
        );

        rootBorderPane.setRight(partyInputBox);
    }

    @Override
    public Scene createScene() {
        return new Scene(rootBorderPane, width, height);
    }

    @Override
    public void handle(MouseEvent event) {
        if (event.getSource() == addPartyButton) {
            try {
                String code = codeField.getText();
                String date = dateField.getText();
                String details = detailsField.getText();
                double price = Double.parseDouble(priceField.getText());
                
                createNewParty(code, date, details, price);
                
                tableSync();
                clearTextFields();
            } catch (NumberFormatException e) {
                System.out.println("Invalid input for price. Please enter a valid number.");
            }
        }

        if (event.getSource() == modifyButton) {
            String code = codeField.getText();
            String date = dateField.getText();
            String details = detailsField.getText();
            double price = Double.parseDouble(priceField.getText());

            modifyParty(code, date, details, price);

            tableSync();
            clearTextFields();
        }

        if (event.getSource() == cancelButton) {
            cancelParty(codeField.getText());

            tableSync();
            clearTextFields();
        }

        if (event.getSource() == goBackButton) {
            Main.primaryStage.setScene(Main.UserScene);
            Main.primaryStage.setTitle("User Scene");
        }
    }

    public void modifyParty(String code, String date, String details, double price) {
        LocalDateTime partyDateTime = LocalDateTime.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        LocalDateTime currentDateTime = LocalDateTime.now();
        Duration duration = Duration.between(currentDateTime, partyDateTime);

        if (duration.toHours() <= 24) {
            PartyEvent modifiedParty = new PartyEvent(code, date, details, price);
            // Implement your modification logic here
        } else {
            System.out.println("Party modification is not allowed at this time.");
        }
    }

    public void cancelParty(String code) {
        partyList.removeIf(party -> party.getPartyCode().equals(code));
    }

    public void createNewParty(String code, String date, String details, double price) {
        PartyEvent newParty = new PartyEvent(code, date, details, price);
        partyList.add(newParty);
    }

    public void tableSync() {
        partyTableView.getItems().setAll(partyList);
    }

    public void clearTextFields() {
        codeField.clear();
        dateField.clear();
        detailsField.clear();
        priceField.clear();
    }
}
