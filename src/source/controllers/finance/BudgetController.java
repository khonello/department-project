package source.controllers.finance;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*; 
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.converter.DefaultStringConverter;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;

public class BudgetController implements Initializable{
    
    public class BudgetItem {

        SimpleStringProperty nameProperty = new SimpleStringProperty();
        SimpleStringProperty percentageProperty = new SimpleStringProperty();
        SimpleStringProperty monthProperty = new SimpleStringProperty();
        SimpleBooleanProperty deleteProperty = new SimpleBooleanProperty();

        public BudgetItem(String name, String percentage, String month, boolean delete) {

            this.nameProperty.set(name);
            this.percentageProperty.set(percentage);
            this.monthProperty.set(month);
            this.deleteProperty.set(delete);

        }

        public SimpleStringProperty getPercentageProperty() {
            return new SimpleStringProperty(this.percentageProperty.getValue().toString() + "%");
        }
    }
    
    ObservableList<BudgetItem> budgetItems = FXCollections.observableArrayList(new BudgetItem(null, null, null, false));
    
    XYChart.Series<String, Number> lineSeries1 = new XYChart.Series<>();
    XYChart.Series<String, Number> lineSeries2 = new XYChart.Series<>();
            
    Double subTotalPercent = 0.0;

    @FXML
    TableView<BudgetItem> budgetTableView;

    @FXML
    TableColumn<BudgetItem, String> budgetItemNameColumn;
    @FXML
    TableColumn<BudgetItem, String> budgetItemPercentageColumn;
    @FXML
    TableColumn<BudgetItem, String> budgetItemMonthColumn;
    @FXML
    TableColumn<BudgetItem, Boolean> budgetItemDeleteColumn;

    @FXML
    LineChart<String, Number> lineChart;

    @FXML
    Label percentLeftLabel;
    @FXML
    Label budgetAmountLeftLabel;
    @FXML
    Label budgetAmountLabel;

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
    private void handleCreateBudgetClick(Event event) {

        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        
        // Parent newParent = loadFXML("logout.fxml");
        // Scene newScene = new Scene(newParent);

        // stage.setScene(newScene);

        showPopup(stage, "Provide a number greater than zero");

    }

