package com.example.audakel.fammap.model;

import com.example.audakel.fammap.filter.Filter;

/**
 * Created by audakel on 5/30/16.
 */
public class Line extends Filter {
    /**
     * Color of your line
     */
    private int color;


    public Line( int color, String title, String description) {
        super(title, description);
        this.color = color;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }


}
