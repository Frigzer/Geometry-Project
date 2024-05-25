package org.example.geometryproject;

public class Line extends javafx.scene.shape.Line {
    Point p1, p2;
    double a, b;
/*
    public Line(Point p1, Point p2) {
        this.p1 = p1;
        this.p2 = p2;
        if(p1.x == p2.x) {
            a = 1;
            b = p1.x;
        }
        else {
            a = (p1.y - p2.y) / (p1.x - p2.x);
            b = p1.y - ((p1.y - p2.y) / (p1.x - p2.x)) * p1.x;
        }
    }

    public boolean isPointOnLine(Point p) {
        if(p != null) {
            if(p1.x == p2.x && p.x == p1.x)
                return true;
            return p.y == (a * p.x) + b;
        }
        return false;
    }

    public boolean isPointOnSegment(Point p3) {
        return isPointOnLine(p3) && p3.x >= Math.min(p1.x, p2.x) && p3.x <= Math.max(p1.x, p2.x) && p3.y >= Math.min(p1.y, p2.y) && p3.y <= Math.max(p1.y, p2.y);
    }

    public int pointPosition(Point p) {
        double temp = p.x * a + b - p.y;
        //double temp = (p.x - p1.x) * (p2.y - p1.y) - (p.y - p1.y) * (p2.x - p1.x);
        //double A = this.a, B = -1, C = this.b;
        //double temp = A * p.x + B * p.y + C;
        if(temp < 0)
            return -1;
            //System.out.println("Punkt znajduje się na prawo od prostej");
        else if (temp > 0)
            return 1;
            //System.out.println("Punkt znajduje się na lewo od prostej");
        else
            return 0;
            //System.out.println("Punkt znajduje się na prostej");
    }

    void translate(double dx, double dy) {
        p1.x += dx;
        p1.y += dy;
        p2.x += dx;
        p2.y += dy;

        a = (p1.y - p2.y) / (p1.x - p2.x);
        b = p1.y - ((p1.y - p2.y) / (p1.x - p2.x)) * p1.x;
    }

    Point reflectPoint(Point p) {
        Point newPoint = new Point();
        if(a == 0 & b == 0) {
            newPoint.x = p.x;
            newPoint.y = -p.y;
        }
        else if(a == 1 && b == 0) {
            newPoint.x = p.y;
            newPoint.y = p.x;
        }
        else if(a == 0) {
            newPoint.x = p.x;
            newPoint.y = 2 * b - p.y;
        }
        else if(b == 0) {
            newPoint.x = (1 - a * a) / (1 + a * a) * p.x + (2 * a) / (1 + a * a) * p.y;
            newPoint.y = (2 * a) / (1 + a * a) * p.x - (1 - a * a) / (1 + a * a) * p.y;
        }
        else {

            //double d = Math.abs((A * p.x) + (B * p.y) + C) / Math.sqrt((A * A) + (B * B));
            double a2 = -Math.pow(a, -1);
            double b2 = p.y - p.x * a2;

            double tempx = (b2 - b) / (a - a2);
            double tempy = a * tempx + b;

            newPoint.x = - p.x + 2 * tempx;
            newPoint.y = - p.y + 2 * tempy;

            //System.out.println("y = " + a2 + "x + " + b2);

        }
        return newPoint;
    }


    public Point findIntersectionPoint(Line line) {

        if((p1.y - p2.y) / (p1.x - p2.x) == (line.p1.y - line.p2.y) / (line.p1.x - line.p2.x))
            return null;
         else {
            Point newPoint = new Point();

            if(this.p1.x == this.p2.x) {
                newPoint.x = this.p1.x;
                newPoint.y = ((line.p1.y - line.p2.y) / (line.p1.x - line.p2.x)) * newPoint.x + (line.p1.y - ((line.p1.y - line.p2.y) / (line.p1.x - line.p2.x)) * line.p1.x);
            } else if (line.p1.x == line.p2.x) {
                newPoint.x = line.p1.x;
                newPoint.y = ((p1.y - p2.y) / (p1.x - p2.x)) * newPoint.x + (p1.y - ((p1.y - p2.y) / (p1.x - p2.x)) * p1.x);
            }
            else {
                newPoint.x = ((line.p1.y - ((line.p1.y - line.p2.y) / (line.p1.x - line.p2.x)) * line.p1.x) - (p1.y - ((p1.y - p2.y) / (p1.x - p2.x)) * p1.x)) / ((p1.y - p2.y) / (p1.x - p2.x) - (line.p1.y - line.p2.y) / (line.p1.x - line.p2.x));
                newPoint.y = (p1.y - p2.y) / (p1.x - p2.x) * newPoint.x + (p1.y - ((p1.y - p2.y) / (p1.x - p2.x)) * p1.x);
            }
            return newPoint;
        }
    }

    public Point findIntersectionFromGeneralForm(Line line) {
        double A = this.a, B = -1, C = this.b;
        double D = line.a, E = -1, F = line.b;

        if(A/D == B/E)
            return null;
        else {
            Point newPoint = new Point();

            if(this.p1.x == this.p2.x) {
                newPoint.x = this.p1.x;
                newPoint.y = D * newPoint.x + F;
            }
            else if (line.p1.x == line.p2.x) {
                newPoint.x = F;
                newPoint.y = A * newPoint.x + C;
            }
            else {
                newPoint.x = (B * F - C * E) / (A * E - B * D);
                newPoint.y = (C * D - A * F) / (A * E - B * D);
            }
            return newPoint;
        }
    }

    public Point findIntersectionSegment(Line line) {
        if(this.a == line.a) {
            return null;
        }
        else {
            Point newPoint = new Point();

            if(this.p1.x == this.p2.x) {
                newPoint.x = this.b;
                newPoint.y = line.a * newPoint.x + line.b;
            }
            else if (line.p1.x == line.p2.x) {
                newPoint.x = line.b;
                newPoint.y = this.a * newPoint.x + this.b;
            } else {
                newPoint.x = (line.b - b) / (a - line.a);
                newPoint.y = a * newPoint.x + b;
            }

            if(this.isPointOnSegment(newPoint)) {
                return newPoint;
            }

            return null;
        }
    }

    public double getPointLineDistance(Point p) {
        if(isPointOnLine(p))
            return 0;
        else {
            double A = a, B = -1, C = b;
            double d = Math.abs((A * p.x) + (B * p.y) + C) / Math.sqrt((A * A) + (B * B));
            return d;
        }
    }

    public double getPointSideDistance(Point p) {

        double A = a, B = -1, C = b;
        double d = Math.abs((A * p.x) + (B * p.y) + C) / Math.sqrt((A * A) + (B * B));
        if(p.x >= Math.min(p1.x, p2.x) && p.x <= Math.max(p1.x, p2.x) && p.y >= Math.min(p1.y, p2.y) && p.y <= Math.max(p1.y, p2.y))
            return d;
        return 1000;

    }


 */
}
