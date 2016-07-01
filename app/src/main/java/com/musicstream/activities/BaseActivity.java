package com.musicstream.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.musicstream.R;

import butterknife.BindView;
import butterknife.ButterKnife;

abstract class BaseActivity extends AppCompatActivity {
    @Nullable
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResource());
        ButterKnife.bind(this);
        ButterKnife.setDebug(true);
    }

    public Toolbar getToolbar() {
        return toolbar;
    }

    protected void setActionBarIcon(int iconRes) {
        toolbar.setNavigationIcon(iconRes);
    }

    public void setActionBarTitle(String title) {
        TextView titleToolbar = ButterKnife.findById(this, R.id.toolbar_title);
        titleToolbar.setText(title);
    }

    public void setActionBarTitle(int idRes) {
        TextView titleToolbar = ButterKnife.findById(this, R.id.toolbar_title);
        titleToolbar.setText(idRes);
    }

    protected abstract int getLayoutResource();
}