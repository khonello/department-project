package source.controllers.infotech;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Optional;
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

public class MonitorController implements Initializable {
        
    HashMap<String, HashMap<String, String>> devicesHashMap = new HashMap<>();

    ObservableList<String> deviceCategoryItems = FXCollections.observableArrayList("Laptop", "Desktop", "Printer");
    ObservableList<String> laptopListingItems = FXCollections.observableArrayList();
    ObservableList<String> desktopListingItems = FXCollections.observableArrayList();
    ObservableList<String> printerListingItems = FXCollections.observableArrayList();

    @FXML
    Label deviceNameLabel;
    @FXML
    Label deviceCompanyLabel;
    @FXML
    Label deviceStatusLabel;

    @FXML
    Button addDeviceButton;
    @FXML
    Button saveChangesButton;

    @FXML
    TextArea deviceDescriptionTextArea;

    @FXML
    ListView<String> categoryListView;
    @FXML
    ListView<String> listingListView;

    @FXML
    private void handleHelpDeskClick(Event event) {

        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        
        Parent newParent = loadFXML("/source/fxml/infotech/", "helpdesk.fxml");
        Scene newScene = new Scene(newParent);

        stage.setTitle("IT - Help Desk");
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
    private void handleDeviceAddClick(Event event) {

        String selectedCategory = categoryListView.getSelectionModel().getSelectedItem();
        HashMap<String, String> deviceInfo = takeDeviceInfo("Enter Device Information");

        if (deviceInfo != null) {

            String deviceName = deviceInfo.get("name");
            String deviceCompany = deviceInfo.get("company");
            String deviceStatus = deviceInfo.get("status");

            switch (selectedCategory) {
                case "Laptop":
                    laptopListingItems.add(deviceName);
                    listingListView.setItems(laptopListingItems);
                    break;
                case "Desktop":
                    desktopListingItems.add(deviceName);
                    listingListView.setItems(desktopListingItems);
                    break;
                case "Printer":
                    printerListingItems.add(deviceName);
                    listingListView.setItems(printerListingItems);
                    break;
                default:
                    break;
            }

            HashMap<String, String> newDeviceInfo = new HashMap<>();

            newDeviceInfo.put("company", deviceCompany);
            newDeviceInfo.put("status", deviceStatus);
            devicesHashMap.put(deviceName, newDeviceInfo);

            listingListView.refresh();
        }
    }

    @FXML
    private void handleSaveChangesClick(Event event) {

        String selectedDevice = listingListView.getSelectionModel().getSelectedItem();
        String description = deviceDescriptionTextArea.getText();
        
        HashMap<String, String> deviceInfo = devicesHashMap.get(selectedDevice);

        deviceInfo.put("description", description);
        deviceDescriptionTextArea.setText(null);

        saveChangesButton.setDisable(true);
    }

    @FXML
    private void handleTextChangedClick(Event event) {

        if (!deviceDescriptionTextArea.getText().isEmpty() && listingListView.getSelectionModel().getSelectedItem() != null) {

            saveChangesButton.setDisable(false);
        } else {
            saveChangesButton.setDisable(true);
        }

    }

    @FXML 
    private void handleUpdateButtonClick(Event event) {

        Alert loginAlert = new Alert(AlertType.INFORMATION);
        loginAlert.setContentText("Devices updated");
        loginAlert.show();    
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        categoryListView.getItems().addAll(deviceCategoryItems);
        categoryListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            addDeviceButton.setDisable(false);

            deviceNameLabel.setText(null);
            deviceStatusLabel.setText(null);
            deviceCompanyLabel.setText(null);

            switch (newValue) {
                case "Laptop":
                    listingListView.setItems(laptopListingItems);
                    break;
                case "Desktop":
                    listingListView.setItems(desktopListingItems);
                    break;
                case "Printer":
                    listingListView.setItems(printerListingItems);
                    break;
                default:
                    break;
            }
        });

        listingListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {

            if (newValue != null) {

                HashMap<String, String> deviceInfoHashMap = devicesHashMap.get(newValue);
                if (deviceInfoHashMap != null) {

                    deviceNameLabel.setText(newValue);
                    deviceCompanyLabel.setText(deviceInfoHashMap.get("company"));
                    deviceStatusLabel.setText(deviceInfoHashMap.get("status"));
                    deviceDescriptionTextArea.setText(deviceInfoHashMap.get("description"));
                }
            }
        });
    }

    private HashMap<String, String> takeDeviceInfo(String contentText) {

        Dialog<HashMap<String, String>> dialog = new Dialog<>();
        ButtonType okButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        GridPane grid = new GridPane();
        
        TextField deviceName = new TextField();
        TextField deviceCompany = new TextField();
        TextField deviceStatus = new TextField();

        dialog.setTitle("Device Info");
        dialog.setHeaderText(contentText);

        dialog.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);

        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        deviceName.setPromptText("Device Name");
        deviceCompany.setPromptText("Device Company");
        deviceStatus.setPromptText("Device Status");

        grid.add(new Label("Device Name:"), 0, 0);
        grid.add(deviceName, 1, 0);
        grid.add(new Label("Device Company:"), 0, 1);
        grid.add(deviceCompany, 1, 1);
        grid.add(new Label("Device Status:"), 0, 2);
        grid.add(deviceStatus, 1, 2);

        Node okButton = dialog.getDialogPane().lookupButton(okButtonType);
        okButton.setDisable(true);

        deviceName.textProperty().addListener((observable, oldValue, newValue) -> 
            okButton.setDisable(newValue.trim().isEmpty() || deviceCompany.getText().trim().isEmpty() || deviceStatus.getText().trim().isEmpty())
        );
        deviceCompany.textProperty().addListener((observable, oldValue, newValue) -> 
            okButton.setDisable(newValue.trim().isEmpty() || deviceName.getText().trim().isEmpty() || deviceStatus.getText().trim().isEmpty())
        );
        deviceStatus.textProperty().addListener((observable, oldValue, newValue) -> 
            okButton.setDisable(newValue.trim().isEmpty() || deviceName.getText().trim().isEmpty() || deviceCompany.getText().trim().isEmpty())
        );

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == okButtonType) {
                HashMap<String, String> result = new HashMap<>();
                result.put("name", deviceName.getText());
                result.put("company", deviceCompany.getText());
                result.put("status", deviceStatus.getText());
                result.put("description", null);
                return result;
            }
            return null;
        });

        Optional<HashMap<String, String>> result = dialog.showAndWait();
        return result.orElse(null);

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