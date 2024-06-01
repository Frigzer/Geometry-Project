package org.example.geometryproject.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.example.geometryproject.GameApplication;
import org.example.geometryproject.Settings;

import java.io.IOException;

public class MainMenuController {

    private Stage primaryStage;
    private Settings settings;

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void setSettings(Settings settings) {
        this.settings = settings;
    }

    @FXML
    private void startGame() {
        try {
            GameApplication gameApp = new GameApplication();
            gameApp.setSettings(settings); // Przekazanie ustawień do gry
            gameApp.start(primaryStage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void openSettings() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/SettingsMenu.fxml"));
            Pane root = loader.load();

            SettingsMenuController controller = loader.getController();
            controller.setPrimaryStage(primaryStage);
            controller.setSettings(settings);

            Scene scene = new Scene(root, 800, 600);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Settings");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void exitGame() {
        System.exit(0);
    }
}
