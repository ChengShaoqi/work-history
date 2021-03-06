package com.example.cloudmusic.activity;

import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.cloudmusic.adapter.SongListAdapter;
import com.example.cloudmusic.domain.MusicMetaData;
import com.example.cloudmusic.MyApplication;
import com.example.cloudmusic.R;
import com.example.cloudmusic.adapter.CloudMusicFragmentPagerAdapter;
import com.example.cloudmusic.adapter.LoopPicturePagerAdapter;
import com.example.cloudmusic.db.MusicDbHelper;
import com.example.cloudmusic.domain.SongList;
import com.example.cloudmusic.fragment.FindFragment;
import com.example.cloudmusic.fragment.MineFragment;
import com.example.cloudmusic.domain.Music;
import com.example.cloudmusic.service.MusicService;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

import static com.example.cloudmusic.service.MusicService.MEDIA_PLAYER_PAUSE;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, FindFragment.IFindFragmentCallBack,
        MineFragment.IMineFragmentCallBack, MusicService.MainActivityCallback {
    private static final String TAG = "csqMainActivity";
    public static final int MAIN_ACTIVITY_STATE_CODE = 1;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private ViewPager mMainActivityViewPager;
    private int[] mPictureViewResId;
    private List<Fragment> mFragmentList = new ArrayList<>();
    private CloudMusicFragmentPagerAdapter mCloudMusicFragmentPagerAdapter;
    private ImageView mIvBarMenu;
    private TextView mTvToolBarMine;
    private TextView mTvToolBarFind;
    private ImageView mIvBarSearch;
    private TextView mTvToolBarCloud_village;
    private TextView mTvToolBarVideo;
    private LinearLayout mLinearLayout;
    private MusicService mMusicService;
    private String[] mMusicNameList;
    private String[] mSingerNameList;
    private String[] mMusicPathList;
    private String[] mLyricPath;
    private MusicDbHelper mMusicDbHelper;
    private ImageView mMusicImage;
    private TextView mMusicName;
    private TextView mSingerName;
    private LinearLayout mMusicLayout;
    private ImageView mPreMusic;
    private ImageView mMusicState;
    private ImageView mNextMusic;
    private List<Music> mMusicList = new ArrayList<>();
    private LinearLayout mSettingLayout;
    private int[] mRankListImageResId = {
            R.mipmap.ranklist_acg,
            R.mipmap.ranklist_first,
            R.mipmap.ranklist_second,
            R.mipmap.ranklist_third,
            R.mipmap.ranklist_fifth,
            R.mipmap.ranklist_six};
    private List<SongList> mSongList = new ArrayList<>();
    private int[] mSongListImageId = new int[]{
            R.drawable.song_list_1,
            R.drawable.song_list_2,
            R.drawable.song_list_3,
            R.drawable.song_list_4,
            R.drawable.song_list_5,
            R.drawable.song_list_6,
            R.drawable.song_list_7,
            R.drawable.song_list_8,
            R.drawable.song_list_9,
            R.drawable.song_list_10,
            R.drawable.song_list_11,
            R.drawable.song_list_12
    };
    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.LocalBinder localBinder = (MusicService.LocalBinder) service;
            mMusicService = localBinder.getService();
            ((MyApplication) getApplication()).setMMusicService(mMusicService);
            mMusicService.setMainActivityCallback(MainActivity.this);
            Log.i(TAG, "onServiceConnected: " + mMusicService);
            initBottomDisplayUi();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.e(TAG, "onServiceDisconnected: 服务出现异常");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mMusicDbHelper = new MusicDbHelper(this, "MusicStore.db", null, 1);
        //初始化数据库数据
        initDatabaseData();
        //初始化我的歌单的数据
        initMineFragmentSongListData();

        initView();

        initViewPager();

        initLoopPictureView();
        //绑定服务
        Intent intent = new Intent(this, MusicService.class);
        startService(intent);
        bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
        ((MyApplication) getApplication()).setMServiceConnection(mServiceConnection);
        ((MyApplication) getApplication()).setMCurrentActivity(MAIN_ACTIVITY_STATE_CODE);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        ((MyApplication) getApplication()).setMCurrentActivity(MAIN_ACTIVITY_STATE_CODE);
        initBottomDisplayUi();
    }

    @Override
    protected void onDestroy() {
        unbindService(((MyApplication) getApplication()).getMServiceConnection());
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.bar_menu:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.bar_mine:
                mMainActivityViewPager.setCurrentItem(0);
                mTvToolBarMine.setTextColor(getResources().getColor(R.color.black));
                mTvToolBarFind.setTextColor(getResources().getColor(R.color.initColor));
                break;
            case R.id.bar_find:
                mMainActivityViewPager.setCurrentItem(1);
                mTvToolBarMine.setTextColor(getResources().getColor(R.color.initColor));
                mTvToolBarFind.setTextColor(getResources().getColor(R.color.black));
                break;
            case R.id.bottom_music_linear_layout:
                Log.d(TAG, "onClick: 点击了底部信息栏");
                Intent intent = new Intent(this, MusicDetailActivity.class);
                startActivity(intent);
                break;
            case R.id.pre_music:
                ((MyApplication) getApplication()).getMMusicService().preMusic();
                initBottomDisplayUi();
                break;
            case R.id.music_state_button:
                MyApplication ma = ((MyApplication) getApplication());
                Log.i(TAG, "onClick: 改变播放状态按钮" + ma.getMMusicService());
                if (!ma.getMMediaState()) {
                    ma.getMMusicService().initMediaPlayerAndPlayMusic(mMusicList.get(ma.getMPosition()).getMMusicPath());
                    mMusicState.setImageResource(R.mipmap.pause_music);
                } else {
                    ma.getMMusicService().changeMusicState();
                    int musicState = ((MyApplication) getApplication()).getMMusicState();
                    if (musicState == MEDIA_PLAYER_PAUSE) {
                        mMusicState.setImageResource(R.mipmap.start_music);
                    } else {
                        mMusicState.setImageResource(R.mipmap.pause_music);
                    }
                }
                break;
            case R.id.next_music:
                ((MyApplication) getApplication()).getMMusicService().nextMusic();
                initBottomDisplayUi();
                break;
            case R.id.setting_layout:
                Intent intentToSettings = new Intent(this, SettingActivity.class);
                startActivity(intentToSettings);
                break;
            default:
                break;
        }
    }

    private void initBottomDisplayUi() {
        Log.e(TAG, "initBottomDisplayUi: 准备更新main页面的底部栏");
        Music music = mMusicList.get(((MyApplication) getApplication()).getMPosition());
        Log.e(TAG, "initBottomDisplayUi: " + music);
        Log.e(TAG, "initBottomDisplayUi: " + mMusicService);

        mMusicService.getMetaData(music.getMMusicPath());
        MusicMetaData musicMetaData = ((MyApplication) getApplication()).getMMusicMetaData();

        mMusicImage.setImageBitmap(musicMetaData.getMMusicCoverImage());
        mMusicName.setText(musicMetaData.getMMusicName());
        mSingerName.setText(musicMetaData.getMSingerName());
        mMusicState.setImageResource(((MyApplication) getApplication()).getMMediaState() ?
                ((MyApplication) getApplication()).getMMusicState() == MusicService.MEDIA_PLAYER_PAUSE ?
                        R.mipmap.start_music : R.mipmap.pause_music :
                R.mipmap.start_music);
    }

    private void initDatabaseData() {
        mMusicNameList = new String[]{
                getString(R.string.song_name_dqsj),
                getString(R.string.song_name_pxyz),
                getString(R.string.song_name_jh),
                getString(R.string.song_name_mzzj)};
        mSingerNameList = new String[]{
                getString(R.string.singer_xs),
                getString(R.string.singer_xs),
                getString(R.string.singer_xs),
                getString(R.string.singer_xs)};
        mMusicPathList = new String[]{
                getString(R.string.song_path_xs_dqsj),
                getString(R.string.song_path_xs_pxyz),
                getString(R.string.song_path_xs_jh),
                getString(R.string.song_path_xs_mzzj)};

        mLyricPath = new String[]{
                getString(R.string.song_lrc_path_xs_dqsj),
                getString(R.string.song_lrc_path_xs_pxyz),
                getString(R.string.song_lrc_path_xs_jh),
                getString(R.string.song_lrc_path_xs_mzzj)
        };
        mMusicList = queryMusicData();
        //如果查询到数据库的歌曲个数和要插入的个数相等，则直接返回，否则进行数据库插入数据
        if (mMusicList.size() == mMusicPathList.length) {
            return;
        }

        SQLiteDatabase db = mMusicDbHelper.getWritableDatabase();
        for (int i = 0; i < mMusicPathList.length; i++) {
            ContentValues values = new ContentValues();
            values.put("music_name", mMusicNameList[i]);
            values.put("singer_name", mSingerNameList[i]);
            values.put("path", mMusicPathList[i]);
            values.put("lyric_path", mLyricPath[i]);
            db.insert("Music", null, values);
        }
    }

    public List<Music> queryMusicData() {
        mMusicList.clear();
        Uri uri = Uri.parse("content://com.example.cloudmusic.provider/music");
        Cursor cursor = getContentResolver().query(uri, null, null,
                null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String musicName = cursor.getString(cursor.getColumnIndex("music_name"));
                String singerName = cursor.getString(cursor.getColumnIndex("singer_name"));
                String path = cursor.getString(cursor.getColumnIndex("path"));
                String lyricPath = cursor.getString(cursor.getColumnIndex("lyric_path"));
                Music music = new Music(musicName, singerName, path, lyricPath);
                mMusicList.add(music);
            }
        }
        if (cursor != null) {
            cursor.close();
        }
        Log.d(TAG, "queryMusicData: " + mMusicList.size());
        Log.i(TAG, "queryMusicData: 准备setMMusicList");
        ((MyApplication) getApplication()).setMMusicList(mMusicList);
        return ((MyApplication) getApplication()).getMMusicList();
    }

    private void initMineFragmentSongListData() {
        for (int i = 0; i < mSongListImageId.length; i++) {
            SongList songList = new SongList(mSongListImageId[i],
                    getString(R.string.fragment_mine_songList_name) + (i + 1),
                    (i + 1) + getString(R.string.fragment_mine_songList_number));
            mSongList.add(songList);
        }
    }

    private void initView() {
        mDrawerLayout = findViewById(R.id.draw_layout);
        mNavigationView = findViewById(R.id.nav_view);
        mIvBarMenu = findViewById(R.id.bar_menu);
        mTvToolBarMine = findViewById(R.id.bar_mine);
        mTvToolBarFind = findViewById(R.id.bar_find);
        mTvToolBarCloud_village = findViewById(R.id.bar_cloud_village);
        mTvToolBarVideo = findViewById(R.id.bar_video);
        mIvBarSearch = findViewById(R.id.bar_search);
        mMainActivityViewPager = findViewById(R.id.activity_main_viewpager);
        mLinearLayout = findViewById(R.id.HorizontalScrollView_item);
        mMusicImage = findViewById(R.id.bottom_container_image);
        mMusicName = findViewById(R.id.local_music_name);
        mSingerName = findViewById(R.id.local_music_singer_name);
        mMusicLayout = findViewById(R.id.bottom_music_linear_layout);
        mPreMusic = findViewById(R.id.pre_music);
        mMusicState = findViewById(R.id.music_state_button);
        mNextMusic = findViewById(R.id.next_music);
        mSettingLayout = findViewById(R.id.setting_layout);

        mIvBarMenu.setOnClickListener(this);
        mTvToolBarMine.setOnClickListener(this);
        mTvToolBarFind.setOnClickListener(this);
        mTvToolBarCloud_village.setOnClickListener(this);
        mTvToolBarVideo.setOnClickListener(this);
        mIvBarSearch.setOnClickListener(this);
        mMusicLayout.setOnClickListener(this);
        mPreMusic.setOnClickListener(this);
        mMusicState.setOnClickListener(this);
        mNextMusic.setOnClickListener(this);
        //显示出导航栏每项图标
        mNavigationView.setItemIconTintList(null);
        mSettingLayout.setOnClickListener(this);
    }

    private void initLoopPictureView() {
        mPictureViewResId = new int[]{
                R.mipmap.seventh, R.mipmap.first, R.mipmap.second, R.mipmap.third, R.mipmap.fourth,
                R.mipmap.fifth, R.mipmap.sixth, R.mipmap.seventh, R.mipmap.first
        };
    }

    private void initViewPager() {
        mMainActivityViewPager.addOnPageChangeListener(new MyPagerChangeListener());
        mFragmentList.add(new MineFragment(MainActivity.this));
        mFragmentList.add(new FindFragment(MainActivity.this));

        mCloudMusicFragmentPagerAdapter = new CloudMusicFragmentPagerAdapter(getSupportFragmentManager(),
                mFragmentList);
        mMainActivityViewPager.setAdapter(mCloudMusicFragmentPagerAdapter);
        mMainActivityViewPager.setCurrentItem(1);
        mTvToolBarFind.setTextColor(getResources().getColor(R.color.black));//初始化显示第一个页面
    }

    @Override
    public void updateFindFragmentUi(final ViewPager viewPager) {
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            private int mCurrentPosition;

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mCurrentPosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == ViewPager.SCROLL_STATE_IDLE) {
                    if (mCurrentPosition == viewPager.getAdapter().getCount() - 1) {
                        viewPager.setCurrentItem(1, false);
                    } else if (mCurrentPosition == 0) {
                        viewPager.setCurrentItem(viewPager.getAdapter().getCount() - 2,
                                false);
                    }
                }
            }
        });
        viewPager.setAdapter(new LoopPicturePagerAdapter(MainActivity.this, mPictureViewResId));

        loopPictureInThread(viewPager);

    }

    //轮询图片
    private void loopPictureInThread(final ViewPager viewPager) {
        new Thread() {
            public void run() {
                do {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            int currentItem = viewPager.getCurrentItem();
                            if (currentItem == mPictureViewResId.length - 1) {
                                viewPager.setCurrentItem(0);
                            } else {
                                viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                            }
                        }
                    });
                } while (true);
            }
        }.start();
    }

    @Override
    public void updateFindFragmentUi(LinearLayout linearLayout) {
        String[] rankListTitle = {
                getString(R.string.rank_list_acgyyb),
                getString(R.string.rank_list_xgb),
                getString(R.string.rank_list_ycb),
                getString(R.string.rank_list_rgb),
                getString(R.string.rank_list_gdyyb),
                getString(R.string.rank_list_bsb)
        };
        for (int i = 0; i < mRankListImageResId.length; i++) {
            View view = LayoutInflater.from(this).inflate(R.layout.song_list_item, linearLayout, false);
            ImageView imageView = view.findViewById(R.id.song_list_image);
            TextView textView = view.findViewById(R.id.song_list_text);
            imageView.setImageResource(mRankListImageResId[i]);
            textView.setText(rankListTitle[i]);
            linearLayout.addView(view);
        }
    }

    @Override
    public void updateMineFragmentUi(RecyclerView recyclerView) {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        SongListAdapter songListAdapter = new SongListAdapter(mSongList);
        recyclerView.setAdapter(songListAdapter);
    }

    @Override
    public void updateBottomUi() {
        initBottomDisplayUi();
    }

    private class MyPagerChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            switch (position) {
                case 0:
                    mTvToolBarMine.setTextColor(getResources().getColor(R.color.black));
                    mTvToolBarFind.setTextColor(getResources().getColor(R.color.initColor));
                    break;
                case 1:
                    mTvToolBarMine.setTextColor(getResources().getColor(R.color.initColor));
                    mTvToolBarFind.setTextColor(getResources().getColor(R.color.black));
                    break;
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

}
