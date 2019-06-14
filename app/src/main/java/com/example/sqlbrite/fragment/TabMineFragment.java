package com.example.sqlbrite.fragment;

import android.Manifest;
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
import com.example.sqlbrite.activity.TakePictureActivity;
import com.example.sqlbrite.database.IdentificationDatabaseHelper;
import com.example.sqlbrite.widgets.CircleImageView;
import com.jakewharton.rxbinding2.view.RxView;
import com.safframework.log.L;
import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;
import com.tbruyelle.rxpermissions2.RxPermissions;

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
    private MainActivity mainActivity;

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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mainActivity = (MainActivity) context;
    }

    private void initView(){
        RxPermissions rxPermissions = new RxPermissions(mainActivity);

        RxView.clicks(imageView)
                .throttleFirst(600,TimeUnit.MILLISECONDS)
                .compose(rxPermissions.ensure(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE))
                .subscribe(new Consumer<Boolean>() {

                    @Override
                    public void accept(@NonNull Boolean granted) throws Exception {

                        if (granted) {
                            Intent intent = new Intent(context,AlbumSelectionActivity.class);
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
                        byte[] bytes = cursor.getBlob(cursor.getColumnIndex("pic"));
                        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                        imageView.setBackgroundColor(Color.WHITE);
                        imageView.setImageBitmap(changeBitmapSize(bitmap,80,80));
                        textAccount.setText(account);
                        textNickname.setText(nickname);
                        bitmap.recycle();
                    }
                    cursor.close();
                    briteDatabase.close();
                }
            }
        });
    }


}
