package com.musicstream.events;

import com.musicstream.rest.model.Track;

import java.util.List;

/**
 * Created by Serhii Slobodyanuk on 15.04.2016.
 */
public class UpdateTrackState {

    private int itemSelected;

    public UpdateTrackState(int itemSelected) {
        this.itemSelected = itemSelected;
    }

    public int getItemSelected() {
        return itemSelected;
    }
}
