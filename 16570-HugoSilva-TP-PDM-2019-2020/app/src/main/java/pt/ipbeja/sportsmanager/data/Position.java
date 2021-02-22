/*
 * Copyright 2021 Hugo Silva @ IPBeja
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package pt.ipbeja.sportsmanager.data;

import java.util.Objects;

/**
 * Position Class
 *
 * @author Hugo Silva - 16570
 * @version 2021-02-22
 */
public class Position {
    private double latitude;
    private double longitude;

    /**
     * Position object constructor
     *
     * @param latitude  event's latitude
     * @param longitude event's longitude
     */
    public Position(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    /**
     * Gets position latitude
     *
     * @return position latitude
     */
    public double getLatitude() {
        return this.latitude;
    }

    /**
     * Sets position latitude
     */
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    /**
     * Gets position longitude
     *
     * @return position longitude
     */
    public double getLongitude() {
        return this.longitude;
    }

    /**
     * Sets position longitude
     */
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    /**
     * Gets position as String object
     *
     * @return position as String
     */
    @Override
    public String toString() {
        return "Position{" +
                "latitude=" + this.latitude +
                ", longitude=" + this.longitude +
                '}';
    }

    /**
     * Compares position
     *
     * @param object object to compare
     * @return true if same position, false otherwise
     */
    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Position position = (Position) object;
        return Double.compare(position.latitude, this.latitude) == 0 &&
                Double.compare(position.longitude, this.longitude) == 0;
    }

    /**
     * Gets position hash code
     *
     * @return position hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(this.latitude, this.longitude);
    }
}
