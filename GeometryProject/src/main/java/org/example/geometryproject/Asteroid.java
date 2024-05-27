package org.example.geometryproject;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;

import java.util.ArrayList;
import java.util.List;

public class Asteroid extends Polygon {
    private Point2D velocity;
    private List<Point2D> points;
    private ConvexHull convexHull;

    public Asteroid(double x, double y, double size, Point2D velocity) {
        this.velocity = velocity;
        this.points = generateAsteroidPoints(x, y, size);
        convexHull = new ConvexHull(points);
        List<Point2D> hullPoints = convexHull.getHull();

        for (Point2D point : hullPoints) {
            getPoints().addAll(point.getX(), point.getY());
        }
        setFill(Color.GRAY);
    }

    private List<Point2D> generateAsteroidPoints(double centerX, double centerY, double size) {
        List<Point2D> points = new ArrayList<>();
        int numPoints = 10 + (int) (Math.random() * 5); // losowa liczba punktów od 10 do 15
        double angleStep = 360.0 / numPoints;

        for (int i = 0; i < numPoints; i++) {
            double angle = i * angleStep;
            double offsetX = (Math.random() * size / 2) - size / 4;
            double offsetY = (Math.random() * size / 2) - size / 4;
            double x = centerX + (size + offsetX) * Math.cos(Math.toRadians(angle));
            double y = centerY + (size + offsetY) * Math.sin(Math.toRadians(angle));
            points.add(new Point2D(x, y));
        }

        return points;
    }

    public void update() {
        setLayoutX(getLayoutX() + velocity.getX());
        setLayoutY(getLayoutY() + velocity.getY());
        convexHull.updatePosition(velocity.getX(), velocity.getY());
    }

    public boolean isOffScreen(double width, double height) {
        return getLayoutY() > height || getLayoutX() < 0 || getLayoutX() > width;
    }

    public Point2D getVelocity() {
        return velocity;
    }

    public ConvexHull getConvexHull() {
        return convexHull;
    }
}
