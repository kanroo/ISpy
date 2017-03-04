package com.roodapps.ispy;

import android.app.Application;

import com.roodapps.ispy.utility.MyHandler;

/**
 * Created by Ethan on 25/02/2017.
 */

public class MainApplication extends Application
{
    private static MyHandler handler;

    @Override
    public void onCreate() {
        super.onCreate();
        handler = new MyHandler();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        handler = null;
    }

    public static MyHandler getHandler() { return handler; }
}
