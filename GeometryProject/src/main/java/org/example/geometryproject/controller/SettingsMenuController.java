package org.example.geometryproject.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.example.geometryproject.main.Settings;

import java.io.IOException;

public class SettingsMenuController {

    @FXML
    private ComboBox<String> controlModeComboBox;
    @FXML
    private TextField moveUpKey;
    @FXML
    private TextField moveDownKey;
    @FXML
    private TextField moveLeftKey;
    @FXML
    private TextField moveRightKey;

    @FXML
    private ComboBox<String> windowSizeComboBox;

    private Stage primaryStage;
    private Settings settings;

    private int screenWidth;
    private int screenHeight;

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void setSettings(Settings settings) {
        this.settings = settings;
        // Initialize the controls with the current settings
        loadSettings();
    }

    public void setScreenDimensions(int screenWidth, int screenHeight) {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
    }

    @FXML
    private void saveSettings() {
        settings.setControlMode(controlModeComboBox.getValue());
        settings.setMoveUpKey(moveUpKey.getText());
        settings.setMoveDownKey(moveDownKey.getText());
        settings.setMoveLeftKey(moveLeftKey.getText());
        settings.setMoveRightKey(moveRightKey.getText());

        String windowSize = windowSizeComboBox.getValue();
        if (windowSize != null) {
            String[] dimensions = windowSize.split("x");
            settings.setScreenWidth(Integer.parseInt(dimensions[0]));
            settings.setScreenHeight(Integer.parseInt(dimensions[1]));
        }

        backToMainMenu();
    }

    @FXML
    private void backToMainMenu() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/MainMenu.fxml"));
            Pane root = loader.load();

            MainMenuController controller = loader.getController();
            controller.setPrimaryStage(primaryStage);
            controller.setSettings(settings);
            controller.setScreenDimensions(settings.getScreenWidth(), settings.getScreenHeight());

            Scene scene = new Scene(root, settings.getScreenWidth(), settings.getScreenHeight());
            primaryStage.setScene(scene);
            primaryStage.setTitle("Main Menu");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadSettings() {
        controlModeComboBox.setValue(settings.getControlMode());
        moveUpKey.setText(settings.getMoveUpKey());
        moveDownKey.setText(settings.getMoveDownKey());
        moveLeftKey.setText(settings.getMoveLeftKey());
        moveRightKey.setText(settings.getMoveRightKey());

        String currentSize = settings.getScreenWidth() + "x" + settings.getScreenHeight();
        windowSizeComboBox.setValue(currentSize);
    }
}
