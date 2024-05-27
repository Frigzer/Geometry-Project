package org.example.geometryproject;

import javafx.geometry.Point2D;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;

import java.awt.geom.AffineTransform;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Scanner;

public class ConvexHull {
    List<Point2D> points;
    Point2D center;
    private List<HullLine> sides;

    public ConvexHull(String filename) {
        points = new ArrayList<>();
        sides = new ArrayList<>();
        loadPointsFromFile(filename);

        calculateJarvis();

        double x0 = (findLeftmostPoint().getX() + findRightmostPoint().getX()) / 2;
        double y0 = (findTopmostPoint().getY() + findBottommostPoint().getY()) / 2;

        center = new Point(x0, y0);

        setPosition(400, 550);
    }

    public ConvexHull(List<Point2D> points) {
        this.points = points;
        sides = new ArrayList<>();
        calculateJarvis();

        double x0 = (findLeftmostPoint().getX() + findRightmostPoint().getX()) / 2;
        double y0 = (findTopmostPoint().getY() + findBottommostPoint().getY()) / 2;

        center = new Point(x0, y0);

    }


    public void calculateJarvis() {
        if (points.size() < 3)
            return;

        sides.clear();

        Point2D startPoint = findLeftmostPoint();
        Point2D currentPoint = startPoint;
        do {
            Point2D nextVertex = null;
            for (Point2D vertex : points) {
                if (vertex.equals(currentPoint))
                    continue;
                if (nextVertex == null || isClockwise(currentPoint, nextVertex, vertex)) {
                    nextVertex = vertex;
                }
            }
            sides.add(new HullLine(currentPoint,  nextVertex));
            currentPoint = nextVertex;
        } while (!currentPoint.equals(startPoint));
    }



    private boolean isClockwise(Point2D a, Point2D b, Point2D c) {
        return (b.getX() - a.getX()) * (c.getY() - a.getY()) - (b.getY() - a.getY()) * (c.getX() - a.getX()) > 0;
    }

    private double findDistance(Point2D p1, Point2D p2, Point2D p) {
        return Math.abs((p.getY() - p1.getY()) * (p2.getX() - p1.getX()) - (p2.getY() - p1.getY()) * (p.getX() - p1.getX()));
    }

    private int quickOrientation(Point2D p, Point2D q, Point2D r) {
        double val = (q.getY() - p.getY()) * (r.getX() - q.getX()) - (q.getX() - p.getX()) * (r.getY() - q.getY());
        if (val == 0) return 0;  // kolinearne
        return (val > 0) ? 1 : -1; // zgodnie lub przeciwnie do ruchu wskazówek zegara
    }

    private Point2D findLeftmostPoint() {
        Point2D leftmost = points.get(0);
        for (Point2D p : points) {
            if (p.getX() < leftmost.getX())
                leftmost = p;
        }
        return leftmost;
    }

    private Point2D findRightmostPoint() {
        Point2D rightmost = points.get(0);
        for (Point2D p : points) {
            if (p.getX() > rightmost.getX())
                rightmost = p;
        }
        return rightmost;
    }

    private Point2D findTopmostPoint() {
        Point2D topmost = points.get(0);
        for (Point2D p : points) {
            if (p.getY() > topmost.getY())
                topmost = p;
        }
        return topmost;
    }

    private Point2D findBottommostPoint() {
        Point2D bottommost = points.get(0);
        for (Point2D p : points) {
            if (p.getY() < bottommost.getY())
                bottommost = p;
        }
        return bottommost;
    }

