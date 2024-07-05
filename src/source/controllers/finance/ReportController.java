package source.controllers.finance;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*; 
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import javafx.util.converter.DefaultStringConverter;
import javafx.util.converter.IntegerStringConverter;

public class ReportController implements Initializable{
    
    public class Reports {

        SimpleStringProperty nameProperty = new SimpleStringProperty();
        SimpleStringProperty dateProperty = new SimpleStringProperty();
        SimpleBooleanProperty deleteProperty = new SimpleBooleanProperty();

        public Reports(String name, String date, boolean delete) {

            this.nameProperty.set(name);
            this.dateProperty.set(date);
            this.deleteProperty.set(delete);

        }
    }
    public class Resources {
        
        SimpleStringProperty nameProperty = new SimpleStringProperty();
        SimpleStringProperty countProperty = new SimpleStringProperty(null);
        SimpleBooleanProperty incrementButtonProperty = new SimpleBooleanProperty(false);
        SimpleBooleanProperty decrementButtonProperty = new SimpleBooleanProperty(false);
        SimpleBooleanProperty deleteProperty = new SimpleBooleanProperty(false);

        public Resources(String name, String count, Boolean incrementButton, Boolean decrementButton, Boolean delete) {

            this.nameProperty.set(name);
            this.countProperty.set(count);
            this.incrementButtonProperty.set(incrementButton);
            this.decrementButtonProperty.set(decrementButton);
            this.deleteProperty.set(delete);
        }
    }
    
    Integer generatedTimes = 1;
    ObservableList<Reports> reportItems = FXCollections.observableArrayList();        // load from database
    
    TreeItem<Resources> rootItem = new TreeItem<>(new Resources("Resources", null, false, false, false));

    @FXML
    Button printButton;

    @FXML
    TableView<Reports> reportTableView;

    @FXML
    TableColumn<Reports, String> reportNameColumn;
    @FXML
    TableColumn<Reports, String> reportDateColumn;
    @FXML
    TableColumn<Reports, Boolean> reportDeleteColumn;

    @FXML
    TreeTableView<Resources> resourcesTreeTableView;

    @FXML
    TreeTableColumn<Resources, String> resourceNameColumn;
    @FXML
    TreeTableColumn<Resources, Resources> resourceCountColumn;
    @FXML
    TreeTableColumn<Resources, Resources> resourceIncrementColumn;
    @FXML
    TreeTableColumn<Resources, Resources> resourceDecrementColumn;
    @FXML
    TreeTableColumn<Resources, Resources> resourceDeleteColumn;


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
    private void handleTechSupportClick(Event event) {

        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        
        Parent newParent = loadFXML("/source/fxml/finance/", "techsupport.fxml");
        Scene newScene = new Scene(newParent);

        stage.setTitle("Finance - Tech Support");
        stage.setScene(newScene);
    }

    @FXML
    private void handleGenerateClick(Event event) {

        String localDateString = LocalDate.now().toString();
        reportTableView.getItems().add(new Reports("Report #" + generatedTimes.toString(), localDateString, false));
        generatedTimes++;
    }

