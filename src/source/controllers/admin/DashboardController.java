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
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import javafx.util.converter.DefaultStringConverter;

public class DashboardController implements Initializable {

    public class Events {

        SimpleStringProperty nameProperty = new SimpleStringProperty();
        SimpleStringProperty dateProperty = new SimpleStringProperty();
        SimpleStringProperty typeProperty = new SimpleStringProperty();
        SimpleBooleanProperty deleteProperty = new SimpleBooleanProperty();

        public Events(String name, String date, String type, Boolean delete) {

            this.nameProperty.set(name);
            this.dateProperty.set(date);
            this.typeProperty.set(type);
            this.deleteProperty.set(false);
        }
    }

    ObservableList<Events> eventItems = FXCollections.observableArrayList();        // load from database
    ObservableList<String> eventTypeObservableList = FXCollections.observableArrayList("----------", "Birthday", "Meeting", "Holiday", "Other");

    @FXML
    TableView<Events> eventTableView;

    @FXML
    TableColumn<Events, String> eventNameColumn;
    @FXML
    TableColumn<Events, LocalDate> eventDateColumn;
    @FXML
    TableColumn<Events, String> eventTypeColumn;
    @FXML
    TableColumn<Events, Boolean> eventDeleteColumn;

    @FXML
    PieChart dashboardChart;

    @FXML
    private void handleProjectClick(Event event) {

        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        
        Parent newParent = loadFXML("/source/fxml/admin/", "project.fxml");
        Scene newScene = new Scene(newParent);

        stage.setTitle("Administration - Project");
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
        
        eventNameColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DefaultStringConverter()));
        eventDateColumn.setCellFactory(column -> {

            TableCell<Events, LocalDate> localDateCeil = new TableCell<Events, LocalDate>() {
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
        eventTypeColumn.setCellFactory(column -> {

            TableCell<Events, String> comboBoxCell = new TableCell<Events, String>() {
                private ComboBox<String> comboBox = new ComboBox<String>();

                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {

                        setText(null);
                        setGraphic(null);
                    } else {

                        comboBox.setItems(eventTypeObservableList);
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
        eventDeleteColumn.setCellFactory(CheckBoxTableCell.forTableColumn(eventDeleteColumn));
        
        eventNameColumn.setCellValueFactory(cell -> {
            
            cell.getValue().nameProperty.addListener((observable, oldValue, newValue) -> {

                if (!newValue.isEmpty()) {

                    if (eventTableView.getItems().indexOf(cell.getValue()) == eventTableView.getItems().size() - 1) {

                        eventTableView.getItems().add(new Events(null, null, null, false));
                    }
                }
            });
            return cell.getValue().nameProperty;
        });
        eventDeleteColumn.setCellValueFactory(cell -> {

            cell.getValue().deleteProperty.addListener((observable, oldValue, newValue) -> {

                if (newValue) {

                    eventTableView.getItems().remove(cell.getValue());
                }
            });
            return cell.getValue().deleteProperty;
        });

        PieChart.Data employesData = new PieChart.Data("Employees", 50);            // fetch from database
        PieChart.Data eventsData = new PieChart.Data("Events", 120);                // 
        PieChart.Data projectsData = new PieChart.Data("Projects", 81);             //

        eventItems.add(new Events(null, null, null, false));            // set into database

        eventTableView.getItems().addAll(eventItems);
        dashboardChart.getData().addAll(employesData, eventsData, projectsData);
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
