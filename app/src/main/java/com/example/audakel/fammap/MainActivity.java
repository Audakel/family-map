package com.example.audakel.fammap;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.audakel.fammap.filter.FilterActivity;
import com.example.audakel.fammap.model.Event;
import com.example.audakel.fammap.model.Person;
import com.example.audakel.fammap.person.PersonActivity;
import com.example.audakel.fammap.search.SearchActivity;
import com.example.audakel.fammap.settings.Settings;
import com.example.audakel.fammap.settings.SettingsActivity;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.example.audakel.fammap.Constants.BAPTISM;
import static com.example.audakel.fammap.Constants.BASE_URL;
import static com.example.audakel.fammap.Constants.BIRTH;
import static com.example.audakel.fammap.Constants.CENSUS;
import static com.example.audakel.fammap.Constants.CHRISTENING;
import static com.example.audakel.fammap.Constants.DEATH;
import static com.example.audakel.fammap.Constants.EVENT_INTENT_LATLNG;
import static com.example.audakel.fammap.Constants.MARRIAGE;
import static com.example.audakel.fammap.Constants.MISSING_PREF;
import static com.example.audakel.fammap.Constants.SHARAED_PREFS_BASE;
import static com.example.audakel.fammap.Constants.ZOOM_LATLNG;
import static com.example.audakel.fammap.MySingleton.*;

/**
 * This is the main screen. shows a map with a title bar for filtering and adding events
 */
