package com.musicstream.utils;

/**
 * Created by Serhii Slobodyanuk on 24.03.2016.
 */
public class Constants {

    public static final int NOTIFICATION_ID = 1;

    public static final String CLIENT_ID = "0328c8ebe2fc9dddacc8715648edfb99";
    public static final String CLIENT_SECRET = "e0b18214d949513a87a860790077f92e";
    public static final String BASE_API_URL = "http://api-v2.soundcloud.com";

    public static final String PACKAGE = "com.musicstream.";

    public static final String BROADCAST_ACTION = PACKAGE + "broadcast";

    public static final String ACTION_PLAY = PACKAGE + "PLAY";
    public static final String ACTION_INIT_PLAYER = PACKAGE + "INIT_PLAYER";
    public static final String ACTION_PAUSE = PACKAGE + "PAUSE";
    public static final String ACTION_NEXT = PACKAGE + "NEXT";
    public static final String ACTION_PREVIOUS = PACKAGE + "PREVIOUS";
    public static final String ACTION_STOP = PACKAGE + "STOP";

    public static final String ACTION_WIDGET_RECEIVER = PACKAGE + "WIDGET_RECEIVER";

    public static final String INTENT_MEDIAS_KEY = PACKAGE + "MEDIAS";
    public static final String INTENT_SEEKBAR_KEY = PACKAGE + "SEEKBAR";
    public static final String INTENT_SECONDARY_SEEKBAR_KEY = "SECONDARY_SEEKBAR";
    public static final String INTENT_DURATION_KEY = PACKAGE + "DURATION";
    public static final String INTENT_CURRENT_SONG = PACKAGE + "CURRENT_SONG";
    public static final String INTENT_GENRE_KEY = PACKAGE + "GENRE";
    public static final String INTENT_PLAYING_SONG_KEY = "PLAYING_SONG";
    public static final String ACTION_PROGRESS = PACKAGE + "PROGRESS";
}
