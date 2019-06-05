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
import com.example.sqlbrite.activity.MainActivity;
import com.example.sqlbrite.adapter.IDCardForBackHistoryAdapter;
import com.example.sqlbrite.database.IdentificationDatabaseHelper;
import com.example.sqlbrite.model.IDCardForBackHistory;
import com.example.sqlbrite.model.IDCardForBackHistory.IDCardForBackHistoryArray;
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

public class IDCardForBackHistoryFragment extends Fragment {

    private IdentificationDatabaseHelper dbHelper;
    private BriteDatabase briteDatabase;
    private SqlBrite sqlBrite;

    protected View view;
    protected Context context;

    private List<IDCardForBackHistoryArray> textList = new ArrayList<IDCardForBackHistoryArray>();

    private IDCardForBackHistoryAdapter adapter;

    private ListView listView;
    private TextView back;
    private FrameLayout layout_title;
    private TextView text_title_left;
    private TextView text_title_right;

    private int id;

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
        initSwitchFragment();
    }

    private void getIDCardForFrontHistoryData() {
        Observable<SqlBrite.Query> observable = briteDatabase.createQuery("back_id_card","SELECT * FROM back_id_card ORDER BY id");
        observable.subscribe(new Action1<SqlBrite.Query>() {
            @Override
            public void call(SqlBrite.Query query) {
                Cursor cursor = query.run();
                if (cursor.getCount() != 0) {
                    while (cursor.moveToNext()) {
                        id = cursor.getInt(cursor.getColumnIndex("id"));
                        String issuingAuthority = cursor.getString(cursor.getColumnIndex("issuingAuthority"));
                        String dateOfIssue = cursor.getString(cursor.getColumnIndex("dateOfIssue"));
                        String expirationDate = cursor.getString(cursor.getColumnIndex("expirationDate"));
                        byte[] bytes = cursor.getBlob(cursor.getColumnIndex("pic"));
                        IDCardForBackHistory history = new IDCardForBackHistory();
                        IDCardForBackHistoryArray textHistoryArray = history.new IDCardForBackHistoryArray(id, issuingAuthority, dateOfIssue, expirationDate, bytes);
                        textList.add(textHistoryArray);
                        adapter = new IDCardForBackHistoryAdapter(context, textList);
                        listView.setAdapter(adapter);
                        initRemoveIDCardForBackItemView();
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

    private void initRemoveIDCardForBackItemView() {
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
        briteDatabase.delete("back_id_card", "id=" + id);
        briteDatabase.close();
    }

    private void getDetails() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                TextView textView = parent.getChildAt(position).findViewById(R.id.text_id);
                int text_id = Integer.parseInt(textView.getText().toString());
                IDCardForBackDetailsFragment fragment = new IDCardForBackDetailsFragment();
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
