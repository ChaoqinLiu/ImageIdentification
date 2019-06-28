package com.example.sqlbrite.activity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Window;
import android.widget.TextView;

import com.example.sqlbrite.R;
import com.example.sqlbrite.app.BaseActivity;
import com.jakewharton.rxbinding2.view.RxView;
import com.safframework.injectview.annotations.InjectView;

import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Consumer;

public class AboutUsActivity extends BaseActivity {

    @InjectView(R.id.versionCode)
    TextView versionCode;

    @InjectView(R.id.app_name)
    TextView app_name;

    @InjectView(R.id.back)
    TextView back;

    private final String TYPE_USER = "type_user";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        Typeface iconfont = Typeface.createFromAsset(getAssets(), "iconfont/iconfont.ttf");
        setContentView(R.layout.activity_about_us);
        back.setTypeface(iconfont);
        initView();
    }

    private void initView(){
        PackageManager packageManager = getPackageManager();
        String name = null;
        String appName = null;
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(),0);
            name = packageInfo.versionName;
            int labelRes = packageInfo.applicationInfo.labelRes;
            appName = getResources().getString(labelRes);
            versionCode.setText(name);
            app_name.setText(appName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        RxView.clicks(back)
                .throttleFirst(600,TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception{
                        Intent intent = new Intent(AboutUsActivity.this,MainActivity.class);
                        intent.putExtra("type",TYPE_USER);
                        startActivity(intent);
                        finish();
                    }
                });
    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(AboutUsActivity.this, MainActivity.class);
        intent.putExtra("type", "type_user");
        startActivity(intent);
        finish();
    }

}
