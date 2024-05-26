package org.example.geometryproject;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainApplication extends Application {
    String shipFile = "terran_wraith.txt";

    //private Polygon player;
    private ImageView player;
    private double speed = 5.0;
    private double targetX, targetY;
    private Set<String> keysPressed = new HashSet<>();
    private boolean controlModeRelativeToCursor = false;
    private Point2D bowPoint;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Pane root = new Pane();
        Scene scene = new Scene(root, 800, 600);

        // Przykład użycia klasy ConvexHull do utworzenia kształtu statku
        ConvexHull convexHull = new ConvexHull(shipFile);
        List<Point2D> points = convexHull.getHull(); // Tworzymy kwadrat o wymiarach 50x50

        double[] hullDimensions = convexHull.getHullDimensions();
        double hullWidth = hullDimensions[0];
        double hullHeight = hullDimensions[1];

        // Załadowanie obrazu statku
        Image shipImage = new Image("terran_wraith_1.png");
        player = new ImageView(shipImage);
        player.setFitWidth(hullWidth); // Ustawienie szerokości statku na podstawie szerokości otoczki wypukłej
        player.setFitHeight(hullHeight); // Ustawienie wysokości statku na podstawie wysokości otoczki wypukłej
        player.setPreserveRatio(true); // Zachowanie proporcji obrazu

        // Ustawienie początkowej pozycji statku
        player.setX(400 - player.getFitWidth() / 2); // Centrowanie statku na środku
        player.setY(550 - player.getFitHeight() / 2); // Ustawienie statku na dole okna

        //player.setLayoutX(400);
        //player.setLayoutY(550);

        root.getChildren().add(player);

        targetX = player.getLayoutX();
        targetY = player.getLayoutY();

        bowPoint = new Point2D(player.getX() + player.getFitWidth() / 2, player.getY());  // Zakładamy, że pierwszy punkt to dziób


        scene.setOnMouseMoved(this::handleMouseMovement);

        scene.setOnKeyPressed(event -> {
            keysPressed.add(event.getCode().toString());
            if (event.getCode().toString().equals("SPACE")) {
                controlModeRelativeToCursor = !controlModeRelativeToCursor; // Przełączanie trybu sterowania
            }
        });

        scene.setOnKeyReleased(event -> {
            keysPressed.remove(event.getCode().toString());
        });

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (controlModeRelativeToCursor) {
                    movePlayerRelativeToCursor();
                } else {
                    movePlayerClassic();
                }
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

    /*
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
     */

    private void movePlayerRelativeToCursor() {

        //double deltaX = targetX - (player.getLayoutX() + player.getBoundsInParent().getWidth() / 2);
        //double deltaY = targetY - (player.getLayoutY() + player.getBoundsInParent().getHeight() / 2);
        Point2D currentBowPoint = player.localToParent(bowPoint);

        double deltaX = targetX - currentBowPoint.getX();
        double deltaY = targetY - currentBowPoint.getY();
        double distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
        double directionX = deltaX / distance;
        double directionY = deltaY / distance;

        if (keysPressed.contains("W")) {
            player.setLayoutX(player.getLayoutX() + directionX * speed);
            player.setLayoutY(player.getLayoutY() + directionY * speed);
        }
        if (keysPressed.contains("S")) {
            player.setLayoutX(player.getLayoutX() - directionX * speed);
            player.setLayoutY(player.getLayoutY() - directionY * speed);
        }
        if (keysPressed.contains("D")) {
            player.setLayoutX(player.getLayoutX() - directionY * speed);
            player.setLayoutY(player.getLayoutY() + directionX * speed);
        }
        if (keysPressed.contains("A")) {
            player.setLayoutX(player.getLayoutX() + directionY * speed);
            player.setLayoutY(player.getLayoutY() - directionX * speed);
        }

        double angle = Math.toDegrees(Math.atan2(deltaY, deltaX));
        player.setRotate(angle);
    }

    private void movePlayerClassic() {
        if (keysPressed.contains("W")) {
            player.setLayoutY(player.getLayoutY() - speed);
        }
        if (keysPressed.contains("S")) {
            player.setLayoutY(player.getLayoutY() + speed);
        }
        if (keysPressed.contains("A")) {
            player.setLayoutX(player.getLayoutX() - speed);
        }
        if (keysPressed.contains("D")) {
            player.setLayoutX(player.getLayoutX() + speed);
        }

       // double deltaX = targetX - (player.getLayoutX() + player.getBoundsInParent().getWidth() / 2);
        //double deltaY = targetY - (player.getLayoutY() + player.getBoundsInParent().getHeight() / 2);

        Point2D currentBowPoint = player.localToParent(bowPoint);

        double deltaX = targetX - currentBowPoint.getX();
        double deltaY = targetY - currentBowPoint.getY();

        double angle = Math.toDegrees(Math.atan2(deltaY, deltaX));
        player.setRotate(angle);
    }

    private double calculateAngle(Point2D from, Point2D to) {
        double deltaX = to.getX() - from.getX();
        double deltaY = to.getY() - from.getY();
        return Math.toDegrees(Math.atan2(deltaY, deltaX));
    }

    private Point2D getBowPoint() {
        // Zakładamy, że dziób statku to najbardziej wysunięty punkt otoczki wypukłej
        // W tym przykładzie używamy prawego punktu jako dziobu
        ConvexHull convexHull = new ConvexHull(shipFile);
        return convexHull.getTopmostPoint();
    }
}
