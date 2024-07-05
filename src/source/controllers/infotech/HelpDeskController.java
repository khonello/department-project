package source.controllers.infotech;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.ResourceBundle;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*; 
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Callback;

public class HelpDeskController implements Initializable {

    HashMap<String, HashMap<String, String>> ticketHashMap = new HashMap<>();

    @FXML
    ListView<String> ticketListView;
    
    @FXML
    TextArea mustKnowTextArea;
    
    @FXML
    Label isHandledLabel;

    @FXML
    Button issueHandledButton;

    @FXML
    Button deleteTicketButton;

    @FXML
    private void handleHelpDeskClick(Event event) {

        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        
        Parent newParent = loadFXML("/source/fxml/infotech/", "monitor.fxml");
        Scene newScene = new Scene(newParent);

        stage.setTitle("IT - Monitor");
        stage.setScene(newScene);

    }

    @FXML
    private void handleLogoutClick(Event event) {

        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        
        Alert loginAlert = new Alert(AlertType.WARNING);

        loginAlert.setContentText("Are you sure you want to logout ?");
        loginAlert.showAndWait().ifPresent(buttonType -> {
            if (buttonType == ButtonType.OK) {

                Parent newParent = loadFXML("/source/fxml/", "logout.fxml");
                Scene newScene = new Scene(newParent);

                stage.setScene(newScene);
            }
        });

    }

    @FXML
    private void handleIssueHandledClick(Event event) {

        String selectedItem = ticketListView.getSelectionModel().getSelectedItem();
        HashMap<String, String> ticketInfo = ticketHashMap.get(selectedItem);

        ticketInfo.put("status", "1");
        updateStatus(ticketInfo);
    }

    @FXML
    private void handleDeleteTicketClick(Event event) {

        String selectedItem = ticketListView.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            ticketListView.getItems().remove(selectedItem);
            ticketHashMap.remove(selectedItem);
        }
    }

    @FXML
    private void handleAddTicketClick(Event event) {

        Dialog<String> dialog = new Dialog<>();
        ButtonType okButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        GridPane gridPane = new GridPane();

        TextField ticketNameTextField = new TextField("Ticket #" + (ticketHashMap.keySet().size() + 1));
        TextArea additionalInfoTextArea = new TextArea();

        dialog.setTitle("Ticket Info");
        dialog.setHeaderText("Create a new ticket");

        dialog.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);

        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(20, 150, 10, 10));

        ticketNameTextField.setPromptText("Ticket Name");
        ticketNameTextField.setEditable(false);
        additionalInfoTextArea.setPromptText("Additional Info");

        gridPane.add(new Label("Ticket Name:"), 0, 0);
        gridPane.add(ticketNameTextField, 1, 0);
        gridPane.add(new Label("Additional Info:"), 0, 1);
        gridPane.add(additionalInfoTextArea, 1, 1);

        Node okButton = dialog.getDialogPane().lookupButton(okButtonType);
        okButton.setDisable(true);

        additionalInfoTextArea.textProperty().addListener((observable, oldValue, newValue) -> 
            okButton.setDisable(newValue.trim().isEmpty())
        );

        dialog.getDialogPane().setContent(gridPane);
        dialog.setResultConverter(new Callback<ButtonType, String>() {
            @Override
            public String call(ButtonType dialogButton) {

                if (dialogButton == okButtonType) {
                    HashMap<String, String> newInfo = new HashMap<>();

                    newInfo.put("mustKnow", additionalInfoTextArea.getText());
                    newInfo.put("status", "0");
                    newInfo.put("deleted", "0");

                    ticketHashMap.put(ticketNameTextField.getText(), newInfo);
                    return null;
                }
                return null;
            }
        });
        dialog.showAndWait();

        ArrayList<String> arrayList = new ArrayList<>(ticketHashMap.keySet());
        Collections.sort(arrayList);

        ticketListView.getItems().setAll(arrayList);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        ticketListView.getItems().addAll(ticketHashMap.keySet());
        ticketListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            
            issueHandledButton.setDisable(false);
            deleteTicketButton.setDisable(false);

            if (newValue != null) {

                HashMap<String, String> ticketInfo = ticketHashMap.get(newValue);

                updateStatus(ticketInfo);
                mustKnowTextArea.setText(ticketInfo.get("mustKnow"));

            }
        });
    }

    private void updateStatus(HashMap<String, String> ticketInfo) {

        if (ticketInfo.get("status") != "0") {

            isHandledLabel.setText("Handled");
            isHandledLabel.setStyle("-fx-text-fill: #00ab0b;");

            issueHandledButton.setDisable(true);
        } else {

            isHandledLabel.setText("Not Handled");
            isHandledLabel.setStyle("-fx-text-fill: #da1f0e;");

            issueHandledButton.setDisable(false);
        }
    }

    private Parent loadFXML(String fxmlFolderName, String fxmlFileName) {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlFolderName + fxmlFileName));
            
            return fxmlLoader.load();
        } catch (IOException e) {
            
            e.printStackTrace();
            return null;
        }
    }
}
