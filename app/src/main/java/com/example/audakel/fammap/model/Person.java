package com.example.audakel.fammap.model;

import com.example.audakel.fammap.MySingleton;
import com.google.android.gms.maps.model.PolylineOptions;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.example.audakel.fammap.Constants.Relation.CHILD;
import static com.example.audakel.fammap.Constants.Relation.FATHER;
import static com.example.audakel.fammap.Constants.Relation.MOTHER;
import static com.example.audakel.fammap.Constants.Relation.SPOUSE;
import static com.example.audakel.fammap.MySingleton.*;
import static com.example.audakel.fammap.MySingleton.getSettings;

/**
 * Created by audakel on 5/27/16.
 */
public class Person extends Searchable {
    /**
     * firstName of person
     */
    private String firstName;
    /**
     * lastName of person
     */
    private String lastName;
    /**
     * gender of person
     */
    private Gender gender;



    /**
     * gender of person, must be either "m" or "f"
     */
    public enum Gender{m, f}
    /**
     * id in db of person
     */
    private String personID;
    /**
     * Id of father if available
     */
    private String father;
    /**
     * Id of mother if available
     */
    private String mother;
    /**
     * Kids
     */
    private List<String> children;
    /**
     * spouse id if available
     */
    private String spouse;
    /**
     *  List of all people in family
     */
    private List<FamilyMember> family;
    /**
     * Lines to draw on map for spouse
     */
    private PolylineOptions spouseLine;
    /**
     * Lines to draw on map for family tree
     */
    private PolylineOptions familyTreeLine;
    /**
     * Lines to draw on map for life story
     */
    private PolylineOptions lifeStoryLine;


