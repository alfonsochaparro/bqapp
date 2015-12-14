package com.alfonsochap.bqdropboxapp.network;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class NetworkQueue {

    private static NetworkQueue sInstance;
    private RequestQueue mRequestQueue;
    private static Context sContext;

    private NetworkQueue(Context context) {
        sContext = context;
        mRequestQueue = getRequestQueue();
    }

    public static synchronized NetworkQueue getmInstance(Context context) {
        if(sInstance == null) {
            sInstance = new NetworkQueue(context);
        }
        return sInstance;
    }

    public RequestQueue getRequestQueue() {
        if(mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(sContext.getApplicationContext());
        }
        return  mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req){
        getRequestQueue().add(req);
    }

}