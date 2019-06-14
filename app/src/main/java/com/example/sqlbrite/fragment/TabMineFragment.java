package com.example.sqlbrite.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.sqlbrite.R;
import com.example.sqlbrite.activity.AlbumSelectionActivity;
import com.example.sqlbrite.activity.MainActivity;
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

public class TabMineFragment extends Fragment {

    private IdentificationDatabaseHelper dbHelper;
    private BriteDatabase briteDatabase;
    private SqlBrite sqlBrite;

    protected View view;
    protected Context context;

    private final String TYPE_USER = "type_user";

    private CircleImageView imageView;
    private TextView textNickname;
    private TextView textAccount;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        context = getActivity();
        view = inflater.inflate(R.layout.fragment_tab_mine,container,false);
        imageView = view.findViewById(R.id.icon_user);
        textNickname = view.findViewById(R.id.text_nickname);
        textAccount = view.findViewById(R.id.text_account);
        initView();
        return view;
    }

    private void initView(){
        RxView.clicks(imageView)
                .throttleFirst(600,TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception{
                        Intent intent = new Intent(context,AlbumSelectionActivity.class);
                        intent.putExtra("type",TYPE_USER);
                        startActivity(intent);
                    }
                });
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        dbHelper = IdentificationDatabaseHelper.getInstance(context,CURRENTVERSION);
        sqlBrite = SqlBrite.create();
        briteDatabase = sqlBrite.wrapDatabaseHelper(dbHelper,AndroidSchedulers.mainThread());
        getUserlData();
    }

    private void getUserlData(){
        SharedPreferences preferences = getActivity().getSharedPreferences("user",MODE_PRIVATE);
        String name = preferences.getString("account","");
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
                    }
                    cursor.close();
                    briteDatabase.close();
                }
            }
        });
    }

}
