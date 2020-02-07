package com.example.cloudmusic.domain;

public class SongList {
    private int mImageId;
    private String mSongListName;
    private String mSongNumber;

    public SongList(int imageId, String songListName, String songNumber) {
        this.mImageId = imageId;
        this.mSongListName = songListName;
        this.mSongNumber = songNumber;
    }

    public int getMImageId() {
        return mImageId;
    }

    public void setMImageId(int imageId) {
        this.mImageId = imageId;
    }

    public String getMSongListName() {
        return mSongListName;
    }

    public void setMSongListName(String songListName) {
        this.mSongListName = songListName;
    }

    public String getMSongNumber() {
        return mSongNumber;
    }

    public void setMSongNumber(String songNumber) {
        this.mSongNumber = songNumber;
    }
}
