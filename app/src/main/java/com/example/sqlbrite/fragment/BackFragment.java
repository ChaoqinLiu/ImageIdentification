package com.example.sqlbrite.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.sqlbrite.R;
import com.example.sqlbrite.activity.IntelligentDetectionActivity;
import com.example.sqlbrite.activity.MainActivity;

public class BackFragment extends Fragment implements View.OnClickListener {

    protected View view;
    protected Context context;
    private IntelligentDetectionActivity activity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        context = (IntelligentDetectionActivity) getActivity();
        activity = (IntelligentDetectionActivity) getActivity();
        view = inflater.inflate(R.layout.fragment_back,container,false);
        TextView back = view.findViewById(R.id.back);
        Typeface iconfont = Typeface.createFromAsset(context.getAssets(), "iconfont/iconfont.ttf");
        back.setTypeface(iconfont);
        back.setOnClickListener(this);
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (IntelligentDetectionActivity) context;//保存Context引用
    }

    @Override
    public void onClick(View v){
        Intent intent = new Intent(context, MainActivity.class);
        startActivity(intent);
        activity.finish();
    }

}
