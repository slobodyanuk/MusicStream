package com.musicstream.events;

import com.musicstream.rest.model.Track;

import java.util.List;

/**
 * Created by Serhii Slobodyanuk on 15.04.2016.
 */
public class UpdateListState {

    private int itemSelected;
    private List<Track> mTracks;

    public UpdateListState(int itemSelected) {
        this.itemSelected = itemSelected;
    }

    public UpdateListState(List<Track> tracks) {
        this.mTracks = tracks;
    }

    public List<Track> getTracks() {
        return mTracks;
    }


    public int getItemSelected() {
        return itemSelected;
    }
}
