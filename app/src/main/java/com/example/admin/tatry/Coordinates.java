package com.example.admin.tatry;

public class Coordinates {
    private static double Latitude, Longitude;

    public static void setCoordinates(double latitude, double longitude) {
        Latitude = latitude;
        Longitude = longitude;
    }
    public static double getLatitude() {
        return Latitude;
    }
    public static double getLongitude() {
        return Longitude;
    }
}
