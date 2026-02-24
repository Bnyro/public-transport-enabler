package de.schildbach.pte.util;

import de.schildbach.pte.dto.Location;
import de.schildbach.pte.dto.LocationType;
import de.schildbach.pte.dto.Point;

public class LocationUtil {
    public static Location coord(final int lat, final int lon) {
        return new Location(LocationType.COORD, null, Point.from1E6(lat, lon));
    }

    public static Location coord(final Point coord) {
        return new Location(LocationType.COORD, null, coord);
    }
}
