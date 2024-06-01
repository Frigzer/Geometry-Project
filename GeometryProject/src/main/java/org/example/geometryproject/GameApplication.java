package org.example.geometryproject;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;
import javafx.scene.input.MouseButton;
import javafx.application.Platform;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.util.Duration;
import javafx.animation.PauseTransition;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GameApplication extends Application {
    String shipFile = "terran_wraith.txt";

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
    private ConvexHull convexHull;
    private Polygon hullPolygon;
    private double previousAngle = 0;
    private AnimationTimer timer;
    private int score = 0;
    private Text scoreText;
    private Settings settings;
    private boolean isPaused = false;
    private Stage primaryStage;

    public void setSettings(Settings settings) {
        this.settings = settings;
    }

    public Settings getSettings() {
        return settings;
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        root = new Pane();
        Scene scene = new Scene(root, 800, 600);

        // Ustawienie tła
        Image backgroundImage = new Image("earth.gif");
        ImageView backgroundImageView = new ImageView(backgroundImage);
        backgroundImageView.setFitWidth(800);
        backgroundImageView.setFitHeight(600);
        backgroundImageView.setPreserveRatio(false);
        root.getChildren().add(backgroundImageView);

        // Przykład użycia klasy ConvexHull do utworzenia kształtu statku
        convexHull = new ConvexHull(shipFile);
        List<Point2D> points = convexHull.getHull(); // Tworzymy kwadrat o wymiarach 50x50

        double[] hullDimensions = convexHull.getHullDimensions();
        double hullWidth = hullDimensions[0];
        double hullHeight = hullDimensions[1];

        // Załadowanie obrazu statku
        Image shipImage = new Image("terran_wraith_1.png");
        player = new ImageView(shipImage);
        player.setFitWidth(hullWidth); // Ustawienie szerokości statku na podstawie szerokości otoczki wypukłej
        player.setFitHeight(hullHeight); // Ustawienie wysokości statku na podstawie wysokości otoczki wypukłej
        //player.setPreserveRatio(true); // Zachowanie proporcji obrazu

        // Ustawienie początkowej pozycji statku
        player.setLayoutX(400 - player.getFitWidth() / 2); // Centrowanie statku na środku
        player.setLayoutY(550 - player.getFitHeight() / 2); // Ustawienie statku na dole okna

        shipWidth = player.getFitWidth();
        shipHeight = player.getFitHeight();

        hullPolygon = convexHull.toPolygon();
        hullPolygon.setStroke(Color.RED);
        hullPolygon.setFill(null);
        //root.getChildren().add(hullPolygon);

        //player.setLayoutX(400);
        //player.setLayoutY(550);

        root.getChildren().add(player);

        targetX = player.getLayoutX();
        targetY = player.getLayoutY();

        bowPoint = new Point2D(player.getX() + player.getFitWidth() / 2, player.getY() + player.getFitHeight() / 2);  // Zakładamy, że pierwszy punkt to dziób

        // Inicjalizacja tekstu punktów
        scoreText = new Text("Score: 0");
        scoreText.setFont(new Font("Arial", 20));
        scoreText.setFill(Color.WHITE);
        scoreText.setX(10);
        scoreText.setY(20);
        root.getChildren().add(scoreText);

        scene.setOnMouseMoved(this::handleMouseMovement);
        scene.setOnMousePressed(this::handleMousePressed);

        scene.setOnKeyPressed(event -> {
            keysPressed.add(event.getCode().toString());
            if (event.getCode().toString().equals("SPACE")) {
                controlModeRelativeToCursor = !controlModeRelativeToCursor; // Przełączanie trybu sterowania
            }
        });

        scene.setOnKeyPressed(event -> {
            keysPressed.add(event.getCode().toString());
            if (event.getCode().toString().equals("SPACE")) {
                controlModeRelativeToCursor = !controlModeRelativeToCursor; // Przełączanie trybu sterowania
            }
            if (event.getCode().toString().equals("ESCAPE")) {
                if (isPaused) {
                    resumeGame();
                } else {
                    pauseGame();
                }
            }
        });

        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (settings != null && settings.getControlMode().equals("Relative to Cursor")) {
                    controlModeRelativeToCursor = true;
                } else {
                    controlModeRelativeToCursor = false;
                }
                if (!isPaused) {
                    if (controlModeRelativeToCursor) {
                        movePlayerRelativeToCursor();
                    } else {
                        movePlayerClassic();
                    }
                    updateBullets();
                    updateAsteroids();
                    updateHullPolygon();
                    checkCollisions();
                    spawnAsteroids();
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
            double size = 20 + Math.random() * 30; // Losowy rozmiar od 20 do 50
            double angle = Math.random() * Math.PI / 4 - Math.PI / 8; // Losowy kąt od -22.5 do 22.5 stopni
            Point2D velocity = new Point2D(Math.sin(angle), Math.cos(angle)).multiply(2);

            Asteroid asteroid = new Asteroid(x, y, size, velocity);
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
        for (Asteroid asteroid : asteroids) {
            for (HullLine shipLine : convexHull.getSides()) {
                for (HullLine asteroidLine : asteroid.getConvexHull().getSides()) {
                    if (linesIntersect(shipLine, asteroidLine)) {
                        stopGame();
                        return;
                    }
                }
            }
        }

        for (int i = 0; i < bullets.size(); i++) {
            Bullet bullet = bullets.get(i);
            for (int j = 0; j < asteroids.size(); j++) {
                Asteroid asteroid = asteroids.get(j);
                for (HullLine asteroidLine : asteroid.getConvexHull().getSides()) {
                    if (lineIntersectsCircle(asteroidLine, bullet.getPosition(), bullet.getRadius())) {
                        root.getChildren().remove(bullet);
                        bullets.remove(i);
                        i--;
                        root.getChildren().remove(asteroid);
                        asteroids.remove(j);
                        j--;

                        score++; // Zwiększenie punktów po zniszczeniu asteroidy
                        updateScoreText(); // Aktualizacja tekstu punktów

                        break;
                    }
                }
            }
        }
    }

    private boolean linesIntersect(HullLine l1, HullLine l2) {
        double x1 = l1.p1.getX();
        double y1 = l1.p1.getY();
        double x2 = l1.p2.getX();
        double y2 = l1.p2.getY();
        double x3 = l2.p1.getX();
        double y3 = l2.p1.getY();
        double x4 = l2.p2.getX();
        double y4 = l2.p2.getY();

        double denom = (y4 - y3) * (x2 - x1) - (x4 - x3) * (y2 - y1);
        if (denom == 0) {
            return false; // linie są równoległe
        }

        double ua = ((x4 - x3) * (y1 - y3) - (y4 - y3) * (x1 - x3)) / denom;
        double ub = ((x2 - x1) * (y1 - y3) - (y2 - y1) * (x1 - x3)) / denom;

        return (ua >= 0 && ua <= 1) && (ub >= 0 && ub <= 1);
    }

    private boolean lineIntersectsCircle(HullLine line, Point2D center, double radius) {
        Point2D start = line.p1;
        Point2D end = line.p2;

        double dx = end.getX() - start.getX();
        double dy = end.getY() - start.getY();
        double fx = start.getX() - center.getX();
        double fy = start.getY() - center.getY();

        double a = dx * dx + dy * dy;
        double b = 2 * (fx * dx + fy * dy);
        double c = (fx * fx + fy * fy) - radius * radius;

        double discriminant = b * b - 4 * a * c;

        if (discriminant >= 0) {
            discriminant = Math.sqrt(discriminant);
            double t1 = (-b - discriminant) / (2 * a);
            double t2 = (-b + discriminant) / (2 * a);

            if (t1 >= 0 && t1 <= 1) {
                return true;
            }

            if (t2 >= 0 && t2 <= 1) {
                return true;
            }
        }

        return false;
    }

    private void updateScoreText() {
        scoreText.setText("Score: " + score);
    }

    private void showGameOver() {
        Text gameOverText = new Text("Game Over\n  Score: " + score);
        gameOverText.setFont(new Font("Arial", 50));
        gameOverText.setFill(Color.RED);
        gameOverText.setX(275);
        gameOverText.setY(275);
        root.getChildren().add(gameOverText);

        PauseTransition pause = new PauseTransition(Duration.seconds(4));
        pause.setOnFinished(event -> Platform.exit());
        pause.play();
    }

    private void stopGame() {

        showGameOver();
    }

    public void pauseGame() {
        isPaused = true;
        showPauseMenu();
    }

    public void resumeGame() {
        isPaused = false;
    }

    private void showPauseMenu() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/PauseMenu.fxml"));
            Pane root = loader.load();

            PauseMenuController controller = loader.getController();
            controller.setPrimaryStage(primaryStage);
            controller.setGameApplication(this);

            Scene scene = new Scene(root, 800, 600);
            primaryStage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void movePlayerRelativeToCursor() {
        Point2D currentBowPoint = player.localToParent(bowPoint);

        double deltaX = targetX - currentBowPoint.getX();
        double deltaY = targetY - currentBowPoint.getY();
        double distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
        double directionX = deltaX / distance;
        double directionY = deltaY / distance;

        if (keysPressed.contains(settings.getMoveUpKey())) {
            double oldX = player.getLayoutX();
            double oldY = player.getLayoutY();
            player.setLayoutX(player.getLayoutX() + directionX * speed);
            player.setLayoutY(player.getLayoutY() + directionY * speed);
            convexHull.updatePosition(player.getLayoutX() - oldX, player.getLayoutY() - oldY);
            updateHullPolygon();
        }
        if (keysPressed.contains(settings.getMoveDownKey())) {
            double oldX = player.getLayoutX();
            double oldY = player.getLayoutY();
            player.setLayoutX(player.getLayoutX() - directionX * speed);
            player.setLayoutY(player.getLayoutY() - directionY * speed);
            convexHull.updatePosition(player.getLayoutX() - oldX, player.getLayoutY() - oldY);
            updateHullPolygon();
        }
        if (keysPressed.contains(settings.getMoveRightKey())) {
            double oldX = player.getLayoutX();
            double oldY = player.getLayoutY();
            player.setLayoutX(player.getLayoutX() + directionY * speed);
            player.setLayoutY(player.getLayoutY() - directionX * speed);
            convexHull.updatePosition(player.getLayoutX() - oldX, player.getLayoutY() - oldY);
            updateHullPolygon();
        }
        if (keysPressed.contains(settings.getMoveLeftKey())) {
            double oldX = player.getLayoutX();
            double oldY = player.getLayoutY();
            player.setLayoutX(player.getLayoutX() - directionY * speed);
            player.setLayoutY(player.getLayoutY() + directionX * speed);
            convexHull.updatePosition(player.getLayoutX() - oldX, player.getLayoutY() - oldY);
            updateHullPolygon();
        }

        double angle = Math.toDegrees(Math.atan2(deltaY, deltaX));
        if (angle != previousAngle) {
            player.setRotate(angle);
            convexHull.rotate(Math.toRadians(angle - previousAngle), new Point2D(player.getLayoutX() + shipWidth / 2, player.getLayoutY() + shipHeight / 2));
            updateHullPolygon();
            previousAngle = angle;
        }
    }

    private void movePlayerClassic() {
        double oldX = player.getLayoutX();
        double oldY = player.getLayoutY();

        if (keysPressed.contains(settings.getMoveUpKey())) {
            player.setLayoutY(player.getLayoutY() - speed);
        }
        if (keysPressed.contains(settings.getMoveDownKey())) {
            player.setLayoutY(player.getLayoutY() + speed);
        }
        if (keysPressed.contains(settings.getMoveLeftKey())) {
            player.setLayoutX(player.getLayoutX() - speed);
        }
        if (keysPressed.contains(settings.getMoveRightKey())) {
            player.setLayoutX(player.getLayoutX() + speed);
        }

        convexHull.updatePosition(player.getLayoutX() - oldX, player.getLayoutY() - oldY);
        updateHullPolygon();

        Point2D currentBowPoint = player.localToParent(bowPoint);

        double deltaX = targetX - currentBowPoint.getX();
        double deltaY = targetY - currentBowPoint.getY();

        double angle = Math.toDegrees(Math.atan2(deltaY, deltaX));
        if (angle != previousAngle) {
            player.setRotate(angle);
            convexHull.rotate(Math.toRadians(angle - previousAngle), new Point2D(player.getLayoutX() + shipWidth / 2, player.getLayoutY() + shipHeight / 2));
            updateHullPolygon();
            previousAngle = angle;
        }
    }

    private void updateHullPolygon() {
        hullPolygon.getPoints().clear();
        for (Point2D point : convexHull.getHull()) {
            hullPolygon.getPoints().addAll(point.getX(), point.getY());
        }
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

    private void drawHullLines(ConvexHull hull, Color color) {
        for (HullLine hullLine : hull.getSides()) {
            Line line = new Line(hullLine.p1.getX(), hullLine.p1.getY(), hullLine.p2.getX(), hullLine.p2.getY());
            line.setStroke(color);
            line.setStrokeWidth(2);
            root.getChildren().add(line);
        }
    }
}