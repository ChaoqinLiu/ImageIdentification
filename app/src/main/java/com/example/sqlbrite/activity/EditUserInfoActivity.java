package com.example.sqlbrite.activity;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sqlbrite.R;
import com.example.sqlbrite.app.BaseActivity;
import com.example.sqlbrite.database.IdentificationDatabaseHelper;
import com.jakewharton.rxbinding2.view.RxView;
import com.safframework.injectview.annotations.InjectView;
import com.safframework.log.L;
import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.concurrent.TimeUnit;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import rx.Observable;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

import static com.example.sqlbrite.database.IdentificationDatabaseHelper.CURRENTVERSION;
import static com.example.sqlbrite.utils.BitmapUtil.changeBitmapSize;

public class EditUserInfoActivity extends BaseActivity {

    private IdentificationDatabaseHelper dbHelper;
    private BriteDatabase briteDatabase;
    private SqlBrite sqlBrite;

    private final String TYPE_USER = "type_user";

    @InjectView(R.id.text_back)
    TextView back;

    @InjectView(R.id.image_user)
    ImageView imageView;

    @InjectView(R.id.nickname)
    TextView textNickname;

    @InjectView(R.id.account)
    TextView textAccount;

    @InjectView(R.id.edit_image)
    TextView edit_image;

    @InjectView(R.id.edit_nickname)
    TextView edit_nickname;

    private String nickname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Typeface iconfont = Typeface.createFromAsset(getAssets(), "iconfont/iconfont.ttf");
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_edit_user_info);
        back.setTypeface(iconfont);
        edit_image.setTypeface(iconfont);
        edit_nickname.setTypeface(iconfont);

        nickname = getIntent().getStringExtra("nickname");

        initEditUserInfo();
    }

    @Override
    protected void onStart() {
        super.onStart();
        dbHelper = IdentificationDatabaseHelper.getInstance(this,CURRENTVERSION);
        sqlBrite = SqlBrite.create();
        briteDatabase = sqlBrite.wrapDatabaseHelper(dbHelper, Schedulers.io());
        getUserInfo();
    }

    private void getUserInfo() {
        SharedPreferences preferences = getSharedPreferences("user", MODE_PRIVATE);
        String name = preferences.getString("account", "");
        Observable<SqlBrite.Query> observable = briteDatabase.createQuery("user","SELECT * FROM user WHERE name=?",new String(name));
        observable.subscribe(new Action1<SqlBrite.Query>() {
            @Override
            public void call(SqlBrite.Query query) {
                Cursor cursor = query.run();
                if (cursor.getCount() != 0) {
                    while (cursor.moveToNext()) {
                        String account = cursor.getString(cursor.getColumnIndex("name"));
                        String nickname = cursor.getString(cursor.getColumnIndex("nickname"));
                        textAccount.setText(account);
                        textNickname.setText(nickname);
                        byte[] bytes = cursor.getBlob(cursor.getColumnIndex("pic"));
                        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        imageView.setImageBitmap(changeBitmapSize(bitmap, 80, 80));
                        bitmap.recycle();
                    }
                }
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                L.i(throwable.getMessage());
            }
        });
    }

    private void initEditUserInfo(){
        RxPermissions rxPermissions = new RxPermissions(this);

        RxView.clicks(edit_image)
                .throttleFirst(600,TimeUnit.MILLISECONDS)
                .compose(rxPermissions.ensure(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE))
                .subscribe(new Consumer<Boolean>() {

                    @Override
                    public void accept(@NonNull Boolean granted) throws Exception {

                        if (granted) {
                            Intent intent = new Intent(EditUserInfoActivity.this,AlbumSelectionActivity.class);
                            intent.putExtra("type",TYPE_USER);
                            startActivity(intent);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        L.i(throwable.getMessage());
                    }
                });

        RxView.clicks(edit_nickname)
                .throttleFirst(600,TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        Intent intent = new Intent(EditUserInfoActivity.this, EditNicknameActivity.class);
                        intent.putExtra("nickname",nickname);
                        startActivity(intent);
                        finish();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        L.i(throwable.getMessage());
                    }
                });

        RxView.clicks(back)
                .throttleFirst(600,TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@android.support.annotation.NonNull Object o) throws Exception {
                        Intent intent = new Intent(EditUserInfoActivity.this, MainActivity.class);
                        intent.putExtra("type", "type_user");
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
        Intent intent = new Intent(EditUserInfoActivity.this, MainActivity.class);
        intent.putExtra("type", "type_user");
        startActivity(intent);
        finish();
    }

}
