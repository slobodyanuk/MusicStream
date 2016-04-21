package com.musicstream.interfaces;

/**
 * Created by Serhii Slobodyanuk on 13.04.2016.
 */
public interface MediaControlListener {
    void onPlayClick();
    void onSkipToNextClick();
    void onSkipToPreviousClick();
    void onShuffleTracksClick(boolean allow);
}
