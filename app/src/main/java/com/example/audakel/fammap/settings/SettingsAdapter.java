package com.example.audakel.fammap.settings;

/**
 * Created by audakel on 6/1/16.
 */

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
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
import com.example.audakel.fammap.MainActivity;
import com.example.audakel.fammap.MyService;
import com.example.audakel.fammap.MySingleton;
import com.example.audakel.fammap.filter.FilterActivity;
import com.example.audakel.fammap.model.Line;
import com.example.audakel.fammap.search.SearchActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.view.View.INVISIBLE;
import static com.example.audakel.fammap.Constants.BASE_URL;
import static com.example.audakel.fammap.Constants.EVENTS_API;
import static com.example.audakel.fammap.Constants.MISSING_PREF;
import static com.example.audakel.fammap.Constants.PEOPLE_API;
import static com.example.audakel.fammap.Constants.SHARAED_PREFS_BASE;
import static com.example.audakel.fammap.Constants.USER_AUTH;

/**
 * SettingsView is responsible for translating an Settings into views to render onscreen
 */
public class SettingsAdapter extends ArrayAdapter<Line> {
    private final Context context;
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
        this.context = c;
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

            // Reset button
            if (position == 4){
                settingsView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        AsyncTask.execute(new Runnable() {
                            @Override
                            public void run() {
                                request(EVENTS_API, null);
                                request(PEOPLE_API, null);

                            }
                        });

                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                ((SettingsActivity)context).onBackPressed();

                            }
                        }, 2000);



                    }
                });
            }

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


    private JSONObject request(String endpoint, String json)  {
        Response response = null;
        Request request = null;
        String url = getBaseUrl() + endpoint;
        OkHttpClient client = new OkHttpClient();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");

        if (json == null){
            request = new Request.Builder()
                    .url(url)
                    .addHeader(USER_AUTH, getAuthorization())
                    .build();
        }
        else{
            RequestBody body = RequestBody.create(JSON, json);
            request = new Request.Builder()
                    .url(url)
                    .addHeader(USER_AUTH, getAuthorization())
                    .post(body)
                    .build();
        }
        try {
            response = client.newCall(request).execute();
            Log.d(TAG, "post: endpoint=" + endpoint + " res=" + response);
            return new JSONObject(response.body().string());
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public String getBaseUrl() {
        SharedPreferences prefs = context.getSharedPreferences(SHARAED_PREFS_BASE, Context.MODE_PRIVATE);
        return prefs.getString(SHARAED_PREFS_BASE + BASE_URL, MISSING_PREF);
    }

    public String getAuthorization() {
//        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences prefs = context.getSharedPreferences(SHARAED_PREFS_BASE, Context.MODE_PRIVATE);

        return prefs.getString(SHARAED_PREFS_BASE + USER_AUTH, MISSING_PREF);

        // TODO:: fix shared prefs
//        return "id3x7199-tw54-c";
    }

}
