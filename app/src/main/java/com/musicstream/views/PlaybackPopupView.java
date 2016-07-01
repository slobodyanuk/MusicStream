package com.musicstream.views;

import android.content.Context;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.musicstream.R;
import com.musicstream.activities.MainActivity;
import com.musicstream.adapters.MainMediaAdapter;
import com.musicstream.adapters.PlaybackAdapter;
import com.musicstream.rest.model.Track;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by slobodyanuk on 29.06.16.
 */
public class PlaybackPopupView {

    private static final int POPUP_ANIM_DURATION = 500;

    private PopupWindow mPopupView;
    private MainActivity mActivity;
    private Point mPoint;
    private PlaybackAdapter mMediaAdapter;
    private LinearLayoutManager mLayoutManager;

    public PlaybackPopupView(MainActivity activity) {
        mActivity = activity;
    }

    public void initPopupView(List<Track> mMediaItems, int position) {
        if (mMediaItems == null
                || mMediaItems.size() == 0){
            mMediaItems = new ArrayList<>();
        }

        int[] location = new int[2];
        mActivity.getToolbar().getLocationOnScreen(location);

        mPoint = new Point();
        mPoint.x = location[0];
        mPoint.y = location[1];

        LinearLayout viewGroup = ButterKnife.findById(mActivity, R.id.popup);
        LayoutInflater layoutInflater = (LayoutInflater) mActivity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = layoutInflater.inflate(R.layout.playback_layout, viewGroup);

        ImageButton close = ButterKnife.findById(layout, R.id.collapse);
        RecyclerView mMediaList = ButterKnife.findById(layout, R.id.list);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupView.dismiss();
            }
        });

        layout.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        mActivity.getWindow().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int popupWidth = (int) (displayMetrics.widthPixels - (displayMetrics.widthPixels * 0.10));
        int popupHeight = (int) (displayMetrics.heightPixels - (displayMetrics.heightPixels * 0.15));

        mPopupView = new PopupWindow(mActivity);
        mPopupView.setContentView(layout);
        mPopupView.setWidth(popupWidth);
        mPopupView.setHeight(popupHeight);
        mPopupView.setFocusable(true);

        int OFFSET_X = 300;
        int OFFSET_Y = 100;

        mPopupView.setBackgroundDrawable(new BitmapDrawable());
        mPopupView.showAtLocation(layout, Gravity.CENTER, 0, 0);

        mMediaAdapter = new PlaybackAdapter(mActivity, mMediaItems);
        mLayoutManager = new LinearLayoutManager(mActivity);
        mMediaList.setLayoutManager(mLayoutManager);
        mMediaList.setItemAnimator(null);
        mMediaAdapter.setSelectedItem(position);
        mMediaList.setAdapter(mMediaAdapter);
    }

    public void dismissView() {
        if (mPopupView != null && mPopupView.isShowing()) {
            mPopupView.dismiss();
        }
    }

    public void setSelectedItem(int position){
        mMediaAdapter.setSelectedItem(position);
    }
}
