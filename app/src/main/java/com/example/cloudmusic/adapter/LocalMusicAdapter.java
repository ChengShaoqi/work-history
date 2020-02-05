package com.example.cloudmusic.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.cloudmusic.R;
import com.example.cloudmusic.domain.Music;

import java.util.List;

public class LocalMusicAdapter extends ArrayAdapter {
    private static final String TAG = "csqLocalMusicAdapter";
    private int mResourceId;
    private LinearLayout mLinearLayout;
    private List<Music> mMusicList;
    private Context mContext;

    public LocalMusicAdapter(@NonNull Context context, int textViewResourceId, @NonNull List<Music> objects) {
        super(context, textViewResourceId, objects);
        mContext = context;
        mResourceId = textViewResourceId;
        mMusicList = objects;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull final ViewGroup parent) {
        final Music music = (Music) getItem(position);
        View view;
        ViewHolder viewHolder;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(mResourceId, parent,
                    false);
            viewHolder = new ViewHolder();
            viewHolder.mMusicName = view.findViewById(R.id.local_music_name);
            viewHolder.mSingerName = view.findViewById(R.id.local_music_singer_name);
            viewHolder.mSelectedMenu = view.findViewById(R.id.ic_local_music_select);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.mMusicName.setText(music.getMMusicName());
        viewHolder.mSingerName.setText(music.getMSingerName());
        viewHolder.mSelectedMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "点击了歌曲选项按钮", Toast.LENGTH_SHORT).show();
            }
        });

//        mLinearLayout = view.findViewById(R.id.music_item_layout);
//        mLinearLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //底部框更新ui，开启音乐
//                Log.d(TAG, "onClick: " + position);
//                mILocalMusicAdapterCallback.initBottomDisplayUi(position);
//            }
//        });

        return view;
    }

    @Nullable
    @Override
    public Object getItem(int position) {
        return mMusicList.get(position);
    }

    private class ViewHolder {
        private TextView mMusicName;
        private TextView mSingerName;
        private ImageView mSelectedMenu;
    }

}
