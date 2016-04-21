package com.musicstream.rest;

import android.os.AsyncTask;

import com.musicstream.interfaces.LoginListener;
import com.musicstream.utils.Constants;
import com.soundcloud.api.ApiWrapper;
import com.soundcloud.api.Token;

import java.io.IOException;

/**
 * Created by Serhii Slobodyanuk on 28.03.2016.
 */
public class SoundLoginTask extends AsyncTask<Void, Void, String> {

    private ApiWrapper mWrapper;
    private Token mToken;
    private String username;
    private String password;
    private LoginListener mCallback;

    public SoundLoginTask(LoginListener callback, String username, String password) {
        this.mCallback = callback;
        this.password = password;
        this.username = username;
    }

    @Override
    protected String doInBackground(Void... params) {
        try {
            mWrapper = new ApiWrapper(Constants.CLIENT_ID, Constants.CLIENT_SECRET, null, null);
            mToken = mWrapper.login(username, password);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return String.valueOf(mToken);
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if (!s.equals("null")) {
            mCallback.onLoginCompleted(true);
        }else{
            mCallback.onLoginCompleted(false);
        }
    }
}
