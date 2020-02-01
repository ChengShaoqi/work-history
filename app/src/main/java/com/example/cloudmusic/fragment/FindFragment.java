package com.example.cloudmusic.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.cloudmusic.R;
import com.example.cloudmusic.activity.MainActivity;



public class FindFragment extends Fragment {
    private IFindFragmentCallBack mIFindFragmentCallBack;

    public FindFragment(MainActivity mainActivity) {
        this.mIFindFragmentCallBack = mainActivity;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_find, container, false);
        ViewPager viewPager = view.findViewById(R.id.viewpager_picture);
        LinearLayout linearLayout = view.findViewById(R.id.HorizontalScrollView_item);
        mIFindFragmentCallBack.updateFindFragmentUi(viewPager);
        mIFindFragmentCallBack.updateFindFragmentUi(linearLayout);
        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mIFindFragmentCallBack = (IFindFragmentCallBack) context;
    }

    public interface IFindFragmentCallBack {
        void updateFindFragmentUi(ViewPager viewPager);
        void updateFindFragmentUi(LinearLayout linearLayout);
    }
}
