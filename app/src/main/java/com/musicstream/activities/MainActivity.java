package com.musicstream.activities;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.musicstream.R;
import com.musicstream.enums.ServiceConnectionState;
import com.musicstream.events.RestResponseEvent;
import com.musicstream.fragments.BaseFragment;
import com.musicstream.fragments.MainFragment;
import com.musicstream.interfaces.LoginListener;
import com.musicstream.interfaces.MediaControlListener;
import com.musicstream.rest.RestClient;
import com.musicstream.rest.SoundLoginTask;
import com.musicstream.rest.model.Track;
import com.musicstream.rest.model.Tracks;
import com.musicstream.services.MusicService;
import com.musicstream.utils.Constants;
import com.musicstream.utils.MenuItemKey;
import com.musicstream.utils.PreferencesManager;
import com.musicstream.utils.TimeUtils;
import com.musicstream.utils.Utils;
import com.musicstream.utils.ViewAnimator;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.squareup.picasso.Picasso;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import yalantis.com.sidemenu.interfaces.Resourceble;
import yalantis.com.sidemenu.model.SlideMenuItem;

import static com.musicstream.enums.ServiceConnectionState.BOUND;
import static com.musicstream.enums.ServiceConnectionState.DISCONNECT;
import static com.musicstream.enums.ServiceConnectionState.UNBOUND;
import static com.musicstream.utils.Constants.ACTION_INIT_PLAYER;
import static com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelSlideListener;
import static com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelState;
import static com.zhy.view.flowlayout.TagFlowLayout.OnTagClickListener;

