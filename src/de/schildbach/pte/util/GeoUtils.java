package de.schildbach.pte.util;

import java.util.List;

import de.schildbach.pte.dto.Point;

public class GeoUtils {
    public static double geoDistanceInMeters(final Point pointA, final Point pointB) {
        final double rad = Math.PI / 180;
        final double lat1 = pointA.getLatAsDouble() * rad;
        final double lon1 = pointA.getLonAsDouble() * rad;
        final double lat2 = pointB.getLatAsDouble() * rad;
        final double lon2 = pointB.getLonAsDouble() * rad;
        final double sinDLat = Math.sin(((lat2 - lat1) * rad) / 2);
        final double sinDLon = Math.sin(((lon2 - lon1) * rad) / 2);
        final double a = sinDLat * sinDLat + Math.cos(lat1) * Math.cos(lat2) * sinDLon * sinDLon;
        final double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return 6371000 * c;
    }

    public static class PointAndDistance {
        public final Point point;
        public final int pointBeforeIndex;
        public final int pointAfterIndex;
        public final double distanceInMeters;

        public PointAndDistance(
                final Point point,
                final int pointBeforeIndex,
                final int pointAfterIndex,
                final double distanceInMeters) {
            this.point = point;
            this.pointBeforeIndex = pointBeforeIndex;
            this.pointAfterIndex = pointAfterIndex;
            this.distanceInMeters = distanceInMeters;
        }
    }

    public static PointAndDistance findClosestPointOnLine(
            final Point position,
            final Point pointA,
            final Point pointB) {
        final double distanceA = geoDistanceInMeters(position, pointA);
        final double distanceB = geoDistanceInMeters(position, pointB);
        final double distanceS = geoDistanceInMeters(pointA, pointB);
        if (distanceS < 0.0001)
            return new PointAndDistance(pointA, 0, 0, distanceA);
        final double relativeDistance =
                (distanceS * distanceS + distanceA * distanceA - distanceB * distanceB) /
                        (2 * distanceS * distanceS);
        if (relativeDistance <= 0.0)
            return new PointAndDistance(pointA, -1, 0, distanceA);
        if (relativeDistance >= 1.0)
            return new PointAndDistance(pointB, 1, 2, distanceB);
        final Point closestPoint = Point.fromDouble(
                pointA.getLatAsDouble()
                        + relativeDistance * (pointB.getLatAsDouble() - pointA.getLatAsDouble()),
                pointA.getLonAsDouble()
                        + relativeDistance * (pointB.getLonAsDouble() - pointA.getLonAsDouble()));
        return new PointAndDistance(
                closestPoint,
                0, 1,
                geoDistanceInMeters(position, closestPoint));
    }

    public static PointAndDistance findClosestPointOnTrack(
            final Point position,
            final List<Point> track,
            int startIndex, int endIndex) {
        if (track == null || track.isEmpty())
            return null;
        final int lastIndex = track.size() - 1;
        if (startIndex < 0)
            startIndex = 0;
        if (endIndex < 0 || endIndex > lastIndex)
            endIndex = lastIndex + 1;
        if (startIndex >= lastIndex) {
            final Point lastPoint = track.get(lastIndex);
            return new PointAndDistance(
                    lastPoint, lastIndex, lastIndex + 1,
                    GeoUtils.geoDistanceInMeters(position, lastPoint));
        }
        Point closestPoint = null;
        int pointBeforeIndex = -1;
        int pointAfterIndex = -1;
        double distance = Double.MAX_VALUE;
        Point pointA = track.get(startIndex);
        for (int indexAfter = startIndex + 1; indexAfter < endIndex; ++indexAfter) {
            final Point pointB = track.get(indexAfter);
            final PointAndDistance toLine = findClosestPointOnLine(position, pointA, pointB);
            if (closestPoint == null) {
                closestPoint = toLine.point;
                distance = toLine.distanceInMeters;
                pointBeforeIndex = toLine.pointBeforeIndex;
                pointAfterIndex = toLine.pointAfterIndex;
            } else if (toLine.distanceInMeters <= distance) {
                closestPoint = toLine.point;
                distance = toLine.distanceInMeters;
                final int bi = toLine.pointBeforeIndex;
                final int ax = bi + indexAfter;
                final int bx = ax - 1;
                if (bi < 0 && bx == pointBeforeIndex) {
                    // before this section and exactly after the previous
                    // treat like it lies between the same point
                    pointBeforeIndex = bx;
                    pointAfterIndex = bx;
                } else {
                    pointBeforeIndex = bx;
                    pointAfterIndex = ax;
                }
            }
            pointA = pointB;
        }
        return new PointAndDistance(closestPoint, pointBeforeIndex, pointAfterIndex, distance);
    }
}
