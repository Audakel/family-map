package com.example.audakel.fammap.filter;

import java.util.Random;

/**
 * Created by audakel on 5/27/16.
 */
public class Filter {
    /**
     * Status (on/off)
     */
    private boolean checked;
    /**
     * Large title
     */
    private String title;
    /**
     * Description
     */
    private String description;
    /**
     * random id for help with ui button press
     */
    private double id = -1;


    public Filter(String title, String description) {
        this.checked = true;
        this.title = title;
        this.description = description;
    }

    public Filter(String title) {
        this.checked = true;
        this.title = title;
        this.description = "Show " + title;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
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

    public double getId() {
        if (id == -1) {
            setId(new Random().nextInt(999999));
        }

        return id;
    }

    public void setId(double id) {
        this.id = id;
    }
}
