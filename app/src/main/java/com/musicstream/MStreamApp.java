package com.musicstream;

import android.app.Application;

import com.musicstream.utils.PreferencesManager;

public class MStreamApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        PreferencesManager.initializeInstance(this);
    }
}
