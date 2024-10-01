package org.example.geometryproject;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.example.geometryproject.controller.MainMenuController;
import org.example.geometryproject.main.Settings;

import java.io.IOException;

public class MainApplication extends Application {
    private Settings settings = new Settings();


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        showMainMenu(primaryStage);
    }

    private void showMainMenu(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MainMenu.fxml"));
            Pane root = loader.load();

            MainMenuController controller = loader.getController();
            controller.setPrimaryStage(primaryStage);
            controller.setSettings(settings);
            controller.setScreenDimensions(settings.getScreenWidth(), settings.getScreenHeight());

            Scene scene = new Scene(root, settings.getScreenWidth(), settings.getScreenHeight());
            //scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
            primaryStage.setScene(scene);
            primaryStage.setTitle("Main Menu");
            primaryStage.setResizable(false);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
