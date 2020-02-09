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

import com.example.cloudmusic.LrcView;
import com.example.cloudmusic.domain.MusicMetaData;
import com.example.cloudmusic.MyApplication;
import com.example.cloudmusic.R;
import com.example.cloudmusic.domain.Music;
import com.example.cloudmusic.util.circleimageutil.CircleImageView;
import com.example.cloudmusic.service.MusicService;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;


import static com.example.cloudmusic.service.MusicService.MEDIA_PLAYER_PAUSE;

public class MusicDetailActivity extends AppCompatActivity implements View.OnClickListener, MusicService.MusicDetailActivityCallback {
    private static final String TAG = "csqMusicDetailActivity";
    public static final int MUSIC_DETAIL_ACTIVITY_STATE_CODE = 3;
    public static int mCurrentDisplayStateCode;
    public static final int MUSIC_DETAIL_ACTIVITY_MUSIC_DATA_STATE_CODE = 4;
    public static final int MUSIC_DETAIL_ACTIVITY_LRC_STATE_CODE = 5;
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
    private MusicService mMusicService;
    private String[] mLrcNames = new String[]{"许嵩 - 大千世界.lrc", "许嵩 - 平行宇宙.lrc", "许嵩 - 江湖.lrc", "许嵩 - 明智之举.lrc"};

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_detail);

        mMusicService = ((MyApplication) getApplication()).getMMusicService();
        mMusicService.setMusicDetailActivityCallback(this);

        mMusicMetaData = ((MyApplication) getApplication()).getMMusicMetaData();
        mMusicList = ((MyApplication) getApplication()).getMMusicList();
        mMediaState = ((MyApplication) getApplication()).getMMediaState();
        Log.i(TAG, "音乐播放页面的播放状态是：" + mMediaState);
        mMediaPlayer = ((MyApplication) getApplication()).getMMediaPlayer();

        //初始化Toolbar
        initToolbar();

        //初始化控件
        initView();

        //填充内容
        renderMusicMetaDataAndSeekBarView();
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            ActionBar ab = getSupportActionBar();
            if (ab != null) {
                ab.setDisplayHomeAsUpEnabled(true);
                ab.setHomeAsUpIndicator(R.drawable.actionbar_back);
            }
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        ((MyApplication) getApplication()).setMCurrentActivity(MUSIC_DETAIL_ACTIVITY_STATE_CODE);
        mCurrentDisplayStateCode = MUSIC_DETAIL_ACTIVITY_MUSIC_DATA_STATE_CODE;
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
    private void renderMusicMetaDataAndSeekBarView() {
        Music music = mMusicList.get(((MyApplication) getApplication()).getMPosition());
        ((MyApplication) getApplication()).getMMusicService().getMetaData(music.getMMusicPath());
        mMusicMetaData = ((MyApplication) getApplication()).getMMusicMetaData();

        mTitleTv.setText(mMusicMetaData.getMMusicName());
        mArtistTv.setText(mMusicMetaData.getMSingerName());
        mMusicImageIv.setImageBitmap(mMusicMetaData.getMMusicCoverImage());

        Log.i(TAG, "renderMusicMetaDataAndSeekBarView: " + mMediaState);
        if (mMediaState) {
            int musicState = ((MyApplication) getApplication()).getMMusicState();
            if (musicState == MusicService.MEDIA_PLAYER_PAUSE) {
                mMusicStateIv.setImageResource(R.drawable.play_rdi_btn_play);
                mMusicImageIv.pauseAnim();
            } else {
                mMusicStateIv.setImageResource(R.drawable.play_rdi_btn_pause);
                mMusicImageIv.playAnim();
            }
        } else {
            mMusicStateIv.setImageResource(R.drawable.play_rdi_btn_play);
            mMusicImageIv.pauseAnim();
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
                renderLrcView();
                mLrcView.setVisibility(View.VISIBLE);
                mCurrentDisplayStateCode = MUSIC_DETAIL_ACTIVITY_LRC_STATE_CODE;
                break;
            case R.id.lrc_view:
                mLrcView.setVisibility(View.GONE);
                mMusicImageIv.setVisibility(View.VISIBLE);
                mCurrentDisplayStateCode = MUSIC_DETAIL_ACTIVITY_MUSIC_DATA_STATE_CODE;
                break;
            case R.id.playing_pre:
                ((MyApplication) getApplication()).getMMusicService().preMusic();
                renderMusicMetaDataAndSeekBarView();
                renderLrcView();
                break;
            case R.id.playing_play:
                MyApplication ma = ((MyApplication) getApplication());
                Log.i(TAG, "onClick: 改变播放状态按钮" + ma.getMMusicService());
                if (!ma.getMMediaState()) {
                    ma.getMMusicService().initMediaPlayerAndPlayMusic(mMusicList.get(ma.getMPosition()).getMMusicPath());
                    mMusicStateIv.setImageResource(R.mipmap.pause_music);
                } else {
                    ma.getMMusicService().changeMusicState();
                    int musicState = ((MyApplication) getApplication()).getMMusicState();
                    if (musicState == MEDIA_PLAYER_PAUSE) {
                        mMusicStateIv.setImageResource(R.drawable.play_rdi_btn_play);
                        mMusicImageIv.pauseAnim();
                    } else {
                        mMusicStateIv.setImageResource(R.drawable.play_rdi_btn_pause);
                        mMusicImageIv.playAnim();
                    }
                }
                break;
            case R.id.playing_next:
                ((MyApplication) getApplication()).getMMusicService().nextMusic();
                renderMusicMetaDataAndSeekBarView();
                renderLrcView();
                break;
            default:
                break;
        }
    }

    private void renderLrcView() {
        int currentPosition = (((MyApplication) getApplication()).getMPosition());
        //从assets目录下读取歌词文件内容
        String lrcStr = getFromAssets(mLrcNames[currentPosition]);
        Log.d(TAG, "onClick: " + lrcStr);
        mLrcView.setLrc(lrcStr);
        mLrcView.setPlayer(mMediaPlayer);
        mLrcView.init();
    }

    //从assets目录下读取歌词文件内容
    public String getFromAssets(String fileName) {
        try {
            InputStreamReader inputReader = new InputStreamReader(getResources().getAssets().open(fileName));
            BufferedReader bufReader = new BufferedReader(inputReader);
            String line;
            StringBuilder result = new StringBuilder();
            while ((line = bufReader.readLine()) != null) {
                if (line.trim().equals(""))
                    continue;
                result.append(line).append("\r\n");
            }
            return result.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void updateActivityUi() {
        renderMusicMetaDataAndSeekBarView();
    }

    @Override
    public void updateLrcViewUi() {
        renderLrcView();
    }
}
