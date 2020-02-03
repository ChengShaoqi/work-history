package com.example.cloudmusic.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cloudmusic.MusicMetaData;
import com.example.cloudmusic.MyApplication;
import com.example.cloudmusic.R;
import com.example.cloudmusic.adapter.LocalMusicAdapter;
import com.example.cloudmusic.db.MusicDbHelper;
import com.example.cloudmusic.fragment.LocalMusicFragment;
import com.example.cloudmusic.fragment.LocalMusicOtherFragment;
import com.example.cloudmusic.item.Music;
import com.example.cloudmusic.service.MusicService;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import static com.example.cloudmusic.service.MusicService.MEDIA_PLAYER_PAUSE;

public class LocalMusicActivity extends AppCompatActivity implements LocalMusicFragment.ILocalMusicCallback, View.OnClickListener {
    private static final String TAG = "csqLocalMusicActivity";
    private List<String> mTitleList;
    private List<Fragment> mFragmentList;
    private List<Music> mMusicList = new ArrayList<>();
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private LinearLayout mLinearLayout;
    private ImageView mMusicImage;
    private TextView mMusicName;
    private TextView mSingerName;
    private ImageView mPreMusic;
    private ImageView mMusicState;
    private ImageView mNextMusic;
    private MusicMetaData mMusicMetaData;
    private MusicService mMusicService;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    //未完成
                    break;
                case 1:
                    mMusicImage.setImageBitmap(mMusicMetaData.getMMusicCoverImage());
                    mMusicName.setText(mMusicMetaData.getMMusicName());
                    mSingerName.setText(mMusicMetaData.getMSingerName());
                    mMusicState.setImageResource(((MyApplication) getApplication()).getMMediaState() ?
                            ((MyApplication) getApplication()).getMMusicState() == MusicService.MEDIA_PLAYER_PAUSE ?
                                    R.mipmap.start_music : R.mipmap.pause_music :
                            R.mipmap.start_music);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_music);

        //初始化Toolbar
        initToolbar();

        mMusicList = ((MyApplication) getApplication()).getMMusicList();
        //初始化LocalMusicActivity的tabLayout和viewPager
        initView();
        //渲染LocalMusicActivity的tabLayout和viewPager
        initViewData();


        initBottomDisplayUi(((MyApplication) getApplication()).getMPosition());
        Log.i(TAG, "onCreate: " + mMusicMetaData.getMMusicName());

//        Music music = mMusicList.get(((MyApplication) getApplication()).getMPosition());
//        mMusicMetaData = ((MyApplication) getApplication()).getMMusicService().getMetaData(music.getMMusicPath());
//        mMusicImage.setImageBitmap(mMusicMetaData.getMMusicCoverImage());
//        mMusicName.setText(mMusicMetaData.getMMusicName());
//        mSingerName.setText(mMusicMetaData.getMSingerName());
//        mMusicState.setImageResource(R.mipmap.start_music);

