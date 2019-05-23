package com.example.sqlbrite.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sqlbrite.R;
import com.example.sqlbrite.adapter.ImageHistoryAdapter;
import com.example.sqlbrite.app.BaseActivity;
import com.example.sqlbrite.database.IdentificationDatabaseHelper;
import com.example.sqlbrite.fragment.DrivingLicenseHistoryFragment;
import com.example.sqlbrite.fragment.ImageHistoryFragment;
import com.example.sqlbrite.fragment.TabHistoryFragment;
import com.example.sqlbrite.fragment.TextHistoryFragment;
import com.example.sqlbrite.fragment.TranslationHistoryFragment;
import com.example.sqlbrite.model.ImageHistory;
import com.example.sqlbrite.model.ImageHistory.ImageHistoryArray;
import com.example.sqlbrite.util.BitmapUtil;
import com.jakewharton.rxbinding2.view.RxView;
import com.safframework.injectview.annotations.InjectView;
import com.safframework.log.L;
import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

public class DisplayHistoryActivity extends BaseActivity {

    private String type_image;
    private String type_text;
    private String type_id_card;
    private String type_bank_card;
    private String type_license_plate;
    private String type_driver_license;
    private String type_train_ticket;
    private String type_passport;
    private String type_driving_license;
    private String type_business_license;

    @InjectView(R.id.text_back)
    TextView back;

    @InjectView(R.id.text_record)
    TextView text_record;

    @InjectView(R.id.translation_record)
    TextView translation_record;

    @InjectView(R.id.prompt)
    TextView prompt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_display_history);

        Typeface iconfont = Typeface.createFromAsset(getAssets(), "iconfont/iconfont.ttf");
        back.setTypeface(iconfont);

        Intent intent = getIntent();
        type_image = intent.getStringExtra("type_image");
        type_text = intent.getStringExtra("type_text");
        type_driving_license = intent.getStringExtra("type_driving_license");
        displayHistoryByType();
        initBack();
    }

    private void displayHistoryByType(){
        if (type_image != null) {
            getDisplayImageHistoryFragment();
        } else if (type_text != null) {
            text_record.setText("识别记录");
            translation_record.setText("翻译记录");
            getDisplayTextHistoryFragment();
        } else if (type_driving_license != null){
            getDisplayDrivingLicenseHistoryFragment();
        }
    }

    private void getDisplayImageHistoryFragment() {
        ImageHistoryFragment fragment = new ImageHistoryFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_text,fragment).commit();
    }

    private void getDisplayDrivingLicenseHistoryFragment() {
        DrivingLicenseHistoryFragment fragment = new DrivingLicenseHistoryFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_text,fragment).commit();
    }

    private void getDisplayTextHistoryFragment() {

        TextHistoryFragment fragment = new TextHistoryFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_text,fragment).commit();

        RxView.clicks(text_record)
                .throttleFirst(600,TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        TextHistoryFragment fragment = new TextHistoryFragment();
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_text,fragment).commit();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        System.out.println("onError: " + throwable.getMessage());
                    }
                });

        RxView.clicks(translation_record)
                .throttleFirst(600,TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        TranslationHistoryFragment fragment = new TranslationHistoryFragment();
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_text, fragment).commit();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        L.i(throwable.getMessage());
                    }
                });
    }

    private void initBack(){
        RxView.clicks(back)
                .throttleFirst(600,TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        Intent intent = new Intent(DisplayHistoryActivity.this, MainActivity.class);
                        //intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        intent.putExtra("flag","flag");
                        startActivity(intent);
                        finish();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        L.i(throwable.getMessage());
                    }
                });
    }

}
