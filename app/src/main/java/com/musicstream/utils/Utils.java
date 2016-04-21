package com.musicstream.utils;


import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;

import com.musicstream.rest.model.Track;
import com.musicstream.rest.model.TrackCollection;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Serhii Slobodyanuk on 01.04.2016.
 */
public class Utils {

    public static ArrayList<Track> getAvailableTracks(List<TrackCollection> trackCollectionList) {
        ArrayList<Track> tracks = new ArrayList<>();
        Track track;
        for (TrackCollection trackCollection : trackCollectionList) {
            track = trackCollection.getTrack();
            if (track.isStreamable() &&
                    track.getPolicy().equals("ALLOW")) {
                tracks.add(track);
            }
        }
        return tracks;
    }

    public static boolean isServiceRunning(Activity activity, Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) activity.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
