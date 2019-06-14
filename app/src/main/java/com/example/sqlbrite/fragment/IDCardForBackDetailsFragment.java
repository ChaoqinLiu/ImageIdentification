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

public class IDCardForBackDetailsFragment extends Fragment {

    private IdentificationDatabaseHelper dbHelper;
    private BriteDatabase briteDatabase;
    private SqlBrite sqlBrite;

    protected View view;
    protected Context context;

    private ImageView imageView;
    private TextView textViewIssuingAuthority;
    private TextView textViewDateOfIssue;
    private TextView textViewExpirationDate;
    private TextView back;

    private int id;
    private String issuingAuthority;
    private String dateOfIssue;
    private String expirationDate;
    private byte[] bytes;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        context = (DisplayHistoryActivity) getActivity();
        view = inflater.inflate(R.layout.fragment_id_card_for_back_details,container,false);
        Typeface iconfont = Typeface.createFromAsset(context.getAssets(), "iconfont/iconfont.ttf");
        imageView = view.findViewById(R.id.image_id_card_back);
        textViewIssuingAuthority = view.findViewById(R.id.text_issuingAuthority);
        textViewDateOfIssue = view.findViewById(R.id.text_dateOfIssue);
        textViewExpirationDate = view.findViewById(R.id.text_expirationDate);
        back = view.findViewById(R.id.back);
        back.setTypeface(iconfont);
        Bundle bundle = getArguments();
        id = bundle.getInt("id");
        initBack();
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
        Observable<SqlBrite.Query> observable = briteDatabase.createQuery("back_id_card","SELECT * FROM back_id_card WHERE id=" + id);
        observable.subscribe(new Action1<SqlBrite.Query>() {
            @Override
            public void call(SqlBrite.Query query) {
                Cursor cursor = query.run();
                if (cursor.getCount() != 0) {
                    while (cursor.moveToNext()) {
                        issuingAuthority = cursor.getString(cursor.getColumnIndex("issuingAuthority"));
                        dateOfIssue = cursor.getString(cursor.getColumnIndex("dateOfIssue"));
                        expirationDate = cursor.getString(cursor.getColumnIndex("expirationDate"));
                        bytes = cursor.getBlob(cursor.getColumnIndex("pic"));
                        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        imageView.setImageBitmap(BitmapUtil.changeBitmapSize(bitmap, 300, 200));
                        textViewIssuingAuthority.setText(issuingAuthority);
                        textViewDateOfIssue.setText(dateOfIssue);
                        textViewExpirationDate.setText(expirationDate);
                        bitmap.recycle();
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
                        IDCardForBackHistoryFragment fragment = new IDCardForBackHistoryFragment();
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
