package source.controllers.finance;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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

public class TechSupportController implements Initializable {

    HashMap<String, HashMap<String, String>> complaintHashMap = new HashMap<>();

    ObservableList<String> deviceCategoryItems = FXCollections.observableArrayList("---------------", "Laptop", "Desktop", "Printer");
    ObservableList<String> laptopListingItems = FXCollections.observableArrayList("Dell", "HP", "MSI", "Chromebook");
    ObservableList<String> desktopListingItems = FXCollections.observableArrayList("Pentium");
    ObservableList<String> printerListingItems = FXCollections.observableArrayList("LaserJet", "InkJet", "3D Printer");

    @FXML
    Button contactSupportButton;

    @FXML
    TextField nameTextField;

    @FXML
    ComboBox<String> deviceCategoryComboBox;

    @FXML
    ListView<String> deviceNameListView;

    @FXML
    private void handleBudgetClick(Event event) {

        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        
        Parent newParent = loadFXML("/source/fxml/finance/", "budget.fxml");
        Scene newScene = new Scene(newParent);

        stage.setTitle("Finance - Budget");
        stage.setScene(newScene);

    }

    @FXML
    private void handleReportClick(Event event) {

        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        
        Parent newParent = loadFXML("/source/fxml/finance/", "report.fxml");
        Scene newScene = new Scene(newParent);

        stage.setTitle("Finance - Report");
        stage.setScene(newScene);

    }

    @FXML
    private void handleContactSupportClick(Event event) {
        
        Dialog<String> dialog = new Dialog<>();
        ButtonType okButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        GridPane gridPane = new GridPane();

        TextField complaintTextField = new TextField("Complaint #" + (complaintHashMap.keySet().size() + 1));
        TextArea additionalInfoTextArea = new TextArea();

        dialog.setTitle("Complaint Info");
        dialog.setHeaderText("Create a new complaint");

        dialog.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);

        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(20, 150, 10, 10));

        complaintTextField.setPromptText("Complaint Name");
        complaintTextField.setEditable(false);
        additionalInfoTextArea.setPromptText("Additional Info");

        gridPane.add(new Label("Complaint Name:"), 0, 0);
        gridPane.add(complaintTextField, 1, 0);
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

                    newInfo.put("respondTo", nameTextField.getText());
                    newInfo.put("deviceName", deviceNameListView.getSelectionModel().getSelectedItem());
                    newInfo.put("deviceCategory", deviceCategoryComboBox.getSelectionModel().getSelectedItem());

                    complaintHashMap.put(complaintTextField.getText(), newInfo);
                    return null;
                }
                return null;
            }
        });
        dialog.showAndWait();

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

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        deviceCategoryComboBox.setValue("---------------");
        deviceCategoryComboBox.getItems().addAll(deviceCategoryItems);
        deviceCategoryComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            
            contactSupportButton.setDisable(true);
            if (deviceCategoryComboBox.getSelectionModel().getSelectedIndex() != 0) {
                
                switch (deviceCategoryComboBox.getSelectionModel().getSelectedItem()) {
                    case "Laptop":

                        deviceNameListView.setItems(laptopListingItems);
                        break;
                    case "Desktop":

                        deviceNameListView.setItems(desktopListingItems);
                        break;

                    case "Printer":
                        deviceNameListView.setItems(printerListingItems);
                        break;
                }
            }
        });

        deviceNameListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {

            if (newValue != null) {

                contactSupportButton.setDisable(false);
            }
        });
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
