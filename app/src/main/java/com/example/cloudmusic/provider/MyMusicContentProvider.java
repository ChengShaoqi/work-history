package com.example.cloudmusic.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.example.cloudmusic.db.MusicDbHelper;

public class MyMusicContentProvider extends ContentProvider {
    private static final String TAG = "csqMyMusicContentProvider";
    public static final int MUSIC_DIR = 0;
    public static final int MUSIC_ITEM = 1;


    public static final String AUTHORITY = "com.example.cloudmusic.provider";
    private static UriMatcher mUriMatcher;
    private MusicDbHelper mDbHelper;

    static {
        mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        mUriMatcher.addURI(AUTHORITY, "music", MUSIC_DIR);
        mUriMatcher.addURI(AUTHORITY, "music/#", MUSIC_ITEM);

    }

    public MyMusicContentProvider() {
    }

    @Override
    public boolean onCreate() {
        mDbHelper = new MusicDbHelper(getContext(), "MusicStore.db", null, 1);
        return true;
    }

    @Override
    public String getType(Uri uri) {
        switch (mUriMatcher.match(uri)) {
            case MUSIC_DIR:
                return "vnd.android.cursor.dir/vnd.com.example.cloudmusic.provider.music";
            case MUSIC_ITEM:
                return "vnd.android.cursor.item/vnd.com.example.cloudmusic.provider.music";
        }
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        Uri uriReturn = null;
        switch (mUriMatcher.match(uri)) {
            case MUSIC_DIR:
            case MUSIC_ITEM:
                long newContactId = db.insert("Music", null, values);
                uriReturn = Uri.parse("content://" + AUTHORITY + "/music/" + newContactId);
                break;
            default:
                break;
        }

        getContext().getContentResolver().notifyChange(uri,null);

        return uriReturn;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Cursor cursor = null;
        switch (mUriMatcher.match(uri)) {
            case MUSIC_DIR:
                cursor = db.query("Music", projection, selection,
                        selectionArgs, null, null, sortOrder);
                break;
            case MUSIC_ITEM:
                String musicId = uri.getPathSegments().get(1);
                cursor = db.query("Music", projection, "id = ?",
                        new String[]{musicId}, null, null, sortOrder);
                break;
            default:
                break;
        }

        if(cursor!=null){
            //添加通知对象
            cursor.setNotificationUri(getContext().getContentResolver(),uri);
        }

        return cursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        int updatedRows = 0;
        switch (mUriMatcher.match(uri)) {
            case MUSIC_DIR:
                updatedRows = db.update("Music", values, selection, selectionArgs);
                break;
            case MUSIC_ITEM:
                String musicId = uri.getPathSegments().get(1);
                updatedRows = db.update("Music", values, "id = ?", new String[]{musicId});
                break;
            default:
                break;
        }

        if(updatedRows>0){     //通知，数据源发生改变
            getContext().getContentResolver().notifyChange(uri,null);
        }

        return updatedRows;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        int deletedRows = 0;
        switch (mUriMatcher.match(uri)) {
            case MUSIC_DIR:
                deletedRows = db.delete("Music", selection, selectionArgs);
                break;
            case MUSIC_ITEM:
                String musicId = uri.getPathSegments().get(1);
                deletedRows = db.delete("Music", "id = ?", new String[]{musicId});
                break;
            default:
                break;
        }

        getContext().getContentResolver().notifyChange(uri,null);

        return deletedRows;
    }
}
