package com.example.audakel.fammap.model;

import com.example.audakel.fammap.MySingleton;
import com.example.audakel.fammap.person.Person;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by audakel on 5/27/16.
 */
public class Event extends Searchable{
    /**
     * brief overview of the event
     */
    private String description;
    /**
     * id of person in db
     */
    private String personID;
    /**
     * city of event
     */
    private String city;
    /**
     * country of event
     */
    private String country;
    /**
     * lat for map
     */
    private double latitude;
    /**
     * lng for map
     */
    private double longitude;
    /**
     * year of event
     */
    private Integer year;

    public Event(String description, String personID, String city, String country,
                 double latitude, double longitude, Integer year)
    {
        this.description = description;
        this.personID = personID;
        this.city = city;
        this.country = country;
        this.latitude = latitude;
        this.longitude = longitude;
        this.year = year;
    }

    public int compareEvent(Event other) {
        return Integer.compare(
                Integer.valueOf(this.getYear()),
                Integer.valueOf(other.getYear())
        );
    }

    /**
     * hack to get evenst from the serach view
     */
    @Override
    public String getIdHack() {
        return "event:" + getLatitude() + "," + getLongitude();
    }

    /**
     * HACKKKK!!!!! to lazy to do something else so just multiply lat * lng to get id....
     * @return
     */
    public Double getID() {
        return getLatitude() * getLongitude();
    }

    /**
     * takes the id of the event and matches it to a person object
     * @return person
     */
    public Person getPersonByID() {
        return MySingleton.getFamilyMap().getPeopleHashMap().get(getPersonID());
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }


    @Override
    public String getTitle() {

        return getDescription() + " : " + getPersonByID().getTitle() + " : " + getCity();
    }

    public LatLng getLatLng() {
        return new LatLng(getLatitude(),getLongitude());
    }

}
