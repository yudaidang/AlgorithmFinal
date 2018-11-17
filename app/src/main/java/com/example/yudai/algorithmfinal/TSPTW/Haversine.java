package com.example.yudai.algorithmfinal.TSPTW;

public class Haversine {
    private static final int EARTH_RADIUS = 6371; // Approx Earth radius in KM

    public static double distance(double mLatitude1, double mLongitude1, double mLatitude2, double mLongitude2) {

        double dLat = Math.toRadians((mLatitude2 - mLatitude1));
        double dLong = Math.toRadians((mLongitude2 - mLongitude1));

        double a = haversin(dLat) + Math.cos(Math.toRadians(mLatitude1)) * Math.cos(Math.toRadians(mLatitude2)) * haversin(dLong);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS * c * 1.5; // <-- d
    }

    public static double haversin(double val) {
        return Math.pow(Math.sin(val / 2), 2);
    }

    public double travelTime(double mLatitude1, double mLongitude1, double mLatitude2, double mLongitude2) {
        double mDistance = distance(mLatitude1, mLongitude1, mLatitude2, mLongitude2);
        return ((double) Math.round((((mDistance / 50) * 60) * 1.3) * 1) / 1);
    }
}
