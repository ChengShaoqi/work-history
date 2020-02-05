package com.example.cloudmusic;

import android.app.Application;
import android.content.ServiceConnection;
import android.media.MediaPlayer;

import com.example.cloudmusic.domain.Music;
import com.example.cloudmusic.domain.MusicMetaData;
import com.example.cloudmusic.service.MusicService;

import java.util.ArrayList;
import java.util.List;

public class MyApplication extends Application {

    private List<Music> mMusicList = new ArrayList<>();
    private int mPosition = 0;
    private Music mMusic;
    private MusicService.LocalBinder mBinder;
    private ServiceConnection mServiceConnection;
    private MusicService mMusicService;
    private MusicMetaData mMusicMetaData;
    private boolean mMediaState;
    private int mMusicState = MusicService.MEDIA_PLAYER_PAUSE;
    private MediaPlayer mMediaPlayer;
    private int mCurrentActivity;

    public List<Music> getMMusicList() {
        return mMusicList;
    }

    public void setMMusicList(List<Music> mMusicList) {
        this.mMusicList = mMusicList;
    }

    public int getMPosition() {
        return mPosition;
    }

    public void setMPosition(int mPosition) {
        this.mPosition = mPosition;
    }


    public Music getMMusic() {
        return mMusic;
    }

    public void setMMusic(Music mMusic) {
        this.mMusic = mMusic;
    }

    public MusicService.LocalBinder getMBinder() {
        return mBinder;
    }

    public void setMBinder(MusicService.LocalBinder mBinder) {
        this.mBinder = mBinder;
    }

    public ServiceConnection getMServiceConnection() {
        return mServiceConnection;
    }

    public void setMServiceConnection(ServiceConnection mServiceConnection) {
        this.mServiceConnection = mServiceConnection;
    }

    public MusicService getMMusicService() {
        return mMusicService;
    }

    public void setMMusicService(MusicService mMusicService) {
        this.mMusicService = mMusicService;
    }

    public MusicMetaData getMMusicMetaData() {
        return mMusicMetaData;
    }

    public void setMMusicMetaData(MusicMetaData mMusicMetaData) {
        this.mMusicMetaData = mMusicMetaData;
    }

    public boolean getMMediaState() {
        return mMediaState;
    }

    public void setMMediaState(boolean mMediaState) {
        this.mMediaState = mMediaState;
    }

    public int getMMusicState() {
        return mMusicState;
    }

    public void setMMusicState(int mMusicState) {
        this.mMusicState = mMusicState;
    }

    public MediaPlayer getMMediaPlayer() {
        return mMediaPlayer;
    }

    public void setMMediaPlayer(MediaPlayer mMediaPlayer) {
        this.mMediaPlayer = mMediaPlayer;
    }

    public int getMCurrentActivity() {
        return mCurrentActivity;
    }

    public void setMCurrentActivity(int mCurrentActivity) {
        this.mCurrentActivity = mCurrentActivity;
    }
}
