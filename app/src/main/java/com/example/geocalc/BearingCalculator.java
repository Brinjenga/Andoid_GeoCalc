package com.example.geocalc;

    public class BearingCalculator {
        public static double bearing(double lat1, double long1, double lat2, double long2) {
            double bearing = 0.0;
            lat1 = Math.toRadians(lat1);
            long1 = Math.toRadians(long1);
            lat2 = Math.toRadians(lat2);
            long2 = Math.toRadians(long2);

            double bearingradians = Math.atan2(Math.asin(long2 - long1) * Math.cos(lat2), Math.cos(lat1) * Math.sin(lat2) - Math.sin(lat1) * Math.cos(lat2) * Math.cos(long2 - long1));
            double bearingdegrees = Math.toDegrees(bearingradians);
            //bearingdegrees = bearingdegrees < 0? 360 + $bearingdegrees : $bearingdegrees; this is php code
            if (bearingdegrees < 0) {
                bearingdegrees = 360 + bearingdegrees;
            }

            return bearingdegrees;
        }
    }