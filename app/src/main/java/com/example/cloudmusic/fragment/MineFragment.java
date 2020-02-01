package com.example.cloudmusic.fragment;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.cloudmusic.R;
import com.example.cloudmusic.activity.MainActivity;


public class MineFragment extends Fragment implements View.OnClickListener{
    private IMineFragmentCallBack mIMineFragmentCallBack;

    public MineFragment(MainActivity mainActivity) {
        this.mIMineFragmentCallBack = mainActivity;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mine, container, false);
        LinearLayout linearLayout = view.findViewById(R.id.layout_local_music);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction("android.intent.action.localMusicActivity");
//                intent.setComponent(new ComponentName("com.example.cloudmusic.activity",
//                        "com.example.cloudmusic.activity.LocalMusicActivity"));
                startActivity(intent);
            }
        });
        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mIMineFragmentCallBack = (IMineFragmentCallBack) context;
    }

    public interface IMineFragmentCallBack {
        void updateMineFragmentUi(ListView listView);
    }

    @Override
    public void onClick(View v) {

    }
}
