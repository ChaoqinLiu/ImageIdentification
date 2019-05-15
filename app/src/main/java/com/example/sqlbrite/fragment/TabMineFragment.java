package com.example.sqlbrite.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sqlbrite.R;

public class TabMineFragment extends Fragment {

    protected View view;
    protected Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        context = getActivity();
        view = inflater.inflate(R.layout.fragment_tab_mine,container,false);

        return view;
    }


}
