package source.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController implements Initializable{
    
    private Stage primaryStage;


    @FXML
    private TextField userNameTextField;
    @FXML
    private TextField passwordTextField;

    @FXML
    private Button submitButton;

    @FXML
    private ComboBox<String> departmentComboBox;

    @FXML
    private void submitButtonHandleClick(Event event) {

        Alert isValid = new Alert(AlertType.WARNING, "Provide required info", null);
        Alert isAllowed = new Alert(AlertType.WARNING, "Wrong username or password", null);

        if (validateInput()) {

            if (authenticate()) {

                switch (departmentComboBox.getSelectionModel().getSelectedIndex()) {
                    case 0:
                        
                            Parent adminParent = loadFXML("/source/fxml/admin/", "dashboard.fxml");
                            Scene adminScene = new Scene(adminParent);

                            primaryStage.setTitle("Administration - Dashboard");
                            primaryStage.setScene(adminScene);
                        break;
                    case 1:

                            Parent financeParent = loadFXML("/source/fxml/finance/", "budget.fxml");
                            Scene financeScene = new Scene(financeParent);

                            primaryStage.setTitle("Finance - Budget");
                            primaryStage.setScene(financeScene);
                        break;
                    case 2:

                            Parent infotechParent = loadFXML("/source/fxml/infotech/", "monitor.fxml");
                            Scene infoTechScene = new Scene(infotechParent);

                            primaryStage.setTitle("InfoTech - Monitor");
                            primaryStage.setScene(infoTechScene);

                        break;
                    default:
                        break;
                }

            } else {

                isAllowed.showAndWait();
            }

        } else {
            isValid.showAndWait();
        }

    }

    @FXML
    private void forgottenHandleClick(Event event) {

        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        
        Alert loginAlert = new Alert(AlertType.INFORMATION);
        loginAlert.setContentText("How did that happen?");
        loginAlert.show();
        
        // Parent newParent = loadFXML("logout.fxml");
        // Scene newScene = new Scene(newParent);

        // stage.setScene(newScene);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
        departmentComboBox.getItems().addAll(FXCollections.observableArrayList("Administration", "Finance", "IT"));
        departmentComboBox.setValue("Administration");
    }

    public void setPrimaryStage(Stage primaryStage) {
        
        primaryStage.setResizable(false);
        this.primaryStage = primaryStage;
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

    private boolean validateInput() {
        
        if (!userNameTextField.getText().isEmpty() && !passwordTextField.getText().isEmpty()) {

            return true;
        }else {

            return false;
        }
    }

    private boolean authenticate() {

        try {

            return true;
        } catch (Exception e) {
            
            return false;
        }
    }
}