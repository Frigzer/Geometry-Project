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
import javafx.scene.input.MouseButton;

import java.io.IOException;
import java.util.ArrayList;
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
    private List<Bullet> bullets = new ArrayList<>();
    private List<Asteroid> asteroids = new ArrayList<>();
    private Pane root;
    private double shipWidth, shipHeight;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        root = new Pane();
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
        player.setLayoutX(400 - player.getFitWidth() / 2); // Centrowanie statku na środku
        player.setLayoutY(550 - player.getFitHeight() / 2); // Ustawienie statku na dole okna

        shipWidth = player.getFitWidth();
        shipHeight = player.getFitHeight();

        //player.setLayoutX(400);
        //player.setLayoutY(550);

        root.getChildren().add(player);

        targetX = player.getLayoutX();
        targetY = player.getLayoutY();

        bowPoint = new Point2D(player.getX() + player.getFitWidth() / 2, player.getY() + player.getFitHeight() / 2);  // Zakładamy, że pierwszy punkt to dziób


        scene.setOnMouseMoved(this::handleMouseMovement);
        scene.setOnMousePressed(this::handleMousePressed);

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
                updateBullets();
                updateAsteroids();
                checkCollisions();
                spawnAsteroids();
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

    private void handleMousePressed(MouseEvent event) {
        if (event.getButton() == MouseButton.PRIMARY) {
            fireBullets();
        }
    }

    private void fireBullets() {
        double angle = Math.toRadians(player.getRotate());
        Point2D direction = new Point2D(Math.cos(angle), Math.sin(angle));

        double bulletSpeed = 10.0;
        Point2D velocity = direction.multiply(bulletSpeed);

        double shipCenterX = player.getLayoutX() + shipWidth / 2;
        double shipCenterY = player.getLayoutY() + shipHeight / 2;

        // Wyznaczenie odległości od środka do skrzydeł
        double wingOffset = shipWidth / 2 - 5;

        // Pozycje wystrzałów z lewego i prawego skrzydła
        Point2D leftWing = new Point2D(
                shipCenterX + wingOffset * Math.cos(angle + Math.PI / 2),
                shipCenterY + wingOffset * Math.sin(angle + Math.PI / 2)
        );

        Point2D rightWing = new Point2D(
                shipCenterX + wingOffset * Math.cos(angle - Math.PI / 2),
                shipCenterY + wingOffset * Math.sin(angle - Math.PI / 2)
        );

        Bullet leftBullet = new Bullet(leftWing.getX(), leftWing.getY(), velocity);
        Bullet rightBullet = new Bullet(rightWing.getX(), rightWing.getY(), velocity);

        bullets.add(leftBullet);
        bullets.add(rightBullet);
        root.getChildren().add(leftBullet);
        root.getChildren().add(rightBullet);
    }

    private void updateBullets() {
        for (int i = 0; i < bullets.size(); i++) {
            Bullet bullet = bullets.get(i);
            bullet.update();
            if (bullet.isOffScreen(800, 600)) {
                root.getChildren().remove(bullet);
                bullets.remove(i);
                i--;
            }
        }
    }

    private void spawnAsteroids() {
        if (Math.random() < 0.02) { // Szansa na spawn asteroidy przy każdym wywołaniu
            double x = Math.random() * 800;
            double y = -20;
            double angle = Math.random() * Math.PI / 4 - Math.PI / 8; // Losowy kąt od -22.5 do 22.5 stopni
            Point2D velocity = new Point2D(Math.sin(angle), Math.cos(angle)).multiply(2);

            Asteroid asteroid = new Asteroid(x, y, velocity);
            asteroids.add(asteroid);
            root.getChildren().add(asteroid);
        }
    }

    private void updateAsteroids() {
        for (int i = 0; i < asteroids.size(); i++) {
            Asteroid asteroid = asteroids.get(i);
            asteroid.update();
            if (asteroid.isOffScreen(800, 600)) {
                root.getChildren().remove(asteroid);
                asteroids.remove(i);
                i--;
            }
        }
    }

    private void checkCollisions() {
        // Sprawdzanie kolizji statku z asteroidami
        for (Asteroid asteroid : asteroids) {
            if (player.getBoundsInParent().intersects(asteroid.getBoundsInParent())) {
                // Koniec gry
                stopGame();
                return;
            }
        }

        // Sprawdzanie kolizji pocisków z asteroidami
        for (int i = 0; i < bullets.size(); i++) {
            Bullet bullet = bullets.get(i);
            for (int j = 0; j < asteroids.size(); j++) {
                Asteroid asteroid = asteroids.get(j);
                if (bullet.getBoundsInParent().intersects(asteroid.getBoundsInParent())) {
                    root.getChildren().remove(bullet);
                    bullets.remove(i);
                    i--;
                    root.getChildren().remove(asteroid);
                    asteroids.remove(j);
                    j--;
                    break;
                }
            }
        }
    }

    private void stopGame() {
        // Zatrzymanie gry
        System.out.println("Game Over");
        System.exit(0);
    }


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
