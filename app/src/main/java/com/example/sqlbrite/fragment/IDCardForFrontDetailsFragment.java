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

public class IDCardForFrontDetailsFragment extends Fragment {

    private IdentificationDatabaseHelper dbHelper;
    private BriteDatabase briteDatabase;
    private SqlBrite sqlBrite;

    protected View view;
    protected Context context;

    private ImageView imageView;
    private TextView textViewAddress;
    private TextView textViewBirthday;
    private TextView textViewName;
    private TextView textViewIDNumber;
    private TextView textViewGender;
    private TextView textViewNationality;
    private TextView back;
    private TextView prompt;
    private TextView text_record;
    private TextView translation_record;

    private int id;
    private String address;
    private String birthday;
    private String name;
    private String idNumber;
    private String gender;
    private String nationality;
    private byte[] bytes;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        context = (DisplayHistoryActivity) getActivity();
        view = inflater.inflate(R.layout.item_result_id_card_for_front,container,false);
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
        imageView = view.findViewById(R.id.image_id_card);
        textViewAddress = view.findViewById(R.id.text_address);
        textViewName = view.findViewById(R.id.text_name);
        textViewBirthday = view.findViewById(R.id.text_birthday);
        textViewIDNumber = view.findViewById(R.id.text_idNumber);
        textViewGender = view.findViewById(R.id.text_gender);
        textViewNationality = view.findViewById(R.id.text_nationality);
        text_record = getActivity().findViewById(R.id.text_record);
        translation_record = getActivity().findViewById(R.id.translation_record);
        translation_record.setVisibility(View.GONE);
        text_record.setText("详情");
        FrameLayout.LayoutParams linearParams = (FrameLayout.LayoutParams) text_record.getLayoutParams();
        linearParams.setMarginStart(450);
        text_record.setLayoutParams(linearParams);
        text_record.setClickable(false);
        back = getActivity().findViewById(R.id.text_back);
        prompt = getActivity().findViewById(R.id.prompt);
        getDetailData();
    }

    private void getDetailData() {
        Observable<SqlBrite.Query> observable = briteDatabase.createQuery("front_id_card","SELECT * FROM front_id_card WHERE id=" + id);
        observable.subscribe(new Action1<SqlBrite.Query>() {
            @Override
            public void call(SqlBrite.Query query) {
                Cursor cursor = query.run();
                if (cursor != null) {
                    while (cursor.moveToNext()) {
                        address = cursor.getString(cursor.getColumnIndex("address"));
                        name = cursor.getString(cursor.getColumnIndex("name"));
                        birthday = cursor.getString(cursor.getColumnIndex("birthday"));
                        idNumber = cursor.getString(cursor.getColumnIndex("number"));
                        gender = cursor.getString(cursor.getColumnIndex("gender"));
                        nationality = cursor.getString(cursor.getColumnIndex("nationality"));
                        bytes = cursor.getBlob(cursor.getColumnIndex("pic"));
                        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                        imageView.setImageBitmap(BitmapUtil.changeBitmapSize(bitmap,300,200));
                        textViewAddress.setText(address);
                        textViewName.setText(name);
                        textViewBirthday.setText(birthday);
                        textViewGender.setText(gender);
                        textViewNationality.setText(nationality);
                        textViewIDNumber.setText(idNumber);
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
                        IDCardForFrontHistoryFragment fragment = new IDCardForFrontHistoryFragment();
                        getFragmentManager().beginTransaction().replace(R.id.fragment_text, fragment).commit();
                        prompt.setVisibility(View.GONE);
                        translation_record.setVisibility(View.GONE);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        L.i(throwable.getMessage());
                    }
                });
    }

}
