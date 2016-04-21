package com.musicstream.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;
import android.widget.RemoteViews;

import com.musicstream.R;
import com.musicstream.activities.MainActivity;
import com.musicstream.enums.PlayerState;
import com.musicstream.events.UpdateListState;
import com.musicstream.interfaces.MediaControlListener;
import com.musicstream.rest.model.Track;
import com.musicstream.utils.Constants;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.musicstream.utils.Constants.ACTION_INIT_PLAYER;
import static com.musicstream.utils.Constants.ACTION_NEXT;
import static com.musicstream.utils.Constants.ACTION_PAUSE;
import static com.musicstream.utils.Constants.ACTION_PLAY;
import static com.musicstream.utils.Constants.ACTION_PREVIOUS;
import static com.musicstream.utils.Constants.ACTION_STOP;
import static com.musicstream.utils.Constants.NOTIFICATION_ID;

/**
 * Created by Serhii Slobodyanuk on 13.04.2016.
 */
public class MusicService extends Service implements MediaPlayer.OnPreparedListener,
        MediaPlayer.OnErrorListener, MediaPlayer.OnBufferingUpdateListener,
        MediaPlayer.OnCompletionListener, MediaControlListener {

    public static final String TAG = "music_service";

    private final IBinder mBinder = new MusicServiceBinder();
    private static MusicService mInstance = null;
    private static MediaPlayer mMediaPlayer = null;

    private NotificationManager mNotificationManager;
    private Notification mNotification = null;

    private int mCurrentPlayingSong = -1;
    private int mNotificationImagePlay = android.R.drawable.ic_media_play;
    private int mNotificationImagePause = android.R.drawable.ic_media_pause;

    private boolean foregroundStarted = false;

    private static PlayerState mPlayerState = PlayerState.Retrieving;

    private RemoteViews bigNotificationView;
    private RemoteViews smallNotificationView;
    private List<Track> mMediaItems;
    private ArrayList<Integer> mMediaShufflePositions = new ArrayList<>();
    private int mShufflePosition = -1;
    private String mUrl;
    private Track mTrack;
    private String mSongName;
    private boolean mAllowShuffle = false;

    public static MusicService getInstance() {
        return mInstance;
    }

    public static MediaPlayer getMediaPlayer() {
        if (mMediaPlayer != null) {
            mMediaPlayer.reset();
            return mMediaPlayer;
        } else {
            return mMediaPlayer = new MediaPlayer();
        }
    }

    @Override
    public void onCreate() {
        mInstance = this;
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        switch (intent.getAction()) {
            case ACTION_INIT_PLAYER:
                initMediaPlayer();
                break;
            case ACTION_PLAY:
                initCurrentMedia();
                asyncMediaPlayer();
                setUpAsForeground();
                notifyForeground(mNotificationImagePause);
                break;
            case ACTION_PAUSE:
                playMusic();
                break;
            case ACTION_NEXT:
                onSkipToNextClick();
                break;
            case ACTION_PREVIOUS:
                onSkipToPreviousClick();
                break;
            case ACTION_STOP:
                stopForeground(true);
                stopSelf();
                mNotificationManager.cancel(NOTIFICATION_ID);
                if (mMediaPlayer != null) {
                    mMediaPlayer.stop();
                }
                break;
        }
        return START_NOT_STICKY;
    }

    private void initMediaPlayer() {
        mMediaPlayer = getMediaPlayer();
        mMediaPlayer.setOnPreparedListener(MusicService.this);
        mMediaPlayer.setOnErrorListener(this);
        mMediaPlayer.setOnBufferingUpdateListener(this);
        mMediaPlayer.setOnCompletionListener(this);
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        mPlayerState = PlayerState.Preparing;
    }

    private void initCurrentMedia() {
        mTrack = mMediaItems.get(mCurrentPlayingSong);
        mUrl = mTrack.getStreamUri();
        mSongName = mTrack.getTitle();
    }

    public void setTrack(int position) {
        mCurrentPlayingSong = position;
        initCurrentMedia();
        mMediaPlayer.reset();
        asyncMediaPlayer();
        setUpAsForeground();
        notifyForeground(mNotificationImagePause);
    }

    public PlayerState getPlayerState() {
        return mPlayerState;
    }

    public int getCurrentPlayingSong() {
        return mCurrentPlayingSong;
    }

    public List<Track> getCurrentPlaylist() {
        return mMediaItems;
    }

    public void setTracksList(List<Track> tracks) {
        mMediaItems = tracks;
        for (int position = 0; position < mMediaItems.size(); position++) {
            mMediaShufflePositions.add(position);
        }
    }

    private void asyncMediaPlayer() {
        try {
            mMediaPlayer.setDataSource(mUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            mMediaPlayer.prepareAsync();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }

        mPlayerState = PlayerState.Preparing;
    }

    public void playMusic() {
        if (!mPlayerState.equals(PlayerState.Playing)) {
            mMediaPlayer.start();
            notifyForeground(mNotificationImagePause);
            mPlayerState = PlayerState.Playing;
        } else if (mPlayerState.equals(PlayerState.Playing)) {
            sendBroadcast(new Intent(Constants.ACTION_PAUSE));
            mMediaPlayer.pause();
            notifyForeground(mNotificationImagePlay);
            mPlayerState = PlayerState.Paused;
        }
    }

    public void seekMusicTo(int progress) {
        if (mMediaPlayer != null) {
            mMediaPlayer.seekTo(progress);
        }
    }

    @Override
    public void onPlayClick() {
        if (mCurrentPlayingSong == -1) {
            playFirstTrack();
        } else {
            playMusic();
        }
    }

    @Override
    public void onSkipToNextClick() {
        if (canPlayNextTrack()) {
            mCurrentPlayingSong = (mAllowShuffle)
                    ? mMediaShufflePositions.get(++mShufflePosition)
                    : ++mCurrentPlayingSong;

            setTrack(mCurrentPlayingSong);
            EventBus.getDefault().post(new UpdateListState(mCurrentPlayingSong));
        }
    }

    @Override
    public void onSkipToPreviousClick() {
        if (canPlayPreviousTrack()) {
            mCurrentPlayingSong = (mAllowShuffle)
                    ? mMediaShufflePositions.get(--mShufflePosition)
                    : --mCurrentPlayingSong;

            setTrack(mCurrentPlayingSong);
            EventBus.getDefault().post(new UpdateListState(mCurrentPlayingSong));
        }
    }

    @Override
    public void onShuffleTracksClick(boolean allow) {
        mAllowShuffle = allow;
        if (mAllowShuffle) {
            Collections.shuffle(mMediaShufflePositions);
        }
    }

    private void playFirstTrack() {
        mCurrentPlayingSong = 0;
        setTrack(mCurrentPlayingSong);
        EventBus.getDefault().post(new UpdateListState(mCurrentPlayingSong));
    }

    private boolean canPlayNextTrack() {
        if (mAllowShuffle) {
            return mMediaShufflePositions.size() - 1 != mShufflePosition;
        } else {
            return mMediaItems.size() - 1 != mCurrentPlayingSong;
        }
    }

    private boolean canPlayPreviousTrack() {
        if (mAllowShuffle) {
            return mShufflePosition != 0 && mShufflePosition != -1;
        } else {
            return mCurrentPlayingSong != 0 && mCurrentPlayingSong != -1;
        }
    }

    public class MusicServiceBinder extends Binder {
        public MusicService getService() {
            return getInstance();
        }
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        playMusic();
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        if (mp.isPlaying()) {
            mPlayerState = PlayerState.Playing;
            sendBroadcast(new Intent(Constants.ACTION_PROGRESS)
                    .putExtra(Constants.INTENT_SEEKBAR_KEY, mp.getCurrentPosition())
                    .putExtra(Constants.INTENT_SECONDARY_SEEKBAR_KEY, (mp.getDuration() * percent) / 100)
                    .putExtra(Constants.INTENT_DURATION_KEY, mp.getDuration()));
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        mPlayerState = PlayerState.Stopped;
        onSkipToNextClick();
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        mPlayerState = PlayerState.Stopped;

        if (what == -38) {
            return false;
        }

        String errorMsg = "Preparation/playback error: ";
        switch (what) {
            default:
                errorMsg += String.format("Unknown error %d", what);
                break;
            case MediaPlayer.MEDIA_ERROR_IO:
                errorMsg += "I/O error";
                break;
            case MediaPlayer.MEDIA_ERROR_MALFORMED:
                errorMsg += "Malformed";
                break;
            case MediaPlayer.MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK:
                errorMsg += "Not valid for progressive playback";
                break;
            case MediaPlayer.MEDIA_ERROR_SERVER_DIED:
                errorMsg += "Server died";
                break;
            case MediaPlayer.MEDIA_ERROR_TIMED_OUT:
                errorMsg += "Timed out";
                break;
            case MediaPlayer.MEDIA_ERROR_UNSUPPORTED:
                errorMsg += "Unsupported";
                break;
        }
        Log.e(TAG, errorMsg);
        return false;
    }

    private void setUpAsForeground() {

        bigNotificationView = new RemoteViews(getPackageName(),
                R.layout.player_status_bar_expanded);
        smallNotificationView = new RemoteViews(getPackageName(),
                R.layout.player_status_bar_small);

        Intent closeIntent = new Intent(this, MusicService.class);
        closeIntent.setAction(ACTION_STOP);
        Intent pauseIntent = new Intent(this, MusicService.class);
        pauseIntent.setAction(ACTION_PAUSE);
        Intent nextIntent = new Intent(this, MusicService.class);
        nextIntent.setAction(ACTION_NEXT);
        Intent previousIntent = new Intent(this, MusicService.class);
        previousIntent.setAction(ACTION_PREVIOUS);

        PendingIntent pendingCloseIntent = PendingIntent.getService(this, 0,
                closeIntent, 0);
        PendingIntent pendingPauseIntent = PendingIntent.getService(this, 0,
                pauseIntent, 0);
        PendingIntent pendingNextIntent = PendingIntent.getService(this, 0,
                nextIntent, 0);
        PendingIntent pendingPreviousIntent = PendingIntent.getService(this, 0,
                previousIntent, 0);

        bigNotificationView.setOnClickPendingIntent(R.id.status_bar_collapse, pendingCloseIntent);
        smallNotificationView.setOnClickPendingIntent(R.id.status_bar_collapse, pendingCloseIntent);

        bigNotificationView.setOnClickPendingIntent(R.id.status_bar_play, pendingPauseIntent);
        smallNotificationView.setOnClickPendingIntent(R.id.status_bar_play, pendingPauseIntent);

        bigNotificationView.setOnClickPendingIntent(R.id.status_bar_next, pendingNextIntent);
        smallNotificationView.setOnClickPendingIntent(R.id.status_bar_next, pendingNextIntent);

        bigNotificationView.setOnClickPendingIntent(R.id.status_bar_prev, pendingPreviousIntent);
        smallNotificationView.setOnClickPendingIntent(R.id.status_bar_prev, pendingPreviousIntent);

        bigNotificationView.setTextViewText(R.id.status_bar_track_name, mSongName);
        smallNotificationView.setTextViewText(R.id.status_bar_track_name, mSongName);

        PendingIntent pi =
                PendingIntent.getActivity(getApplicationContext(), 0, new Intent(getApplicationContext(), MainActivity.class),
                        PendingIntent.FLAG_UPDATE_CURRENT);

        mNotification = new NotificationCompat.Builder(getApplicationContext())
                .setSmallIcon(R.drawable.ic_play)
                .setOngoing(true)
                .setContentIntent(pi)
                .setContent(smallNotificationView)
                .setPriority(Notification.PRIORITY_MAX)
                .build();

        mNotification.bigContentView = bigNotificationView;
        final String imageUrl = mMediaItems.get(mCurrentPlayingSong).getArtworkUrl();

        if (!TextUtils.isEmpty(imageUrl)) {
            Picasso
                    .with(MusicService.this)
                    .load(imageUrl)
                    .into(bigNotificationView, R.id.status_bar_album_art, NOTIFICATION_ID, mNotification);
        } else {
            bigNotificationView.setImageViewResource(R.id.status_bar_album_art, R.drawable.artwork_default);
        }
        if (foregroundStarted) {
            mNotificationManager.notify(NOTIFICATION_ID, mNotification);
        } else {
            mNotificationManager.notify(NOTIFICATION_ID, mNotification);
            startForeground(NOTIFICATION_ID, mNotification);
            foregroundStarted = true;
        }
    }

    public void notifyForeground(int srcImage) {
        bigNotificationView.setImageViewResource(R.id.status_bar_play, srcImage);
        smallNotificationView.setImageViewResource(R.id.status_bar_play, srcImage);
        mNotificationManager.notify(NOTIFICATION_ID, mNotification);
    }
}