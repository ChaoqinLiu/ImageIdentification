package com.example.sqlbrite.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sqlbrite.R;
import com.example.sqlbrite.activity.DisplayHistoryActivity;
import com.example.sqlbrite.activity.MainActivity;
import com.example.sqlbrite.adapter.DrivingLicenseHistoryAdapter;
import com.example.sqlbrite.database.IdentificationDatabaseHelper;
import com.example.sqlbrite.model.DrivingLicenseHistory;
import com.example.sqlbrite.model.DrivingLicenseHistory.DrivingLicenseHistoryArray;
import com.jakewharton.rxbinding2.view.RxView;
import com.safframework.log.L;
import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

public class DrivingLicenseHistoryFragment extends Fragment {

    private IdentificationDatabaseHelper dbHelper;
    private BriteDatabase briteDatabase;
    private SqlBrite sqlBrite;

    protected View view;
    protected Context context;

    private List<DrivingLicenseHistoryArray> textList = new ArrayList<DrivingLicenseHistoryArray>();

    private DrivingLicenseHistoryAdapter adapter;

    private ListView listView;
    private TextView back;
    private TextView prompt;
    private TextView text_record;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        context = (DisplayHistoryActivity) getActivity();
        view = inflater.inflate(R.layout.fragment_text_history,container,false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        dbHelper = IdentificationDatabaseHelper.getInstance(context,16);
        sqlBrite = SqlBrite.create();
        briteDatabase = sqlBrite.wrapDatabaseHelper(dbHelper,AndroidSchedulers.mainThread());
        listView = view.findViewById(R.id.text_view_list);
        back = getActivity().findViewById(R.id.text_back);
        text_record = getActivity().findViewById(R.id.text_record);
        text_record.setText("识别记录");
        FrameLayout.LayoutParams linearParams = (FrameLayout.LayoutParams) text_record.getLayoutParams();
        linearParams.setMarginStart(400);
        text_record.setLayoutParams(linearParams);
        text_record.setClickable(false);
        prompt = getActivity().findViewById(R.id.prompt);
        getDrivingLicenseHistoryData();
    }

    private void getDrivingLicenseHistoryData() {
        Observable<SqlBrite.Query> observable = briteDatabase.createQuery("driving_license","SELECT * FROM driving_license ORDER BY id");
        observable.subscribe(new Action1<SqlBrite.Query>() {
            @Override
            public void call(SqlBrite.Query query) {
                Cursor cursor = query.run();
                if (cursor != null) {
                    while (cursor.moveToNext()) {
                        int id = cursor.getInt(cursor.getColumnIndex("id"));
                        String owner = cursor.getString(cursor.getColumnIndex("owner"));
                        String address = cursor.getString(cursor.getColumnIndex("address"));
                        byte[] bytes = cursor.getBlob(cursor.getColumnIndex("pic"));
                        DrivingLicenseHistory history = new DrivingLicenseHistory();
                        DrivingLicenseHistoryArray textHistoryArray = history.new DrivingLicenseHistoryArray(id,address,owner,bytes);
                        textList.add(textHistoryArray);
                        adapter = new DrivingLicenseHistoryAdapter(context,textList);
                        listView.setAdapter(adapter);
                        initRemoveDrivingLicenseItemView();
                        getDetails();
                        initBack();
                    }
                }
                cursor.close();
                briteDatabase.close();
            }
        });
    }

    private void initRemoveDrivingLicenseItemView() {
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                prompt.setVisibility(View.GONE);
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("确定删除?");
                builder.setTitle("提示");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(textList.remove(position) != null ){
                            try {
                                TextView textView = parent.getChildAt(position).findViewById(R.id.text_id);
                                int id = Integer.parseInt(textView.getText().toString());
                                deleteDrivingLicenseItemDataFromDatabase(id);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }else {
                            L.i("failed");
                        }
                        textList.clear();
                        if (adapter != null) {
                            adapter.notifyDataSetChanged();
                            Toast.makeText(context, "删除成功", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.create().show();
                return true;  //不触发点击，当return false时长击点击都触发
            }
        });
    }

    private void deleteDrivingLicenseItemDataFromDatabase(int id){
        briteDatabase.delete("driving_license", "id=" + id);
        briteDatabase.close();
    }

    private void getDetails() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                TextView textView = parent.getChildAt(position).findViewById(R.id.text_id);
                int text_id = Integer.parseInt(textView.getText().toString());
                DrivingLicenseDetailsFragment fragment = new DrivingLicenseDetailsFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("id",text_id);
                fragment.setArguments(bundle);
                getFragmentManager().beginTransaction().replace(R.id.fragment_text, fragment).commit();
                prompt.setVisibility(View.GONE);
            }
        });
    }

    private void initBack(){
        RxView.clicks(back)
                .throttleFirst(600,TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        intent.putExtra("flag", "flag");
                        getActivity().startActivity(intent);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        L.i(throwable.getMessage());
                    }
                });
    }


}
