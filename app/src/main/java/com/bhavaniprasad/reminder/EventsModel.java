package com.bhavaniprasad.reminder;

public class EventsModel {
    public int id;
    public String name,description,date;


    public EventsModel(int id, String name, String description, String date) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public String getDate() {
        return date;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }
}
