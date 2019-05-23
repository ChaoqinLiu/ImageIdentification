package com.example.sqlbrite.fragment;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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

public class TranslationDetailsFragment extends Fragment {

    private IdentificationDatabaseHelper dbHelper;
    private SQLiteDatabase sqLiteDatabase;

    private BriteDatabase briteDatabase;
    private SqlBrite sqlBrite;

    protected View view;
    protected Context context;

    private ImageView imageView;
    private TextView textViewOriginal;
    private TextView textViewTranslation;
    private TextView text_record;
    private TextView translation_record;
    private TextView back;
    private TextView prompt;

    private int id;
    private String original;
    private String translation;
    private byte[] bytes;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        context = (DisplayHistoryActivity) getActivity();
        view = inflater.inflate(R.layout.fragment_translation_details,container,false);
        Bundle bundle = getArguments();
        id = bundle.getInt("id");
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        dbHelper = IdentificationDatabaseHelper.getInstance(context,16);
        sqLiteDatabase = dbHelper.getReadableDatabase(); //读操作
        sqlBrite = SqlBrite.create();
        briteDatabase = sqlBrite.wrapDatabaseHelper(dbHelper,AndroidSchedulers.mainThread());
        imageView = view.findViewById(R.id.image_details);
        textViewOriginal = view.findViewById(R.id.text_details_original);
        textViewTranslation = view.findViewById(R.id.text_details_translation);
        text_record = getActivity().findViewById(R.id.text_record);
        translation_record = getActivity().findViewById(R.id.translation_record);
        back = getActivity().findViewById(R.id.text_back);
        prompt = getActivity().findViewById(R.id.prompt);
        text_record.setText("详情");
        FrameLayout.LayoutParams linearParams = (FrameLayout.LayoutParams) text_record.getLayoutParams();
        linearParams.setMarginStart(450);
        text_record.setLayoutParams(linearParams);
        text_record.setClickable(false);
        translation_record.setVisibility(View.GONE);
        getDetailData();
    }

    private void getDetailData() {
        Observable<SqlBrite.Query> observable = briteDatabase.createQuery("translation","SELECT * FROM translation WHERE id=" + id);
        observable.subscribe(new Action1<SqlBrite.Query>() {
            @Override
            public void call(SqlBrite.Query query) {
                Cursor cursor = query.run();
                if (cursor != null) {
                    while (cursor.moveToNext()) {
                        original = cursor.getString(cursor.getColumnIndex("original"));
                        translation = cursor.getString(cursor.getColumnIndex("translation"));
                        bytes = cursor.getBlob(cursor.getColumnIndex("pic"));
                        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                        imageView.setImageBitmap(BitmapUtil.changeBitmapSize(bitmap,290,150));
                        textViewOriginal.setText(original);
                        textViewTranslation.setText(translation);
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
                        TranslationHistoryFragment fragment = new TranslationHistoryFragment();
                        getFragmentManager().beginTransaction().replace(R.id.fragment_text, fragment).commit();
                        prompt.setVisibility(View.GONE);
                        text_record.setText("识别记录");
                        FrameLayout.LayoutParams linearParams = (FrameLayout.LayoutParams) text_record.getLayoutParams();
                        linearParams.setMarginStart(300);
                        text_record.setLayoutParams(linearParams);
                        text_record.setClickable(true);
                        translation_record.setVisibility(View.VISIBLE);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        L.i(throwable.getMessage());
                    }
                });
    }

}
