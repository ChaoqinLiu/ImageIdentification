package com.example.sqlbrite.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
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
import com.example.sqlbrite.adapter.TextHistoryAdapter;
import com.example.sqlbrite.database.IdentificationDatabaseHelper;
import com.example.sqlbrite.model.TextHistory;
import com.example.sqlbrite.model.TextHistory.TextHistoryArray;
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

public class TextHistoryFragment extends Fragment {

    private IdentificationDatabaseHelper dbHelper;
    private BriteDatabase briteDatabase;
    private SqlBrite sqlBrite;

    protected View view;
    protected Context context;

    private List<TextHistoryArray> textList = new ArrayList<TextHistoryArray>();

    private TextHistoryAdapter adapter;

    private ListView listView;
    private TextView back;
    private TextView text_title_left;
    private TextView text_title_right;
    private FrameLayout layout_title;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        context = (DisplayHistoryActivity) getActivity();
        view = inflater.inflate(R.layout.fragment_text_history,container,false);
        Typeface iconfont = Typeface.createFromAsset(context.getAssets(), "iconfont/iconfont.ttf");
        listView = view.findViewById(R.id.text_view_list);
        back = view.findViewById(R.id.text_back);
        text_title_left = view.findViewById(R.id.text_title_left);
        text_title_right = view.findViewById(R.id.text_title_right);
        text_title_left.setText("识别记录");
        text_title_right.setText("翻译记录");
        back.setTypeface(iconfont);
        layout_title = view.findViewById(R.id.title_common);
        layout_title.setVisibility(View.GONE);
        initSwitchFragment();
        initBack();
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        dbHelper = IdentificationDatabaseHelper.getInstance(context,16);
        sqlBrite = SqlBrite.create();
        briteDatabase = sqlBrite.wrapDatabaseHelper(dbHelper,AndroidSchedulers.mainThread());
        getTextHistoryData();
    }

    private void getTextHistoryData() {
        Observable<SqlBrite.Query> observable = briteDatabase.createQuery("text","SELECT * FROM text ORDER BY id");
        observable.subscribe(new Action1<SqlBrite.Query>() {
            @Override
            public void call(SqlBrite.Query query) {
                Cursor cursor = query.run();
                if (cursor.getCount() != 0) {
                    while (cursor.moveToNext()) {
                        int id = cursor.getInt(cursor.getColumnIndex("id"));
                        String words = cursor.getString(cursor.getColumnIndex("words"));
                        byte[] bytes = cursor.getBlob(cursor.getColumnIndex("pic"));
                        TextHistory history = new TextHistory();
                        TextHistory.TextHistoryArray textHistoryArray = history.new TextHistoryArray(id,words,bytes);
                        textList.add(textHistoryArray);
                        adapter = new TextHistoryAdapter(context,textList);
                        listView.setAdapter(adapter);
                        initRemoveTextItemView();
                        getDetails();
                    }
                } else {
                    PromptFragment fragment = new PromptFragment();
                    getFragmentManager().beginTransaction().replace(R.id.frameLayout_prompt,fragment).commit();
                }
                cursor.close();
                briteDatabase.close();
            }
        });
    }

    private void initRemoveTextItemView() {
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
                                deleteTextItemDataFromDatabase(id);
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

    private void deleteTextItemDataFromDatabase(int id){
        briteDatabase.delete("text", "id=" + id);
        briteDatabase.close();
    }

    private void getDetails() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                TextView textView = parent.getChildAt(position).findViewById(R.id.text_id);
                int text_id = Integer.parseInt(textView.getText().toString());
                TextDetailsFragment fragment = new TextDetailsFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("id",text_id);
                fragment.setArguments(bundle);
                getFragmentManager().beginTransaction().replace(R.id.fragment_text, fragment).commit();
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

    private void initSwitchFragment(){
        RxView.clicks(text_title_left)
                .throttleFirst(600,TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        TextHistoryFragment fragment = new TextHistoryFragment();
                        getFragmentManager().beginTransaction().replace(R.id.fragment_text,fragment).commit();
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
                        TranslationHistoryFragment fragment = new TranslationHistoryFragment();
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
