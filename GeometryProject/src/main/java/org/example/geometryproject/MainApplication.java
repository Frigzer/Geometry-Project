package org.example.geometryproject;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.scene.image.Image;
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
    private double targetX, targetY;
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

        targetX = player.getX();
        targetY = player.getY();

        scene.setOnMouseMoved(this::handleMouseMovement);

        scene.setOnKeyPressed(event -> {
            keysPressed.add(event.getCode().toString());
        });

        scene.setOnKeyReleased(event -> {
            keysPressed.remove(event.getCode().toString());
        });

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                movePlayer();
            }
        };
        timer.start();

        // Ustawienie niestandardowego kursora
        Image crosshairImage = new Image("crosshair.png");
        scene.setCursor(new ImageCursor(crosshairImage, crosshairImage.getWidth() / 2, crosshairImage.getHeight() / 2));


        primaryStage.setScene(scene);
        primaryStage.setTitle("Simple JavaFX Game");
        primaryStage.show();
    }

    private void handleMouseMovement(MouseEvent event) {
        targetX = event.getX();
        targetY = event.getY();
    }

    private void moveTowardsTarget() {
        double deltaX = targetX - (player.getX() + player.getWidth() / 2);
        double deltaY = targetY - (player.getY() + player.getHeight() / 2);
        double distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY);

        if (distance > speed) {
            double directionX = deltaX / distance;
            double directionY = deltaY / distance;

            player.setX(player.getX() + directionX * speed);
            player.setY(player.getY() + directionY * speed);
        }

        double angle = Math.toDegrees(Math.atan2(deltaY, deltaX));
        player.setRotate(angle);
    }

    private void movePlayer() {
        double deltaX = targetX - (player.getX() + player.getWidth() / 2);
        double deltaY = targetY - (player.getY() + player.getHeight() / 2);
        double distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
        double directionX = deltaX / distance;
        double directionY = deltaY / distance;

        if (keysPressed.contains("W")) {
            player.setX(player.getX() + directionX * speed);
            player.setY(player.getY() + directionY * speed);
        }
        if (keysPressed.contains("S")) {
            player.setX(player.getX() - directionX * speed);
            player.setY(player.getY() - directionY * speed);
        }
        if (keysPressed.contains("D")) {
            player.setX(player.getX() - directionY * speed);
            player.setY(player.getY() + directionX * speed);
        }
        if (keysPressed.contains("A")) {
            player.setX(player.getX() + directionY * speed);
            player.setY(player.getY() - directionX * speed);
        }

        double angle = Math.toDegrees(Math.atan2(deltaY, deltaX));
        player.setRotate(angle);
    }
}
