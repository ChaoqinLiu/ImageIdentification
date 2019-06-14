package com.example.sqlbrite.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sqlbrite.R;
import com.example.sqlbrite.activity.MainActivity;
import com.example.sqlbrite.adapter.DriverLicenseHistoryAdapter;
import com.example.sqlbrite.database.IdentificationDatabaseHelper;
import com.example.sqlbrite.model.DriverLicenseHistory;
import com.example.sqlbrite.model.DriverLicenseHistory.DriverLicenseHistoryArray;
import com.safframework.log.L;
import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;

import java.util.ArrayList;
import java.util.List;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

import static com.example.sqlbrite.common.InitBack.initBackMain;
import static com.example.sqlbrite.database.IdentificationDatabaseHelper.CURRENTVERSION;

public class DriverLicenseHistoryFragment extends Fragment {

    private IdentificationDatabaseHelper dbHelper;
    private BriteDatabase briteDatabase;
    private SqlBrite sqlBrite;

    protected View view;
    protected Context context;

    private List<DriverLicenseHistoryArray> textList = new ArrayList<DriverLicenseHistoryArray>();

    private DriverLicenseHistoryAdapter adapter;

    private ListView listView;
    private TextView back;
    private LinearLayout layout_title;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        context = getActivity();
        view = inflater.inflate(R.layout.fragment_text_history,container,false);
        listView = view.findViewById(R.id.text_view_list);
        Typeface iconfont = Typeface.createFromAsset(context.getAssets(), "iconfont/iconfont.ttf");
        back = view.findViewById(R.id.back);
        back.setTypeface(iconfont);
        layout_title = view.findViewById(R.id.title_common_other);
        layout_title.setVisibility(View.GONE);
        initBackMain(back,context,MainActivity.class);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        dbHelper = IdentificationDatabaseHelper.getInstance(context,CURRENTVERSION);
        sqlBrite = SqlBrite.create();
        briteDatabase = sqlBrite.wrapDatabaseHelper(dbHelper,AndroidSchedulers.mainThread());
        getDrivingLicenseHistoryData();
    }

    private void getDrivingLicenseHistoryData() {
        Observable<SqlBrite.Query> observable = briteDatabase.createQuery("driver_license","SELECT * FROM driver_license ORDER BY id");
        observable.subscribe(new Action1<SqlBrite.Query>() {
            @Override
            public void call(SqlBrite.Query query) {
                Cursor cursor = query.run();
                if (cursor.getCount() != 0) {
                    while (cursor.moveToNext()) {
                        int id = cursor.getInt(cursor.getColumnIndex("id"));
                        String name = cursor.getString(cursor.getColumnIndex("name"));
                        String number = cursor.getString(cursor.getColumnIndex("certificateNumber"));
                        byte[] bytes = cursor.getBlob(cursor.getColumnIndex("pic"));
                        DriverLicenseHistory history = new DriverLicenseHistory();
                        DriverLicenseHistoryArray textHistoryArray = history.new DriverLicenseHistoryArray(id, name, number, bytes);
                        textList.add(textHistoryArray);
                        adapter = new DriverLicenseHistoryAdapter(context, textList);
                        listView.setAdapter(adapter);
                        initRemoveDriverLicenseItemView();
                        getDetails();
                    }
                } else {
                    PromptFragment fragment = new PromptFragment();
                    getFragmentManager().beginTransaction().replace(R.id.frameLayout_prompt, fragment).commit();
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

    private void initRemoveDriverLicenseItemView() {
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
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
                                deleteDriverLicenseItemDataFromDatabase(id);
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

    private void deleteDriverLicenseItemDataFromDatabase(int id){
        briteDatabase.delete("driver_license", "id=" + id);
        briteDatabase.close();
    }

    private void getDetails() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                TextView textView = parent.getChildAt(position).findViewById(R.id.text_id);
                int text_id = Integer.parseInt(textView.getText().toString());
                DriverLicenseDetailsFragment fragment = new DriverLicenseDetailsFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("id",text_id);
                fragment.setArguments(bundle);
                getFragmentManager().beginTransaction().replace(R.id.fragment_text, fragment).commit();
            }
        });
    }

}
