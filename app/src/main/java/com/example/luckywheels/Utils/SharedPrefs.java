package com.example.luckywheels.Utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Set;

//SharedPreferences manager class
public class SharedPrefs {

    public static final String KEY_USER_ID = "user_id";
    public static final String KEY_USER_FIRST_NAME = "first_name";
    public static final String KEY_USER_MIDDLE_NAME = "middle_name";
    public static final String KEY_USER_LAST_NAME = "last_name";
    public static final String KEY_USER_EMAIL = "email";
    public static final String KEY_USER_PASSWORD = "password";
    public static final String KEY_USER_POINTS = "points";
    public static final String KEY_USER_BALANCE = "balance";
    public static final String KEY_USER_REF_CODE = "referral_code";
    public static final String KEY_USER_REF_TIMES = "ref_times";

    public static final String KEY_LOG_IN_STATE = "Logged_in";
    public static final String KEY_CONNECTION_STATE = "Connection_state";


    //SharedPreferences file name
    private static String SHARED_PREFS_FILE_NAME = "my_app_shared_prefs";

    //here you can centralize all your shared prefs keys
    public static String KEY_MY_SHARED_BOOLEAN = "my_shared_boolean";
    public static String KEY_MY_SHARED_FOO = "my_shared_foo";

    //User Info Shared Prefs Keys
    public static String LOGGED_IN_KEY = "first_time";
    public static String LOGGED_IN_ID = "id_first_time";
    public static String LOGGED_IN_USER_NAME = "name_first_time";
    public static String LOGGED_IN_USER_BALANCE = "balance_first_time";
    public static String LOGGED_IN_USER_POINTS = "points_first_time";
    public static String LOGGED_IN_USER_RC = "referral_code_first_time";

    public static String TIMER_TIME_LEFT = "time_left";
    //to determine visibility when destroy activity
    public static String SPIN_TIMER_STATE = "spin_timer_state";
    public static String RECEIVER_STATE = "broadcast_receiver_state";


    //get the SharedPreferences object instance
    //create SharedPreferences file if not present


    private static SharedPreferences getPrefs(Context context) {
        return context.getSharedPreferences(SHARED_PREFS_FILE_NAME, Context.MODE_PRIVATE);
    }

    //Save Booleans
    public static void savePref(Context context, String key, boolean value) {
        getPrefs(context).edit().putBoolean(key, value).commit();
    }

    //Get Booleans
    public static boolean getBoolean(Context context, String key) {
        return getPrefs(context).getBoolean(key, false);
    }

    //Get Booleans if not found return a predefined default value
    public static boolean getBoolean(Context context, String key, boolean defaultValue) {
        return getPrefs(context).getBoolean(key, defaultValue);
    }

    //Strings
    public static void save(Context context, String key, String value) {
        getPrefs(context).edit().putString(key, value).commit();
    }

    public static String getString(Context context, String key) {
        return getPrefs(context).getString(key, "");
    }

    public static String getString(Context context, String key, String defaultValue) {
        return getPrefs(context).getString(key, defaultValue);
    }

    //Integers
    public static void save(Context context, String key, int value) {
        getPrefs(context).edit().putInt(key, value).commit();
    }

    public static int getInt(Context context, String key) {
        return getPrefs(context).getInt(key, 0);
    }

    public static int getInt(Context context, String key, int defaultValue) {
        return getPrefs(context).getInt(key, defaultValue);
    }

    //Floats
    public static void save(Context context, String key, float value) {
        getPrefs(context).edit().putFloat(key, value).commit();
    }

    public static float getFloat(Context context, String key) {
        return getPrefs(context).getFloat(key, 0);
    }

    public static float getFloat(Context context, String key, float defaultValue) {
        return getPrefs(context).getFloat(key, defaultValue);
    }

    //Longs
    public static void save(Context context, String key, long value) {
        getPrefs(context).edit().putLong(key, value).commit();
    }

    public static long getLong(Context context, String key) {
        return getPrefs(context).getLong(key, 0);
    }

    public static long getLong(Context context, String key, long defaultValue) {
        return getPrefs(context).getLong(key, defaultValue);
    }

    //StringSets
    public static void save(Context context, String key, Set<String> value) {
        getPrefs(context).edit().putStringSet(key, value).commit();
    }

    public static Set<String> getStringSet(Context context, String key) {
        return getPrefs(context).getStringSet(key, null);
    }

    public static Set<String> getStringSet(Context context, String key, Set<String> defaultValue) {
        return getPrefs(context).getStringSet(key, defaultValue);
    }
}
