package com.example.cloudmusic.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cloudmusic.R;
import com.example.cloudmusic.domain.SongList;

import java.util.List;

public class SongListAdapter extends RecyclerView.Adapter<SongListAdapter.ViewHolder> {
    private List<SongList> mSongList;

    public SongListAdapter(List<SongList> mSongList) {
        this.mSongList = mSongList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_mine_song_list_item,
                parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SongList songList = mSongList.get(position);
        holder.mSongListIv.setImageResource(songList.getMImageId());
        holder.mSongListNameTv.setText(songList.getMSongListName());
        holder.mSongNumberTv.setText(songList.getMSongNumber());
    }

    @Override
    public int getItemCount() {
        return mSongList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView mSongListIv;
        TextView mSongListNameTv;
        TextView mSongNumberTv;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            mSongListIv = itemView.findViewById(R.id.song_list_image);
            mSongListNameTv = itemView.findViewById(R.id.song_list_name_text);
            mSongNumberTv = itemView.findViewById(R.id.song_list_number_text);
        }
    }
}
