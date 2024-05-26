package org.example.geometryproject;

import javafx.geometry.Point2D;

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
    List<Point2D> hull;
    Point2D center;

    public ConvexHull(String filename) {
        points = new ArrayList<>();
        hull = new ArrayList<>();
        loadPointsFromFile(filename);

        double x0 = (findLeftmostPoint().getX() + findRightmostPoint().getX()) / 2;
        double y0 = (findTopmostPoint().getY() + findBottommostPoint().getY()) / 2;

        center = new Point(x0, y0);

        setPosition(400, 550);

        calculateQuickHull();
    }

    public void calculateQuickHull() {
        if (points.size() < 3)
            return;

        Point2D leftmost = findLeftmostPoint();
        Point2D rightmost = findRightmostPoint();

        hull.add(leftmost);
        hull.add(rightmost);

        List<Point2D> pointsAbove = new ArrayList<>();
        List<Point2D> pointsBelow = new ArrayList<>();

        for (Point2D p : points) {
            if (quickOrientation(leftmost, rightmost, p) == 1)
                pointsAbove.add(p);
            else if (quickOrientation(leftmost, rightmost, p) == -1)
                pointsBelow.add(p);
        }

        findHull(leftmost, rightmost, pointsAbove);
        findHull(rightmost, leftmost, pointsBelow);

        // Remove duplicate endpoints
        hull = new ArrayList<>(new LinkedHashSet<>(hull));
    }

    private void findHull(Point2D p1, Point2D p2, List<Point2D> points) {
        int index = hull.indexOf(p2);
        if (points.size() == 0)
            return;

        if (points.size() == 1) {
            hull.add(index, points.get(0));
            return;
        }

        double maxDistance = 0;
        int farthestPointIndex = -1;

        for (int i = 0; i < points.size(); i++) {
            double distance = findDistance(p1, p2, points.get(i));
            if (distance > maxDistance) {
                maxDistance = distance;
                farthestPointIndex = i;
            }
        }

        Point2D farthestPoint = points.get(farthestPointIndex);
        hull.add(index, farthestPoint);

        List<Point2D> newPoints1 = new ArrayList<>();
        List<Point2D> newPoints2 = new ArrayList<>();

        for (Point2D point : points) {
            if (quickOrientation(p1, farthestPoint, point) == 1)
                newPoints1.add(point);
            else if (quickOrientation(farthestPoint, p2, point) == 1)
                newPoints2.add(point);
        }

        findHull(p1, farthestPoint, newPoints1);
        findHull(farthestPoint, p2, newPoints2);
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

        double angle = -Math.PI / 2;  // -90 stopni, aby skierować dziób do góry

        AffineTransform rotation = AffineTransform.getRotateInstance(angle, center.getX(), center.getY());

        for (int i = 0; i < points.size(); i++) {
            Point2D point = points.get(i);
            double[] src = {point.getX(), point.getY()};
            double[] dst = new double[2];
            rotation.transform(src, 0, dst, 0, 1);
            points.set(i, new Point2D(dst[0], dst[1]));
        }



        double tempX = x0 - center.getX();
        double tempY = y0 - center.getY();

        translate(tempX, tempY);


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

    private void translate(double dx, double dy) {
        for (int i = 0; i < points.size(); i++) {
            Point2D point = points.get(i);
            points.set(i, new Point2D(point.getX() + dx, point.getY() + dy));
        }
    }

    public List<Point2D> getHull() {
        return hull;
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
}
