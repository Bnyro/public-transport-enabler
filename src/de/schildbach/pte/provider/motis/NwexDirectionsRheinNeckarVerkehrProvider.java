/*
 * Copyright the original author or authors.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package de.schildbach.pte.provider.motis;

import de.schildbach.pte.NetworkId;
import okhttp3.HttpUrl;

/**
 * @author Dan Cojocaru
 */
public class NwexDirectionsRheinNeckarVerkehrProvider extends AbstractMotisProvider {
    public NwexDirectionsRheinNeckarVerkehrProvider() {
        super(NetworkId.NWEXRNV, HttpUrl.parse(
                "https://directions.nwex.de/api/providers/rhein-neckar-verkehr"));
    }

    @Override
    public Description getDescription() {
        return new Description.Base() {
            @Override
            public String getName() {
                return "directions.nwex.de Rhein-Neckar-Verkehr";
            }

            @Override
            public String getDescriptionText() {
                return "Rhein-Neckar-Verkehr GmbH Open Transit Data Provider";
            }

            @Override
            public String getUrl() {
                return "https://codeberg.org/networkException/directions-rhein-neckar-verkehr";
            }
        };
    }
}
