package com.example.cloudmusic.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class MusicDbHelper extends SQLiteOpenHelper {

    public static final String CREATE_MUSIC = "create table Music ("
            + "_id integer primary key autoincrement, "
            + "music_name text, "
            + "singer_name text, "
            + "path text, "
            + "lyric_path text)";


    private Context mContext;

    public MusicDbHelper(@Nullable Context context, @Nullable String name,
                         @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_MUSIC);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop table if exists Music");
        onCreate(sqLiteDatabase);
    }
}
