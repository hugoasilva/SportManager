package pt.ipbeja.sportsmanager.data;

public class Event {
    //    private int id;
    private int imageResource;
    private String name;
    private String latitude;
    private String longitude;
    private String date;
    private String time;
    private String category;

    public Event() {

    }

    public Event(int imageResource, String name, String latitude,
                 String longitude, String date, String time, String category) {
//        this.id = id;
        this.imageResource = imageResource;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
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

    public String getLatitude() {
        return this.latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return this.longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
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