    private void loadPointsFromFile(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.trim().split("\\s+");
                if (parts.length == 2) {
                    double x = Double.parseDouble(parts[0]);
                    double y = Double.parseDouble(parts[1]);
                    points.add(new Point2D(x, y));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setPosition(double x0, double y0) {
        Point2D topPoint = findTopmostPoint();

        double angle = -Math.PI / 2 ;  // -90 stopni, aby skierować dziób do góry

        List<Point2D> rotatedPoints = new ArrayList<>();
        for (Point2D point : points) {
            double newX = center.getX() + (point.getX() - center.getX()) * Math.cos(angle) - (point.getY() - center.getY()) * Math.sin(angle);
            double newY = center.getY() + (point.getX() - center.getX()) * Math.sin(angle) + (point.getY() - center.getY()) * Math.cos(angle);
            rotatedPoints.add(new Point2D(newX, newY));
        }
        points = rotatedPoints;

        // Translacja punktów
        double dx = x0 - center.getX();
        double dy = y0 - center.getY();
        translate(dx, dy);

    }

    public void rotate(double angle, Point2D center) {
        List<Point2D> rotatedPoints = new ArrayList<>();
        for (Point2D point : points) {
            double newX = center.getX() + (point.getX() - center.getX()) * Math.cos(angle) - (point.getY() - center.getY()) * Math.sin(angle);
            double newY = center.getY() + (point.getX() - center.getX()) * Math.sin(angle) + (point.getY() - center.getY()) * Math.cos(angle);
            rotatedPoints.add(new Point2D(newX, newY));
        }
        points = rotatedPoints;
        calculateJarvis();
    }

    public double[] getHullDimensions() {
        double minX = Double.MAX_VALUE;
        double maxX = Double.MIN_VALUE;
        double minY = Double.MAX_VALUE;
        double maxY = Double.MIN_VALUE;

        for (Point2D point : points) {
            if (point.getX() < minX) minX = point.getX();
            if (point.getX() > maxX) maxX = point.getX();
            if (point.getY() < minY) minY = point.getY();
            if (point.getY() > maxY) maxY = point.getY();
        }

        double width = maxX - minX;
        double height = maxY - minY;
        return new double[]{width, height};
    }

    public List<Point2D> getHull() {
        return points;
    }

    private void translate(double dx, double dy) {
        for (int i = 0; i < points.size(); i++) {
            Point2D point = points.get(i);
            points.set(i, new Point2D(point.getX() + dx, point.getY() + dy));
        }
        center = new Point2D(center.getX() + dx, center.getY() + dy);
    }

    public void updatePosition(double dx, double dy) {
        for (int i = 0; i < points.size(); i++) {
            Point2D point = points.get(i);
            points.set(i, new Point2D(point.getX() + dx, point.getY() + dy));
        }
        calculateJarvis(); // Recalculate sides
    }


    public List<HullLine> getSides() {
        return sides;
    }

    public Polygon toPolygon() {
        Polygon polygon = new Polygon();
        for (Point2D point : points) {
            polygon.getPoints().addAll(point.getX(), point.getY());
        }
        return polygon;
    }

    public Point2D getLeftmostPoint() {
        return findLeftmostPoint();
    }

    public Point2D getRightmostPoint() {
        return findRightmostPoint();
    }

    public Point2D getTopmostPoint() {
        return findTopmostPoint();
    }

    public Point2D getBottommostPoint() {
        return findBottommostPoint();
    }

    public boolean contains(double x, double y) {
        int n = points.size();
        boolean result = false;
        for (int i = 0, j = n - 1; i < n; j = i++) {
            if ((points.get(i).getY() > y) != (points.get(j).getY() > y) &&
                    (x < (points.get(j).getX() - points.get(i).getX()) * (y - points.get(i).getY()) / (points.get(j).getY() - points.get(i).getY()) + points.get(i).getX())) {
                result = !result;
            }
        }
        return result;
    }

    /*
    public void detectCollision(Point2D point, List<Line> sides, Pane root, double delay) {
        for (Line side : sides) {
            Line2D.Double line = new Line2D.Double(side.p1.getX(), side.p1.getY(), side.p2.getX(), side.p2.getY());
            Point2D.Double point2D = new Point2D.Double(point.getX(), point.getY());
            if (line.ptSegDist(point2D) <= 5) {


                break;
            }
        }

     */
}
