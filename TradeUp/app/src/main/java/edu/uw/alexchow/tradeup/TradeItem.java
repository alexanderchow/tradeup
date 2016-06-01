package edu.uw.alexchow.tradeup;

import android.location.Location;

/**
 * Created by alexchow on 5/23/16.
 */
public class TradeItem {
    public String id, posterName, image, title, description, time;
    public double latitude, longitude;


    public TradeItem() {
    }

    public TradeItem(String id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
    }

    public TradeItem(String id, String posterName, String image,
                     String title, String description, String time, double latitude, double longitude) {
        super();
        this.id = id;
        this.posterName = posterName;
        this.image = image;
        this.title = title;
        this.description = description;
        this.time = time;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getPosterName() {
        return posterName;
    }

    public void setPosterName(String posterName) {
        this.posterName = posterName;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public double getLatitude() { return latitude; }

    public void setLatitude(double latitude) { this.latitude = latitude; }

    public double getLongitude() { return longitude; }

    public void setLongitude(double longitude) { this.longitude = longitude; }

}