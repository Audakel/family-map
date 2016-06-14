package com.example.audakel.fammap.model;

import com.example.audakel.fammap.person.Person;
import com.example.audakel.fammap.settings.Settings;

import java.util.ArrayList;
import java.util.HashMap;

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
     * Hash map of peopld id -> people for faster lookups. Lazy creator
     */
    private HashMap<String, Person> peopleHashMap;
    /**
     * Hash map of peopld id -> events for faster lookups. Lazy creator
     */
    private HashMap<String, ArrayList<Event>> eventHashMap;
    /**
     * Logged in user person obj
     */
    private Person user;
    /**
     * Hash map of mother id -> array of kids ids
     */
    private HashMap<String, ArrayList<String>> motherChildrenHashMap;


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

    /**
     * for speed purposes, it will do a lazy create of a hash map of ids -> events, if needed
     * and then use that as a lookup in the future
     *
     * @param id persons id
     * @return
     */
    public ArrayList<Event> getEventsById(String id) {
        // Make the new one if the first time
        if (getEventHashMap() == null){
            setEventHashMap(new HashMap<String, ArrayList<Event>>());

            for (int i = 0; i < getEvents().size(); i++) {
                String key = getEvents().get(i).getPersonID();
                Event value = getEvents().get(i);

                // If it has that person already, just add the event
                if (getEventHashMap().containsKey(key)){
                    getEventHashMap().get(key).add(value);
                }
                // First time for this person, add new array
                else{
                    ArrayList<Event> tempEvents = new ArrayList<>();
                    tempEvents.add(value);
                    getEventHashMap().put(key, tempEvents);
                }
            }
        }
        return getEventHashMap().get(id);
    }

    /**
     * for speed purposes, it will do a lazy create of a hash map of mother id -> array of people who
     * have that id for mother and then use that as a lookup in the future
     *
     * @param motherId mom id
     * @return list of kids ids
     */
    public ArrayList<String> getChildrenByMother(String motherId) {
        // Make the new one if the first time
        if (getMotherChildrenHashMap() == null) {
            setMotherChildrenHashMap(new HashMap<String, ArrayList<String >>());

            for (int i = 0; i < getPeople().size(); i++) {
                String key = getPeople().get(i).getMother();
                String value = getPeople().get(i).getPersonID();

                // If it has that person already, just add the event
                if (getMotherChildrenHashMap().containsKey(key)) {
                    getMotherChildrenHashMap().get(key).add(value);
                }
                // First time for this person, add new array
                else {
                    ArrayList<String> tempEvents = new ArrayList<>();
                    tempEvents.add(value);
                    getMotherChildrenHashMap().put(key, tempEvents);
                }
            }
        }
        return getMotherChildrenHashMap().get(motherId);
    }

    /**
     * Maps person id (str) to person obj
     * @return hash map
     */
    public HashMap<String, Person> getPeopleHashMap() {
        if (peopleHashMap == null){
            setPeopleHashMap(new HashMap<String, Person>());
            for (int i = 0; i < getPeople().size(); i++) {
                if (getPeople().get(i).getPersonID() == null) continue;

                String id = getPeople().get(i).getPersonID();
                Person person = getPeople().get(i);
                peopleHashMap.put(id, person);
            }
        }
        return peopleHashMap;
    }

    public void setPeopleHashMap(HashMap<String, Person> peopleHashMap) {
        this.peopleHashMap = peopleHashMap;
    }

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

    public HashMap<String, ArrayList<Event>> getEventHashMap() {
        return eventHashMap;
    }

    public void setEventHashMap(HashMap<String, ArrayList<Event>> eventHashMap) {
        this.eventHashMap = eventHashMap;
    }

    public Person getUser() {
        return user;
    }

    public void setUser(Person user) {
        this.user = user;
    }

    public HashMap<String, ArrayList<String>> getMotherChildrenHashMap() {
        return motherChildrenHashMap;
    }

    public void setMotherChildrenHashMap(HashMap<String, ArrayList<String>> motherChildrenHashMap) {
        this.motherChildrenHashMap = motherChildrenHashMap;
    }
}
