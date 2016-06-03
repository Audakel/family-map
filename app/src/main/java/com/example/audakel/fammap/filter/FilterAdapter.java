package com.example.audakel.fammap.filter;

/**
 * Created by audakel on 6/1/16.
 */

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.example.audakel.fammap.MySingleton;

import java.util.ArrayList;
import java.util.List;

/**
 * FilterView is responsible for translating an Filter into views to render onscreen
 */
public class FilterAdapter extends ArrayAdapter<Filter> {
    /**
     * for logging
     */
    private String TAG = getClass().getSimpleName();
    /**
     * keep track of all the switches to see what filter they corespond with
     */
    private ArrayList<Switch> switches;


    /**
     * default constructor
     * @param c
     * @param items
     */
    public FilterAdapter(Context c, List<Filter> items) {
        super(c, 0, items);
        switches = new ArrayList<>();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        FilterView filterView = (FilterView) convertView;

        if (filterView == null) {
            filterView = FilterView.inflate(parent);
        }

        filterView.setItem(getItem(position));
        filterView.getmSwitch().setTag(getItem(position).getId());
        filterView.getmSwitch().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Filter filter = getFilterByHash((double) buttonView.getTag());
                filter.setChecked(isChecked);
                Log.d(TAG, "onCheckedChanged: " + filter.getTitle() + " is " + filter.isChecked());
            }
        });
        return filterView;
    }

    /**
     * Helper that loops through all the filters and looks at the hash code of the switch tag
     * and compares it to the filters hash to find the right one
     *
     * @return filter obj
     */
    private Filter getFilterByHash(double id){
        for(Filter filter : MySingleton.getSettings().getFilters().getAllFiltersArray()) {
            if (filter.getId() == id) return filter;
        }
        return null;
    }

}
