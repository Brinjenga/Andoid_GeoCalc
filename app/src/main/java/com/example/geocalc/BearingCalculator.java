package com.example.geocalc;

public class BearingCalculator {

    public static double bearing(double lat1, double lon1, double lat2, double lon2){

        double longDiff= lon2-lon1;
        double y = Math.sin(Math.toRadians(longDiff))*Math.cos(Math.toRadians(lat2));
        double x = Math.cos(Math.toRadians(lat1))*Math.sin(Math.toRadians(lat2))-
                Math.sin(Math.toRadians(lat1))*Math.cos(Math.toRadians(lat2))*Math.cos(Math.toRadians(longDiff));

        return Math.toDegrees(Math.atan2(y, x)+360)%360;
    }
}
