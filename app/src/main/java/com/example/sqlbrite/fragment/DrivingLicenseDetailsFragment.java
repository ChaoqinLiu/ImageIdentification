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

public class DrivingLicenseDetailsFragment extends Fragment {

    private IdentificationDatabaseHelper dbHelper;
    private BriteDatabase briteDatabase;
    private SqlBrite sqlBrite;

    protected View view;
    protected Context context;

    private ImageView imageView;
    private TextView textViewAddress;
    private TextView textViewNumberPlateNumber;
    private TextView textViewVehicleType;
    private TextView textViewOwner;
    private TextView textViewNatureOfUse;
    private TextView textViewBrandModelNumber;
    private TextView textViewEngineNumber;
    private TextView textViewVehicleIdentificationNumber;
    private TextView textViewRegistrationDate;
    private TextView textViewIssuingCertificateOfDate;
    private TextView back;

    private int id;
    private String address;
    private String numberPlateNumber;
    private String vehicleType;
    private String owner;
    private String natureOfUse;
    private String brandModelNumber;
    private String engineNumber;
    private String vehicleIdentificationNumber;
    private String registrationDate;
    private String issuingCertificateOfDate;
    private byte[] bytes;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        context = (DisplayHistoryActivity) getActivity();
        view = inflater.inflate(R.layout.fragment_driving_license_details,container,false);
        Typeface iconfont = Typeface.createFromAsset(context.getAssets(), "iconfont/iconfont.ttf");
        back = view.findViewById(R.id.back);
        back.setTypeface(iconfont);
        imageView = view.findViewById(R.id.image_driving_license);
        textViewAddress = view.findViewById(R.id.text_owner_address);
        textViewNumberPlateNumber = view.findViewById(R.id.text_number_plate_number);
        textViewVehicleType = view.findViewById(R.id.text_vehicle_type);
        textViewOwner = view.findViewById(R.id.text_owner);
        textViewNatureOfUse = view.findViewById(R.id.text_nature_of_use);
        textViewBrandModelNumber = view.findViewById(R.id.text_brand_model_number);
        textViewEngineNumber = view.findViewById(R.id.text_engine_number);
        textViewVehicleIdentificationNumber = view.findViewById(R.id.text_vehicle_identification_number);
        textViewRegistrationDate = view.findViewById(R.id.text_registration_date);
        textViewIssuingCertificateOfDate = view.findViewById(R.id.text_issuing_certificate_of_date);
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
        Observable<SqlBrite.Query> observable = briteDatabase.createQuery("driving_license","SELECT * FROM driving_license WHERE id=" + id);
        observable.subscribe(new Action1<SqlBrite.Query>() {
            @Override
            public void call(SqlBrite.Query query) {
                Cursor cursor = query.run();
                if (cursor.getCount() != 0) {
                    while (cursor.moveToNext()) {
                        address = cursor.getString(cursor.getColumnIndex("address"));
                        numberPlateNumber = cursor.getString(cursor.getColumnIndex("numberPlateNumber"));
                        vehicleType = cursor.getString(cursor.getColumnIndex("vehicleType"));
                        owner = cursor.getString(cursor.getColumnIndex("owner"));
                        natureOfUse = cursor.getString(cursor.getColumnIndex("natureOfUse"));
                        brandModelNumber = cursor.getString(cursor.getColumnIndex("brandModelNumber"));
                        engineNumber = cursor.getString(cursor.getColumnIndex("engineNumber"));
                        vehicleIdentificationNumber = cursor.getString(cursor.getColumnIndex("vehicleIdentificationNumber"));
                        registrationDate = cursor.getString(cursor.getColumnIndex("registrationDate"));
                        issuingCertificateOfDate = cursor.getString(cursor.getColumnIndex("issuingCertificateOfDate"));
                        bytes = cursor.getBlob(cursor.getColumnIndex("pic"));
                        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        imageView.setImageBitmap(BitmapUtil.changeBitmapSize(bitmap, 300, 200));
                        textViewAddress.setText(address);
                        textViewNumberPlateNumber.setText(numberPlateNumber);
                        textViewVehicleType.setText(vehicleType);
                        textViewOwner.setText(owner);
                        textViewNatureOfUse.setText(natureOfUse);
                        textViewBrandModelNumber.setText(brandModelNumber);
                        textViewEngineNumber.setText(engineNumber);
                        textViewVehicleIdentificationNumber.setText(vehicleIdentificationNumber);
                        textViewRegistrationDate.setText(registrationDate);
                        textViewIssuingCertificateOfDate.setText(issuingCertificateOfDate);
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
                        DrivingLicenseHistoryFragment fragment = new DrivingLicenseHistoryFragment();
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
