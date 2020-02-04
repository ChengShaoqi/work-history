package com.example.cloudmusic.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.animation.TimeAnimator;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.Toast;

import com.example.cloudmusic.R;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    private ImageView mActivityBackIv;
    private RelativeLayout mNetWorkRelativeLayoutItem_1;
    private RelativeLayout mNetWorkRelativeLayoutItem_2;
    private RelativeLayout mNetWorkRelativeLayoutItem_3;
    private RelativeLayout mNetWorkRelativeLayoutItem_4;
    private Switch mNetWorkSwitchItem_1;
    private Switch mNetWorkSwitchItem_2;
    private Switch mNetWorkSwitchItem_3;
    private Switch mNetWorkSwitchItem_4;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        mSharedPreferences = getSharedPreferences("switchState", MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();
        //初始化控件
        initView();
        //读取状态信息并更新
        getSwitchStateAndUpdateUi();
    }

    private void getSwitchStateAndUpdateUi() {
        if (mSharedPreferences != null) {
            boolean mNetWorkSwitchItem_1_state = mSharedPreferences.getBoolean("mNetWorkSwitchItem_1_state", false);
            mNetWorkSwitchItem_1.setChecked(mNetWorkSwitchItem_1_state);
            boolean mNetWorkSwitchItem_2_state = mSharedPreferences.getBoolean("mNetWorkSwitchItem_2_state", false);
            mNetWorkSwitchItem_2.setChecked(mNetWorkSwitchItem_2_state);
            boolean mNetWorkSwitchItem_3_state = mSharedPreferences.getBoolean("mNetWorkSwitchItem_3_state", false);
            mNetWorkSwitchItem_3.setChecked(mNetWorkSwitchItem_3_state);
            boolean mNetWorkSwitchItem_4_state = mSharedPreferences.getBoolean("mNetWorkSwitchItem_4_state", false);
            mNetWorkSwitchItem_4.setChecked(mNetWorkSwitchItem_4_state);
        }
    }

    private void initView() {
        mActivityBackIv = findViewById(R.id.setting_activity_back);
        mNetWorkRelativeLayoutItem_1 = findViewById(R.id.network_item_1);
        mNetWorkRelativeLayoutItem_2 = findViewById(R.id.network_item_2);
        mNetWorkRelativeLayoutItem_3 = findViewById(R.id.network_item_3);
        mNetWorkRelativeLayoutItem_4 = findViewById(R.id.network_item_4);
        mNetWorkSwitchItem_1 = findViewById(R.id.network_switch_1);
        mNetWorkSwitchItem_2 = findViewById(R.id.network_switch_2);
        mNetWorkSwitchItem_3 = findViewById(R.id.network_switch_3);
        mNetWorkSwitchItem_4 = findViewById(R.id.network_switch_4);

        mActivityBackIv.setOnClickListener(this);
        mNetWorkRelativeLayoutItem_1.setOnClickListener(this);
        mNetWorkRelativeLayoutItem_2.setOnClickListener(this);
        mNetWorkRelativeLayoutItem_3.setOnClickListener(this);
        mNetWorkRelativeLayoutItem_4.setOnClickListener(this);
        mNetWorkSwitchItem_1.setOnCheckedChangeListener(this);
        mNetWorkSwitchItem_2.setOnCheckedChangeListener(this);
        mNetWorkSwitchItem_3.setOnCheckedChangeListener(this);
        mNetWorkSwitchItem_4.setOnCheckedChangeListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.setting_activity_back:
                onBackPressed();
                break;
            case R.id.network_item_1:
                if (mNetWorkSwitchItem_1.isChecked()) {
                    mNetWorkSwitchItem_1.setChecked(false);
                } else {
                    mNetWorkSwitchItem_1.setChecked(true);
                }
                mEditor.putBoolean("mNetWorkSwitchItem_1_state", mNetWorkSwitchItem_1.isChecked());
                mEditor.apply();
                break;
            case R.id.network_item_2:
                if (mNetWorkSwitchItem_2.isChecked()) {
                    mNetWorkSwitchItem_2.setChecked(false);
                } else {
                    mNetWorkSwitchItem_2.setChecked(true);
                    ;
                }
                mEditor.putBoolean("mNetWorkSwitchItem_2_state", mNetWorkSwitchItem_2.isChecked());
                mEditor.apply();
                mEditor.commit();
                break;
            case R.id.network_item_3:
                if (mNetWorkSwitchItem_3.isChecked()) {
                    mNetWorkSwitchItem_3.setChecked(false);
                } else {
                    mNetWorkSwitchItem_3.setChecked(true);
                }
                mEditor.putBoolean("mNetWorkSwitchItem_3_state", mNetWorkSwitchItem_3.isChecked());
                mEditor.apply();
                break;
            case R.id.network_item_4:
                if (mNetWorkSwitchItem_4.isChecked()) {
                    mNetWorkSwitchItem_4.setChecked(false);
                } else {
                    mNetWorkSwitchItem_4.setChecked(true);
                }
                mEditor.putBoolean("mNetWorkSwitchItem_4_state", mNetWorkSwitchItem_4.isChecked());
                mEditor.apply();
                break;
            default:
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.network_switch_1:
                mEditor.putBoolean("mNetWorkSwitchItem_1_state", mNetWorkSwitchItem_1.isChecked());
                mEditor.apply();
                break;
            case R.id.network_switch_2:
                mEditor.putBoolean("mNetWorkSwitchItem_2_state", mNetWorkSwitchItem_2.isChecked());
                mEditor.apply();
                break;
            case R.id.network_switch_3:
                mEditor.putBoolean("mNetWorkSwitchItem_3_state", mNetWorkSwitchItem_3.isChecked());
                mEditor.apply();
                break;
            case R.id.network_switch_4:
                mEditor.putBoolean("mNetWorkSwitchItem_4_state", mNetWorkSwitchItem_4.isChecked());
                mEditor.apply();
                break;
            default:
                Toast.makeText(this, "点击了开关按钮", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
