package com.example.audakel.fammap;

/**
 * Created by audakel on 5/28/16.
 */
public class Constants {
    public static final String SHARAED_PREFS_BASE = "fammap";
    public static final String ZOOM_LATLNG = "z_ll";
    //    public static String BASE_URL = "http://192.168.1.6:8080/";
//    public static String API_LOGIN_URL = BASE_URL + "user/login";
    public static String POST = "POST";
    public static String USER_AUTH = "Authorization";

    public static String USERNAME = "userName";
    public static String PERSON_ID = "personId";
    public static String MISSING_PREF = "missing pref!!";
    public static String EVENTS_API = "event/";
    public static boolean SKIP_LOGIN = false;
    public static String PEOPLE_API = "person/";
    public static String PORT = "port";
    public static String SERVER = "server";
    public static String BASE_URL = "base_url";

    public static final String MARRIAGE = "marriage";
    public static final String BIRTH = "birth";
    public static final String DEATH = "death";
    public static final String CENSUS = "census";
    public static final String CHRISTENING = "christening";
    public static final String BAPTISM = "baptism";
    public static final String MALE = "male";
    public static final String FEMALE = "female";
    public static final String MOTHER = "mother";
    public static final String FATHER = "father";

    public static String PERSON_INTENT_ID = "person_id";
    public static String EVENT_INTENT_LATLNG = "event_id";

    public enum Relation {FATHER, MOTHER, SPOUSE, CHILD}

}