    /**
     * This is really only called from the gson constructor
     * @param firstName
     * @param lastName
     * @param gender
     * @param personID
     */
    public Person(String firstName, String lastName, Gender gender, String personID, String spouse, String father, String mother) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.personID = personID;
        this.spouse = spouse;
        this.father = father;
        this.mother = mother;
    }

    /**
     * For search view
     * @return
     */
    @Override
    public String getTitle() {
        return getFirstName() + " " + getLastName();
    }

    /**
     * Sets up a easy list of ids, if one of the ids is null, it will not make that member
     * @return list of family members
     */
    public ArrayList<FamilyMember> generateFamily() {
        ArrayList<FamilyMember> family = new ArrayList<>();

        if (father != null) family.add(new FamilyMember(FATHER, father));
        if (mother != null) family.add(new FamilyMember(MOTHER, mother));
        if (spouse != null) family.add(new FamilyMember(SPOUSE, spouse));

        if (getChildren() == null) return family;

        for (String childId : getChildren()) {
            family.add(new FamilyMember(CHILD, childId));
        }

        return family;
    }

    /**
     * trys and gets all the kids
     * @return
     */
    public List<String> getChildren() {
        if (children == null){
            if (getMother() == null) return null;

            children = getFamilyMap().getChildrenByMother(getMother());
        }
        return children;
    }

    /**
     * Takes the persons ID and returns all the events we have on them
     */
    public ArrayList<Event> generateEvents() {
        ArrayList<Event> eventArray = getFamilyMap().getEventsById(getPersonID());

        Collections.sort(eventArray, new Comparator<Event>() {
            @Override
            public int compare(Event event1, Event event2) {
                return event1.compareEvent(event2);
            }
        });

        return eventArray;
    }

    /**
     * Helper to get a specific event, by passing the "event id" aka (lat * lng)
     */
    public Event generateEvent(Double eventId) {
        ArrayList<Event> eventArray = generateEvents();

        for (Event event : eventArray){
            if (Double.compare(event.getID(), eventId) == 0) return event;
        }

        return null;
    }


    /**
     * Looks at all the events and takes the LatLng and makes lines btwn them all
     * @return
     */
    private PolylineOptions makeLifeStoryLine() {
        PolylineOptions lifeStoryLine = new PolylineOptions();

        for(Event event : generateEvents()) {
            lifeStoryLine.add(event.getLatLng())
                    .width(10f)
                    .color(getSettings().getLifeSoryLine().getColor());
        }

        return lifeStoryLine;
    }

    /**
     * Spouse Lines
     *
     A line is drawn linking the selected event to the birth event of the person’s
     spouse (i.e., the person associated with the selected event). If there is no birth event
     recorded for the spouse, the earliest available event for the spouse is used instead. If
     the person has no recorded spouse, or the recorded spouse has no events, no line is
     drawn.

     * Looks at your spouse, if you have one it will make a PolyLine to the first event they have registered,
     * normally birth
     *
     * @param eventId that was clicked, to draw the line from
     * @return
     */
    public PolylineOptions getSpouseLine(Double eventId) {
        // Do they have a spouse
        if(spouse == null) return null;

        // Get the potential spouse events
        Person spouse = getFamilyMap().getPeopleHashMap().get(getSpouse());
        ArrayList<Event> spouseEvents = spouse.generateEvents();

        // Check if the spouse has any events
        if(spouseEvents.size() == 0) return null;

        PolylineOptions spouseLine = new PolylineOptions();
        spouseLine.add(spouseEvents.get(0).getLatLng())
                .width(10f)
                .color(getSettings().getSpouseLine().getColor());

        spouseLine.add(generateEvent(eventId).getLatLng());

        return spouseLine;
    }

    /**
     * Family Tree Lines
     *
     Lines linking the selected event to the person’s ancestors (i.e., the person
     associated with the selected event) are drawn as follows:
      A line is drawn between the selected event and the birth event of the selected
     person’s father. If the person’s father does not have a birth event, the earliest
     available event for the father is used instead. If the person has no recorded
     father, or the recorded father has no events, no line is drawn.
      A line is drawn between the selected event and the birth event of the selected
     person’s mother. The same logic that applies to the father also applies to the
     mother.
      Lines are drawn recursively from parents’ birth events to grandparents’ birth
     events, from grandparents’ birth events to great grandparents’ birth events, etc.
     including all available generations. As lines are drawn recursively up the family
     tree, they should become progressively and noticeably thinner.
     * @param eventId
     * @return
     */
    public PolylineOptions getFamilyTreeLine(Double eventId) {
        PolylineOptions familyTreeLine = new PolylineOptions();

        familyTreeLine.add(generateEvent(eventId).getLatLng())
                .color(getSettings().getFamilyTreeLine().getColor());

        //what side of the fam to show
        Boolean fatherSide = getSettings().getFilters().getFatherSideEvents().isChecked();
        Boolean motherSide = getSettings().getFilters().getMotherSideEvents().isChecked();

        // Do both sides of the family
        if (fatherSide && motherSide){
            familyTreeHelper(this, familyTreeLine, 20);
        }

        // Mother side is false, so do just father side
        if (fatherSide){
            if(this.getFather() == null) return familyTreeLine;

            Person currentPerson = getFamilyMap().getPeopleHashMap().get(getFather());
            familyTreeHelper(currentPerson, familyTreeLine, 20);

        }

        // Father side is false, so do just mother side
        if (motherSide){
            if(this.getMother() == null) return familyTreeLine;

            Person currentPerson = getFamilyMap().getPeopleHashMap().get(getMother());
            familyTreeHelper(currentPerson, familyTreeLine, 40);
        }

        return familyTreeLine;
    }

    private void familyTreeHelper(Person currentPerson, PolylineOptions familyTreeLine, float width) {

        if(currentPerson.getFather() != null) {
            Person newCurrentPerson = getFamilyMap().getPeopleHashMap().get(getFather());
            ArrayList<Event> tempEvents = newCurrentPerson.generateEvents();

            if(tempEvents.size() > 0) {
                familyTreeLine.add(tempEvents.get(0).getLatLng())
                        .width(width);

                // Recursion call
                familyTreeHelper(newCurrentPerson, familyTreeLine, width - 5);
            }
        }

        if(currentPerson.getMother() != null) {
            Person newCurrentPerson = getFamilyMap().getPeopleHashMap().get(getMother());
            ArrayList<Event> tempEvents = newCurrentPerson.generateEvents();

            if(tempEvents.size() > 0) {
                familyTreeLine.add(tempEvents.get(0).getLatLng())
                        .width(width);

                // Recursion call
                familyTreeHelper(newCurrentPerson, familyTreeLine, width - 5);
            }
        }
    }


    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }

    public String getFather() {
        return father;
    }

    public void setFather(String father) {
        this.father = father;
    }

    public String getMother() {
        return mother;
    }

    public void setMother(String mother) {
        this.mother = mother;
    }


    public void setChildren(List<String> children) {
        this.children = children;
    }

    public String getSpouse() {
        return spouse;
    }

    public void setSpouse(String spouse) {
        this.spouse = spouse;
    }

    public List<FamilyMember> getFamily() {
        if (family == null){
            setFamily(generateFamily());
        }
        return family;
    }

    public void setFamily(List<FamilyMember> family) {
        this.family = family;
    }



    public void setSpouseLine(PolylineOptions spouseLine) {
        this.spouseLine = spouseLine;
    }


    public void setFamilyTreeLine(PolylineOptions familyTreeLine) {
        this.familyTreeLine = familyTreeLine;
    }

    public PolylineOptions getLifeStoryLine() {
        if (lifeStoryLine == null){
            setLifeStoryLine(makeLifeStoryLine());
        }
        return lifeStoryLine;
    }

    public void setLifeStoryLine(PolylineOptions lifeStoryLine) {
        this.lifeStoryLine = lifeStoryLine;
    }
}
