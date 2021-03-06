package com.musicstream.adapters;

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
public class MainMediaAdapter extends RecyclerView.Adapter<MainMediaAdapter.ViewHolder> {

    private List<Track> list;
    private Context mContext;
    private int imagePlaying = R.drawable.ic_play;
    private int imagePausing = R.drawable.ic_pause;
    private int selectedPosition = -1;
    private OnClickListener callback;
    private boolean update = false;

    public MainMediaAdapter(Fragment context, List<Track> list) {
        Log.e("adapter", String.valueOf(this));
        this.list = list;
        callback = (OnClickListener) context;
        this.mContext = context.getActivity();
    }

    public void updateAdapter(List<Track> list) {
        this.list = new ArrayList<>();
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.media_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Track item = getItem(position);
        holder.mArtistNameView.setText(item.getTitle());

        String url = getItem(position).getArtworkUrl();
        url = (TextUtils.isEmpty(url) ? "null" : url);

        Glide
                .with(mContext)
                .load(url)
                .error(R.drawable.artwork_default)
                .into(holder.mArtWork);

        if (selectedPosition == position) {
            holder.mRootLayout.setBackgroundColor(mContext.getResources().getColor(R.color.colorAccentTransparent));
            holder.mIconPlay.setImageResource(imagePausing);
        } else {
            holder.mRootLayout.setBackgroundColor(mContext.getResources().getColor(android.R.color.white));
            holder.mIconPlay.setImageResource(imagePlaying);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private Track getItem(int position) {
        return list.get(position);
    }

    public int getSelectedItem() {
        return selectedPosition;
    }

    public void setSelectedItem(int position) {
        notifyItemChanged(selectedPosition);
        selectedPosition = position;
        notifyItemChanged(position);
    }

    public void resetItem(int position) {
        selectedPosition = -1;
        notifyItemChanged(position);
    }

    public List<Track> getTracks(){
        return list;
    }

    public void resetItems() {
        notifyDataSetChanged();
    }

    public interface OnClickListener {
        void onTrackClick(int position);
    }

    public final class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.root)
        LinearLayout mRootLayout;
        @BindView(R.id.play_eq)
        ImageView mIconPlay;
        @BindView(R.id.artwork)
        ImageView mArtWork;
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
