package com.mtaner.android_ticmeet;

import java.io.Serializable;

public class Event implements Serializable {
   String name;
    String category;
     String date;
     String location;
     String eventImage;



    public Event(String category,String eventImage ) {
        this.category=category;
        this.eventImage=eventImage;
    }

    public Event(String name, String category, String date, String location, String eventImage) {
        this.name = name;
        this.category = category;
        this.date = date;
        this.location = location;
        this.eventImage=eventImage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getEventImage() {
        return eventImage;
    }

    public void setEventImage(String eventImage) {
        this.eventImage = eventImage;
    }
}

