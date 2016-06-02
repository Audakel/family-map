package com.example.audakel.fammap.filter;

/**
 * Created by audakel on 5/27/16.
 */
public class Filter {
    /**
     * Status (on/off)
     */
    private boolean staus;
    /**
     * Large title
     */
    private String title;
    /**
     * Description
     */
    private String description;


    public Filter(String title, String description) {
        this.staus = true;
        this.title = title;
        this.description = description;
    }

    public Filter(String title) {
        this.staus = true;
        this.title = title;
        this.description = "Filter by " + title;
    }

    public boolean isStaus() {
        return staus;
    }

    public void setStaus(boolean staus) {
        this.staus = staus;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
