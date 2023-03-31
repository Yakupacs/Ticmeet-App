package com.mtaner.android_ticmeet;

import java.util.ArrayList;

public class Category {
    private String eventCategory;
    private ArrayList<Event> eventArrayList;

    // creating a constructor.
    public Category(String eventCategory, ArrayList<Event> eventArrayList) {
        this.eventCategory = eventCategory;
        this.eventArrayList = eventArrayList;

    }


    public String getEventCategory() {
        return eventCategory;
    }

    public void setEventCategory(String eventCategory) {
        this.eventCategory = eventCategory;
    }


    public ArrayList<Event> getEventArrayList() {
        return eventArrayList;
    }

    public void setEventArrayList(ArrayList<Event> eventArrayList) {
        this.eventArrayList = eventArrayList;
    }
}
