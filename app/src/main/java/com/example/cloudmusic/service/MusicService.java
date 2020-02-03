package com.example.cloudmusic.service;

import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.example.cloudmusic.MusicMetaData;
import com.example.cloudmusic.MyApplication;
import com.example.cloudmusic.R;
import com.example.cloudmusic.item.Music;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MusicService extends Service {
    private static final String TAG = "csqMusicService";
    public static final int MEDIA_PLAYER_START = 1;
    public static final int MEDIA_PLAYER_PLAY = 2;
    public static final int MEDIA_PLAYER_PAUSE = 3;
    public static final int MEDIA_PLAYER_STOP = 4;

    private MediaPlayer mMediaPlayer;
    private LocalBinder mLocalBinder = new LocalBinder();
    private MediaMetadataRetriever mMediaMetadataRetriever = new MediaMetadataRetriever();
    private List<Music> mMusicList = new ArrayList<>();

    public MusicService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mMusicList = ((MyApplication) getApplication()).getMMusicList();
        mMediaPlayer = new MediaPlayer();
        ((MyApplication) getApplication()).setMMediaPlayer(mMediaPlayer);
        Log.i(TAG, "onCreate: musicService的list" + mMusicList);
    }

    public MusicMetaData getMetaData(String path) {
        mMediaMetadataRetriever.setDataSource(path);
        byte[] picture = mMediaMetadataRetriever.getEmbeddedPicture();
        Bitmap bitmap = BitmapFactory.decodeByteArray(picture, 0, picture.length);
        String title = mMediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE); // API LEVEL 10, 即从GB2.3.3开始有此功能
        Log.d(TAG, "title:" + title);
        String artist = mMediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
        Log.d(TAG, "artist:" + artist);
        String duration = mMediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION); // 播放时长单位为毫秒
        String time = timeParse(duration);
        Log.d(TAG, "duration:" + time);
        MusicMetaData musicMetaData = new MusicMetaData(bitmap, title, artist, time);
        ((MyApplication)getApplication()).setMMusicMetaData(musicMetaData);
        return musicMetaData;
    }

    //初始化MediaPlayer并播放指定位置的音乐
    public void initMediaPlayer(String path) {
        mMediaPlayer.reset();
        try {
            //在build.gradle(app)改为targetSdkVersion 22（不然会出现FileNotFoundException）
            mMediaPlayer.setDataSource(path);
            mMediaPlayer.prepare();
        } catch (IOException e) {
            Log.e(TAG, "initMediaPlayer: 初始化音源异常", e);
        }
        mMediaPlayer.start();
        ((MyApplication)getApplication()).setMMusicState(MEDIA_PLAYER_PLAY);
        ((MyApplication)getApplication()).setMMediaState(true);
        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer arg0) {
                mMediaPlayer.start();
                ((MyApplication)getApplication()).setMMusicState(MEDIA_PLAYER_PLAY);
                mMediaPlayer.setLooping(true);
            }
        });
    }

    //播放暂停
    public void changeMusicState() {
        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.pause();
            ((MyApplication)getApplication()).setMMusicState(MEDIA_PLAYER_PAUSE);
        } else {
            mMediaPlayer.start();
            ((MyApplication)getApplication()).setMMusicState(MEDIA_PLAYER_PLAY);
        }
    }

    // 上一曲
    public boolean preMusic() {
        int currentPosition = ((MyApplication) getApplication()).getMPosition();
        if (currentPosition == 0) {
            initMediaPlayer(mMusicList.get(mMusicList.size() - 1).getMMusicPath());
            ((MyApplication) getApplication()).setMPosition(mMusicList.size() - 1);
            return true;
        } else if (currentPosition > 0) {
            Log.i(TAG, "preMusic: mMusicList" + mMusicList);
            initMediaPlayer(mMusicList.get(currentPosition - 1).getMMusicPath());
            ((MyApplication) getApplication()).setMPosition(currentPosition - 1);
            return true;
        } else {
            return false;
        }
    }

    //下一曲
    public boolean nextMusic() {
        int currentPosition = ((MyApplication) getApplication()).getMPosition();
        if (currentPosition == (mMusicList.size() - 1)) {
            initMediaPlayer(mMusicList.get(0).getMMusicPath());
            ((MyApplication) getApplication()).setMPosition(0);
            return true;
        } else if (currentPosition < (mMusicList.size() - 1)) {
            initMediaPlayer(mMusicList.get(currentPosition + 1).getMMusicPath());
            ((MyApplication) getApplication()).setMPosition(currentPosition + 1);
            return true;
        } else {
            return false;
        }
    }

    public boolean getMediaPlayerState() {
        return mMediaPlayer.isPlaying();
    }

    public int getCurrentPosition(){
        return mMediaPlayer.getCurrentPosition();
    }

//    private MusicMetaData getMainActivityMusicMetaData(MusicService musicService) {
//        Log.e(TAG, "initBottomDisplayUi: 准备更新main页面的底部栏");
//        Music music = mMusicList.get(((MyApplication) getApplication()).getMPosition());
////        mMusicService = ((MyApplication) getApplication()).getMMusicService();
//        musicService.getMetaData(music.getMMusicPath());
//        MusicMetaData musicMetaData = ((MyApplication) getApplication()).getMMusicMetaData();
//
//       return musicMetaData;
//    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG, "onBind: 返回binder" + mLocalBinder);
        ((MyApplication) getApplication()).setMBinder(mLocalBinder);
        return mLocalBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        mMediaPlayer.release();
        return super.onUnbind(intent);
    }

    public class LocalBinder extends Binder {
        public MusicService getService() {
            return MusicService.this;
        }
    }

    public static String timeParse(String duration) {
        String time = "";
        long durationTime = Integer.valueOf(duration);

        long minute = durationTime / 60000;
        long seconds = durationTime % 60000;

        long second = Math.round((float) seconds / 1000);
        if (minute < 10) {
            time += "0";
        }
        time += minute + ":";
        if (second < 10) {
            time += "0";
        }
        time += second;
        return time;
    }


}
