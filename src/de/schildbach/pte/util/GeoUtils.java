package de.schildbach.pte.util;

import de.schildbach.pte.dto.Point;

public class GeoUtils {
    public static double geoDistanceInMeters(final Point pointA, final Point pointB) {
        final double rad = Math.PI / 180;
        final double lat1 = pointA.getLatAsDouble() * rad;
        final double lon1 = pointA.getLonAsDouble() * rad;
        final double lat2 = pointB.getLatAsDouble() * rad;
        final double lon2 = pointB.getLonAsDouble() * rad;
        final double sinDLat = Math.sin((lat2 - lat1) / 2);
        final double sinDLon = Math.sin((lon2 - lon1) / 2);
        final double a = sinDLat * sinDLat + Math.cos(lat1) * Math.cos(lat2) * sinDLon * sinDLon;
        final double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return 6371000 * c;
    }

    public static Point getRemotePoint(final Point point, final double bearing, final double distanceInMeters) {
        final double lat1Rad = Math.toRadians(point.getLatAsDouble());
        final double lon1Rad = Math.toRadians(point.getLonAsDouble());
        final double bearingRad = Math.toRadians(bearing);
        final double delta = distanceInMeters / 6371000;
        final double lat2Rad = Math.asin(Math.sin(lat1Rad) * Math.cos(delta) +
                Math.cos(lat1Rad) * Math.sin(delta) * Math.cos(bearingRad));
        final double lon2Rad = lon1Rad + Math.atan2(Math.sin(bearingRad) * Math.sin(delta) * Math.cos(lat1Rad),
                Math.cos(delta) - Math.sin(lat1Rad) * Math.sin(lat2Rad));
        return Point.fromDouble(Math.toDegrees(lat2Rad), Math.toDegrees(lon2Rad));
    }
}
