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
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sqlbrite.R;
import com.example.sqlbrite.activity.DisplayHistoryActivity;
import com.example.sqlbrite.activity.MainActivity;
import com.example.sqlbrite.adapter.IDCardForFrontHistoryAdapter;
import com.example.sqlbrite.database.IdentificationDatabaseHelper;
import com.example.sqlbrite.model.IDCardForFrontHistory;
import com.example.sqlbrite.model.IDCardForFrontHistory.IDCardForFrontHistoryArray;
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

import static com.example.sqlbrite.common.InitBack.initBackMain;

public class IDCardForFrontHistoryFragment extends Fragment {

    private IdentificationDatabaseHelper dbHelper;
    private BriteDatabase briteDatabase;
    private SqlBrite sqlBrite;

    protected View view;
    protected Context context;

    private List<IDCardForFrontHistoryArray> textList = new ArrayList<IDCardForFrontHistoryArray>();

    private IDCardForFrontHistoryAdapter adapter;

    private ListView listView;
    private TextView back;
    private FrameLayout layout_title;
    private TextView text_title_left;
    private TextView text_title_right;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        context = getActivity();
        view = inflater.inflate(R.layout.fragment_text_history,container,false);
        Typeface iconfont = Typeface.createFromAsset(context.getAssets(), "iconfont/iconfont.ttf");
        listView = view.findViewById(R.id.text_view_list);
        back = view.findViewById(R.id.text_back);
        back.setTypeface(iconfont);
        layout_title = view.findViewById(R.id.title_common);
        text_title_left = view.findViewById(R.id.text_title_left);
        text_title_right = view.findViewById(R.id.text_title_right);
        text_title_left.setText("反面记录");
        text_title_right.setText("正面记录");
        layout_title.setVisibility(View.GONE);
        initSwitchFragment();
        initBackMain(back,context,MainActivity.class);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        dbHelper = IdentificationDatabaseHelper.getInstance(context,16);
        sqlBrite = SqlBrite.create();
        briteDatabase = sqlBrite.wrapDatabaseHelper(dbHelper,AndroidSchedulers.mainThread());
        getIDCardForFrontHistoryData();
    }

    private void getIDCardForFrontHistoryData() {
        Observable<SqlBrite.Query> observable = briteDatabase.createQuery("front_id_card","SELECT * FROM front_id_card ORDER BY id");
        observable.subscribe(new Action1<SqlBrite.Query>() {
            @Override
            public void call(SqlBrite.Query query) {
                Cursor cursor = query.run();
                if (cursor.getCount() != 0) {
                    while (cursor.moveToNext()) {
                        int id = cursor.getInt(cursor.getColumnIndex("id"));
                        String name = cursor.getString(cursor.getColumnIndex("name"));
                        String number = cursor.getString(cursor.getColumnIndex("number"));
                        byte[] bytes = cursor.getBlob(cursor.getColumnIndex("pic"));
                        IDCardForFrontHistory history = new IDCardForFrontHistory();
                        IDCardForFrontHistoryArray textHistoryArray = history.new IDCardForFrontHistoryArray(id, name, number, bytes);
                        textList.add(textHistoryArray);
                        adapter = new IDCardForFrontHistoryAdapter(context, textList);
                        listView.setAdapter(adapter);
                        initRemoveIDCardForFrontItemView();
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

    private void initRemoveIDCardForFrontItemView() {
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
        briteDatabase.delete("front_id_card", "id=" + id);
        briteDatabase.close();
    }

    private void getDetails() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                TextView textView = parent.getChildAt(position).findViewById(R.id.text_id);
                int text_id = Integer.parseInt(textView.getText().toString());
                IDCardForFrontDetailsFragment fragment = new IDCardForFrontDetailsFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("id",text_id);
                fragment.setArguments(bundle);
                getFragmentManager().beginTransaction().replace(R.id.fragment_text, fragment).commit();
            }
        });
    }

    private void initSwitchFragment(){
        RxView.clicks(text_title_left)
                .throttleFirst(600,TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        IDCardForFrontHistoryFragment fragment = new IDCardForFrontHistoryFragment();
                        getFragmentManager().beginTransaction().replace(R.id.fragment_text, fragment).commit();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        L.i(throwable.getMessage());
                    }
                });

        RxView.clicks(text_title_right)
                .throttleFirst(600,TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        IDCardForBackHistoryFragment fragment = new IDCardForBackHistoryFragment();
                        getFragmentManager().beginTransaction().replace(R.id.fragment_text,fragment).commit();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        L.i(throwable.getMessage());
                    }
                });
    }


}
