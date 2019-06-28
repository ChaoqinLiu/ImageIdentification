package com.example.sqlbrite.activity;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sqlbrite.R;
import com.example.sqlbrite.app.BaseActivity;
import com.example.sqlbrite.database.IdentificationDatabaseHelper;
import com.jakewharton.rxbinding2.view.RxView;
import com.safframework.injectview.annotations.InjectView;
import com.safframework.log.L;
import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;

import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Consumer;
import rx.Observable;
import rx.schedulers.Schedulers;

import static com.example.sqlbrite.database.IdentificationDatabaseHelper.CURRENTVERSION;

public class EditNicknameActivity extends BaseActivity {

    private IdentificationDatabaseHelper dbHelper;
    private BriteDatabase briteDatabase;
    private SqlBrite sqlBrite;

    private String nickname;

    @InjectView(R.id.text_back)
    TextView back;

    @InjectView(R.id.edit_nickname)
    EditText editTextNickname;

    @InjectView(R.id.save)
    Button save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_edit_nickname);

        Typeface iconfont = Typeface.createFromAsset(getAssets(), "iconfont/iconfont.ttf");
        back.setTypeface(iconfont);
        nickname = getIntent().getStringExtra("nickname");
        editTextNickname.setText(nickname);
        initView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        dbHelper = IdentificationDatabaseHelper.getInstance(this,CURRENTVERSION);
        sqlBrite = SqlBrite.create();
        briteDatabase = sqlBrite.wrapDatabaseHelper(dbHelper, Schedulers.io());
        updateNickname();
    }

    private void updateNickname() {
        RxView.clicks(save)
                .throttleFirst(600,TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        SharedPreferences preferences = getSharedPreferences("user", MODE_PRIVATE);
                        String name = preferences.getString("account", "");
                        String nickname = editTextNickname.getText().toString().trim();
                        ContentValues values = new ContentValues();
                        values.put("nickname", nickname);
                        briteDatabase.update("user", values, "name=?", new String(name));
                        Toast.makeText(EditNicknameActivity.this, "更改成功", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(EditNicknameActivity.this,EditUserInfoActivity.class);
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

    private void initView(){
        RxView.clicks(back)
                .throttleFirst(600,TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        Intent intent = new Intent(EditNicknameActivity.this, EditUserInfoActivity.class);
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

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(EditNicknameActivity.this, EditUserInfoActivity.class);
        startActivity(intent);
        finish();
    }
}