public class MainActivity extends BaseActivity
        implements ViewAnimator.ViewAnimatorListener,
        LoginListener, SeekBar.OnSeekBarChangeListener,
        SwipeRefreshLayout.OnRefreshListener {

    private static final String CONTENT_FRAGMENT = "content_fragment";
    private static final int MAX_LIMIT = 50;

    @Bind(R.id.left_drawer)
    LinearLayout mMenuFrame;
    @Bind(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @Bind(R.id.toolbar_title)
    TextView mToolbarTextView;
    @Bind(R.id.sliding_layout)
    SlidingUpPanelLayout mSlidingUpPanelLayout;
    @Bind(R.id.controlsFrame)
    LinearLayout mControlsFrame;
    @Bind(R.id.positionSeek)
    SeekBar mSeekBar;
    @Bind(R.id.position)
    TextView mCurrentProgressText;
    @Bind(R.id.duration)
    TextView mTrackDurationText;
    @Bind(R.id.play)
    ImageButton mPlayButton;
    @Bind(R.id.shuffle)
    ImageButton mShuffleButton;
    @Bind(R.id.flow_layout)
    TagFlowLayout mTagFlowLayout;
    @Bind(R.id.artwork)
    ImageView mArtworkView;
    @Bind(R.id.refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    private List<SlideMenuItem> mMenuList = new ArrayList<>();

    private FragmentManager mFragmentManager;
    private MainFragment mMainFragment;
    private Fragment mContentFragment;
    private ViewAnimator mViewAnimator;
    private ActionBarDrawerToggle mDrawerToggle;

    private ServiceConnectionState mServiceConnectionState = DISCONNECT;
    private MusicService mMusicServiceInstance;
    private BroadcastReceiver mBroadcastReceiver;

    private PreferencesManager mPref = PreferencesManager.getInstance();

    private MediaControlListener mMediaControlListener;

    private boolean access;
    private boolean allowShuffle = true;
    private boolean isShuffle = false;
    private String[] genres;
    private int genre;
    private Intent mServiceIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        genres = getResources().getStringArray(R.array.genres);
        genre = mPref.getCurrentGenre();
        access = mPref.isLogin();
        allowShuffle = !mPref.isShuffle();
        mServiceIntent = new Intent(this, MusicService.class);

        if (!access) {
            new SoundLoginTask(this, "ceruy.slobodyanuk@gmail.com", "qwaszx1245784895").execute();
        } else {
            onLoginCompleted(true);
        }

        mShuffleButton.setBackgroundResource(
                (!allowShuffle)
                        ? (R.drawable.shuffle_pressed_background)
                        : (R.drawable.shuffle_background));

        initDrawerLayout();
        initSlidingPanel();
        initSlideMenu();
        initBroadcastReceiver();

        mSeekBar.setProgress(0);
        mSeekBar.setOnSeekBarChangeListener(this);

        setActionBarTitle(R.string.app_name);
        mFragmentManager = getSupportFragmentManager();

        if (savedInstanceState != null) {
            mMainFragment = (MainFragment) mFragmentManager.findFragmentByTag(CONTENT_FRAGMENT);
            mContentFragment = mMainFragment;
        } else {
            mMainFragment = MainFragment.newInstance();
            if (mMainFragment != null) {
                mFragmentManager.beginTransaction()
                        .replace(R.id.content_frame, mMainFragment, CONTENT_FRAGMENT)
                        .commit();

                mContentFragment = mMainFragment;
            } else {
                finish();
            }
        }
    }

    @OnClick(R.id.play)
    void onPlayClick() {
        mMediaControlListener.onPlayClick();
    }

    @OnClick(R.id.next)
    void onNextClick() {
        mMediaControlListener.onSkipToNextClick();
        if (mSlidingUpPanelLayout.getPanelState() == PanelState.EXPANDED) {
            showArtworkImage(mMusicServiceInstance.getCurrentPlayingSong());
        }

    }

    @OnClick(R.id.previous)
    void onPreviousClick() {
        mMediaControlListener.onSkipToPreviousClick();
        if (mSlidingUpPanelLayout.getPanelState() == PanelState.EXPANDED) {
            showArtworkImage(mMusicServiceInstance.getCurrentPlayingSong());
        }
    }

    @OnClick(R.id.shuffle)
    void onShuffleCLick() {
        mShuffleButton.setBackgroundResource(
                (allowShuffle)
                        ? (R.drawable.shuffle_pressed_background)
                        : (R.drawable.shuffle_background));
        mMediaControlListener.onShuffleTracksClick(allowShuffle);
        mPref.setActionShuffle(allowShuffle);
        allowShuffle = !allowShuffle;
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_main;
    }

    private void initSlideMenu() {
        mDrawerLayout.setScrimColor(Color.TRANSPARENT);
        mViewAnimator = new ViewAnimator(this,
                getMenuList(),
                mContentFragment,
                mDrawerLayout,
                this);
    }

    private void initDrawerLayout() {
        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setTitle("");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        mDrawerToggle.syncState();
        mDrawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                mToolbarTextView.setTranslationX(slideOffset * (getResources().getDimension(R.dimen.menu_wight) / 2));
                if (slideOffset > 0.6 && mMenuFrame.getChildCount() == 0) {
                    mViewAnimator.showMenuContent();
                }
            }

            @Override
            public void onDrawerOpened(View drawerView) {
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                mMenuFrame.removeAllViews();
                mMenuFrame.invalidate();
            }

            @Override
            public void onDrawerStateChanged(int newState) {
            }
        });
    }

    private void initBroadcastReceiver() {
        mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (intent.getAction()) {
                    case Constants.ACTION_PAUSE:
                        mPlayButton.setImageResource(R.drawable.ic_play);
                        break;
                    case Constants.ACTION_PROGRESS:
                        mPlayButton.setImageResource(R.drawable.ic_pause);
                        int progress = intent.getIntExtra(Constants.INTENT_SEEKBAR_KEY, 0);
                        int duration = intent.getIntExtra(Constants.INTENT_DURATION_KEY, 0);
                        mSeekBar.setProgress(progress);
                        mCurrentProgressText.setText(TimeUtils.getDurationString(progress));
                        mTrackDurationText.setText(TimeUtils.getDurationString(duration));
                        mSeekBar.setMax(duration);
                        break;
                }
            }
        };

        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.ACTION_PROGRESS);
        filter.addAction(Constants.ACTION_PAUSE);
        registerReceiver(mBroadcastReceiver, filter);
    }

    private void startMusicService() {
        mServiceIntent.setAction(ACTION_INIT_PLAYER);
        startService(mServiceIntent);
        bindMusicService();
    }

    private void bindMusicService() {
        if (!mServiceConnectionState.equals(BOUND)) {
            bindService(mServiceIntent, mServiceConnection, Context.BIND_AUTO_CREATE);
            mServiceConnectionState = BOUND;
        }
    }

    private void unBindMusicService() {
        if (mServiceConnectionState.equals(BOUND)) {
            unbindService(mServiceConnection);
            mServiceConnectionState = UNBOUND;
        }
    }

    private final ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.MusicServiceBinder binder = (MusicService.MusicServiceBinder) service;
            mMusicServiceInstance = binder.getService();
            mMediaControlListener = mMusicServiceInstance;
            mServiceConnectionState = BOUND;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mServiceConnectionState = UNBOUND;
        }
    };

    public MusicService getMusicServiceInstance() {
        if (mMusicServiceInstance == null) {
            mMusicServiceInstance = MusicService.getInstance();
        }
        return mMusicServiceInstance;
    }

    private void initSlidingPanel() {
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent,
                R.color.colorPrimary, R.color.colorPink, R.color.colorSeaGreen, R.color.colorYellow);

        String[] formattedGenres = new String[genres.length];
        for (int i = 0; i < genres.length; i++) {
            formattedGenres[i] = genres[i].replace("soundcloud:genres:", "");
        }

        mTagFlowLayout.setMaxSelectCount(1);

        TagAdapter<String> mTagFlowAdapter = new TagAdapter<String>(formattedGenres) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                LayoutInflater mInflater = LayoutInflater.from(MainActivity.this);
                TextView tv = (TextView) mInflater.inflate(R.layout.item_genres,
                        mTagFlowLayout, false);
                tv.setText(s);
                return tv;
            }
        };

        mTagFlowLayout.setAdapter(mTagFlowAdapter);
        mTagFlowAdapter.setSelectedList(genre);

        mTagFlowLayout.setOnTagClickListener(new OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                genre = position;
                mSlidingUpPanelLayout.setPanelState(PanelState.COLLAPSED);
                Call<Tracks> call = RestClient.getApiService().getTracks("top", genres[genre], Constants.CLIENT_ID, MAX_LIMIT);
                call.enqueue(callback);
                return true;
            }
        });

        mControlsFrame.setBackgroundResource(R.color.colorCyanTransparent);
        mSlidingUpPanelLayout.addPanelSlideListener(new PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                mSlidingUpPanelLayout.invalidate();
            }

            @Override
            public void onPanelStateChanged(View panel, PanelState previousState, PanelState newState) {
                if (newState == PanelState.EXPANDED) {
                    if (mMusicServiceInstance != null && mMusicServiceInstance.getCurrentPlayingSong() != -1) {
                        showArtworkImage(mMusicServiceInstance.getCurrentPlayingSong());
                    }
                }
            }
        });
    }

    private void showArtworkImage(int position) {
        String url = null;
        Track track = mMusicServiceInstance.getCurrentPlaylist().get(position);
        url = track.getFullArtworkUrl();
        url = (TextUtils.isEmpty(url) ? "null" : url);

        if (mSlidingUpPanelLayout.getPanelState() == PanelState.EXPANDED) {
            Picasso
                    .with(this)
                    .load(url)
                    .error(R.drawable.artwork_default)
                    .fit()
                    .into(mArtworkView);
        }
    }

    private List<SlideMenuItem> getMenuList() {
        mMenuList.add(new SlideMenuItem(MenuItemKey.ITEM_CLOSE, R.drawable.ic_close));
        mMenuList.add(new SlideMenuItem(MenuItemKey.ITEM_TOP, R.drawable.ic_menu_popular));
        mMenuList.add(new SlideMenuItem(MenuItemKey.ITEM_PLAYLIST, R.drawable.ic_menu_playlist));
        return mMenuList;
    }

    public void addFragment(Fragment fragment, int layoutContainer, String title) {
        mContentFragment = fragment;
        mFragmentManager = getSupportFragmentManager();
        mFragmentManager.beginTransaction()
                .add(layoutContainer, fragment, title)
                .addToBackStack(title)
                .commit();

    }

    public LinearLayout getPlayerControlView() {
        return mControlsFrame;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (fromUser) {
            if (mMusicServiceInstance != null) {
                mMusicServiceInstance.seekMusicTo(progress);
                seekBar.setProgress(progress);
                mCurrentProgressText.setText(TimeUtils.getDurationString(progress));
            }
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onRefresh() {
        mSwipeRefreshLayout.setRefreshing(true);
        Call<Tracks> call = RestClient.getApiService().getTracks("top", genres[genre], Constants.CLIENT_ID, MAX_LIMIT);
        call.enqueue(callback);
    }

    @Override
    public void onBackPressed() {
        mFragmentManager = getSupportFragmentManager();
        int i = mFragmentManager.getBackStackEntryCount();
        if (i > 0) {
            FragmentManager.BackStackEntry backEntry = mFragmentManager.getBackStackEntryAt(mFragmentManager.getBackStackEntryCount() - 1);
            String str = backEntry.getName();
            BaseFragment currentFragment = (BaseFragment) mFragmentManager.findFragmentByTag(str);
            if (currentFragment != null && currentFragment instanceof MainFragment) {
                finish();
            } else {
                mFragmentManager.popBackStack();
            }
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public Fragment onSwitch(Resourceble slideMenuItem, Fragment screenShotable, int position) {
        return null;
    }

    @Override
    public void disableHomeButton() {
        assert getSupportActionBar() != null;
        getSupportActionBar().setHomeButtonEnabled(false);
    }

    @Override
    public void enableHomeButton() {
        assert getSupportActionBar() != null;
        getSupportActionBar().setHomeButtonEnabled(true);
        mDrawerLayout.closeDrawers();
    }

    @Override
    public void addViewToContainer(View view) {
        mMenuFrame.addView(view);
    }

    @Override
    public void onLoginCompleted(boolean success) {
        if (success) {

            if (!Utils.isServiceRunning(this, MusicService.class)) {
                startMusicService();
            } else {
                bindMusicService();
            }

            mPref.setLogin(true);
            Call<Tracks> call = RestClient.getApiService().getTracks("top", genres[genre], Constants.CLIENT_ID, MAX_LIMIT);
            call.enqueue(callback);
        } else {
            mPref.setLogin(false);
            Toast.makeText(this, "Error", Toast.LENGTH_LONG).show();
        }
    }

    private Callback callback = new Callback<Tracks>() {
        @Override
        public void onResponse(Call<Tracks> call, Response<Tracks> response) {
            mSwipeRefreshLayout.setRefreshing(false);
            EventBus.getDefault().post(new RestResponseEvent(response));
        }

        @Override
        public void onFailure(Call<Tracks> call, Throwable t) {
            mSwipeRefreshLayout.setRefreshing(false);
            Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_LONG).show();
        }
    };

    @Override
    protected void onStop() {
        super.onStop();
        if (!Utils.isServiceRunning(this, MusicService.class)) {
            mPref.clear();
        }
        unBindMusicService();
        try {
            unregisterReceiver(mBroadcastReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
