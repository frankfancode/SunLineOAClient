package org.ff.sunlineoaclient;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class OaApplication extends Application {
    private static OaApplication instance;

    public static OaApplication getInstance() {
        return instance;
    }

    public static int progressing = 0;

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        instance = this;
    }

    private String JSESSIONID;

    /**
     * @return the jSESSIONID
     */
    public String getJSESSIONID() {
        return JSESSIONID;
    }

    /**
     * @param jSESSIONID the jSESSIONID to set
     */
    public void setJSESSIONID(String jSESSIONID) {
        JSESSIONID = jSESSIONID;
    }

    public static boolean isConnected() {
        Context ac = getInstance().getApplicationContext();
        String service = ac.CONNECTIVITY_SERVICE;

        ConnectivityManager connectivity = (ConnectivityManager) ac.getSystemService(service);
        NetworkInfo activeNetwork = connectivity.getActiveNetworkInfo();
        boolean isConnected = (activeNetwork != null) && (activeNetwork.isConnectedOrConnecting());
        return isConnected;

    }
}
