package org.example.geometryproject.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import org.example.geometryproject.main.Game;
import org.example.geometryproject.main.Settings;

import java.io.IOException;

public class MainMenuController {

    @FXML
    private MediaView backgroundVideo;
    @FXML
    private Pane pane;

    private Stage primaryStage;
    private Settings settings;
    private int screenWidth;
    private int screenHeight;

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void setSettings(Settings settings) {
        this.settings = settings;
    }

    public void setScreenDimensions(int screenWidth, int screenHeight) {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;

        pane.setPrefSize(screenWidth, screenHeight);

        String videoPath = getClass().getResource("/textures/main_menu.mp4").toExternalForm();

        // Tworzenie obiektu Media i MediaPlayer
        Media media = new Media(videoPath);

        MediaPlayer mediaPlayer = new MediaPlayer(media);

        // Ustawienie MediaPlayer w MediaView
        backgroundVideo.setMediaPlayer(mediaPlayer);
        backgroundVideo.setPreserveRatio(false);
        backgroundVideo.setFitWidth(screenWidth);
        backgroundVideo.setFitHeight(screenHeight);

        mediaPlayer.setMute(true);
        mediaPlayer.play();
    }

    @FXML
    private void startGame() {
        try {
            Game gameApp = new Game(screenWidth, screenHeight);
            gameApp.setSettings(settings); // Przekazanie ustawień do gry
            gameApp.start(primaryStage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void openSettings() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/SettingsMenu.fxml"));
            Pane root = loader.load();

            SettingsMenuController controller = loader.getController();
            controller.setPrimaryStage(primaryStage);
            controller.setSettings(settings);
            controller.setScreenDimensions(screenWidth, screenHeight);

            Scene scene = new Scene(root, screenWidth, screenHeight);
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
