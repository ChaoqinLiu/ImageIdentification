package com.example.sqlbrite.fragment;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sqlbrite.R;
import com.example.sqlbrite.activity.DisplayHistoryActivity;
import com.example.sqlbrite.database.IdentificationDatabaseHelper;
import com.example.sqlbrite.util.BitmapUtil;
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

public class TextDetailsFragment extends Fragment {

    private IdentificationDatabaseHelper dbHelper;
    private BriteDatabase briteDatabase;
    private SqlBrite sqlBrite;

    protected View view;
    protected Context context;

    private ImageView imageView;
    private TextView textView;
    private TextView text_record;
    private TextView translation_record;
    private TextView details;
    private TextView back;
    private TextView prompt;

    private int id;
    private String words;
    private byte[] bytes;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        context = (DisplayHistoryActivity) getActivity();
        view = inflater.inflate(R.layout.fragment_text_details,container,false);
        Bundle bundle = getArguments();
        id = bundle.getInt("id");
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        dbHelper = IdentificationDatabaseHelper.getInstance(context,16);
        sqlBrite = SqlBrite.create();
        briteDatabase = sqlBrite.wrapDatabaseHelper(dbHelper,AndroidSchedulers.mainThread());
        imageView = view.findViewById(R.id.image_details);
        textView = view.findViewById(R.id.text_details);
        text_record = getActivity().findViewById(R.id.text_record);
        translation_record = getActivity().findViewById(R.id.translation_record);
        details = getActivity().findViewById(R.id.details);
        translation_record.setVisibility(View.GONE);
        text_record.setVisibility(View.GONE);
        back = getActivity().findViewById(R.id.text_back);
        prompt = getActivity().findViewById(R.id.prompt);
        details.setText("详情");
        getDetailData();
    }

    private void getDetailData() {
        Observable<SqlBrite.Query> observable = briteDatabase.createQuery("text","SELECT * FROM text WHERE id=" + id);
        observable.subscribe(new Action1<SqlBrite.Query>() {
            @Override
            public void call(SqlBrite.Query query) {
                Cursor cursor = query.run();
                if (cursor != null) {
                    while (cursor.moveToNext()) {
                        words = cursor.getString(cursor.getColumnIndex("words"));
                        bytes = cursor.getBlob(cursor.getColumnIndex("pic"));
                        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                        imageView.setImageBitmap(BitmapUtil.changeBitmapSize(bitmap,290,150));
                        textView.setText(words);
                        initBack();
                    }
                }
                cursor.close();
                briteDatabase.close();
            }
        });
    }


    private void initBack(){
        RxView.clicks(back).throttleFirst(600,TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        TextHistoryFragment fragment = new TextHistoryFragment();
                        getFragmentManager().beginTransaction().replace(R.id.fragment_text, fragment).commit();
                        prompt.setVisibility(View.GONE);
                        translation_record.setVisibility(View.VISIBLE);
                        text_record.setVisibility(View.VISIBLE);
                        details.setVisibility(View.GONE);
                        text_record.setText("识别记录");
                        translation_record.setText("翻译记录");
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        L.i(throwable.getMessage());
                    }
                });
    }

}
