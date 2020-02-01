//package com.example.cloudmusic.util;
//
//import android.content.ContentValues;
//import android.database.sqlite.SQLiteDatabase;
//
//public class MusicUtils {
//    private static final String TAG = "csqMusicUtils";
//    public static void initDataBase() {
//        String[] mMusicNameList;
//        String[] mSingerNameList;
//        String[] mMusicPathList;
//        String[] mLyricPath;
//        InitDatabaseData initDatabaseData = new InitDatabaseData().invoke();
//        mMusicNameList = initDatabaseData.getmMusicNameList();
//        mSingerNameList = initDatabaseData.getmSingerNameList();
//        mMusicPathList = initDatabaseData.getmMusicPathList();
//        mLyricPath = initDatabaseData.getmLyricPath();
////        mMusicList = queryMusicData();
////        if (mMusicList.size() == mMusicPathList.length) {
////            return;
////        }
//
//        SQLiteDatabase db = mMusicDbHelper.getWritableDatabase();
//        for (int i = 0; i < mMusicPathList.length; i++) {
//            ContentValues values = new ContentValues();
//            values.put("music_name", mMusicNameList[i]);
//            values.put("singer_name", mSingerNameList[i]);
//            values.put("path", mMusicPathList[i]);
//            values.put("lyric_path", mLyricPath[i]);
//            db.insert("Music", null, values);
//        }
//    }
//
//    private static class InitDatabaseData {
//        private String[] mMusicNameList;
//        private String[] mSingerNameList;
//        private String[] mMusicPathList;
//        private String[] mLyricPath;
//
//        public String[] getmMusicNameList() {
//            return mMusicNameList;
//        }
//
//        public String[] getmSingerNameList() {
//            return mSingerNameList;
//        }
//
//        public String[] getmMusicPathList() {
//            return mMusicPathList;
//        }
//
//        public String[] getmLyricPath() {
//            return mLyricPath;
//        }
//
//        public InitDatabaseData invoke() {
//            mMusicNameList = new String[]{"大千世界", "平行宇宙", "江湖", "明智之举"};
//            mSingerNameList = new String[]{"许嵩", "许嵩", "许嵩", "许嵩"};
//            mMusicPathList = new String[]{
//                    "/sdcard/netease/cloudmusic/Music/许嵩 - 大千世界.mp3",
//                    "/sdcard/netease/cloudmusic/Music/许嵩 - 平行宇宙.mp3",
//                    "/sdcard/netease/cloudmusic/Music/许嵩 - 江湖.mp3",
//                    "/sdcard/netease/cloudmusic/Music/许嵩 - 明智之举.mp3"};
//
//            mLyricPath = new String[]{
//                    "/sdcard/tencent/qqfile_recv/许嵩 - 大千世界.lrc",
//                    "/sdcard/tencent/qqfile_recv/许嵩 - 平行宇宙.lrc",
//                    "/sdcard/tencent/qqfile_recv/许嵩 - 江湖.lrc",
//                    "/sdcard/tencent/qqfile_recv/许嵩 - 明智之举.lrc"
//            };
//            return this;
//        }
//    }
//}
