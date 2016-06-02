package com.example.audakel.fammap.model;

import android.graphics.Color;
import android.widget.ArrayAdapter;

import com.example.audakel.fammap.filter.Filter;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by audakel on 5/30/16.
 */

/**
 * Collection of lines for easier settings than using default settings in android
 */
public class Settings {
    /**
     * Life Line Story
     *
     * Line are drawn connecting each event in a person’s life story (i.e., the person
     * associated with the selected event), ordered chronologically. (See the Person Activity
     * section for information on how events are ordered chronologically.)
     */
    private Line lifeSoryLine;
    /**
     * Family Tree Lines
     *
     * Lines linking the selected event to the person’s ancestors (i.e., the person
     * associated with the selected event) are drawn as follows:
     *  A line is drawn between the selected event and the birth event of the selected
     * person’s father. If the person’s father does not have a birth event, the earliest
     * available event for the father is used instead. If the person has no recorded
     * father, or the recorded father has no events, no line is drawn.
     *  A line is drawn between the selected event and the birth event of the selected
     * person’s mother. The same logic that applies to the father also applies to the
     * mother.
     *  Lines are drawn recursively from parents’ birth events to grandparents’ birth
     * events, from grandparents’ birth events to great grandparents’ birth events, etc.
     * including all available generations. As lines are drawn recursively up the family
     * tree, they should become progressively and noticeably thinner.
     */
    private Line familyTreeLine;
    /**
     * Spouse Lines
     *
     * A line is drawn linking the selected event to the birth event of the person’s
     * spouse (i.e., the person associated with the selected event). If there is no birth event
     * recorded for the spouse, the earliest available event for the spouse is used instead. If
     * the person has no recorded spouse, or the recorded spouse has no events, no line is
     * drawn.
     */
    private Line spouseLine;
    /**
     * all the filters
     */
    private Filters filters;



    /**
     * Map Type
     *
     * Change the map type to one of the following:
     *  Normal (Default), Hybrid, Satellite, or Terrain
     */
    private enum MapTypes {NORMAL, HYBRID, SATELLITE, TERRAIN}
    private MapTypes mapType;
    /**
     * Just a button
     */
    private Line dataResync;
    /**
     * Just a button
     */
    private Line logout;

    // Filter Settings


    public Settings() {
        this.lifeSoryLine = new Line(Color.RED, "Life Story Lines", "Show Spouse Lines");
        this.familyTreeLine = new Line(Color.GREEN, "Family Tree Lines", "Show Tree Liens");
        this.spouseLine = new Line(Color.BLUE, "Spouse Lines", "Show Spouse Lines");
        this.mapType = MapTypes.NORMAL;
        this.dataResync = new Line(-1, "Resync Data", "From Server");
        this.logout = new Line(-1, "Logout", "Returns to login screen");
    }

    public Line[] getAllSettings(){
        return new Line[] {
                getLifeSoryLine(), getFamilyTreeLine(), getSpouseLine(), getDataResync(), getLogout()
        };
    }

    public Filters getFilters() {
        if (filters == null) filters = new Filters();

        return filters;
    }

    public MapTypes getMapType() {
        return mapType;
    }

    public void setMapType(MapTypes mapType) {
        this.mapType = mapType;
    }

    public Line getLifeSoryLine() {
        return lifeSoryLine;
    }

    public void setLifeSoryLine(Line lifeSoryLine) {
        this.lifeSoryLine = lifeSoryLine;
    }

    public Line getFamilyTreeLine() {
        return familyTreeLine;
    }

    public void setFamilyTreeLine(Line familyTreeLine) {
        this.familyTreeLine = familyTreeLine;
    }

    public Line getSpouseLine() {
        return spouseLine;
    }

    public void setSpouseLine(Line spouseLine) {
        this.spouseLine = spouseLine;
    }

    public Line getDataResync() {
        return dataResync;
    }

    public void setDataResync(Line dataResync) {
        this.dataResync = dataResync;
    }

    public Line getLogout() {
        return logout;
    }

    public void setLogout(Line logout) {
        this.logout = logout;
    }

