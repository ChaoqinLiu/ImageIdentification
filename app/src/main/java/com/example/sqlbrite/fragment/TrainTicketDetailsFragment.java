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

public class TrainTicketDetailsFragment extends Fragment {

    private IdentificationDatabaseHelper dbHelper;
    private BriteDatabase briteDatabase;
    private SqlBrite sqlBrite;

    protected View view;
    protected Context context;

    private ImageView imageView;
    private TextView textViewStartingStation;
    private TextView textViewDestinationStation;
    private TextView textViewSeatCategory;
    private TextView textViewTicketRates;
    private TextView textViewTicketNum;
    private TextView textViewTrainNum;
    private TextView textViewName;
    private TextView textViewDate;
    private TextView back;

    private int id;
    private String startingStation;
    private String destinationStation;
    private String seatCategory;
    private String ticketRates;
    private String ticketNum;
    private String trainNum;
    private String name;
    private String date;
    private byte[] bytes;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        context = getActivity();
        view = inflater.inflate(R.layout.fragment_train_ticket_details,container,false);
        Typeface iconfont = Typeface.createFromAsset(context.getAssets(), "iconfont/iconfont.ttf");
        back = view.findViewById(R.id.back);
        back.setTypeface(iconfont);
        imageView = view.findViewById(R.id.image_train_ticket);
        textViewStartingStation = view.findViewById(R.id.text_starting_station);
        textViewDestinationStation = view.findViewById(R.id.text_destination_station);
        textViewSeatCategory = view.findViewById(R.id.text_seat_category);
        textViewTicketRates = view.findViewById(R.id.text_ticket_rates);
        textViewTicketNum = view.findViewById(R.id.text_ticket_num);
        textViewTrainNum = view.findViewById(R.id.text_train_num);
        textViewName = view.findViewById(R.id.text_passenger_name);
        textViewDate = view.findViewById(R.id.text_departure_date);
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
        Observable<SqlBrite.Query> observable = briteDatabase.createQuery("train_ticket","SELECT * FROM train_ticket WHERE id=" + id);
        observable.subscribe(new Action1<SqlBrite.Query>() {
            @Override
            public void call(SqlBrite.Query query) {
                Cursor cursor = query.run();
                if (cursor.getCount() != 0) {
                    while (cursor.moveToNext()) {
                        startingStation = cursor.getString(cursor.getColumnIndex("startingStation"));
                        destinationStation = cursor.getString(cursor.getColumnIndex("destinationStation"));
                        seatCategory = cursor.getString(cursor.getColumnIndex("seatCategory"));
                        ticketRates = cursor.getString(cursor.getColumnIndex("ticketRates"));
                        ticketNum = cursor.getString(cursor.getColumnIndex("ticketNum"));
                        trainNum = cursor.getString(cursor.getColumnIndex("trainNum"));
                        name = cursor.getString(cursor.getColumnIndex("name"));
                        date = cursor.getString(cursor.getColumnIndex("date"));
                        bytes = cursor.getBlob(cursor.getColumnIndex("pic"));
                        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        imageView.setImageBitmap(BitmapUtil.changeBitmapSize(bitmap, 300, 200));
                        textViewStartingStation.setText(startingStation);
                        textViewDestinationStation.setText(destinationStation);
                        textViewSeatCategory.setText(seatCategory);
                        textViewTicketRates.setText(ticketRates);
                        textViewTicketNum.setText(ticketNum);
                        textViewTrainNum.setText(trainNum);
                        textViewName.setText(name);
                        textViewDate.setText(date);
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
                        TrainTicketHistoryFragment fragment = new TrainTicketHistoryFragment();
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
