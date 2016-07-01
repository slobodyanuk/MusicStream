package com.musicstream.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.musicstream.R;
import com.musicstream.rest.model.Track;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Serhii Slobodyanuk on 14.04.2016.
 */
public class PlaybackAdapter extends RecyclerView.Adapter<PlaybackAdapter.ViewHolder> {

    private List<Track> list;
    private Context mContext;
    private int selectedPosition = -1;
    private OnClickListener callback;
    private boolean update = false;

    public PlaybackAdapter(Activity context, List<Track> list) {
        Log.e("adapter", String.valueOf(this));
        this.list = list;
        callback = (OnClickListener) context;
        this.mContext = context;
    }

    public void updateAdapter(List<Track> list) {
        this.list = new ArrayList<>();
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.playback_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Track item = getItem(position);
        holder.mArtistNameView.setText(item.getTitle());

        if (selectedPosition == position) {
            holder.mRoot.setBackgroundColor(mContext.getResources().getColor(R.color.colorAccent));
        } else {
            holder.mRoot.setBackgroundColor(mContext.getResources().getColor(R.color.colorGray));
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private Track getItem(int position) {
        return list.get(position);
    }

    public List<Track> getTracks(){
        return list;
    }

    public void setSelectedItem(int position) {
        notifyItemChanged(selectedPosition);
        selectedPosition = position;
        notifyItemChanged(position);
    }

    public void resetItems() {
        notifyDataSetChanged();
    }

    public interface OnClickListener {
        void onTrackClick(int position);
    }

    public final class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.root)
        LinearLayout mRoot;

        @BindView(R.id.title)
        TextView mArtistNameView;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            callback.onTrackClick(getAdapterPosition());
        }
    }
}
