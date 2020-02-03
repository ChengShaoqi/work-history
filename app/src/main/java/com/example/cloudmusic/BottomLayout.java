package com.example.cloudmusic;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.cloudmusic.activity.MusicDetailActivity;
import com.example.cloudmusic.service.MusicService;

import static com.example.cloudmusic.service.MusicService.MEDIA_PLAYER_PAUSE;

public class BottomLayout extends LinearLayout implements View.OnClickListener {
    private static final String TAG = "csqBottomLayout";
    private LinearLayout mLinearLayout;
    private ImageView mMusicImage;
    private TextView mMusicName;
    private TextView mSingerName;
    private ImageView mPreMusic;
    private ImageView mMusicState;
    private ImageView mNextMusic;
    public static MusicService.LocalBinder sLocalBinder;
    public static ServiceConnection sServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            sLocalBinder = (MusicService.LocalBinder) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.e(TAG, "onServiceDisconnected: 服务出现异常");
        }
    };

    public BottomLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.bottom_music_display, this);
        initBottomView();
    }

    private void initBottomView() {
        mLinearLayout = findViewById(R.id.bottom_music_linear_layout);
        mMusicImage = findViewById(R.id.bottom_container_image);
        mMusicName = findViewById(R.id.local_music_name);
        mSingerName = findViewById(R.id.local_music_singer_name);
        mPreMusic = findViewById(R.id.pre_music);
        mMusicState = findViewById(R.id.music_state_button);
        mNextMusic = findViewById(R.id.next_music);

        mLinearLayout.setOnClickListener(this);
        mPreMusic.setOnClickListener(this);
        mMusicState.setOnClickListener(this);
        mNextMusic.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bottom_music_linear_layout:
                Log.d(TAG, "onClick: 点击了底部信息栏");
                Intent intent = new Intent(getContext(), MusicDetailActivity.class);
                getContext().startActivity(intent);
                break;
            case R.id.pre_music:
                Log.d(TAG, "onClick: 点击了上一首");
                break;
            case R.id.music_state_button:
                sLocalBinder.getService().changeMusicState();
                int musicState = ((MyApplication) getApplicationWindowToken()).getMMusicState();
                if (musicState == MEDIA_PLAYER_PAUSE) {
                    mMusicState.setImageResource(R.mipmap.start_music);
                } else {
                    mMusicState.setImageResource(R.mipmap.pause_music);
                }
                Log.d(TAG, "onClick: 点击了切换歌曲状态");
                break;
            case R.id.next_music:
                Log.d(TAG, "onClick: 点击了下一首");
                break;
        }
    }
}
