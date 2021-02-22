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
 * Event Class
 *
 * @author Hugo Silva - 16570
 * @version 2021-02-22
 */
public class Event {
    private String name;
    private Position position;
    private String date;
    private String time;
    private String category;
    private String image;

    /**
     * Event object constructor(firebase)
     */
    public Event() {
    }

    /**
     * Event object constructor
     *
     * @param name     event's name
     * @param position event's position
     * @param date     event's date
     * @param time     event's time
     * @param category event's category
     * @param image    event's image
     */
    public Event(String name, Position position,
                 String date, String time, String category, String image) {
        this.name = name;
        this.position = position;
        this.date = date;
        this.time = time;
        this.category = category;
        this.image = image;
    }

    /**
     * Gets event name
     *
     * @return event name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Sets event name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets event position
     *
     * @return event position
     */
    public Position getPosition() {
        return this.position;
    }

    /**
     * Sets event position
     */
    public void setPosition(Position position) {
        this.position = position;
    }

    /**
     * Gets event date
     *
     * @return event date
     */
    public String getDate() {
        return date;
    }

    /**
     * Sets event date
     */
    public void setDate(String date) {
        this.date = date;
    }


    /**
     * Gets event time
     *
     * @return event time
     */
    public String getTime() {
        return this.time;
    }

    /**
     * Sets event time
     */
    public void setTime(String time) {
        this.time = time;
    }

    /**
     * Gets event category
     *
     * @return event category
     */
    public String getCategory() {
        return this.category;
    }

    /**
     * Sets event category
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * Gets event image
     *
     * @return event image
     */
    public String getImage() {
        return this.image;
    }

    /**
     * Sets event image
     */
    public void setImage(String image) {
        this.image = image;
    }

    /**
     * Gets event as String object
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
     * Gets event hash code
     *
     * @return event hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(this.name, this.position, this.date, this.time, this.category);
    }
}