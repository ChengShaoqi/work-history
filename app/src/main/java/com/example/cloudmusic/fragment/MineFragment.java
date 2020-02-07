package com.example.cloudmusic.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cloudmusic.R;
import com.example.cloudmusic.activity.MainActivity;


public class MineFragment extends Fragment implements View.OnClickListener {
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
                //下面这句与上面这句等效
//                intent.setComponent(new ComponentName("com.example.cloudmusic.activity",
//                        "com.example.cloudmusic.activity.LocalMusicActivity"));
                startActivity(intent);
            }
        });
        RecyclerView recyclerView = view.findViewById(R.id.song_list_recycler_view);
        mIMineFragmentCallBack.updateMineFragmentUi(recyclerView);
        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mIMineFragmentCallBack = (IMineFragmentCallBack) context;
    }

    public interface IMineFragmentCallBack {
        void updateMineFragmentUi(RecyclerView recyclerView);
    }

    @Override
    public void onClick(View v) {

    }
}
