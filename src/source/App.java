package source;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import source.controllers.LoginController;

public class App extends Application{

    @Override
    public void start(Stage primaryStage) throws Exception {
        
        FXMLLoader loader = new FXMLLoader(getClass().getResource("./fxml/login.fxml"));
        Parent parent = loader.load();

        LoginController loginController = loader.getController();
        loginController.setPrimaryStage(primaryStage);

        Scene scene = new Scene(parent, 900, 400);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Login");
        primaryStage.setResizable(false);
        primaryStage.show();
    }
    public static void main(String[] args) throws Exception {
        launch(args);
    }
}
