package com.example.audakel.fammap.settings;

import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.audakel.fammap.MySingleton;

/**
 * Created by audakel on 6/1/16.
 */
public class SettingsListFragment extends ListFragment{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);
        SettingsAdapter settingsAdapter = new SettingsAdapter(getActivity(),
                MySingleton.getSettings().getAllSettingsArray());
        setListAdapter(settingsAdapter);

        return v;
    }
}
