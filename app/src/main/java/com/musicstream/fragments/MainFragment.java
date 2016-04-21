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
import com.musicstream.events.RestResponseEvent;
import com.musicstream.events.UpdateListState;
import com.musicstream.rest.model.Track;
import com.musicstream.rest.model.Tracks;
import com.musicstream.services.MusicService;
import com.musicstream.utils.Utils;
import com.musicstream.views.DividerItemDecoration;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import retrofit2.Response;

public class MainFragment extends BaseFragment implements MainMediaAdapter.OnClickListener {

    @Bind(R.id.list_view)
    RecyclerView mMediaList;
    @Bind(R.id.progress)
    RelativeLayout mProgressBar;

    private LinearLayout mControlsFrame;
    private List<Track> mMediaItems;
    private MainMediaAdapter mMediaAdapter;
    private LinearLayoutManager mLayoutManager;

    private int mCurrentTrack = -1;

    private MusicService mMusicServiceInstance;

    public static MainFragment newInstance() {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
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
        //showProgressBar();
        initViews();
    }

    @Subscribe
    public void onEvent(RestResponseEvent event) {
        hideProgressBar();
        initMediaList(event.getResponse());
        mMusicServiceInstance = ((MainActivity) getActivity()).getMusicServiceInstance();
        mMusicServiceInstance.setTracksList(mMediaItems);
        mCurrentTrack = mMusicServiceInstance.getCurrentPlayingSong();
        //mMediaAdapter.setSelectedItem(mCurrentTrack);
        if (mCurrentTrack != -1) {
            mLayoutManager.scrollToPosition(mCurrentTrack);
        }
    }

    @Subscribe
    public void onEvent(UpdateListState event) {
        if (mMediaAdapter != null ) {
            if (event.getTracks() == null) {
                mCurrentTrack = event.getItemSelected();
                mMediaAdapter.setSelectedItem(mCurrentTrack);
                mLayoutManager.scrollToPosition(mCurrentTrack);
            }else {
                mCurrentTrack = mMusicServiceInstance.getCurrentPlayingSong();
                mMediaItems = mMusicServiceInstance.getCurrentPlaylist();
                mMediaAdapter.updateAdapter(mMediaItems);
                mLayoutManager.scrollToPosition(mCurrentTrack);
            }
        }
    }

    private void initMediaList(Response<Tracks> response) {
        mMediaItems = new ArrayList<Track>();
        mMediaItems = Utils.getAvailableTracks(response.body().getCollection());
        if (mMediaAdapter != null) {
            mMediaAdapter.updateAdapter(mMediaItems);
            mMediaList.smoothScrollToPosition(0);
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
        if (mCurrentTrack != position) {
            mCurrentTrack = position;
            mMediaAdapter.setSelectedItem(position);
            mMusicServiceInstance.setTrack(position);
        } else {
            mMusicServiceInstance.playMusic();
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
