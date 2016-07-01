package com.musicstream.rest;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.musicstream.interfaces.LoginListener;
import com.musicstream.rest.model.User;
import com.musicstream.utils.Constants;
import com.musicstream.utils.PreferencesManager;
import com.soundcloud.api.ApiWrapper;
import com.soundcloud.api.Request;
import com.soundcloud.api.Token;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

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

        String response = "null";

        try {
            mWrapper = new ApiWrapper(Constants.CLIENT_ID, Constants.CLIENT_SECRET, null, null);
            mToken = mWrapper.login(username, password);
            HttpResponse resp = mWrapper.get(Request.to("/me"));

            if (!String.valueOf(mToken).equals("null")){
                response = EntityUtils.toString(resp.getEntity());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if (!s.equals("null")) {
            Gson gson = new Gson();

            User user = gson.fromJson(s, User.class);
            PreferencesManager.getInstance().setUserId(String.valueOf(user.getId()));
            Log.e("userID", PreferencesManager.getInstance().getUserId());
            mCallback.onLoginCompleted(true);
        }else{
            mCallback.onLoginCompleted(false);
        }
    }
}