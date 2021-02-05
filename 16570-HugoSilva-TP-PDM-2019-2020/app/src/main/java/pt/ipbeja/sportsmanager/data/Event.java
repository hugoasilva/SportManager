package pt.ipbeja.sportsmanager.data;

import java.util.Objects;

/**
 * Event Class
 *
 * @author Hugo Silva - 16570
 * @version 2021-02-05
 */
public class Event {
    //    private int id;
    private int icon;
    private String name;
    private Position position;
    private String date;
    private String time;
    private String category;

    /**
     * Event object constructor(firebase)
     */
    public Event() {
    }

    /**
     * Event object constructor
     *
     * @param icon     event's icon
     * @param name     event's name
     * @param position event's position
     * @param date     event's date
     * @param time     event's time
     * @param category event's category
     */
    public Event(int icon, String name, Position position,
                 String date, String time, String category) {
//        this.id = id;
        this.icon = icon;
        this.name = name;
        this.position = position;
        this.date = date;
        this.time = time;
        this.category = category;
    }

    /**
     * Get event icon
     *
     * @return event icon
     */
    public int getIcon() {
        return this.icon;
    }

    /**
     * Set event icon
     */
    public void setIcon(int icon) {
        this.icon = icon;
    }

    /**
     * Get event name
     *
     * @return event name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Set event name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get event position
     *
     * @return event position
     */
    public Position getPosition() {
        return this.position;
    }

    /**
     * Set event position
     */
    public void setPosition(Position position) {
        this.position = position;
    }

    /**
     * Get event date
     *
     * @return event date
     */
    public String getDate() {
        return date;
    }

    /**
     * Set event date
     */
    public void setDate(String date) {
        this.date = date;
    }


    /**
     * Get event time
     *
     * @return event time
     */
    public String getTime() {
        return this.time;
    }

    /**
     * Set event time
     */
    public void setTime(String time) {
        this.time = time;
    }

    /**
     * Get event category
     *
     * @return event category
     */
    public String getCategory() {
        return this.category;
    }

    /**
     * Set event category
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * Get event as String object
     *
     * @return event as String
     */
    @Override
    public String toString() {
        return "Event{" +
                "name=" + this.name +
                ", position=" + this.position.toString() +
                ", date=" + this.date +
                ", time=" + this.time +
                ", category=" + this.category +
                '}';
    }

    /**
     * Get event hash code
     *
     * @return event hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(this.name, this.position, this.date, this.time, this.category);
    }
}