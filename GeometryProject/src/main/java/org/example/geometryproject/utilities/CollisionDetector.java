package org.example.geometryproject.utilities;

import javafx.geometry.Point2D;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;

import java.util.List;

public class CollisionDetector {

    public void detectCollision(Point2D point, List<Line> sides, Pane root, double delay) {

    }

    private void drawExplosion(Point2D point, Pane root) {
        // Przykładowa funkcja rysująca eksplozję
        // Możesz dodać tutaj kod rysowania eksplozji na podstawie punktu kolizji
    }

    private void removePointAfterCollision(Point2D point) {
        // Przykładowa funkcja usuwająca punkt po kolizji
        // Możesz dodać tutaj kod usuwania punktu z listy lub sceny
    }

    private void changeConvexHullSideColor(Line side) {
        // Przykładowa funkcja zmieniająca kolor boku otoczki po kolizji
        // Możesz dodać tutaj kod zmieniający kolor boku otoczki
    }
}