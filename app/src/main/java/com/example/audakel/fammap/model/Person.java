package com.example.audakel.fammap.model;

/**
 * Created by audakel on 5/27/16.
 */
public class Person {
    /**
     * firstname of person
     */
    private String firstname;
    /**
     * lastname of person
     */
    private String lastname;
    /**
     * gender of person
     */
    private Gender gender;
    /**
     * gender of person, must be either "m" or "f"
     */
    public enum Gender{M, F}
    /**
     * id in db of person
     */
    private String personId;

    public Person(String firstname, String lastname, Gender gender, String personId) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.gender = gender;
        this.personId = personId;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }
}
