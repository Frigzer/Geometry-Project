package org.example.geometryproject.core;

import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/*
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

 */

public class Bullet extends ImageView {
    private Point2D velocity;

    public Bullet(double x, double y, Point2D velocity) {
        super(new Image("energyball.png")); // Załadowanie obrazu pocisku
        this.velocity = velocity;
        setX(x);
        setY(y);
        setFitWidth(10); // Ustawienie szerokości pocisku
        setFitHeight(10); // Ustawienie wysokości pocisku
    }

    public void update() {
        setX(getX() + velocity.getX());
        setY(getY() + velocity.getY());
    }

    public boolean isOffScreen(int screenWidth, int screenHeight) {
        return getX() < 0 || getX() > screenWidth || getY() < 0 || getY() > screenHeight;
    }

    public Point2D getPosition() {
        return new Point2D(getX(), getY());
    }

    public double getRadius() {
        return Math.min(getFitWidth(), getFitHeight()) / 2;
    }
}
