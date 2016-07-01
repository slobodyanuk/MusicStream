package com.musicstream.fragments;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.musicstream.R;
import com.musicstream.activities.MainActivity;
import com.musicstream.adapters.MainMediaAdapter;
import com.musicstream.events.PlaybackEvent;
import com.musicstream.events.RestResponseEvent;
import com.musicstream.events.UpdateListState;
import com.musicstream.events.UpdateTrackState;
import com.musicstream.rest.model.Track;
import com.musicstream.rest.model.Tracks;
import com.musicstream.services.MusicService;
import com.musicstream.utils.Utils;
import com.musicstream.views.DividerItemDecoration;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import retrofit2.Response;

public class MainFragment extends BaseFragment implements MainMediaAdapter.OnClickListener {

    private static final String TITLE = "title";
    @BindView(R.id.list_view)
    RecyclerView mMediaList;
    @BindView(R.id.progress)
    RelativeLayout mProgressBar;
    private LinearLayout mControlsFrame;
    private List<Track> mMediaItems;
    private MainMediaAdapter mMediaAdapter;
    private LinearLayoutManager mLayoutManager;

    private String mTitle;
    private int mCurrentTrack = -1;
    private String mCurrentGenre;

    private MusicService mMusicServiceInstance;

    public static MainFragment newInstance(String title) {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        args.putString(TITLE, title);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mTitle = getArguments().getString(TITLE);
        ((MainActivity) getActivity()).setActionBarTitle("MusicStream :: " + mTitle);
        mCurrentGenre = ((MainActivity) getActivity()).getCurrentGenre();
        initViews();
    }

    @Subscribe
    public void onEvent(RestResponseEvent event) {
        hideProgressBar();
        initMediaList(event.getResponse());

    }

    @Subscribe
    public void onEvent(UpdateListState event) {
        if (mMediaAdapter != null) {
            if (event.getTracks() == null) {
                mCurrentTrack = mMusicServiceInstance.getCurrentPlayingSong();
                if (mMediaAdapter.getTracks().equals(mMusicServiceInstance.getCurrentPlaylist())) {
                    mMediaAdapter.setSelectedItem(mCurrentTrack);
                    mLayoutManager.scrollToPosition(mCurrentTrack);
                }
            } else {
                mCurrentTrack = mMusicServiceInstance.getCurrentPlayingSong();
                mMediaItems = mMusicServiceInstance.getCurrentPlaylist();
                mMediaAdapter.updateAdapter(mMediaItems);
                mLayoutManager.scrollToPosition(mCurrentTrack);
            }
        }
    }

    @Subscribe
    public void onEvent(UpdateTrackState event) {
        List<Track> currentPlaylist = MusicService.getInstance().getCurrentPlaylist();
        if (mMediaItems != null && currentPlaylist != null
                && mMediaItems.size() != 0 && currentPlaylist.size() != 0) {
            if (mMediaItems.get(0).getId() == currentPlaylist.get(0).getId()) {
                mMediaAdapter.setSelectedItem(MusicService.getInstance().getCurrentPlayingSong());
            }
        }
    }

    @Subscribe
    public void onEvent(PlaybackEvent event){
        onTrackClick(event.getPosition());
    }

    private void initMediaList(Response<Tracks> response) {
        mMediaItems = new ArrayList<Track>();
        mMediaItems = Utils.getAvailableTracks(response.body().getCollection());
        if (mMediaAdapter != null) {
            mMediaAdapter.updateAdapter(mMediaItems);
            mMediaList.smoothScrollToPosition(0);
            mMediaAdapter.resetItem(mCurrentTrack);
        } else {
            mMediaAdapter = new MainMediaAdapter(this, mMediaItems);
            mLayoutManager = new LinearLayoutManager(getActivity());
            mMediaList.setLayoutManager(mLayoutManager);
            mMediaList.addItemDecoration(new DividerItemDecoration(getActivity(), R.drawable.divider));
            mMediaList.setItemAnimator(null);
            mMediaList.setAdapter(mMediaAdapter);
        }
    }

    private void initViews() {
        mControlsFrame = ((MainActivity) getActivity()).getPlayerControlView();
    }

    private void hideProgressBar() {
        mProgressBar.setVisibility(View.GONE);
        mMediaList.setVisibility(View.VISIBLE);
        mControlsFrame.setVisibility(View.VISIBLE);
    }

    private void showProgressBar() {
        mProgressBar.setVisibility(View.VISIBLE);
        mMediaList.setVisibility(View.GONE);
        mControlsFrame.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onTrackClick(int position) {
        if (mCurrentTrack != position || !mMediaAdapter.getTracks().equals(mMusicServiceInstance.getCurrentPlaylist())) {
            mCurrentTrack = position;
            mMusicServiceInstance = ((MainActivity) getActivity()).getMusicServiceInstance();
            mMusicServiceInstance.setTracksList(mMediaItems);
            mMusicServiceInstance.setTrackPosition(position);
            mMediaAdapter.setSelectedItem(mCurrentTrack);
            mMusicServiceInstance.setTrack(position);
        } else {
            if (mCurrentGenre.equals(((MainActivity) getActivity()).getCurrentGenre())) {
                mMusicServiceInstance.playMusic();
            } else {
                mMusicServiceInstance.setTrack(position);
                mCurrentGenre = ((MainActivity) getActivity()).getCurrentGenre();
            }
        }
    }

    @Override
    public int getLayoutResource() {
        return R.layout.fragment_main;
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }
}
