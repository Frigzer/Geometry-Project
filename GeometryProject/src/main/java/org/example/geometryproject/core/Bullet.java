package org.example.geometryproject.core;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Bullet extends Circle {
    private Point2D velocity;

    public Bullet(double x, double y, Point2D velocity) {
        super(x, y, 5);
        this.velocity = velocity;
        setFill(Color.YELLOW);
    }

    public void update() {
        setCenterX(getCenterX() + velocity.getX());
        setCenterY(getCenterY() + velocity.getY());
    }

    public boolean isOffScreen(double width, double height) {
        return getCenterX() < 0 || getCenterX() > width || getCenterY() < 0 || getCenterY() > height;
    }

    public Point2D getPosition() {
        return new Point2D(getCenterX(), getCenterY());
    }

    public double getX() {
        return getCenterX();
    }

    public double getY() {
        return getCenterY();
    }
}
