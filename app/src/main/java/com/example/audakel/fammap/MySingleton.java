package com.example.audakel.fammap;

/**
 * Created by audakel on 5/27/16.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.example.audakel.fammap.model.Event;
import com.example.audakel.fammap.model.FamilyMap;
import com.example.audakel.fammap.model.Person;
import com.example.audakel.fammap.model.Settings;
import com.example.audakel.fammap.model.User;

import java.net.URL;
import java.util.ArrayList;

import static com.example.audakel.fammap.Constants.SHARAED_PREFS_BASE;

/**
 * Holds static reference to all the fam map info
 */
public class MySingleton {
    /**
     * instance that everyone will have access to
     */
    private static MySingleton instance;
    /**
     * instance of the familyMap that holds all the people and events of the user
     */
    private static FamilyMap familyMap;
    /**
     * who the currently logged in user is - user object
     */
    private static User user;
    /**
     * context if needed
     */
    private Context context;
    /**
     * helper accesor/mutator for Settings
     */
    private Settings settings;
    /**
     * helper accesor/mutator for events
     */
    private ArrayList<Event> events;
    /**
     * helper accesor/mutator for People
     */
    private ArrayList<Person> people;


    /**
     * constructor that will look at shared prefs for the logged in user and grab his data
     * to init the models
     */
    private MySingleton() {
//        setUser(currentUser());
        familyMap = new FamilyMap();
    }


    /**
     * looks at shared prefs and gets user info
     * @return current user
     */
//    private User currentUser() {
//        SharedPreferences prefs = context.getSharedPreferences("general_settings", Context.MODE_PRIVATE);
//        return new User(
//                "whoami", "whoamiL", Person.Gender.M, "@", "pw",
//                prefs.getString(SHARAED_PREFS_BASE + Constants.PERSON_ID, Constants.MISSING_PREF),
//                prefs.getString(SHARAED_PREFS_BASE + Constants.USER_AUTH, Constants.MISSING_PREF)
//        );
//    }

    /**
     * gets an instance or creates it if needed
     * @return
     */
    public static MySingleton getInstance(Context context) {

        if (instance == null) instance = new MySingleton();

        return instance;
    }


    public void customSingletonMethod() {
        // Custom method
    }

    public static void setInstance(MySingleton instance) {
        MySingleton.instance = instance;
    }

    public static FamilyMap getFamilyMap() {
        return familyMap;
    }

    public static void setFamilyMap(FamilyMap familyMap) {
        MySingleton.familyMap = familyMap;
    }

    public static User getUser() {
        return user;
    }

    public static void setUser(User user) {
        MySingleton.user = user;
    }

    public static Settings getSettings() {
        return getFamilyMap().getSettings();
    }

    public void setSettings(Settings settings) {
        getFamilyMap().setSettings(settings);
    }

    public ArrayList<Event> getEvents() {
        return getFamilyMap().getEvents();
    }

    public void setEvents(ArrayList<Event> events) {
        getFamilyMap().setEvents(events);
    }

    public ArrayList<Person> getPeople() {
        return getFamilyMap().getPeople();
    }

    public void setPeople(ArrayList<Person> people) {
        getFamilyMap().setPeople(people);
    }
}
