package com.example.sqlbrite.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.sqlbrite.R;
import com.example.sqlbrite.activity.MainActivity;
import com.example.sqlbrite.activity.TextTranslationActivity;

public class TBackFragment extends Fragment implements View.OnClickListener {

    private TextTranslationActivity activity;
    protected View view;
    protected Context context;
    private TextTranslationActivity textTranslationActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        context = (TextTranslationActivity) getActivity();
        textTranslationActivity = (TextTranslationActivity) getActivity();
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
        activity = (TextTranslationActivity) context;//保存Context引用
    }

    @Override
    public void onClick(View v){
        Intent intent = new Intent(context, MainActivity.class);
        startActivity(intent);
        textTranslationActivity.finish();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) { // 页面创建
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroy() { // 页面销毁
        super.onDestroy();
    }

    @Override
    public void onDestroyView() { // 销毁碎片视图
        super.onDestroyView();
    }

    @Override
    public void onDetach() { // 把碎片从页面撕下来
        super.onDetach();
    }

    @Override
    public void onPause() { // 页面暂停
        super.onPause();
    }

    @Override
    public void onResume() { // 页面恢复
        super.onResume();
    }

    @Override
    public void onStart() { // 页面启动
        super.onStart();
    }

    @Override
    public void onStop() { // 页面停止
        super.onStop();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) { //在活动页面创建之后
        super.onActivityCreated(savedInstanceState);
    }

}
