package org.example.geometryproject;

import javafx.geometry.Point2D;

import java.util.ArrayList;
import java.util.List;

public class Collision {
    public static boolean checkPolygonCollision(List<Point2D> polygon1, List<Point2D> polygon2) {
        List<Point2D> axes1 = getAxes(polygon1);
        List<Point2D> axes2 = getAxes(polygon2);

        for (Point2D axis : axes1) {
            if (!isProjectionOverlapping(axis, polygon1, polygon2)) {
                return false;
            }
        }

        for (Point2D axis : axes2) {
            if (!isProjectionOverlapping(axis, polygon1, polygon2)) {
                return false;
            }
        }

        return true;
    }

    private static List<Point2D> getAxes(List<Point2D> polygon) {
        List<Point2D> axes = new ArrayList<>();
        for (int i = 0; i < polygon.size(); i++) {
            Point2D p1 = polygon.get(i);
            Point2D p2 = polygon.get((i + 1) % polygon.size());
            Point2D edge = p2.subtract(p1);
            Point2D axis = new Point2D(-edge.getY(), edge.getX());
            axes.add(axis.normalize());
        }
        return axes;
    }

    private static boolean isProjectionOverlapping(Point2D axis, List<Point2D> polygon1, List<Point2D> polygon2) {
        double[] projection1 = projectPolygon(axis, polygon1);
        double[] projection2 = projectPolygon(axis, polygon2);
        return projection1[1] >= projection2[0] && projection2[1] >= projection1[0];
    }

    private static double[] projectPolygon(Point2D axis, List<Point2D> polygon) {
        double min = axis.dotProduct(polygon.get(0));
        double max = min;
        for (int i = 1; i < polygon.size(); i++) {
            double projection = axis.dotProduct(polygon.get(i));
            if (projection < min) {
                min = projection;
            }
            if (projection > max) {
                max = projection;
            }
        }
        return new double[]{min, max};
    }
}
