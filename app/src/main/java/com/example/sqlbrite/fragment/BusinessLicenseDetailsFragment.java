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

public class BusinessLicenseDetailsFragment extends Fragment {

    private IdentificationDatabaseHelper dbHelper;
    private BriteDatabase briteDatabase;
    private SqlBrite sqlBrite;

    protected View view;
    protected Context context;

    private ImageView imageView;
    private TextView textRegisteredCapital;
    private TextView textSocialCreditCode;
    private TextView textCompanyName;
    private TextView textLegalPerson;
    private TextView textDocumentNumber;
    private TextView textFormation;
    private TextView textDateOfEstablishment;
    private TextView textCompanyAddress;
    private TextView textBusinessScope;
    private TextView textTypeOfCompany;
    private TextView textBusinessLicenseValidityPeriod;
    private TextView back;

    private int id;
    private String registeredCapital;
    private String socialCreditCode;
    private String companyName;
    private String legalPerson;
    private String documentNumber;
    private String formation;
    private String dateOfEstablishment;
    private String companyAddress;
    private String businessScope;
    private String typeOfCompany;
    private String businessLicenseValidityPeriod;
    private byte[] bytes;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        context = (DisplayHistoryActivity) getActivity();
        view = inflater.inflate(R.layout.fragment_business_license_details,container,false);
        Typeface iconfont = Typeface.createFromAsset(context.getAssets(), "iconfont/iconfont.ttf");
        back = view.findViewById(R.id.back);
        back.setTypeface(iconfont);
        imageView = view.findViewById(R.id.image_business_license);
        textRegisteredCapital = view.findViewById(R.id.text_registered_capital);
        textSocialCreditCode = view.findViewById(R.id.text_social_credit_code);
        textCompanyName = view.findViewById(R.id.text_company_name);
        textLegalPerson = view.findViewById(R.id.text_legal_person);
        textDocumentNumber = view.findViewById(R.id.text_document_number);
        textFormation = view.findViewById(R.id.text_formation);
        textDateOfEstablishment = view.findViewById(R.id.text_date_of_establishment);
        textCompanyAddress = view.findViewById(R.id.text_company_address);
        textBusinessScope = view.findViewById(R.id.text_business_scope);
        textTypeOfCompany = view.findViewById(R.id.text_type_of_company);
        textBusinessLicenseValidityPeriod = view.findViewById(R.id.text_business_license_validity_period);
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
        Observable<SqlBrite.Query> observable = briteDatabase.createQuery("business_license","SELECT * FROM business_license WHERE id=" + id);
        observable.subscribe(new Action1<SqlBrite.Query>() {
            @Override
            public void call(SqlBrite.Query query) {
                Cursor cursor = query.run();
                if (cursor.getCount() != 0) {
                    while (cursor.moveToNext()) {
                        registeredCapital = cursor.getString(cursor.getColumnIndex("registeredCapital"));
                        socialCreditCode = cursor.getString(cursor.getColumnIndex("socialCreditCode"));
                        companyName = cursor.getString(cursor.getColumnIndex("companyName"));
                        legalPerson = cursor.getString(cursor.getColumnIndex("legalPerson"));
                        documentNumber = cursor.getString(cursor.getColumnIndex("documentNumber"));
                        formation = cursor.getString(cursor.getColumnIndex("formation"));
                        dateOfEstablishment = cursor.getString(cursor.getColumnIndex("dateOfEstablishment"));
                        companyAddress = cursor.getString(cursor.getColumnIndex("companyAddress"));
                        businessScope = cursor.getString(cursor.getColumnIndex("businessScope"));
                        typeOfCompany = cursor.getString(cursor.getColumnIndex("typeOfCompany"));
                        businessLicenseValidityPeriod = cursor.getString(cursor.getColumnIndex("ValidityPeriod"));
                        bytes = cursor.getBlob(cursor.getColumnIndex("pic"));
                        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        imageView.setImageBitmap(BitmapUtil.changeBitmapSize(bitmap, 300, 200));
                        textRegisteredCapital.setText(registeredCapital);
                        textSocialCreditCode.setText(socialCreditCode);
                        textCompanyName.setText(companyName);
                        textLegalPerson.setText(legalPerson);
                        textDocumentNumber.setText(documentNumber);
                        textFormation.setText(formation);
                        textDateOfEstablishment.setText(dateOfEstablishment);
                        textCompanyAddress.setText(companyAddress);
                        textBusinessScope.setText(businessScope);
                        textTypeOfCompany.setText(typeOfCompany);
                        textBusinessLicenseValidityPeriod.setText(businessLicenseValidityPeriod);
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
                        BusinessLicenseHistoryFragment fragment = new BusinessLicenseHistoryFragment();
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
