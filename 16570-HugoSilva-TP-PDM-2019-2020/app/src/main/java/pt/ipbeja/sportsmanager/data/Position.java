package pt.ipbeja.sportsmanager.data;

import java.util.Objects;

/**
 * Position Class
 *
 * @author Hugo Silva - 16570
 * @version 2021-02-05
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
     * Get position latitude
     *
     * @return position latitude
     */
    public double getLatitude() {
        return this.latitude;
    }

    /**
     * Set position latitude
     */
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    /**
     * Get position longitude
     *
     * @return position longitude
     */
    public double getLongitude() {
        return this.longitude;
    }

    /**
     * Set position longitude
     */
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    /**
     * Get position as String object
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
     * Compare position
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
     * Get position hash code
     *
     * @return position hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(this.latitude, this.longitude);
    }
}
