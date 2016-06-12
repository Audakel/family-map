package com.example.audakel.fammap.model;

import com.example.audakel.fammap.Constants;
import com.example.audakel.fammap.Constants.Relation;

/**
 * Created by audakel on 6/5/16.
 */
public class FamilyMember  {
    /**
     * int to map to a fam relation from constants
     */
    private Relation relation;
    /**
     * id of person
     */
    private String personId;

    /**
     * Helper obj to manage fam relations
     * @param relation
     * @param personId
     */
    public FamilyMember(Relation relation, String personId) {

        this.relation = relation;
        this.personId = personId;
    }

    public Relation getRelation() {
        return relation;
    }

    public void setRelation(Relation relation) {
        this.relation = relation;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }
}
