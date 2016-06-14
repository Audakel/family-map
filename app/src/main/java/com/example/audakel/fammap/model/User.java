package com.example.audakel.fammap.model;

import com.example.audakel.fammap.person.Person;

/**
 * Created by audakel on 5/28/16.
 */
public class User extends Person {
    /**
     * email of person
     */
    private String email;
    /**
     * pw to login to app and authenticate
     */
    private String password;
    /**
     * auth token to hit api
     */
    private String authorization;


    public User(String firstname, String lastname, Gender gender, String email, String password, String personId, String authorization,
                String mother, String father) {
        super(firstname, lastname, gender, personId, null, father, mother);
        this.email = email;
        this.password = password;
        this.authorization = authorization;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAuthorization() {
        return authorization;
    }

    public void setAuthorization(String authorization) {
        this.authorization = authorization;
    }
}

