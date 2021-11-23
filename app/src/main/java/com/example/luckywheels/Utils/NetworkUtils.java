package com.example.luckywheels.Utils;

import android.content.Context;
import android.net.ConnectivityManager;

public class NetworkUtils {
//    public static final String BASE_URL =  "http://192.168.1.107:8000";
    public static final String BASE_URL =  "http://192.168.1.36";
//    public static final String BASE_URL =  "http://192.168.43.130";
//    public static final String BASE_URL =   "https://luckywheelapp.000webhostapp.com/";

    public static final String BASE_API_FOLDER = "lucky_wheel_api/user";
    public static final String REGISTER_FILE = "user_register.php";
    public static final String LOGIN_FILE = "user_login.php";
    public static final String GET_WINNERS_LIST_FILE = "user_get_winners_list.php";
    public static final String EDIT_POINTS_FILE = "user_edit_points.php";
    public static final String GET_USER_DATA_FILE = "user_get_data.php";
    public static final String GET_ALL_CONTESTS_FILE = "user_get_all_contests.php";
    public static final String MAKE_USER_PARTICIPANT_FILE = "user_make_participant.php";



    // URL FOR REGISTER
    public static final String REGISTER_URL =
            NetworkUtils.BASE_URL + "/" +
                    NetworkUtils.BASE_API_FOLDER + "/" +
                    NetworkUtils.REGISTER_FILE;
    // URL FOR LOG IN
    public static final String LOG_IN_URL =
            NetworkUtils.BASE_URL + "/" +
                    NetworkUtils.BASE_API_FOLDER + "/" +
                    NetworkUtils.LOGIN_FILE;
    public static final String GET_USER_DATA_URL =
            NetworkUtils.BASE_URL + "/" +
                    NetworkUtils.BASE_API_FOLDER + "/" +
                    NetworkUtils.GET_USER_DATA_FILE;
    public static final String GET_WINNERS_LIST_URL =
            NetworkUtils.BASE_URL + "/" +
                    NetworkUtils.BASE_API_FOLDER + "/" +
                    NetworkUtils.GET_WINNERS_LIST_FILE;
    public static final String GET =
            NetworkUtils.BASE_URL + "/" +
                    NetworkUtils.BASE_API_FOLDER + "/" +
                    NetworkUtils.EDIT_POINTS_FILE;
    public static final String GET_ALL_CONTESTS_URL =
            NetworkUtils.BASE_URL + "/" +
                    NetworkUtils.BASE_API_FOLDER + "/" +
                    NetworkUtils.GET_ALL_CONTESTS_FILE;
    public static final String MAKE_USER_PARTICIPANT_URL =
            NetworkUtils.BASE_URL + "/" +
                    NetworkUtils.BASE_API_FOLDER + "/" +
                    NetworkUtils.MAKE_USER_PARTICIPANT_FILE;
    public static final String EDIT_POINTS_URL =
            NetworkUtils.BASE_URL + "/" +
                    NetworkUtils.BASE_API_FOLDER + "/" +
                    NetworkUtils.EDIT_POINTS_FILE;

    public static boolean checkInternetConnection(Context context){
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return (cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected());
    }
}
