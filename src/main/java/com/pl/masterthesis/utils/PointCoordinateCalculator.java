package com.pl.masterthesis.utils;

public class PointCoordinateCalculator {
    public static Point calculatePointOnLineByDistance(Point firstPoint, Point secondPoint, double distance) {
        double m = distance;
        double n = calculateDistanceBetweenPoints(firstPoint, secondPoint) - distance;
        double calculatedX = (n * firstPoint.getX() + m * secondPoint.getX()) / (m + n);
        double calculatedY = (n * firstPoint.getY() + m * secondPoint.getY()) / (m + n);

        return new Point(calculatedX, calculatedY);
    }

    private static double calculateDistanceBetweenPoints(Point firstPoint, Point secondPoint) {
        return Math.sqrt(Math.pow(secondPoint.getX() - firstPoint.getX(), 2)
                + Math.pow(secondPoint.getY() - firstPoint.getY(), 2));
    }
}
