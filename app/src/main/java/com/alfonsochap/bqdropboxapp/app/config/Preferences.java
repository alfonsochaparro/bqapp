package com.alfonsochap.bqdropboxapp.app.config;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Alfonso on 10/12/2015.
 */
public class Preferences {

    public static final int VIEW_LIST = 0;
    public static final int VIEW_GRID = 1;

    public static final int SORT_NAME = 0;
    public static final int SORT_DATE = 1;

    private static final String SP_NAME = "prefs";

    static SharedPreferences sp;

    public static void init(Context context) {
        sp = context.getSharedPreferences(SP_NAME, 0);
    }



    public static String getToken() {
        return sp.getString("token", "");
    }

    public static boolean hasToken() { return getToken().length() > 0; }

    public static void setToken(String token) {
        sp.edit().putString("token", token).commit();
    }

    public static void removeToken() {
        sp.edit().remove("token").commit();
    }


    public static int getSortMode() { return sp.getInt("sort", SORT_NAME); }
    public static void setSortMode(int sortMode) { sp.edit().putInt("sort", sortMode).commit(); }

    public static int getViewMode() { return sp.getInt("view", VIEW_LIST); }
    public static void setViewMode(int viewMode) { sp.edit().putInt("view", viewMode).commit(); }
}
