package com.example.audakel.fammap;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

import com.example.audakel.fammap.model.Event;
import com.example.audakel.fammap.model.Person;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.example.audakel.fammap.Constants.*;

/**
 * Created by audakel on 5/28/16.
 */
public class MyService extends IntentService {
    public static final int DATA_LOADED = 12;
    /**
     * for logging
     */
    private final String TAG = this.getClass().getSimpleName();
    /**
     * auth token from shared prefs
     */
    private String authorization;
    /**
     * Command to the service load data
     * */
    public static final int MSG_INIT_DATA = 11;
    /**
     * Keeps track of current registered client.
     * */
     Messenger mClient;
    /**
     * makes things run on background threads so as to not make ui unresponsive
     */
    private HandlerThread mHandlerThread;
    /**
     * handler for messages
     */
    private Handler mHandler;
    /**
     * Command to the service to register a client, receiving callbacks
     * from the service.  The Message's replyTo field must be a Messenger of
     * the client where callbacks should be sent.
     */
    public static final int  MSG_REGISTER_CLIENT = 2;
    /**
     * Target we publish for clients to send messages to IncomingHandler.
     */
    private Messenger mMessenger;


    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     *   to name the worker thread, important only for debugging.
     */
    public MyService() {
        super("MyService");
    }


    /**
     * Creades the background thread and the handler for messages from service
     */
    @Override
    public void onCreate() {
        super.onCreate();
        mHandlerThread = new HandlerThread("MyService");
        mHandlerThread.start();
        mHandler = new Handler(mHandlerThread.getLooper()){
            @Override
            public void handleMessage(Message msg)
            {
                switch (msg.what) {
                    case MSG_INIT_DATA:
                        MySingleton.getInstance(getApplicationContext()).getFamilyMap().setEvents(syncEvents());
                        MySingleton.getInstance(getApplicationContext()).getFamilyMap().setPeople(syncPeople());
                        Message message = Message.obtain(null, DATA_LOADED, 0, 0);
                        try {
                            mMessenger.send(message);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                        break;

                    case MSG_REGISTER_CLIENT:
                        mMessenger = msg.replyTo;

                    default:
                        super.handleMessage(msg);
                }
            }
        };

        mMessenger = new Messenger(mHandler);
        Log.d(TAG, "onCreate: ");
    }


    /**
     * When binding to the service, we return an interface to our messenger
     * for sending messages to the service.
     */
    @Override
    public IBinder onBind(Intent intent) {
        return mMessenger.getBinder();
    }

    // Not used
    protected void onHandleIntent(Intent intent) {}

    /**
     * gets all the people in the db
     * @return list of people
     */
    private ArrayList<Person> syncPeople() {
        ArrayList<Person> people = new ArrayList<>();
        Gson gson = new Gson();

        try {
            JSONArray json = request(PEOPLE_API, null).getJSONArray("data");

            for (int i = 0; i < json.length(); i++) {
                Person person = gson.fromJson(json.getJSONObject(i).toString(), Person.class);
                people.add(person);
            }
            Log.d(TAG, "syncPeople: added "+ people.size()+ " people");
            return people;

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;

    }


    /**
     * Calls server and gets all the events
     * @return all the events for a person
     */
    private ArrayList<Event> syncEvents() {
        ArrayList<Event> events = new ArrayList<>();
        Gson gson = new Gson();

        try {
            JSONArray json = request(EVENTS_API, null).getJSONArray("data");

            for (int i = 0; i < json.length(); i++) {
                Event event = gson.fromJson(json.getJSONObject(i).toString(), Event.class);
                events.add(event);
            }
            Log.d(TAG, "syncEvents: added "+ events.size()+ " events");
            return events;

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * helper to GET/POST to our server.
     * @param endpoint defaults to include the base, just add the endpoint
     * @param json body to send for POST, or null for GET
     * @return json from server
     * @throws IOException
     */
    private JSONObject request(String endpoint, String json)  {
        Response response = null;
        Request request = null;
        String url = BASE_URL + endpoint;
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



    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "starting background service to talk to server", Toast.LENGTH_SHORT).show();
        return super.onStartCommand(intent,flags,startId);
    }

    public String getTAG() {
        return TAG;
    }

    public String getAuthorization() {
//        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences prefs = getApplication().getSharedPreferences(SHARAED_PREFS_BASE, Context.MODE_PRIVATE);

        return prefs.getString(SHARAED_PREFS_BASE + USER_AUTH, MISSING_PREF);

        // TODO:: fix shared prefs
//        return "id3x7199-tw54-c";
    }

    public void setAuthorization(String authorization) {
        this.authorization = authorization;
    }



}
