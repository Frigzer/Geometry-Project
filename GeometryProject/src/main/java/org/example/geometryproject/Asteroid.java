package org.example.geometryproject;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Asteroid extends Circle {
    private Point2D velocity;

    public Asteroid(double x, double y, Point2D velocity) {
        super(x, y, 20, Color.GRAY);
        this.velocity = velocity;
    }

    public void update() {
        setCenterX(getCenterX() + velocity.getX());
        setCenterY(getCenterY() + velocity.getY());
    }

    public boolean isOffScreen(double width, double height) {
        return getCenterY() > height || getCenterX() < 0 || getCenterX() > width;
    }

    public Point2D getVelocity() {
        return velocity;
    }
}
