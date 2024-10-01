package org.example.geometryproject.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.example.geometryproject.main.Game;
import org.example.geometryproject.main.Settings;

import java.io.IOException;

public class PauseMenuController {

    private Stage primaryStage;
    private Game game;
    private Settings settings;
    private int screenWidth;
    private int screenHeight;

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void setSettings(Settings settings) {
        this.settings = settings;
    }

    public void setGameApplication(Game game) {
        this.game = game;
    }

    public void setScreenDimensions(int screenWidth, int screenHeight) {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
    }

    @FXML
    private void resumeGame() {
        game.resumeGame();
    }

    @FXML
    private void backToMainMenu() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MainMenu.fxml"));
            Pane root = loader.load();

            MainMenuController controller = loader.getController();
            controller.setPrimaryStage(primaryStage);
            controller.setSettings(game.getSettings());
            controller.setScreenDimensions(screenWidth, screenHeight);

            Scene scene = new Scene(root, screenWidth, screenHeight);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Main Menu");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
