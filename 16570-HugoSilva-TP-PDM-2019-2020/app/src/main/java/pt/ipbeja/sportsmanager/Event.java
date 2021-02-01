package pt.ipbeja.sportsmanager;

public class Event {
    private int id;
    private int imageResource;
    private String name;
    private String location;
    private String date;
    private String time;
    private String category;

    public Event(int id, int imageResource, String name, String location, String date, String time, String category) {
        this.id = id;
        this.imageResource = imageResource;
        this.name = name;
        this.location = location;
        this.date = date;
        this.time = time;
        this.category = category;
    }

    public int getImageResource() {
        return this.imageResource;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return this.location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return this.time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCategory() {
        return this.category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}