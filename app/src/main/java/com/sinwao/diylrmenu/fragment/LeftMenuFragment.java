package com.sinwao.diylrmenu.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.sinwao.diylrmenu.R;

/**
 * 描述：测试点击事件
 * --------个人简介-------
 * QQ:710760186
 * Email：yibin479@yahoo.com
 * Created by 阿酷 on 2017/4/20.
 */

public class LeftMenuFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_left, null);
        view.findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("heloo , my love ");
            }
        });
        return view;
    }
}
