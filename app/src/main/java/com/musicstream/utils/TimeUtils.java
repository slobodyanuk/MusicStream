package com.musicstream.utils;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * Created by Serhii Slobodyanuk on 23.03.2016.
 */
public class TimeUtils {

    public static String getDurationString(long durationMs) {
        return String.format(Locale.getDefault(), "%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(durationMs),
                TimeUnit.MILLISECONDS.toSeconds(durationMs) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(durationMs))
        );
    }
}