public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {
    /** Implemnets google map api */
    private GoogleMap mMap;

    /** Package name for debug log */
    private String TAG = getClass().getSimpleName();

    /** Messenger for communicating with the service. */
    Messenger mService = null;

    /** Flag indicating whether we have called bind on the service. */
    boolean mBound;
    /**
     * View for snackbars to access
     */
    private View view;
    /**
     * floating action bar menu
     */
    private FloatingActionMenu menu;
    /**
     * menu button for search activity
     */
    private FloatingActionButton seachFAB;
    /**
     * menu button for settings activity
     */
    private FloatingActionButton settingsFAB;
    /**
     * menu button for filter activity
     */
    private FloatingActionButton filterFAB;
    /**
     * Get singleton that will hold all app references to things we need
     */
    private MySingleton singleton;
    /**
     * Keeps track of the last person associated with the last clicked event
     */
    private Person lastPersonClicked = null;
    /**
     * Used when coming back from a person activity, to focus the map correctly
     */
    private LatLng focusEvent = null;
    /**
     * Keep track of all the map lines, so we can clear them
     */
    List<Polyline> polylines = new ArrayList<Polyline>();


    /**
     * Map types
     */
    private final int[] MAP_TYPES = {
            GoogleMap.MAP_TYPE_SATELLITE,
            GoogleMap.MAP_TYPE_NORMAL,
            GoogleMap.MAP_TYPE_HYBRID,
            GoogleMap.MAP_TYPE_TERRAIN,
            GoogleMap.MAP_TYPE_NONE
    };

    private int curMapTypeIndex;

    /**
     * UI views for updating text
     */
    private TextView detailTextView;
    private TextView detailTextView2;
    private TextView detailTextView3;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        view = findViewById(android.R.id.content);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Get service going that will talk to the db server
        // Bind to the service
        bindService(new Intent(this, MyService.class), mConnection, Context.BIND_AUTO_CREATE);

        // Get singleton that will hold all app references to things we need
        singleton = getInstance(this);

        // Set up menu buttons
        menu = (FloatingActionMenu) view.findViewById(R.id.menu_fab);

        seachFAB = (FloatingActionButton) view.findViewById(R.id.fab_search);
        seachFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menu.close(true);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(getApplicationContext(), SearchActivity.class));
                    }
                }, 250);
            }
        });
        settingsFAB = (FloatingActionButton) view.findViewById(R.id.fab_settings);
        settingsFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menu.close(true);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
                    }
                }, 250);
            }
        });
        filterFAB = (FloatingActionButton) view.findViewById(R.id.fab_filter);
        filterFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menu.close(true);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(getApplicationContext(), FilterActivity.class));
                    }
                }, 250);
            }
        });

        // Set up UI text display for detail view
        detailTextView = (TextView) findViewById(R.id.detail_message_1);

        // Set the onclick to send the person to a person view
        detailTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lastPersonClicked == null) return;

                Intent intent = new Intent(getApplicationContext(), PersonActivity.class);
                intent.putExtra(Constants.PERSON_INTENT_ID, lastPersonClicked.getPersonID());
                startActivity(intent);
            }
        });

    }

    /**
     * kills the bound service so no leaks happen...
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mBound)  {
            unbindService(mConnection);
        }
    }

    public void initMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        singleton.setmMarkers(new HashMap<Marker, Event>());
        mMap = googleMap;
        if (googleMap != null && getFamilyMap() != null) {
            Log.d(TAG, "onMapReady: about to add " + getFamilyMap().getEvents().size() + " events");

            for (Event event : getFamilyMap().getEvents()){
                LatLng latLng = new LatLng(event.getLatitude(), event.getLongitude());
                Marker marker = mMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title(event.getDescription())
                        .visible(true)
                        .icon(iconColorByEvent(event.getDescription()))
                        .snippet(event.getCity() + ", "  + event.getCountry())
                );

                singleton.getMarkers().put(marker, event);
            }

            // Storing the map int as a string in the description spot
            curMapTypeIndex = Integer.valueOf(getSettings().getMapType().getDescription());
            mMap.setMapType( MAP_TYPES[curMapTypeIndex] );

            // When a marker is clicked, we need to show info in the info box, pop up a display msg and draw lines
            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    Event tempEvent = singleton.getMarkers().get(marker);
                    if (tempEvent == null) return false;

                    lastPersonClicked = tempEvent.getPersonByID();
                    detailTextView.setText(lastPersonClicked.getFirstName() +" " + lastPersonClicked.getLastName() +
                            " was " + getDescriptionModifier(tempEvent.getDescription()) + " here.");

                    Log.d(TAG, "onMarkerClick: title=" + tempEvent.getDescription() + " " + tempEvent.getPersonByID().getPersonID());

                    drawLines(mMap, tempEvent);
                    return  false;
                }
            });
//                mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        }

    }

    /**
     * Takes a map and then looks at the clicked marker + settings to draw all the needed lines.... hopefully :)
     * @param mMap
     */
    private void drawLines(GoogleMap mMap, Event event) {
        // Crashes on:
        // title=birth 37l68a8o-0324-a8p7-vl9p-8qh2h77u23j7

        // Clear the map
        for (Polyline polyine : polylines){
            polyine.remove();
        }
        polylines.clear();

        // Should we draw family tree lines? Don't worry about what side of the fam to draw on
        if (getSettings().getFamilyTreeLine().isChecked()){
            PolylineOptions lines = event.getPersonByID().getFamilyTreeLine(event.getID());
            polylines.add(mMap.addPolyline(lines));

        }

        // Should we draw the life story line?
        if (getSettings().getLifeSoryLine().isChecked()){
            PolylineOptions lines = event.getPersonByID().getLifeStoryLine();
            polylines.add(mMap.addPolyline(lines));
        }

        // Should we draw the spouse line?
        if (getSettings().getSpouseLine().isChecked()){
            PolylineOptions lines = event.getPersonByID().getSpouseLine(event.getID());
            polylines.add(mMap.addPolyline(lines));
        }

    }


    // Menu stuff
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
//        if (id == R.id.action_settings) {
//            return true;
//        }
        return super.onOptionsItemSelected(item);
    }



    // Binder stuff for service
    /**
     * Class for interacting with the main interface of the service.
     */
    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            // This is called when the connection with the service has been
            // established, giving us the object we can use to
            // interact with the service.  We are communicating with the
            // service using a Messenger, so here we get a client-side
            // representation of that from the raw IBinder object.
            mService = new Messenger(service);
            mBound = true;

            messageToService(MyService.MSG_REGISTER_CLIENT);
            messageToService(MyService.MSG_INIT_DATA);
        }

        public void onServiceDisconnected(ComponentName className) {
            // This is called when the connection with the service has been
            // unexpectedly disconnected -- that is, its process crashed.
            mService = null;
            mBound = false;
        }
    };

    /**
     * Handler of incoming messages from service.
     */
    class IncomingHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MyService.DATA_LOADED:
                    Log.d(TAG, "handleMessage: from service, data loaded");

                    Snackbar.make(view, "Hi Bob Bob! Map has been created with "
                            + getFamilyMap().getEvents().size() + " events and "
                            + getFamilyMap().getPeople().size() + " people" ,Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();

                    initMap();
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }

    /**
     * Target we publish for clients to send messages to IncomingHandler.
     */
    final Messenger mMessenger = new Messenger(new IncomingHandler());

    /**
     * This is called once we have a connection to our service. It will contact the remote server
     * and get the initial values of people and events
     */
    public void messageToService(int message) {
        if (!mBound) return;

        // Create and send a message to the service, using a supported 'what' value
        Message msg = Message.obtain(null, message, 0, 0);
        msg.replyTo = mMessenger;
        try {
            mService.send(msg);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }


    /**
     * Refreshes the map from setting. Loops through each icon and will update the visibilty
     * based on settings
     * 
     */
    private void updateMapFromSettings(){

        Iterator it = singleton.getMarkers().entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            Marker tmpMarker = (Marker) pair.getKey();
            Event tmpEvent = (Event) pair.getValue();
            Settings.Filters filters = getSettings().getFilters();

            switch (tmpEvent.getDescription()){
                case MARRIAGE: tmpMarker.setVisible(filters.getMarriageEvents().isChecked());break;
                case CHRISTENING: tmpMarker.setVisible(filters.getChristeningEvents().isChecked());break;
                case BAPTISM: tmpMarker.setVisible(filters.getBaptismEvents().isChecked());break;
                case CENSUS: tmpMarker.setVisible(filters.getCensusEvents().isChecked());break;
                case BIRTH: tmpMarker.setVisible(filters.getBirthEvents().isChecked());break;
                case DEATH: tmpMarker.setVisible(filters.getDeathEvents().isChecked());break;
            }
        }

        // Storing the map int as a string in the description spot
        curMapTypeIndex = Integer.valueOf(getSettings().getMapType().getDescription());
        mMap.setMapType( MAP_TYPES[curMapTypeIndex] );

        // Hide the "get turns to this location" button
        mMap.getUiSettings().setMapToolbarEnabled(false);

        // Check for if a place to zoom exists
        LatLng zoomPoint = zoomEvent();
        if (zoomPoint == null) return;

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(zoomPoint) // Center Set
                .zoom(10.0f)                // Zoom
                .bearing(90)                // Orientation of the camera to east
                .tilt(30)                   // Tilt of the camera to 30 degrees
                .build();                   // Creates a CameraPosition from the builder
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    private LatLng zoomEvent() {
        SharedPreferences prefs = getApplication().getSharedPreferences(SHARAED_PREFS_BASE, Context.MODE_PRIVATE);
        String latLng = prefs.getString(SHARAED_PREFS_BASE + ZOOM_LATLNG, MISSING_PREF);

        // Clear point from mem
        prefs.edit()
                .putString(SHARAED_PREFS_BASE + ZOOM_LATLNG, MISSING_PREF)
                .apply();

        if (latLng.equals(MISSING_PREF)) return null;

        return new LatLng(Double.valueOf(latLng.split(",")[0]), Double.valueOf(latLng.split(",")[1]));
    }

    /**
     * helps get the map icon color
     * @param description what type of event
     * @return a color for the icon
     */
    private BitmapDescriptor iconColorByEvent(String description) {
        switch (description){
            case MARRIAGE: return BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE);
            case CHRISTENING: return BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA);
            case BAPTISM: return BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW);
            case CENSUS: return BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN);
            case BIRTH: return BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET);
            case DEATH: return BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE);
        }
        return null;
    }

    /**
     * funny class to get "what happened here". ie birth event -> "was {birthed} here"
     * @param description
     * @return funny modifier
     */
    private String getDescriptionModifier(String description) {
        switch (description){
            case MARRIAGE: return "given in arraigned marriage";
            case CHRISTENING: return "given a Christian name at baptism as a sign of admission to a Christian Church";
            case BAPTISM: return "submerged completely under the water";
            case CENSUS: return "censied";
            case BIRTH: return "birthed";
            case DEATH: return "deathed";
        }
        return null;
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (singleton.getMarkers() == null) return;

        updateMapFromSettings();
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
        if (requestCode == 1) {
            if(requestCode == RESULT_OK) {
//                String myStr=data.getStringExtra(EVENT_INTENT_LATLNG);
//                focusEvent = new LatLng(Double.valueOf(myStr.split(",")[0]), Double.valueOf(myStr.split(",")[1]));
            }
        }
    }


}


