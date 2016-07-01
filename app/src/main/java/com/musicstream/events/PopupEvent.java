package com.musicstream.events;

/**
 * Created by slobodyanuk on 24.06.16.
 */
public class PopupEvent {
    private String title;

    public PopupEvent(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
