package com.alfonsochap.bqdropboxapp.network;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class NetworkQueue {

    private static NetworkQueue mInstance;
    private RequestQueue mRequestQueue;
    private static Context mCtx;

    private NetworkQueue(Context context) {
        mCtx = context;
        mRequestQueue = getRequestQueue();

    }

    public static synchronized NetworkQueue getmInstance(Context context) {
        if(mInstance == null) {
            mInstance = new NetworkQueue(context);
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if(mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return  mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req){
        getRequestQueue().add(req);
    }

}