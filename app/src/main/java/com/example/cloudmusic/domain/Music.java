package com.example.cloudmusic.domain;

public class Music {
    private String mMusicName;
    private String mSingerName;
    private String mMusicPath;
    private String mLyricPath;

    public Music(String mMusicName, String mSingerName, String mMusicPath, String mLyricPath) {
        this.mMusicName = mMusicName;
        this.mSingerName = mSingerName;
        this.mMusicPath = mMusicPath;
        this.mLyricPath = mLyricPath;
    }

    public String getMMusicName() {
        return mMusicName;
    }

    public void setmMusicName(String mMusicName) {
        this.mMusicName = mMusicName;
    }

    public String getMSingerName() {
        return mSingerName;
    }

    public void setmSingerName(String mSingerName) {
        this.mSingerName = mSingerName;
    }

    public String getMMusicPath() {
        return mMusicPath;
    }

    public void setmMusicPath(String mMusicPath) {
        this.mMusicPath = mMusicPath;
    }

    public String getMLyricPath() {
        return mLyricPath;
    }

    public void setMLyricPath(String mLyricPath) {
        this.mLyricPath = mLyricPath;
    }
}
