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
import com.example.sqlbrite.activity.DisplayHistoryActivity;
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

import static com.example.sqlbrite.database.IdentificationDatabaseHelper.CURRENTVERSION;

public class DriverLicenseDetailsFragment extends Fragment {

    private IdentificationDatabaseHelper dbHelper;
    private BriteDatabase briteDatabase;
    private SqlBrite sqlBrite;

    protected View view;
    protected Context context;

    private ImageView imageView;
    private TextView textViewAddress;
    private TextView textViewBirthday;
    private TextView textViewUserName;
    private TextView textViewCertificateNumber;
    private TextView textViewGender;
    private TextView textViewCountryOfCitizenship;
    private TextView textViewQuasiDrivingModel;
    private TextView textViewInitialLicenseDate;
    private TextView textViewValidityPeriod;
    private TextView textViewTo;
    private TextView back;

    private int id;
    private String address;
    private String birthday;
    private String userName;
    private String certificateNumber;
    private String gender;
    private String countryOfCitizenship;
    private String quasiDrivingModel;
    private String initialLicenseDate;
    private String validityPeriod;
    private String to;
    private byte[] bytes;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        context = (DisplayHistoryActivity) getActivity();
        view = inflater.inflate(R.layout.fragment_driver_license_details,container,false);
        Typeface iconfont = Typeface.createFromAsset(context.getAssets(), "iconfont/iconfont.ttf");
        back = view.findViewById(R.id.back);
        back.setTypeface(iconfont);
        imageView = view.findViewById(R.id.image_driver_license);
        textViewAddress = view.findViewById(R.id.text_user_address);
        textViewBirthday = view.findViewById(R.id.text_date_of_birthday);
        textViewUserName = view.findViewById(R.id.text_user_name);
        textViewCertificateNumber = view.findViewById(R.id.text_certificate_number);
        textViewGender = view.findViewById(R.id.text_user_gender);
        textViewCountryOfCitizenship = view.findViewById(R.id.text_country_of_citizenship);
        textViewQuasiDrivingModel = view.findViewById(R.id.text_quasi_driving_model);
        textViewInitialLicenseDate = view.findViewById(R.id.text_initial_license_date);
        textViewValidityPeriod = view.findViewById(R.id.text_validity_period);
        textViewTo = view.findViewById(R.id.text_to);
        Bundle bundle = getArguments();
        id = bundle.getInt("id");
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        dbHelper = IdentificationDatabaseHelper.getInstance(context,CURRENTVERSION);
        sqlBrite = SqlBrite.create();
        briteDatabase = sqlBrite.wrapDatabaseHelper(dbHelper,AndroidSchedulers.mainThread());
        getDetailData();
    }

    private void getDetailData() {
        Observable<SqlBrite.Query> observable = briteDatabase.createQuery("driver_license","SELECT * FROM driver_license WHERE id=" + id);
        observable.subscribe(new Action1<SqlBrite.Query>() {
            @Override
            public void call(SqlBrite.Query query) {
                Cursor cursor = query.run();
                if (cursor.getCount() != 0) {
                    while (cursor.moveToNext()) {
                        address = cursor.getString(cursor.getColumnIndex("address"));
                        birthday = cursor.getString(cursor.getColumnIndex("birthday"));
                        userName = cursor.getString(cursor.getColumnIndex("name"));
                        certificateNumber = cursor.getString(cursor.getColumnIndex("certificateNumber"));
                        gender = cursor.getString(cursor.getColumnIndex("gender"));
                        countryOfCitizenship = cursor.getString(cursor.getColumnIndex("countryOfCitizenship"));
                        quasiDrivingModel = cursor.getString(cursor.getColumnIndex("quasiDrivingModel"));
                        initialLicenseDate = cursor.getString(cursor.getColumnIndex("initialLicenseDate"));
                        validityPeriod = cursor.getString(cursor.getColumnIndex("validityPeriod"));
                        to = cursor.getString(cursor.getColumnIndex("deadline"));
                        bytes = cursor.getBlob(cursor.getColumnIndex("pic"));
                        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        imageView.setImageBitmap(BitmapUtil.changeBitmapSize(bitmap, 300, 200));
                        textViewAddress.setText(address);
                        textViewBirthday.setText(birthday);
                        textViewUserName.setText(userName);
                        textViewCertificateNumber.setText(certificateNumber);
                        textViewGender.setText(gender);
                        textViewCountryOfCitizenship.setText(countryOfCitizenship);
                        textViewQuasiDrivingModel.setText(quasiDrivingModel);
                        textViewInitialLicenseDate.setText(initialLicenseDate);
                        textViewValidityPeriod.setText(validityPeriod);
                        textViewTo.setText(to);
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
                        DriverLicenseHistoryFragment fragment = new DriverLicenseHistoryFragment();
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
