package org.example.geometryproject;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class MainApplication extends Application {
    private Rectangle player;
    private double speed = 5.0;
    private Set<String> keysPressed = new HashSet<>();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Pane root = new Pane();
        Scene scene = new Scene(root, 800, 600);

        player = new Rectangle(50, 50, 50, 50); // Tworzymy kwadrat o wymiarach 50x50
        player.setFill(Color.BLUE);
        root.getChildren().add(player);

        scene.setOnKeyPressed(event -> {
            keysPressed.add(event.getCode().toString());
            handleMovement();
        });

        scene.setOnKeyReleased(event -> {
            keysPressed.remove(event.getCode().toString());
        });

        scene.setOnMouseMoved(this::handleRotation);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Simple JavaFX Game");
        primaryStage.show();
    }

    private void handleMovement() {
        if (keysPressed.contains("W")) {
            player.setY(player.getY() - speed);
        }
        if (keysPressed.contains("S")) {
            player.setY(player.getY() + speed);
        }
        if (keysPressed.contains("A")) {
            player.setX(player.getX() - speed);
        }
        if (keysPressed.contains("D")) {
            player.setX(player.getX() + speed);
        }
    }

    private void handleRotation(MouseEvent event) {
        double mouseX = event.getX();
        double mouseY = event.getY();
        double deltaX = mouseX - (player.getX() + player.getWidth() / 2);
        double deltaY = mouseY - (player.getY() + player.getHeight() / 2);
        double angle = Math.toDegrees(Math.atan2(deltaY, deltaX));
        player.setRotate(angle);
    }
}
