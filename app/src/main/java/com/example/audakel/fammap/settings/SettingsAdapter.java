package com.example.audakel.fammap.settings;

/**
 * Created by audakel on 6/1/16.
 */

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.example.audakel.fammap.LoginActivity;
import com.example.audakel.fammap.MySingleton;
import com.example.audakel.fammap.filter.FilterActivity;
import com.example.audakel.fammap.model.Line;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.INVISIBLE;

/**
 * SettingsView is responsible for translating an Settings into views to render onscreen
 */
public class SettingsAdapter extends ArrayAdapter<Line> {
    /**
     * for logging
     */
    private String TAG = getClass().getSimpleName();
    /**
     * keep track of all the switches to see what Settings they corespond with
     */
    private ArrayList<Switch> switches;


    /**
     * default constructor
     * @param c
     * @param items
     */
    public SettingsAdapter(Context c, List<Line> items) {
        super(c, 0, items);
        switches = new ArrayList<>();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SettingsView settingsView = (SettingsView) convertView;

        if (settingsView == null) {
            settingsView = settingsView.inflate(parent);
        }

        settingsView.setItem(getItem(position));

        // For the map!
        if (position == 3){
            settingsView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    makeMapPopUp();
                }
            });
        }

        // Hack to no set a switch / spinner for the last 2, which are buttons
        if (position > 2){
            settingsView.getmSwitch().setVisibility(INVISIBLE);
            settingsView.getmSpinner().setVisibility(INVISIBLE);

            // Logout button
            if (position == 5){
                settingsView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getContext().startActivity(new Intent(getContext(), LoginActivity.class));
                    }
                });
            }

            return settingsView;
        }

        settingsView.getmSwitch().setTag(getItem(position).getId());
        settingsView.getmSwitch().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Line lineSetting = getLineSettingsByHash((double) buttonView.getTag());
                lineSetting.setChecked(isChecked);
                Log.d(TAG, "onCheckedChanged: " + lineSetting.getTitle() + " is " + lineSetting.isChecked());
            }
        });
        return settingsView;
    }

    private void makeMapPopUp() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Type of Map");
        builder.setItems(new CharSequence[] 
                {"SATELLITE", "NORMAL", "HYBRID", "TERRAIN"},
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int position) {
                        switch (position) {
                            case 0: makeSnackBar("SATELLITE"); break;
                            case 1: makeSnackBar("NORMAL"); break;
                            case 2: makeSnackBar("HYBRID"); break;
                            case 3: makeSnackBar("TERRAIN"); break;
                        }
                        MySingleton.getSettings().getMapType().setDescription(position+"");
                    }
                });
        builder.create().show();
    }

    private void makeSnackBar( String message){
        View rootView = ((Activity) getContext()).getWindow().getDecorView().findViewById(android.R.id.content);

        Snackbar.make(rootView, "Thou hast chosen " + message, Snackbar.LENGTH_SHORT)
                .setAction("Action", null).show();
    }

    /**
     * Helper that loops through all the Settingss and looks at the hash code of the switch tag
     * and compares it to the Settingss hash to find the right one
     *
     * @return Settings obj
     */
    private Line getLineSettingsByHash(double id){
        for(Line lineSetting : MySingleton.getSettings().getAllSettingsArray()) {
            if (lineSetting.getId() == id) return lineSetting;
        }
        return null;
    }

}
