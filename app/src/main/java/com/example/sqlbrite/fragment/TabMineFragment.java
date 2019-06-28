package com.example.sqlbrite.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.sqlbrite.R;
import com.example.sqlbrite.activity.EditUserInfoActivity;
import com.example.sqlbrite.activity.LoginActivity;
import com.example.sqlbrite.activity.AboutUsActivity;
import com.example.sqlbrite.database.IdentificationDatabaseHelper;
import com.example.sqlbrite.widgets.CircleImageView;
import com.jakewharton.rxbinding2.view.RxView;
import com.safframework.log.L;
import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;

import java.util.concurrent.TimeUnit;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

import static android.content.Context.MODE_PRIVATE;
import static com.example.sqlbrite.database.IdentificationDatabaseHelper.CURRENTVERSION;
import static com.example.sqlbrite.utils.BitmapUtil.changeBitmapSize;
import static com.example.sqlbrite.utils.ValidatorUtil.isEmail;
import static com.example.sqlbrite.utils.ValidatorUtil.isPhone;

public class TabMineFragment extends Fragment {

    private IdentificationDatabaseHelper dbHelper;
    private BriteDatabase briteDatabase;
    private SqlBrite sqlBrite;

    protected View view;
    protected Context context;

    private CircleImageView imageView;
    private TextView textNickname;
    private TextView textAccount;
    private TextView enter;
    private LinearLayout logout;
    private LinearLayout about;
    private TextView text_about;
    private TextView text_logout;

    private String account;
    private String nickname;

    private LocalBroadcastManager broadcastManager;
    private BroadcastReceiver broadcastReceiver;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        context = getActivity();
        Typeface iconfont = Typeface.createFromAsset(context.getAssets(), "iconfont/iconfont.ttf");
        view = inflater.inflate(R.layout.fragment_tab_mine,container,false);
        imageView = view.findViewById(R.id.icon_user);
        textNickname = view.findViewById(R.id.text_nickname);
        textAccount = view.findViewById(R.id.text_account);
        enter = view.findViewById(R.id.text_edit_user);
        text_about = view.findViewById(R.id.text_about);
        text_logout = view.findViewById(R.id.text_logout);
        logout = view.findViewById(R.id.logout);
        about = view.findViewById(R.id.about);
        enter.setTypeface(iconfont);
        text_logout.setTypeface(iconfont);
        text_about.setTypeface(iconfont);
        initView();
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        dbHelper = IdentificationDatabaseHelper.getInstance(context,CURRENTVERSION);
        sqlBrite = SqlBrite.create();
        briteDatabase = sqlBrite.wrapDatabaseHelper(dbHelper,AndroidSchedulers.mainThread());

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.CART_BROADCAST");
        broadcastManager = LocalBroadcastManager.getInstance(getActivity());
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String msg = intent.getStringExtra("refresh");
                if ("refresh".equals(msg)) {
                    getUserData();
                }
            }
        };
        broadcastManager.registerReceiver(broadcastReceiver,intentFilter);

        getUserData();
    }

    private void getUserData(){
        SharedPreferences preferences = getActivity().getSharedPreferences("user",MODE_PRIVATE);
        String name = preferences.getString("account","");
        Observable<SqlBrite.Query> observable = briteDatabase.createQuery("user","SELECT * FROM user WHERE name=?",new String(name));
        observable.subscribe(new Action1<SqlBrite.Query>() {
            @Override
            public void call(SqlBrite.Query query) {
                Cursor cursor = query.run();
                if (cursor.getCount() != 0) {
                    while (cursor.moveToNext()) {
                        account = cursor.getString(cursor.getColumnIndex("name"));
                        nickname = cursor.getString(cursor.getColumnIndex("nickname"));
                        textAccount.setText(account);
                        textNickname.setText(nickname);
                        byte[] bytes = cursor.getBlob(cursor.getColumnIndex("pic"));
                        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                        imageView.setImageBitmap(changeBitmapSize(bitmap,80,80));
                        bitmap.recycle();
                    }
                    cursor.close();
                    briteDatabase.close();
                }
            }
        });
    }

    private void initView(){
        RxView.clicks(enter)
                .throttleFirst(600,TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        Intent intent = new Intent(context, EditUserInfoActivity.class);
                        intent.putExtra("nickname",nickname);
                        startActivity(intent);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        L.i(throwable.getMessage());
                    }
                });

        RxView.clicks(about)
                .throttleFirst(600,TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        Intent intent = new Intent(context, AboutUsActivity.class);
                        startActivity(intent);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        L.i(throwable.getMessage());
                    }
                });

        RxView.clicks(logout)
                .throttleFirst(600,TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        Intent intent = new Intent(context, LoginActivity.class);
                        SharedPreferences preferences = context.getSharedPreferences("user",MODE_PRIVATE);
                        String name = preferences.getString("account","");
                        if (!(isPhone(account) && isEmail(account))) {
                            briteDatabase.delete("user","name=?", new String(name));
                            briteDatabase.close();
                        }
                        preferences.edit().clear().commit();
                        startActivity(intent);
                        getActivity().finish();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        L.i(throwable.getMessage());
                    }
                });

    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        broadcastManager.unregisterReceiver(broadcastReceiver);
    }


}
