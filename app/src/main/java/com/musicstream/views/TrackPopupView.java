package com.musicstream.views;

import android.content.Context;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.musicstream.R;
import com.musicstream.activities.MainActivity;
import com.musicstream.utils.PopupAnimationUtil;

import butterknife.ButterKnife;

/**
 * Created by slobodyanuk on 29.06.16.
 */
public class TrackPopupView {

    private static final int HIDE_POPUP_TIME = 5500;
    private static final int POPUP_ANIM_DURATION = 500;

    private PopupWindow mPopupView;
    private MainActivity mActivity;
    private Point mPoint;

    public TrackPopupView(MainActivity activity) {
        mActivity = activity;
    }

    public void initPopupView(String mTrackName) {

        int[] location = new int[2];
        mActivity.getToolbar().getLocationOnScreen(location);

        mPoint = new Point();
        mPoint.x = location[0];
        mPoint.y = location[1];

        LinearLayout viewGroup = ButterKnife.findById(mActivity, R.id.popup);
        LayoutInflater layoutInflater = (LayoutInflater) mActivity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = layoutInflater.inflate(R.layout.layout_popup, viewGroup);

        TextView trackTitle = ButterKnife.findById(layout, R.id.track_name);
        trackTitle.setText(mTrackName);
        trackTitle.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        trackTitle.setSelected(true);

        layout.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int popupHeight = layout.getMeasuredHeight();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        mActivity.getWindow().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int popupWidth = (int) (displayMetrics.widthPixels / 2);

        mPopupView = new PopupWindow(mActivity);
        mPopupView.setContentView(layout);
        mPopupView.setWidth(popupWidth);
        mPopupView.setHeight(popupHeight);

        int OFFSET_X = 300;
        int OFFSET_Y = 100;

        mPopupView.setBackgroundDrawable(new BitmapDrawable());
        mPopupView.showAtLocation(layout, Gravity.NO_GRAVITY, mPoint.x + OFFSET_X, mPoint.y + OFFSET_Y);

        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (mPopupView.isShowing()) {
                    mPopupView.dismiss();
                }
            }
        };

        PopupAnimationUtil anim = new PopupAnimationUtil(layout, popupWidth);
        anim.setDuration(POPUP_ANIM_DURATION);
        layout.startAnimation(anim);

        mPopupView.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                handler.removeCallbacks(runnable);
            }
        });

        handler.postDelayed(runnable, HIDE_POPUP_TIME);
    }

    public void dismissView(){
        if (mPopupView != null && mPopupView.isShowing()) {
            mPopupView.dismiss();
        }
    }
}
