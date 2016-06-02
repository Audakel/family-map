package com.example.audakel.fammap.filter;

import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.audakel.fammap.MySingleton;
import com.example.audakel.fammap.model.Settings;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by audakel on 6/1/16.
 */
public class FilterListFragment extends ListFragment{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);
        FilterAdapter filterAdapter = new FilterAdapter(getActivity(), MySingleton.getSettings().getFilters().getAllFiltersArray());
        setListAdapter(filterAdapter);

        return v;
    }
}
