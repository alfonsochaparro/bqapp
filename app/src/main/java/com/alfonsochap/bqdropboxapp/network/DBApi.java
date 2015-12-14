package com.alfonsochap.bqdropboxapp.network;

import android.content.Context;

import com.alfonsochap.bqdropboxapp.R;
import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.session.AppKeyPair;

/**
 * Created by Alfonso on 10/12/2015.
 */
public class DBApi {

    private static Context sContext;
    private static DBApi sInstance;

    public DropboxAPI<AndroidAuthSession> api;


    private DBApi(Context context){
        sContext = context;
        AppKeyPair appKeys = new AppKeyPair(sContext.getString(R.string.dropbox_api_key), sContext.getString(R.string.dropbox_api_secret));
        AndroidAuthSession session = new AndroidAuthSession(appKeys);
        api = new DropboxAPI<AndroidAuthSession>(session);
    }

    public static DBApi getInstance(Context context) {
        if(sInstance == null) {
            sInstance = new DBApi(context);
        }
        return sInstance;
    }
}