    /**
     * Subset of settings, additional filter options
     */
    public class Filters {
        /**
         * Filter by Event Type
         *
         * These filters allow the user to select one or more event types that will be
         * displayed on the map. By default, all event types are selected.
         */
        private Filter marriageEvents;
        private Filter christeningEvents;
        private Filter baptismEvents;
        private Filter censusEvents;
        private Filter birthEvents;
        private Filter deathEvents;
        /**
         * Filter by Side
         *
         * These filters allow the user to select which sides of their family will be displayed
         */
        private Filter fatherSideEvents;
        private Filter motherSideEvents;
        /**
         * Filter by Gender
         *
         * These filters allow the user to filter events based on gender. The map updates to
         * only show events associated with people of the selected genders. By default, the map
         * shows events associated with both genders.
         */
        private Filter maleEvents;
        private Filter femaleEvents;

        public Filters() {
            this.marriageEvents = new Filter("Marriage Events");
            this.christeningEvents = new Filter("Christening Events");
            this.baptismEvents = new Filter("Baptism Events");
            this.censusEvents = new Filter("Census Events");
            this.birthEvents = new Filter("Birth Events");
            this.deathEvents = new Filter("Death Events");
            this.fatherSideEvents = new Filter("Father Side Events");
            this.motherSideEvents = new Filter("Mother Side Events");
            this.maleEvents = new Filter("Male Events");
            this.femaleEvents = new Filter("Female Events");
        }

        public Filter[] getAllFilters(){
            return new Filter[] {
                    getMarriageEvents(), getChristeningEvents(), getBaptismEvents(), getCensusEvents(),
                    getBirthEvents(), getDeathEvents(), getFatherSideEvents(), getMotherSideEvents(),
                    getMaleEvents(), getFemaleEvents()
            };
        }

        public ArrayList<Filter> getAllFiltersArray(){
            return new ArrayList<>(Arrays.asList(new Filter[] {
                    getMarriageEvents(), getChristeningEvents(), getBaptismEvents(), getCensusEvents(),
                    getBirthEvents(), getDeathEvents(), getFatherSideEvents(), getMotherSideEvents(),
                    getMaleEvents(), getFemaleEvents()
            }));
        }

        public Filter getMarriageEvents() {
            return marriageEvents;
        }

        public void setMarriageEvents(Filter marriageEvents) {
            this.marriageEvents = marriageEvents;
        }

        public Filter getChristeningEvents() {
            return christeningEvents;
        }

        public void setChristeningEvents(Filter christeningEvents) {
            this.christeningEvents = christeningEvents;
        }

        public Filter getBaptismEvents() {
            return baptismEvents;
        }

        public void setBaptismEvents(Filter baptismEvents) {
            this.baptismEvents = baptismEvents;
        }

        public Filter getCensusEvents() {
            return censusEvents;
        }

        public void setCensusEvents(Filter censusEvents) {
            this.censusEvents = censusEvents;
        }

        public Filter getBirthEvents() {
            return birthEvents;
        }

        public void setBirthEvents(Filter birthEvents) {
            this.birthEvents = birthEvents;
        }

        public Filter getDeathEvents() {
            return deathEvents;
        }

        public void setDeathEvents(Filter deathEvents) {
            this.deathEvents = deathEvents;
        }

        public Filter getFatherSideEvents() {
            return fatherSideEvents;
        }

        public void setFatherSideEvents(Filter fatherSideEvents) {
            this.fatherSideEvents = fatherSideEvents;
        }

        public Filter getMotherSideEvents() {
            return motherSideEvents;
        }

        public void setMotherSideEvents(Filter motherSideEvents) {
            this.motherSideEvents = motherSideEvents;
        }

        public Filter getMaleEvents() {
            return maleEvents;
        }

        public void setMaleEvents(Filter maleEvents) {
            this.maleEvents = maleEvents;
        }

        public Filter getFemaleEvents() {
            return femaleEvents;
        }

        public void setFemaleEvents(Filter femaleEvents) {
            this.femaleEvents = femaleEvents;
        }
    }
}


