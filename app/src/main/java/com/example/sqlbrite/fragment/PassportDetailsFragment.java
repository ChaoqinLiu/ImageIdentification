package com.example.sqlbrite.fragment;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sqlbrite.R;
import com.example.sqlbrite.database.IdentificationDatabaseHelper;
import com.example.sqlbrite.utils.BitmapUtil;
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

public class PassportDetailsFragment extends Fragment {

    private IdentificationDatabaseHelper dbHelper;
    private BriteDatabase briteDatabase;
    private SqlBrite sqlBrite;

    protected View view;
    protected Context context;

    private ImageView imageView;
    private TextView textViewCountryCode;
    private TextView textViewPassportIssuanceLocation;
    private TextView textViewValidUntil;
    private TextView textViewPassportNumber;
    private TextView textViewPassportDateOfIssue;
    private TextView textViewBirthPlace;
    private TextView textViewName;
    private TextView textViewPinyinOfName;
    private TextView textViewBirthday;
    private TextView textViewSex;
    private TextView back;

    private int id;
    private String countryCode;
    private String passportIssuanceLocation;
    private String validUntil;
    private String passportNumber;
    private String passportDateOfIssue;
    private String birthPlace;
    private String name;
    private String pinyinOfName;
    private String birthday;
    private String sex;
    private byte[] bytes;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        context = getActivity();
        view = inflater.inflate(R.layout.fragment_passport_details,container,false);
        Typeface iconfont = Typeface.createFromAsset(context.getAssets(), "iconfont/iconfont.ttf");
        back = view.findViewById(R.id.back);
        back.setTypeface(iconfont);
        imageView = view.findViewById(R.id.image_passport);
        textViewCountryCode = view.findViewById(R.id.text_country_code);
        textViewBirthday = view.findViewById(R.id.text_owner_birthday);
        textViewPassportIssuanceLocation = view.findViewById(R.id.text_passport_issuance_location);
        textViewValidUntil = view.findViewById(R.id.text_valid_until);
        textViewPassportNumber = view.findViewById(R.id.text_vehicle_passport_number);
        textViewPassportDateOfIssue = view.findViewById(R.id.text_passport_date_of_issue);
        textViewBirthPlace = view.findViewById(R.id.text_birth_place);
        textViewName = view.findViewById(R.id.text_owner_name);
        textViewPinyinOfName = view.findViewById(R.id.text_pinyin_of_name);
        textViewSex = view.findViewById(R.id.text_sex);
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
        getDetailData();
    }

    private void getDetailData() {
        Observable<SqlBrite.Query> observable = briteDatabase.createQuery("passport","SELECT * FROM passport WHERE id=" + id);
        observable.subscribe(new Action1<SqlBrite.Query>() {
            @Override
            public void call(SqlBrite.Query query) {
                Cursor cursor = query.run();
                if (cursor.getCount() != 0) {
                    while (cursor.moveToNext()) {
                        countryCode = cursor.getString(cursor.getColumnIndex("countryCode"));
                        birthday = cursor.getString(cursor.getColumnIndex("birthday"));
                        passportIssuanceLocation = cursor.getString(cursor.getColumnIndex("passportIssuanceLocation"));
                        validUntil = cursor.getString(cursor.getColumnIndex("validUntil"));
                        passportNumber = cursor.getString(cursor.getColumnIndex("passportNumber"));
                        passportDateOfIssue = cursor.getString(cursor.getColumnIndex("passportDateOfIssue"));
                        birthPlace = cursor.getString(cursor.getColumnIndex("birthPlace"));
                        name = cursor.getString(cursor.getColumnIndex("name"));
                        pinyinOfName = cursor.getString(cursor.getColumnIndex("pinyinOfName"));
                        sex = cursor.getString(cursor.getColumnIndex("sex"));
                        bytes = cursor.getBlob(cursor.getColumnIndex("pic"));
                        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        imageView.setImageBitmap(BitmapUtil.changeBitmapSize(bitmap, 300, 200));
                        textViewCountryCode.setText(countryCode);
                        textViewBirthday.setText(birthday);
                        textViewPassportIssuanceLocation.setText(passportIssuanceLocation);
                        textViewValidUntil.setText(validUntil);
                        textViewPassportNumber.setText(passportNumber);
                        textViewPassportDateOfIssue.setText(passportDateOfIssue);
                        textViewBirthPlace.setText(birthPlace);
                        textViewPinyinOfName.setText(pinyinOfName);
                        textViewSex.setText(sex);
                        textViewName.setText(name);
                        bitmap.recycle();
                        initBack();
                    }
                }
                cursor.close();
                briteDatabase.close();
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                L.i(throwable.getMessage());
            }
        });
    }


    private void initBack(){
        RxView.clicks(back).throttleFirst(600,TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        PassportHistoryFragment fragment = new PassportHistoryFragment();
                        getFragmentManager().beginTransaction().replace(R.id.fragment_text, fragment).commit();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        L.i(throwable.getMessage());
                    }
                });
    }

}
