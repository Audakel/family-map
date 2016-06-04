package com.example.audakel.fammap;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
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

import com.example.audakel.fammap.filter.FilterActivity;
import com.example.audakel.fammap.model.Event;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.WeakHashMap;

/**
 * This is the main screen. shows a map with a title bar for filtering and adding events
 */
public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {
    /** Implemnets google map api */
    private GoogleMap mMap;

    /** Package name for debug log */
    private String TAG = getClass().getSimpleName();

    /** Hash map to help map clicked markers to the events they represent */
    private WeakHashMap<Marker, Event> mMarkers;

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
        MySingleton singleton = MySingleton.getInstance(this);

        // Set up menu buttons
        menu = (FloatingActionMenu) view.findViewById(R.id.menu_fab);

        seachFAB = (FloatingActionButton) view.findViewById(R.id.fab_search);
        seachFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SearchActivity.class));
            }
        });
        settingsFAB = (FloatingActionButton) view.findViewById(R.id.fab_settings);
        settingsFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
            }
        });
        filterFAB = (FloatingActionButton) view.findViewById(R.id.fab_filter);
        filterFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), FilterActivity.class));
            }
        });

    }

    /**
     * kills the bound service so no leaks happen...
     */
    @Override
    protected void onStop() {
        super.onStop();
        unbindService(mConnection);
//        stopService(new Intent(getBaseContext(), MyService.class));
    }

    public void initMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMarkers = new WeakHashMap<Marker, Event>();
        mMap = googleMap;
        if (googleMap != null && MySingleton.getFamilyMap() != null) {
            Log.d(TAG, "onMapReady: about to add " + MySingleton.getFamilyMap().getEvents().size() + " events");

            for (Event event : MySingleton.getFamilyMap().getEvents()){
                LatLng sydney = new LatLng(event.getLatitude(), event.getLongitude());
                Marker marker = mMap.addMarker(new MarkerOptions()
                        .position(sydney)
                        .title(event.getDescription())
                        .visible(true)
                        .snippet("something about this place")
                );

                mMarkers.put(marker, event);
            }

            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    Event tempEvent = mMarkers.get(marker);
                    Log.d(TAG, "onMarkerClick: title=" + tempEvent.getDescription());
                    return true;
                }
            });
//                mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
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

                    Snackbar.make(view, "Map has been created with "
                            + MySingleton.getFamilyMap().getEvents().size() + " events and "
                            + MySingleton.getFamilyMap().getPeople().size() + " people" ,Snackbar.LENGTH_LONG)
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
     * Sets up the floating action button and menu choices
     */

}


