package com.example.cloudmusic;

import android.graphics.Bitmap;

public class MusicMetaData {
    private Bitmap mMusicCoverImage;
    private String mMusicName;
    private String mSingerName;
    private String mMusicDuration;

    public MusicMetaData(Bitmap mMusicCoverImage, String mMusicName, String mSingerName, String mMusicDuration) {
        this.mMusicCoverImage = mMusicCoverImage;
        this.mMusicName = mMusicName;
        this.mSingerName = mSingerName;
        this.mMusicDuration = mMusicDuration;
    }

    public Bitmap getMMusicCoverImage() {
        return mMusicCoverImage;
    }

    public void setMMusicCoverImage(Bitmap mMusicCoverImage) {
        this.mMusicCoverImage = mMusicCoverImage;
    }

    public String getMMusicName() {
        return mMusicName;
    }

    public void setMMusicName(String mMusicName) {
        this.mMusicName = mMusicName;
    }

    public String getMSingerName() {
        return mSingerName;
    }

    public void setMSingerName(String mSingerName) {
        this.mSingerName = mSingerName;
    }

    public String getMMusicDuation() {
        return mMusicDuration;
    }

    public void setMMusicDuation(String mMusicDuration) {
        this.mMusicDuration = mMusicDuration;
    }
}
