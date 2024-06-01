package org.example.geometryproject.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.example.geometryproject.main.Game;

import java.io.IOException;

public class PauseMenuController {

    private Stage primaryStage;
    private Game game;

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void setGameApplication(Game game) {
        this.game = game;
    }

    @FXML
    private void resumeGame() {
        game.resumeGame();
    }

    @FXML
    private void backToMainMenu() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/MainMenu.fxml"));
            Pane root = loader.load();

            MainMenuController controller = loader.getController();
            controller.setPrimaryStage(primaryStage);
            controller.setSettings(game.getSettings());

            Scene scene = new Scene(root, 800, 600);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Main Menu");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
