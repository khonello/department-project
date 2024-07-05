package source.controllers.admin;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import javafx.util.converter.DefaultStringConverter;

public class ProjectController implements Initializable {

    public class Projects {

        SimpleStringProperty nameProperty = new SimpleStringProperty();
        SimpleStringProperty startDateProperty = new SimpleStringProperty();
        SimpleStringProperty endDateProperty = new SimpleStringProperty();
        SimpleStringProperty supervisorProperty = new SimpleStringProperty();
        SimpleStringProperty departmentProperty = new SimpleStringProperty();
        SimpleStringProperty statusProperty = new SimpleStringProperty();
        SimpleBooleanProperty deleteProperty = new SimpleBooleanProperty();

        public Projects(String name, String startDate, String endDate, String supervisor, String department, String status, boolean delete) {

            this.nameProperty.set(name);
            this.startDateProperty.set(startDate);
            this.endDateProperty.set(endDate);
            this.supervisorProperty.set(supervisor);
            this.departmentProperty.set(department);
            this.statusProperty.set(status);
            this.deleteProperty.set(delete);
        }
    }

    ObservableList<Projects> projectsItems = FXCollections.observableArrayList();
    ObservableList<String> supervisorObservableList = FXCollections.observableArrayList("----------", "Richmond", "Khonello");
    ObservableList<String> departmentObservableList = FXCollections.observableArrayList("----------", "Administration", "Finance", "IT");
    ObservableList<String> statusObservableList = FXCollections.observableArrayList("----------", "Pending", "Rejected", "Approved");

    @FXML
    TableView<Projects> projectTableView;

    @FXML
    TableColumn<Projects, String> projectNameColumn;
    @FXML
    TableColumn<Projects, LocalDate> projectStartColumn;
    @FXML
    TableColumn<Projects, LocalDate> projectEndColumn;
    @FXML
    TableColumn<Projects, String> projectSupervisorColumn;
    @FXML
    TableColumn<Projects, String> projectDepartmentColumn;
    @FXML
    TableColumn<Projects, String> projectStatusColumn;
    @FXML
    TableColumn<Projects, Boolean> projectDeleteColumn;
    
    @FXML
    private void handleDashboardClick(Event event) {

        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        
        Parent newParent = loadFXML("/source/fxml/admin/", "dashboard.fxml");
        Scene newScene = new Scene(newParent);

        stage.setTitle("Administration - Dashboard");
        stage.setScene(newScene);
    }

    @FXML
    private void handleTechSupportClick(Event event) {

        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        
        Parent newParent = loadFXML("/source/fxml/admin/", "techsupport.fxml");
        Scene newScene = new Scene(newParent);

        stage.setTitle("Administration - Tech Support");
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
    private void handleSaveClick(Event event) {

        // Node source = (Node) event.getSource();
        // Stage stage = (Stage) source.getScene().getWindow();
        
        Alert loginAlert = new Alert(AlertType.INFORMATION);
        loginAlert.setContentText("Changes Saved");
        loginAlert.show();
        
        // Parent newParent = loadFXML("logout.fxml");
        // Scene newScene = new Scene(newParent);

        // stage.setScene(newScene);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
        projectNameColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DefaultStringConverter()));
        projectStartColumn.setCellFactory(column -> {

            TableCell<Projects, LocalDate> localDateCeil = new TableCell<Projects, LocalDate>() {
                private DatePicker datePicker = new DatePicker();
                
                @Override
                protected void updateItem(LocalDate item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {

                        setText(null);
                        setGraphic(null);
                    } else {

                        datePicker.setValue(item);
                        setGraphic(datePicker);
                    }
                }

                {
                    datePicker.setOnAction(event -> {
                        commitEdit(datePicker.getValue());
                    });
                    this.setGraphic(datePicker);
                }

            };
            return localDateCeil;
        });
        projectEndColumn.setCellFactory(column -> {

            TableCell<Projects, LocalDate> localDateCeil = new TableCell<Projects, LocalDate>() {
                private DatePicker datePicker = new DatePicker();
                
                @Override
                protected void updateItem(LocalDate item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {

                        setText(null);
                        setGraphic(null);
                    } else {

                        datePicker.setValue(item);
                        setGraphic(datePicker);
                    }
                }

                {
                    datePicker.setOnAction(event -> {
                        commitEdit(datePicker.getValue());
                    });
                    this.setGraphic(datePicker);
                }

            };
            return localDateCeil;
        });
        projectSupervisorColumn.setCellFactory(column -> {

            TableCell<Projects, String> comboBoxCell = new TableCell<Projects, String>() {
                private ComboBox<String> comboBox = new ComboBox<String>();

                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {

                        setText(null);
                        setGraphic(null);
                    } else {

                        comboBox.setItems(supervisorObservableList);
                        comboBox.setValue("----------");
                        comboBox.valueProperty().addListener((observable, oldValue, newValue) -> {

                            if (newValue != null) {
                                commitEdit(newValue);
                            }
                        });
                        setGraphic(comboBox);
                    }
                }

                {
                    //
                }
            };
            return comboBoxCell;
        });
        projectDepartmentColumn.setCellFactory(column -> {

            TableCell<Projects, String> comboBoxCell = new TableCell<Projects, String>() {
                private ComboBox<String> comboBox = new ComboBox<String>();

                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {

                        setText(null);
                        setGraphic(null);
                    } else {

                        comboBox.setItems(departmentObservableList);
                        comboBox.setValue("----------");
                        comboBox.valueProperty().addListener((observable, oldValue, newValue) -> {

                            if (newValue != null) {
                                commitEdit(newValue);
                            }
                        });
                        setGraphic(comboBox);
                    }
                }

                {
                    //
                }
            };
            return comboBoxCell;
        });
        projectStatusColumn.setCellFactory(column -> {

            TableCell<Projects, String> comboBoxCell = new TableCell<Projects, String>() {
                private ComboBox<String> comboBox = new ComboBox<String>();

                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {

                        setText(null);
                        setGraphic(null);
                    } else {

                        comboBox.setItems(statusObservableList);
                        comboBox.setValue("----------");
                        comboBox.valueProperty().addListener((observable, oldValue, newValue) -> {

                            if (newValue != null) {
                                commitEdit(newValue);
                            }
                        });
                        setGraphic(comboBox);
                    }
                }

                {
                    //
                }
            };
            return comboBoxCell;
        });
        projectDeleteColumn.setCellFactory(CheckBoxTableCell.forTableColumn(projectDeleteColumn));

        projectNameColumn.setCellValueFactory(cell -> {
            cell.getValue().nameProperty.addListener((observable, oldValue, newValue) -> {

                if (!newValue.isEmpty()) {

                    if (projectTableView.getItems().indexOf(cell.getValue()) == projectTableView.getItems().size() - 1) {

                        projectTableView.getItems().add(new Projects(null, null, null, null, null, null, false));
                    }
                }
            });
            return cell.getValue().nameProperty;
        });
        projectDeleteColumn.setCellValueFactory(cell -> {
            cell.getValue().deleteProperty.addListener((observable, oldValue, newValue) -> {

                if (newValue) {

                    projectTableView.getItems().remove(cell.getValue());
                }
            });
            return cell.getValue().deleteProperty;
        });
        projectsItems.addAll(new Projects(null, null, null, null, null, null, false));
        projectTableView.getItems().addAll(projectsItems);
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