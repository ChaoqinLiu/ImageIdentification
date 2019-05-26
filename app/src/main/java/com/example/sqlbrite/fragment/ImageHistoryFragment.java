package com.example.sqlbrite.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sqlbrite.R;
import com.example.sqlbrite.activity.DisplayHistoryActivity;
import com.example.sqlbrite.activity.MainActivity;
import com.example.sqlbrite.adapter.ImageHistoryAdapter;
import com.example.sqlbrite.database.IdentificationDatabaseHelper;
import com.example.sqlbrite.model.ImageHistory;
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

public class ImageHistoryFragment extends Fragment {

    private IdentificationDatabaseHelper dbHelper;
    private BriteDatabase briteDatabase;
    private SqlBrite sqlBrite;

    protected View view;
    protected Context context;

    private List<ImageHistory.ImageHistoryArray> imageList = new ArrayList<ImageHistory.ImageHistoryArray>();
    private ImageHistoryAdapter imageHistoryAdapter;

    private ListView listView;
    private TextView back;
    private TextView prompt;
    private TextView title;
    private LinearLayout title_common_other;

    private int id;
    private float score;
    private String root;
    private String keyword;
    private byte[] bytes;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        context = (DisplayHistoryActivity) getActivity();
        view = inflater.inflate(R.layout.fragment_text_history,container,false);
        listView = view.findViewById(R.id.text_view_list);
        back = view.findViewById(R.id.back);
        prompt = view.findViewById(R.id.prompt);
        title = view.findViewById(R.id.text_title_common);
        title.setText("识别记录");
        title_common_other = view.findViewById(R.id.title_common_other);
        title_common_other.setVisibility(View.GONE);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        dbHelper = IdentificationDatabaseHelper.getInstance(context,16);
        sqlBrite = SqlBrite.create();
        briteDatabase = sqlBrite.wrapDatabaseHelper(dbHelper,AndroidSchedulers.mainThread());
        getDisplayImageHistoryData();
    }

    private void getDisplayImageHistoryData() {
        Observable<SqlBrite.Query> observable = briteDatabase.createQuery("image","SELECT * FROM image ORDER BY id");
        observable.subscribe(new Action1<SqlBrite.Query>() {
            @Override
            public void call(SqlBrite.Query query) {
                Cursor cursor = query.run();
                if (cursor != null) {
                    while (cursor.moveToNext()) {
                        id = cursor.getInt(cursor.getColumnIndex("id"));
                        score = cursor.getFloat(cursor.getColumnIndex("score"));
                        root = cursor.getString(cursor.getColumnIndex("root"));
                        keyword = cursor.getString(cursor.getColumnIndex("keyword"));
                        bytes = cursor.getBlob(cursor.getColumnIndex("pic"));
                        ImageHistory result = new ImageHistory();
                        ImageHistory.ImageHistoryArray imageHistoryResult = result.new ImageHistoryArray(id,score,root,keyword,bytes);
                        imageList.add(imageHistoryResult);
                        imageHistoryAdapter = new ImageHistoryAdapter(context,imageList);
                        listView.setAdapter(imageHistoryAdapter);
                        initRemoveImageItemView();
                        initBack();
                    }
                    if (!TextUtils.isEmpty(String.valueOf(id))) {
                        prompt.setVisibility(View.VISIBLE);
                    } else {
                        prompt.setVisibility(View.GONE);
                    }
                }
                cursor.close();
                briteDatabase.close();
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                System.out.println("onError: " + throwable.getMessage());
            }
        });

    }

    private void initRemoveImageItemView() {
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                prompt.setVisibility(View.GONE);
                //定义AlertDialog.Builder对象，当长按列表项的时候弹出确认删除对话框
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("确定删除?");
                builder.setTitle("提示");
                //添加AlertDialog.Builder对象的setPositiveButton()方法
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(imageList.remove(position) != null ){
                            try {
                                //获取item控件内容:id作为删除数据库数据的条件
                                TextView textView = parent.getChildAt(position).findViewById(R.id.text_image);
                                int id = Integer.parseInt(textView.getText().toString());
                                deleteImageItemDataFromDatabase(id);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }else {
                            L.i("failed");
                        }
                        imageList.clear();
                        if (imageHistoryAdapter != null) {
                            imageHistoryAdapter.notifyDataSetChanged();
                            Toast.makeText(context, "删除成功", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                //添加AlertDialog.Builder对象的setNegativeButton()方法
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.create().show();
                return false;
            }
        });
    }

    private void deleteImageItemDataFromDatabase(int id) {
        briteDatabase.delete("image", "id=" + id);
        briteDatabase.close();
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
