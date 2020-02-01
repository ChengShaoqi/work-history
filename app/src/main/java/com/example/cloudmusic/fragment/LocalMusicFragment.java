package com.example.cloudmusic.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.cloudmusic.R;
import com.example.cloudmusic.activity.LocalMusicActivity;
import com.example.cloudmusic.item.Music;

public class LocalMusicFragment extends Fragment {
    private static final String TAG = "csqLocalMusicFragment";
    private ILocalMusicCallback mILocalMusicCallback;
    private Context mContext;

    public LocalMusicFragment(LocalMusicActivity localMusicActivity) {
        this.mILocalMusicCallback = localMusicActivity;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_local_music, container, false);
        ListView listView = view.findViewById(R.id.local_music_list_view);
        mILocalMusicCallback.updateLocalMusicListView(listView);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public interface ILocalMusicCallback {
        void updateLocalMusicListView(ListView listView);
    }
}
