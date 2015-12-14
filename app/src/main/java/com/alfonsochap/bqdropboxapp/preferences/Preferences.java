package com.alfonsochap.bqdropboxapp.preferences;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Alfonso on 10/12/2015.
 */
public class Preferences {

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


}
