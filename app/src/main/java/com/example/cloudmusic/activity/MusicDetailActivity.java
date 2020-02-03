package com.example.cloudmusic.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.icu.text.SimpleDateFormat;
import android.icu.util.TimeZone;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.cloudmusic.MusicMetaData;
import com.example.cloudmusic.MyApplication;
import com.example.cloudmusic.R;
import com.example.cloudmusic.item.Music;
import com.example.cloudmusic.other.CircleImageView;
import com.example.cloudmusic.service.MusicService;


import java.io.File;
import java.util.List;

import me.wcy.lrcview.LrcView;

import static com.example.cloudmusic.service.MusicService.MEDIA_PLAYER_PAUSE;

public class MusicDetailActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "csqMusicDetailActivity";
    private MusicMetaData mMusicMetaData;
    private MediaPlayer mMediaPlayer;
    private TextView mTitleTv;
    private TextView mArtistTv;
    private CircleImageView mMusicImageIv;
    private ImageView mPlayPreIv;
    private ImageView mMusicStateIv;
    private ImageView mPlayNextIv;
    private boolean mMediaState;
    private SeekBar mSeekBar;
    private TextView mCurrentTime;
    private TextView mTotalTime;
    private LrcView mLrcView;
    private List<Music> mMusicList;
    private Handler mHandler = new Handler();

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_detail);

        mMusicMetaData = ((MyApplication) getApplication()).getMMusicMetaData();
        mMusicList = ((MyApplication) getApplication()).getMMusicList();
        mMediaState = ((MyApplication) getApplication()).getMMediaState();
        Log.i(TAG, "音乐播放页面的播放状态是：" + mMediaState);
        mMediaPlayer = ((MyApplication) getApplication()).getMMediaPlayer();

        Toolbar toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            ActionBar ab = getSupportActionBar();
            ab.setDisplayHomeAsUpEnabled(true);
            ab.setHomeAsUpIndicator(R.drawable.actionbar_back);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        }

        //初始化控件
        initView();

        //填充内容
        renderView();
    }

    private void initView() {
        mTitleTv = findViewById(R.id.activity_detail_title_tv);
        mArtistTv = findViewById(R.id.activity_detail_artist_tv);
        mMusicImageIv = findViewById(R.id.circle_image_view);
        mPlayPreIv = findViewById(R.id.playing_pre);
        mMusicStateIv = findViewById(R.id.playing_play);
        mPlayNextIv = findViewById(R.id.playing_next);
        mSeekBar = findViewById(R.id.play_seek);
        mCurrentTime = findViewById(R.id.music_duration_played);
        mTotalTime = findViewById(R.id.music_duration);
        mLrcView = findViewById(R.id.lrc_view);

        mMusicImageIv.setOnClickListener(this);
        mPlayPreIv.setOnClickListener(this);
        mMusicStateIv.setOnClickListener(this);
        mPlayNextIv.setOnClickListener(this);
        mLrcView.setOnClickListener(this);
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                if (mMediaPlayer != null && fromUser) {
//                    mMediaPlayer.seekTo(progress * 1000);
//                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int progress = seekBar.getProgress();
                if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
                    mMediaPlayer.seekTo(progress);
                }
            }
        });


    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void renderView() {
        mTitleTv.setText(mMusicMetaData.getMMusicName());
        mArtistTv.setText(mMusicMetaData.getMSingerName());
        mMusicImageIv.setImageBitmap(mMusicMetaData.getMMusicCoverImage());
        mMusicImageIv.playAnim();

        if (mMediaState) {
            int musicState = ((MyApplication) getApplication()).getMMusicState();
            if (musicState == MusicService.MEDIA_PLAYER_PAUSE) {
                mMusicStateIv.setImageResource(R.drawable.play_rdi_btn_play);
            } else {
                mMusicStateIv.setImageResource(R.drawable.play_rdi_btn_pause);
            }
        } else {
            mMusicStateIv.setImageResource(R.drawable.play_rdi_btn_play);
        }
        mTotalTime.setText(mMusicMetaData.getMMusicDuation());
        updateSeekBar();
    }

    //同步seekBar与进度条时间
    private void updateSeekBar() {
        runOnUiThread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void run() {
                if (mMediaPlayer != null) {
                    int mCurrentPosition = mMediaPlayer.getCurrentPosition();//获取player当前进度，毫秒表示
                    int total = mMediaPlayer.getDuration();//获取当前歌曲总时长
                    mSeekBar.setProgress(mCurrentPosition);//seekBar同步歌曲进度
                    mSeekBar.setMax(total);//seekBar设置总时长
                    mCurrentTime.setText(formatDuration(mCurrentPosition));
                    mTotalTime.setText(formatDuration(total));
                }
                mHandler.postDelayed(this, 0);//延迟运行线程
            }
        });
    }

    //计算歌曲时间
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static String formatDuration(int currentPosition) {
        SimpleDateFormat formatter = new SimpleDateFormat("mm:ss");
        formatter.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));
        return formatter.format(currentPosition);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.circle_image_view:
                mMusicImageIv.setVisibility(View.GONE);
                mLrcView.setVisibility(View.VISIBLE);
                File lyricFile = new File(mMusicList.get((((MyApplication) getApplication()).getMPosition())).getMLyricPath());
                Log.d(TAG, "onClick: 歌词" + lyricFile.getAbsolutePath());
                mLrcView.loadLrc(lyricFile);

                Log.i(TAG, "onClick: " + mLrcView.hasLrc());

                if (mLrcView.hasLrc()) {
                    mLrcView.onDrag(mMediaPlayer.getCurrentPosition());
                } else {
                    mLrcView.setLabel("暂无歌词");
                }
                break;
            case R.id.lrc_view:
                mLrcView.setVisibility(View.GONE);
                mMusicImageIv.setVisibility(View.VISIBLE);
                break;
            case R.id.playing_pre:
                ((MyApplication) getApplication()).getMMusicService().preMusic();
                renderView();
                break;
            case R.id.playing_play:
                MyApplication ma = ((MyApplication) getApplication());
                Log.i(TAG, "onClick: 改变播放状态按钮" + ma.getMMusicService());
                if (!ma.getMMediaState()) {
                    ma.getMMusicService().initMediaPlayer(mMusicList.get(ma.getMPosition()).getMMusicPath());
                    mMusicStateIv.setImageResource(R.mipmap.pause_music);
                } else {
                    ma.getMMusicService().changeMusicState();
                    int musicState = ((MyApplication) getApplication()).getMMusicState();
                    if (musicState == MEDIA_PLAYER_PAUSE) {
                        mMusicStateIv.setImageResource(R.drawable.play_rdi_btn_play);
                    } else {
                        mMusicStateIv.setImageResource(R.drawable.play_rdi_btn_pause);
                    }
                }
                break;
            case R.id.playing_next:
                ((MyApplication) getApplication()).getMMusicService().nextMusic();
                renderView();
                break;
            default:
                break;
        }
    }
}