//        Log.e(TAG, "onCreate:0 " + ((MyApplication) getApplication()).getMMusicService());

    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar_local_music);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("本地音乐");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        toolbar.setBackgroundColor(getResources().getColor(R.color.mainColor));
    }

    @Override
    protected void onStart() {
        initBottomDisplayUi(((MyApplication) getApplication()).getMPosition());
        Log.i(TAG, "onStart: " + mMusicMetaData.getMMusicName());

        super.onStart();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bottom_music_linear_layout:
                Log.d(TAG, "onClick: 点击了底部信息栏");
                Intent intent = new Intent(this, MusicDetailActivity.class);
                startActivity(intent);
                break;
            case R.id.pre_music:
                ((MyApplication) getApplication()).getMMusicService().preMusic();
                initBottomDisplayUi(((MyApplication) getApplication()).getMPosition());
                break;
            case R.id.music_state_button:
                MyApplication ma = ((MyApplication) getApplication());
                Log.i(TAG, "onClick: 改变播放状态按钮" + ma.getMMusicService());
                if (!ma.getMMediaState()) {
                    ma.getMMusicService().initMediaPlayer(mMusicList.get(ma.getMPosition()).getMMusicPath());
                    mMusicState.setImageResource(R.mipmap.pause_music);
                } else {
                    ma.getMMusicService().changeMusicState();
                    int musicState =  ((MyApplication)getApplication()).getMMusicState();
                    if (musicState == MEDIA_PLAYER_PAUSE) {
                        mMusicState.setImageResource(R.mipmap.start_music);
                    } else {
                        mMusicState.setImageResource(R.mipmap.pause_music);
                    }
                }
                break;
            case R.id.next_music:
                ((MyApplication) getApplication()).getMMusicService().nextMusic();
                initBottomDisplayUi(((MyApplication) getApplication()).getMPosition());
                break;
            default:
                break;
        }
    }


    private void initView() {
        mTabLayout = findViewById(R.id.local_music_tab_layout);
        mViewPager = findViewById(R.id.local_music_view_pager);
        mLinearLayout = findViewById(R.id.bottom_music_linear_layout);
        mMusicImage = findViewById(R.id.bottom_container_image);
        mMusicName = findViewById(R.id.local_music_name);
        mSingerName = findViewById(R.id.local_music_singer_name);
        mPreMusic = findViewById(R.id.pre_music);
        mMusicState = findViewById(R.id.music_state_button);
        mNextMusic = findViewById(R.id.next_music);

        mLinearLayout.setOnClickListener(this);
        mPreMusic.setOnClickListener(this);
        mMusicState.setOnClickListener(this);
        mNextMusic.setOnClickListener(this);
    }

    private void initViewData() {
        mTitleList = new ArrayList<>();
        mTitleList.add("单曲");
        mTitleList.add("歌手");
        mTitleList.add("专辑");
        mTitleList.add("文件夹");

        mFragmentList = new ArrayList<>();
        mFragmentList.add(new LocalMusicFragment(this));
        for (int i = 1; i < mTitleList.size(); i++) {
            LocalMusicOtherFragment localMusicOtherFragment = LocalMusicOtherFragment.newInstance(mTitleList.get(i));
            mFragmentList.add(localMusicOtherFragment);
        }

        for (int i = 0; i < mTitleList.size(); i++) {
            mTabLayout.addTab(mTabLayout.newTab().setTag(mTitleList.get(i)));
        }

        FragmentPagerAdapter fragmentPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @NonNull
            @Override
            public Fragment getItem(int position) {
                return mFragmentList.get(position);
            }

            @Override
            public int getCount() {
                return mFragmentList.size();
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                return mTitleList.get(position);
            }
        };
        mViewPager.setAdapter(fragmentPagerAdapter);

        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setTabsFromPagerAdapter(fragmentPagerAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_local_music_action_bar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int selectedId = item.getItemId();
        switch (selectedId) {
            case android.R.id.home:
                LocalMusicActivity.this.finish();
                break;
            case R.id.search_button:
                Toast.makeText(LocalMusicActivity.this, "您点击了搜索按钮",
                        Toast.LENGTH_SHORT).show();
                break;
            case R.id.download_manage:
                Toast.makeText(LocalMusicActivity.this, "您点击了下载管理按钮",
                        Toast.LENGTH_SHORT).show();
                break;
            case R.id.scan_local_music:
                Toast.makeText(LocalMusicActivity.this, "您点击了扫描本地音乐按钮",
                        Toast.LENGTH_SHORT).show();
                break;
            case R.id.choose_sort_way:
                Toast.makeText(LocalMusicActivity.this, "您点击了选择排序方式按钮",
                        Toast.LENGTH_SHORT).show();
                break;
            case R.id.get_cover_lyrics:
                Toast.makeText(LocalMusicActivity.this, "您点击了获取封面歌词按钮",
                        Toast.LENGTH_SHORT).show();
                break;
            case R.id.update_quality:
                Toast.makeText(LocalMusicActivity.this, "您点击了升级音质按钮",
                        Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void initBottomDisplayUi(int position) {
        Music music = mMusicList.get(position);
        mMusicService = ((MyApplication) getApplication()).getMMusicService();
        mMusicService.getMetaData(music.getMMusicPath());
        mMusicMetaData = ((MyApplication) getApplication()).getMMusicMetaData();
        mHandler.sendEmptyMessage(1);

//        if (!mediaState) {
//            Log.i(TAG, "initBottomDisplayUi: ms == null");
//            Log.d(TAG, "initBottomDisplayUi: " + mediaState);
//            mHandler.sendEmptyMessage(1);
//            }else {
//            Log.d(TAG, "initBottomDisplayUi: " + mediaState);
//
//            mHandler.sendEmptyMessage(2);
//        }
//        ((MyApplication) getApplication()).getMMusicService().initMediaPlayer(music.getMMusicPath());

    }

    public void startDisplayMusic(int position) {
        Music music = mMusicList.get(position);
        ((MyApplication) getApplication()).getMMusicService().initMediaPlayer(music.getMMusicPath());
    }

    @Override
    public void updateLocalMusicListView(ListView listView) {
        LocalMusicAdapter localMusicAdapter = new LocalMusicAdapter(LocalMusicActivity.this,
                R.layout.local_music_item, mMusicList);
        listView.setAdapter(localMusicAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "onItemClick: 点击了第" + position + "处item");
                mMusicState.setImageResource(R.mipmap.pause_music);
                Log.i(TAG, "onItemClick: ((MyApplication) getApplication()).getMMediaState() :" +
                        ((MyApplication) getApplication()).getMMediaState() + "\n" +
                        "onItemClick: ((MyApplication) getApplication()).getMMusicState() :" +
                        ((MyApplication) getApplication()).getMMusicState());

                int hitTime = 1;
                //如果，不进行重新播放该歌曲
                if (position == ((MyApplication) getApplication()).getMPosition() && position != 0) {
//                    initBottomDisplayUi(((MyApplication) getApplication()).getMPosition());
                    Log.i(TAG, "onItemClick:(持续点击同一首歌) ((MyApplication) getApplication()).getMMediaState() :" +
                            ((MyApplication) getApplication()).getMMediaState() + "\n" +
                            "onItemClick: ((MyApplication) getApplication()).getMMusicState() :" +
                            ((MyApplication) getApplication()).getMMusicState());
                } else {
                    ((MyApplication) getApplication()).setMPosition(position);
                    Log.d(TAG, "onItemClick: getPosition()" + ((MyApplication) getApplication()).getMPosition());
                    startDisplayMusic(((MyApplication) getApplication()).getMPosition());
                    Log.i(TAG, "onItemClick:(点击另一首歌) ((MyApplication) getApplication()).getMMediaState() :" +
                            ((MyApplication) getApplication()).getMMediaState() + "\n" +
                            "onItemClick: ((MyApplication) getApplication()).getMMusicState() :" +
                            ((MyApplication) getApplication()).getMMusicState());
                    initBottomDisplayUi(((MyApplication) getApplication()).getMPosition());

//                    mMediaState = true;
//                    ((MyApplication) getApplication()).setMMediaState(mMediaState);
                }

//                Music music = mMusicList.get(position);
//                ImageView imageView = findViewById(R.id.music_display_state);
//                imageView.setVisibility(View.VISIBLE);
//                Message message = Message.obtain();
//                message.what = 0;
//                message.obj = music;
//                mHandler.sendMessage(message);
            }
        });
    }
}