    @FXML
    private void handleSaveClick(Event event) {

        // Node source = (Node) event.getSource();
        // Stage stage = (Stage) source.getScene().getWindow();
        
        // Parent newParent = loadFXML("logout.fxml");
        // Scene newScene = new Scene(newParent);

        // stage.setScene(newScene);

        Alert loginAlert = new Alert(AlertType.INFORMATION);
        loginAlert.setContentText("Changes Saved");
        loginAlert.show();

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
    
    @SuppressWarnings("unchecked")
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        budgetItemNameColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DefaultStringConverter()));
        budgetItemPercentageColumn.setCellFactory(column -> new TextFieldTableCell<BudgetItem, String>(new DefaultStringConverter()) {
            @Override
            public void startEdit() {
                if (getTableRow() != null && getTableRow().getItem() != null && getTableRow().getItem().nameProperty.get() != null && !getTableRow().getItem().nameProperty.get().isEmpty()) {

                    super.startEdit();
                    setEditable(true);
                }
            }

            @Override
            public void updateItem(String item, boolean empty) {

                super.updateItem(item, empty);
                if (empty || getTableRow().getItem() == null || getTableRow().getItem().nameProperty.get() == null || getTableRow().getItem().nameProperty.get().isEmpty()) {

                    setEditable(false);
                } else {

                    setEditable(true);
                }
            }
        });
        budgetItemDeleteColumn.setCellFactory(CheckBoxTableCell.forTableColumn(budgetItemDeleteColumn));

        budgetItemNameColumn.setCellValueFactory(cell -> cell.getValue().nameProperty);
        budgetItemPercentageColumn.setCellValueFactory(cell -> {

            cell.getValue().percentageProperty.addListener((observable, oldValue, newValue) -> {

                if (updatePercentageLeft() != 0) {

                    Alert loginAlert = new Alert(AlertType.WARNING);
                    loginAlert.setContentText("The percent set is higher than available");
                    loginAlert.show();

                    cell.getValue().percentageProperty.set(oldValue);
                }
                
                if (budgetAmountLabel.getText().equals("0")) {

                    showPopup(null, "Create a budget first");
                }

                try {

                    new DoubleStringConverter().fromString(newValue);
                } catch (NullPointerException e) {

                    cell.getValue().percentageProperty.set(oldValue);
                }

            });
            return cell.getValue().percentageProperty;
        });
        budgetItemMonthColumn.setCellValueFactory(cell -> new SimpleStringProperty(LocalDate.now().getMonth().toString()));
        budgetItemDeleteColumn.setCellValueFactory(cell -> cell.getValue().deleteProperty);

        budgetItemNameColumn.setOnEditCommit(event -> {
            
            BudgetItem item = event.getRowValue();
            String newValue = event.getNewValue();
            
            item.nameProperty.set(event.getNewValue());
            if (!newValue.isEmpty()) {
                if (budgetTableView.getItems().indexOf(event.getRowValue()) == budgetTableView.getItems().size() - 1) {
                    budgetTableView.getItems().add(new BudgetItem(null, null, null, false));
                }
            }
        });
 
        budgetTableView.getItems().addAll(budgetItems);
        budgetTableView.getItems().addListener((ListChangeListener<BudgetItem>) c -> {
            
            Integer lastItemIndex = c.getList().size() - 1;
            DoubleStringConverter converter = new DoubleStringConverter();

            while (c.next()) {

                c.getList().forEach((item) -> {

                    item.percentageProperty.addListener((observable, oldValue, newValue) -> {
                        if (c.getList().indexOf(item) != lastItemIndex) {
                                        
                            c.getList().subList(0, lastItemIndex).forEach(subItem -> {
                                if (subItem.percentageProperty.get() != null) {
                                
                                    subTotalPercent += converter.fromString(subItem.percentageProperty.get());
                                }
                            });

                            if (item.percentageProperty.get() != null) {

                                lineSeries1.getData().add(new XYChart.Data<String, Number>(item.nameProperty.get(), converter.fromString(item.percentageProperty.get())));
                                lineSeries2.getData().add(new XYChart.Data<String, Number>("EndLine" + c.getList().get(lastItemIndex).nameProperty.get(), subTotalPercent));
                                subTotalPercent = 0.0;
                            }
                        }
                    });

                    item.deleteProperty.addListener((observable, oldValue, newValue) -> {
                        if (newValue) {
                                        
                            updatePercentageLeft();

                            lineSeries1.getData().remove(c.getList().indexOf(item));
                            lineSeries2.getData().remove(c.getList().indexOf(item));

                            budgetTableView.getItems().remove(item);
                        }
                    });
                });
            }
        });
        lineSeries1.setName("Percentage values");
        lineSeries2.setName("Percentage increment");
        lineChart.getData().addAll(lineSeries1, lineSeries2);
    }
    
    private void showPopup(Stage stage, String contentText) {

        TextInputDialog dialog = new TextInputDialog();
        TextField textField = dialog.getEditor();
        
        textField.setTextFormatter(new TextFormatter<>(new IntegerStringConverter(), 0));
        textField.textProperty().addListener((observable, oldValue, newValue) -> {

            if (!newValue.isEmpty()) {

                try {

                    Integer amount = new IntegerStringConverter().fromString(newValue);
                    if (amount > 0) {

                        dialog.getDialogPane().lookupButton(ButtonType.OK).setDisable(false);
                    }
                } catch (Exception e) {

                    textField.setText(newValue.substring(0, newValue.length() - 1));
                }
            }
        });

        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setHeaderText("Budget Amount");
        dialog.setContentText(contentText);
        dialog.getDialogPane().getButtonTypes().remove(ButtonType.CANCEL);
        dialog.getDialogPane().lookupButton(ButtonType.OK).setDisable(true);
        dialog.getDialogPane().getScene().getWindow().setOnCloseRequest((event) -> {
            event.consume();
        });

        dialog.showAndWait().ifPresent(input -> {

            budgetAmountLabel.setText(dialog.getResult() + ".00");
            updateAmountLeft();
        });

    }

    private int updatePercentageLeft() {

        Double totalPercentage = 0.0;
        DoubleStringConverter converter = new DoubleStringConverter();

        for (BudgetItem item : budgetTableView.getItems()) {
            if (item.percentageProperty.get() != null && !item.percentageProperty.get().isEmpty()) {
                try {

                    Double convertedInteger = converter.fromString(item.percentageProperty.get());
                    totalPercentage += convertedInteger;
                } catch (Exception e) {

                    item.percentageProperty.set("");
                }
            }
        }
    
        Double remainingPercent = 100 - totalPercentage;
        if (remainingPercent < 0) {

            return -1;
        } else {

            percentLeftLabel.setText(remainingPercent.toString() + "%");
            updateAmountLeft();

            return 0;
        }
    }

    private void updateAmountLeft() {

            DoubleStringConverter converter = new DoubleStringConverter();

            Double budgetAmount = converter.fromString(budgetAmountLabel.getText());
            Double percentLeft = 100 - converter.fromString(percentLeftLabel.getText().substring(0, percentLeftLabel.getText().length() - 1));
            Double updatedAmount = budgetAmount - (budgetAmount * percentLeft / 100);

            budgetAmountLeftLabel.setText(updatedAmount.toString());
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