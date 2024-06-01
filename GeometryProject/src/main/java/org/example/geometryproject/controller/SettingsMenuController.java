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

    private Stage primaryStage;
    private Settings settings;

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void setSettings(Settings settings) {
        this.settings = settings;
        // Initialize the controls with the current settings
        controlModeComboBox.setValue(settings.getControlMode());
        moveUpKey.setText(settings.getMoveUpKey());
        moveDownKey.setText(settings.getMoveDownKey());
        moveLeftKey.setText(settings.getMoveLeftKey());
        moveRightKey.setText(settings.getMoveRightKey());
    }

    @FXML
    private void saveSettings() {
        settings.setControlMode(controlModeComboBox.getValue());
        settings.setMoveUpKey(moveUpKey.getText());
        settings.setMoveDownKey(moveDownKey.getText());
        settings.setMoveLeftKey(moveLeftKey.getText());
        settings.setMoveRightKey(moveRightKey.getText());
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

            Scene scene = new Scene(root, 800, 600);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Main Menu");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
