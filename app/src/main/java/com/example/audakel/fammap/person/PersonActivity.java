package com.example.audakel.fammap.person;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.audakel.fammap.Constants;
import com.example.audakel.fammap.R;
import com.example.audakel.fammap.model.Event;
import com.example.audakel.fammap.model.FamilyMember;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import static android.widget.ExpandableListView.*;
import static com.example.audakel.fammap.Constants.SHARAED_PREFS_BASE;
import static com.example.audakel.fammap.MySingleton.*;

public class PersonActivity extends AppCompatActivity {
    /**
     * Person obj that we are displaying
     */
    private Person person;

    /**
     * Text views for the fname
     */
    private TextView fname;
    /**
     * Text views for the gender
     */
    private TextView gender;
    /**
     * Text views for the lname
     */
    private TextView lname;
    /**
     * used to send back an event to the main activity
     */
    private String eventLatLong;

    ArrayList<Event> events;
    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        toolbar.setNavigationIcon(R.drawable.ic_keyboard_backspace_white_24dp);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });





        final String person_id = getIntent().getStringExtra(Constants.PERSON_INTENT_ID);
        person = getFamilyMap().getPeopleHashMap().get(person_id);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, person.getPersonID(), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        // Find all the views
        fname = (TextView) findViewById(R.id.firstNameTextView);
        lname = (TextView) findViewById(R.id.lastNameTextView);
        gender = (TextView) findViewById(R.id.genderTextView);

        // Hook the text into the views
        fname.setText(person.getFirstName());
        lname.setText(person.getLastName());
        gender.setText(person.getGender().name());

        // get the listview
        expListView = (ExpandableListView) findViewById(R.id.lvExp);

        // Listview on child click listener
        expListView.setOnChildClickListener(new OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                if (groupPosition == 0) {
                    Toast.makeText(
                            getApplicationContext(),
                            listDataHeader.get(groupPosition)
                                    + " : "
                                    + listDataChild.get(
                                    listDataHeader.get(groupPosition)).get(
                                    childPosition), Toast.LENGTH_SHORT)
                            .show();
                    eventLatLong = events.get(childPosition).getLatitude() + "," + events.get(childPosition).getLongitude();
                    writeToSharedPrefs(eventLatLong);
                    onBackPressed();
                    return false;
                }

                else {
                    Intent intent = new Intent(getApplicationContext(), PersonActivity.class);
                    String personId = person.getFamily().get(childPosition).getPersonId();
                    intent.putExtra(Constants.PERSON_INTENT_ID, personId);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    return false;
                }
            }

        });

        // preparing list data
        prepareListData();

        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);

    }

    /**
     * takes the persons id and hacks it into shared prefs. this will let main activity map zoom correctly
     * on the right event
     *
     * @param value
     */
    private void writeToSharedPrefs(String value){
        SharedPreferences sharedPref = getBaseContext().getSharedPreferences(SHARAED_PREFS_BASE,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(SHARAED_PREFS_BASE + Constants.ZOOM_LATLNG, value);
        editor.commit();
    }


    /**
    * Preparing the list data by passing the id back to our "database" helper
    */
    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        // Adding header data
        listDataHeader.add("_event");
        listDataHeader.add("_fam");


        // Take array of Events and change them to sorted string array
        events = getFamilyMap().getEventsById(person.getPersonID());

        // Adding child data for events
        List<String> stringEvents = prepEventStringArray(events);


        // Take array of Family peps and change them to sorted string array
        ArrayList<FamilyMember> family = person.generateFamily();

        // Adding child data for fam
        List<String> stringFamily = prepFamilyStringArray(family);
//
        listDataChild.put(listDataHeader.get(0), stringEvents); // Header, Child data
        listDataChild.put(listDataHeader.get(1), stringFamily);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra(Constants.EVENT_INTENT_LATLNG, eventLatLong);
        setResult(1, intent);
        finish();
    }


    /**
     * Takes a list of the famMembers objs that make up a fam and formats them to correct string
     * for display
     *
     * @param familyMembers array list of people that make up fam
     * @return array of strings to display
     */
    private List<String> prepFamilyStringArray(ArrayList<FamilyMember> familyMembers){
        List<String> people = new ArrayList<>();

        for (FamilyMember familyMember : familyMembers){
            // Look up a person by famMember id and add to array
            Person person = getFamilyMap().getPeopleHashMap().get(familyMember.getPersonId());
            people.add(familyMember.getRelation().name() + " : " + person.getTitle());
        }

        return people;

    }


    /**
     * Takes a list of the event objs that make up a life and formats them to correct string
     * for display
     *
     * @param events array list of events that make up life
     * @return array of strings to display
     */
    private List<String> prepEventStringArray(ArrayList<Event> events) {
        List<String> stringEvents = new ArrayList<>();

        // Sort the events
        Collections.sort(events, new Comparator<Event>() {
            @Override
            public int compare(Event event1, Event event2) {
                return event1.compareEvent(event2);
            }
        });

        for (int i = 0; i < events.size(); i++) {
            Event e = events.get(i);
            stringEvents.add(e.getDescription() + " " + e.getYear());
        }

        return stringEvents;
    }

}