    @FXML
    private void handlePrintClick(Event event) {

        // Node source = (Node) event.getSource();
        // Stage stage = (Stage) source.getScene().getWindow();
        
        // Parent newParent = loadFXML("logout.fxml");
        // Scene newScene = new Scene(newParent);

        // stage.setScene(newScene);

        Alert loginAlert = new Alert(AlertType.INFORMATION);
        loginAlert.setContentText("Printed");
        loginAlert.show();

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        reportNameColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DefaultStringConverter()));
        reportDeleteColumn.setCellFactory(CheckBoxTableCell.forTableColumn(reportDeleteColumn));

        reportNameColumn.setCellValueFactory(cell -> cell.getValue().nameProperty);
        reportDateColumn.setCellValueFactory(cell -> cell.getValue().dateProperty);
        reportDeleteColumn.setCellValueFactory(cell -> cell.getValue().deleteProperty);

        reportNameColumn.setOnEditCommit(event -> {
            printButton.setDisable(false);
        });
 
        reportTableView.getItems().addAll(reportItems);
        reportTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            printButton.setDisable(false);
        });
        reportTableView.getItems().addListener((ListChangeListener<Reports>) c -> {

            printButton.setDisable(true);
            while (c.next()) {

                c.getList().forEach((item) -> {
                    
                    item.deleteProperty.addListener((observable, oldValue, newValue) -> {
                            if (newValue) {
                                reportTableView.getItems().remove(item);
                            }
                    });
                });
            }
        });

        resourceCountColumn.setCellFactory(column -> new TreeTableCell<Resources, Resources>() {

            @Override
            public void updateItem(Resources rsourrce, boolean empty) {
                super.updateItem(rsourrce, empty);
                if (empty || rsourrce == null) {

                    setGraphic(null);
                    setText(null);
                } else  {

                    switch (rsourrce.nameProperty.get()) {

                        case "Resources":
                        case "Asset":
                        case "Liability":
                        case "Equity":
                            
                            setGraphic(null);
                            setText(null);
                            break;
                        default:
                            
                            setGraphic(null);
                            setText(rsourrce.countProperty.get());
                            break;
                    }
                }
            }
        });
        resourceIncrementColumn.setCellFactory(column -> new TreeTableCell<Resources, Resources>() {

            @Override
            public void updateItem(Resources rsourrce, boolean empty) {
                super.updateItem(rsourrce, empty);
                if (empty || rsourrce == null) {

                    setGraphic(null);
                    setText(null);
                } else  {

                    IntegerStringConverter converter = new IntegerStringConverter();
                    Button button = new Button("+");

                    button.setStyle("-fx-background-color: lightblue;");
                    button.setCursor(Cursor.HAND);
                    button.setOnMouseClicked(event -> {

                        Integer updated = converter.fromString(rsourrce.countProperty.get()) + 1;
                        String updatedString = updated.toString();

                        rsourrce.countProperty.set(updatedString);
                        resourcesTreeTableView.refresh();
                    });
                    switch (rsourrce.nameProperty.get()) {

                        case "Resources":
                        case "Asset":
                        case "Liability":
                        case "Equity":
                            
                            setGraphic(null);
                            setText(null);
                            break;
                        default:
                            
                            setGraphic(button);
                            setText(null);
                            
                            break;
                    }
                }
            }
        });
        resourceDecrementColumn.setCellFactory(column -> new TreeTableCell<Resources, Resources>() {

            @Override
            public void updateItem(Resources rsourrce, boolean empty) {
                super.updateItem(rsourrce, empty);
                if (empty || rsourrce == null) {

                    setGraphic(null);
                    setText(null);
                } else  {

                    IntegerStringConverter converter = new IntegerStringConverter();
                    Button button = new Button("-");

                    button.setStyle("-fx-background-color: lightblue;");
                    button.setCursor(Cursor.HAND);
                    button.setOnMouseClicked(event -> {

                        Integer updated = converter.fromString(rsourrce.countProperty.get()) - 1;
                        if (updated >= 0) {
                            
                            String updatedString = updated.toString();
                            rsourrce.countProperty.set(updatedString);
                        }
                        resourcesTreeTableView.refresh();
                    });
                    switch (rsourrce.nameProperty.get()) {

                        case "Resources":
                        case "Asset":
                        case "Liability":
                        case "Equity":
                            
                            setGraphic(null);
                            setText(null);
                            break;
                        default:
                            
                            setGraphic(button);
                            setText(null);
                            
                            break;
                    }
                }
            }
        });
        resourceDeleteColumn.setCellFactory(column -> new TreeTableCell<Resources, Resources>() {

            @Override
            public void updateItem(Resources rsourrce, boolean empty) {
                super.updateItem(rsourrce, empty);
                if (empty || rsourrce == null) {

                    setGraphic(null);
                    setText(null);
                } else  {

                    CheckBox checkBox = new CheckBox();
                    checkBox.selectedProperty().addListener((observable, oldValue, newValue) -> {

                        try {
                            rootItem.getChildren().forEach(childItem -> {
                                childItem.getChildren().forEach(subItem -> {
                                    if (subItem.getValue().nameProperty.get().equals(rsourrce.nameProperty.get())) {
                                        if (newValue) {
                                            childItem.getChildren().remove(subItem);
                                        }
                                    }
                                });
                            });
                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                        }
                    });
                    switch (rsourrce.nameProperty.get()) {

                        case "Resources":
                        case "Asset":
                        case "Liability":
                        case "Equity":
                            
                            setGraphic(null);
                            setText(null);
                            break;
                        default:
                            
                            setGraphic(checkBox);
                            setText(null);
                            
                            break;
                    }
                }
            }
        });
        
        resourceNameColumn.setCellValueFactory(cell -> cell.getValue().getValue().nameProperty);
        resourceCountColumn.setCellValueFactory(cell -> new SimpleObjectProperty<>(cell.getValue().getValue()));
        resourceIncrementColumn.setCellValueFactory(cell -> new SimpleObjectProperty<>(cell.getValue().getValue()));
        resourceDecrementColumn.setCellValueFactory(cell -> new SimpleObjectProperty<>(cell.getValue().getValue()));
        resourceDeleteColumn.setCellValueFactory(cell -> new SimpleObjectProperty<>(cell.getValue().getValue()));
        setupTree();

    }

    public void setupTree() {

        String[] parentItems = {"Asset", "Liability", "Equity"};
        String[] assetItems = {"Buildings", "Computers"};
        String[] liabilityItems = {"Contracts", "Loans Payable"};
        String[] equityItems = {"General Partnerships", "LLC Membership"};

        for (String item : parentItems) {
            
            TreeItem<Resources> parentTreeItem = new TreeItem<Resources>(new Resources(item, null, false, false, false));
            switch (item) {
                case "Asset":
                    for (String childItem : assetItems) {

                        parentTreeItem.getChildren().add(new TreeItem<Resources>(new Resources(childItem, "1", true, true, false)));
                    }
                    break;

                case "Liability":
                    for (String childItem : liabilityItems) {

                        parentTreeItem.getChildren().add(new TreeItem<Resources>(new Resources(childItem, "1", true, true, false)));
                    }
                    break;
                case "Equity":
                    for (String childItem : equityItems) {

                        parentTreeItem.getChildren().add(new TreeItem<Resources>(new Resources(childItem, "1", true, true, false)));
                    }
                    break;
                default:
                    break;
            }
            rootItem.getChildren().add(parentTreeItem);
            resourcesTreeTableView.setRoot(rootItem);
        }

        rootItem.getChildren().forEach(item -> {
        
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
