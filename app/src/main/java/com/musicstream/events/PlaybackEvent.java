package com.musicstream.events;

/**
 * Created by slobodyanuk on 29.06.16.
 */
public class PlaybackEvent {

    private int position;

    public PlaybackEvent(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }
}
