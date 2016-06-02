package com.example.audakel.fammap.model;

import java.util.ArrayList;

/**
 * holds arrays of events, persons, users etc...
 *
 * Created by audakel on 5/28/16.
 */

public class FamilyMap {
    /**
     * from json import, all the events in the eb
     */
    private ArrayList<Event> events;
    /**
     * all the persons in the db
     */
    private ArrayList<Person> people;
    /**
     * all the settings and filters in a Settings object
     */
    private Settings settings;



    /**
     * Constructor that can take lists to start
     * @param events from db
     * @param people from db
     */
    public FamilyMap(ArrayList<Event> events, ArrayList<Person> people) {
        this.events = events;
        this.people = people;
    }

    /**
     * empty constructor if you want to set stuff latter
     */
    public FamilyMap() {}


    public ArrayList<Event> getEvents() {
        return events;
    }

    public void setEvents(ArrayList<Event> events) {
        this.events = events;
    }

    public ArrayList<Person> getPeople() {
        return people;
    }

    public void setPeople(ArrayList<Person> people) {
        this.people = people;
    }

    public Settings getSettings() {
        if (settings == null) settings = new Settings();

        return settings;
    }

    public void setSettings(Settings settings) {
        this.settings = settings;
    }
}
