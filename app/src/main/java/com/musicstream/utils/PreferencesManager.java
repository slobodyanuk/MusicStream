package com.musicstream.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Serhii Slobodyanuk on 05.04.2016.
 */
public class PreferencesManager {
    private static PreferencesManager sInstance;
    private final SharedPreferences mPref;

    private static final String PREF_NAME = "settings";

    private static final String CURRENT_GENRE = "genre";
    private static final String CURRENT_SONG = "song";
    private static final String ACTION_SHUFFLE = "shuffle";
    private static final String LOGIN = "login";
    private static final String USER_ID = "user_id";


    private PreferencesManager(Context context) {
        mPref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static synchronized void initializeInstance(Context context) {
        if (sInstance == null) {
            sInstance = new PreferencesManager(context);
        }
    }

    public static synchronized PreferencesManager getInstance() {
        if (sInstance == null) {
            throw new IllegalStateException(PreferencesManager.class.getSimpleName() +
                    " is not initialized, call initializeTaskInstance(..) method first.");
        }
        return sInstance;
    }

    public SharedPreferences getPreferences() {
        return mPref;
    }

    public boolean clear() {
        return mPref.edit()
                .clear()
                .commit();
    }

    public void setCurrentGenre(int genre) {
        mPref.edit()
                .putInt(CURRENT_GENRE, genre)
                .apply();
    }

    public int getCurrentGenre() {
        return mPref.getInt(CURRENT_GENRE, 0);
    }

    public void setCurrentSong(int song) {
        mPref.edit()
                .putInt(CURRENT_SONG, song)
                .apply();
    }

    public int getCurrentSong() {
        return mPref.getInt(CURRENT_SONG, 0);
    }

    public void setUserId(String userId) {
        mPref.edit()
                .putString(USER_ID, userId)
                .apply();
    }

    public String getUserId() {
        return mPref.getString(USER_ID, null);
    }


    public void setLogin(boolean login) {
        mPref.edit()
                .putBoolean(LOGIN, login)
                .apply();
    }

    public boolean isLogin() {
        return mPref.getBoolean(LOGIN, false);
    }

    public void setActionShuffle(boolean shuffle){
        mPref.edit()
                .putBoolean(ACTION_SHUFFLE, shuffle)
                .apply();
    }

    public boolean isShuffle(){
        return mPref.getBoolean(ACTION_SHUFFLE, false);
    }
}
