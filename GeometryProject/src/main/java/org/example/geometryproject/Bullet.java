package org.example.geometryproject;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Bullet extends Circle {
    private Point2D velocity;

    public Bullet(double x, double y, Point2D velocity) {
        super(x, y, 5, Color.RED);
        this.velocity = velocity;
    }

    public void update() {
        setCenterX(getCenterX() + velocity.getX());
        setCenterY(getCenterY() + velocity.getY());
    }

    public boolean isOffScreen(double width, double height) {
        return getCenterX() < 0 || getCenterX() > width || getCenterY() < 0 || getCenterY() > height;
    }
}
