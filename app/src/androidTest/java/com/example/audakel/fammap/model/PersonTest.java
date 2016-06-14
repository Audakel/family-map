package com.example.audakel.fammap.model;

import android.test.AndroidTestCase;

import com.example.audakel.fammap.MainActivity;
import com.example.audakel.fammap.MySingleton;
import com.example.audakel.fammap.filter.Filter;
import com.example.audakel.fammap.person.Person;
import com.example.audakel.fammap.settings.Settings;
import com.example.audakel.fammap.settings.SettingsActivity;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by audakel on 6/13/16.
 */

public class PersonTest extends AndroidTestCase {
    MainActivity mainActivity;

    Person father;
    Person mother;
    Person child;
    Settings settings;
    Event personEvent1;
    Event personEvent2;
    Event personEvent3;


    public void setUp() throws Exception {
        super.setUp();

        Gson gson = new Gson();
        String fatherString = "\"descendant\":\"bob\",\"personID\":\"58md7s87-u7fu-f244-5vqr-8nh51a87i6k7\",\"firstName\":\"Lee\",\"lastName\":\"Nibert\",\"gender\":\"m\",\"spouse\":\"406nv551-8957-l2y5-315g-08se07u4p12i\"";
        String motherString = "\"descendant\":\"bob\",\"personID\":\"406nv551-8957-l2y5-315g-08se07u4p12i\",\"firstName\":\"Alpha\",\"lastName\":\"Tu\",\"gender\":\"f\",\"spouse\":\"58md7s87-u7fu-f244-5vqr-8nh51a87i6k7\"";
        String childString = "\"descendant\":\"bob\",\"personID\":\"dtshj47r-u8b2-ofx2-1su5-w02962md7fyd\",\"firstName\":\"Freddie\",\"lastName\":\"Mcpeek\",\"gender\":\"m\",\"father\":\"58md7s87-u7fu-f244-5vqr-8nh51a87i6k7\",\"mother\":\"406nv551-8957-l2y5-315g-08se07u4p12i\",\"spouse\":\"48xmrser-b4lq-u1n2-20m7-6819m7sw6508\"";

        String personString1 = "\"eventID\":\"3gp20q3c-2966-64t5-m6wr-3cs55i809683\",\"personID\":\"58md7s87-u7fu-f244-5vqr-8nh51a87i6k7\",\"latitude\":18.4333,\"longitude\":-63.3833,\"country\":\"United Kingdom\",\"city\":\"Road Town\",\"description\":\"birth\",\"year\":\"1981\",\"descendant\":\"bob\"";
        String personString2 = "\"eventID\":\"7639e8u6-58hc-yvd2-o4f8-h35je27q10bj\",\"personID\":\"58md7s87-u7fu-f244-5vqr-8nh51a87i6k7\",\"latitude\":18.4333,\"longitude\":-63.3833,\"country\":\"United Kingdom\",\"city\":\"Road Town\",\"description\":\"baptism\",\"year\":\"1982\",\"descendant\":\"bob\"";
        String personString3 = "\"eventID\":\"3gp20q3c-2966-64t5-m6wr-3cs55i809683\",\"personID\":\"58md7s87-u7fu-f244-5vqr-8nh51a87i6k7\",\"latitude\":18.4333,\"longitude\":-63.3833,\"country\":\"United Kingdom\",\"city\":\"Road Town\",\"description\":\"death\",\"year\":\"1983\",\"descendant\":\"bob\"";
        
        personEvent1 = gson.fromJson(personString1, Event.class);
        personEvent2 = gson.fromJson(personString2, Event.class);
        personEvent3 = gson.fromJson(personString3, Event.class);

        father = gson.fromJson(fatherString, Person.class);
        mother = gson.fromJson(motherString, Person.class);
        child = gson.fromJson(childString, Person.class);

        settings = new Settings();
    }

    public void tearDown() throws Exception {

    }


    public void testParsePerson() throws Exception {
        assertEquals(father.getLastName(), "Lee");
        assertEquals(mother.getPersonID(), "58md7s87-u7fu-f244-5vqr-8nh51a87i6k7");
        assertEquals(child.getFirstName(), "Freddie");
        assertEquals(father.getLastName(), "Nibert");
        assertEquals(mother.getGender(), Person.Gender.f);
        assertEquals(child.getFather(), "7hbh61c0-ou18-x6eg-4bwi-8q18m14yg2b7");
        assertEquals(father.getSpouse(), "406nv551-8957-l2y5-315g-08se07u4p12i");
    }

    public void testSettings() throws Exception{
        assertNotNull(settings);
        assertEquals(settings.getMapType(), 1);
        settings.getFilters().setBaptismEvents(new Filter("Baptism", "Baptism"));
        assertTrue(settings.getFilters().getBaptismEvents().isChecked());
        settings.getFilters().getBaptismEvents().setChecked(false);
        assertFalse(settings.getFilters().getBaptismEvents().isChecked());
    }

    public void testChronological() throws Exception {
        ArrayList<Event> events = father.generateEvents();
        assertEquals(events.get(0).getYear(), Integer.valueOf(1981));
        assertEquals(events.get(2).getYear(), Integer.valueOf(1983));
        assertEquals(events.get(0).getDescription(), "birth");
        assertEquals(events.get(2).getDescription(), "death");

    }


}