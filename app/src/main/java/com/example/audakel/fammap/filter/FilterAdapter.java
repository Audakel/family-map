package com.example.audakel.fammap.filter;

/**
 * Created by audakel on 6/1/16.
 */

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

/**
 * FilterView is responsible for translating an Filter into views to render onscreen
 */
public class FilterAdapter extends ArrayAdapter<Filter> {

    public FilterAdapter(Context c, List<Filter> items) {
        super(c, 0, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        FilterView filterView = (FilterView) convertView;
        if (filterView == null)
            filterView = FilterView.inflate(parent);

        filterView.setItem(getItem(position));
        return filterView;
    }
}
