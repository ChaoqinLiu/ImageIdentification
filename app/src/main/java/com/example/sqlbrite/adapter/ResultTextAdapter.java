package com.example.sqlbrite.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.sqlbrite.R;
import com.example.sqlbrite.model.TextResult;

import java.util.ArrayList;

public class ResultTextAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<TextResult.WordsResult> arrayList = new ArrayList<TextResult.WordsResult>();

    public ResultTextAdapter(Context context, ArrayList<TextResult.WordsResult> result_list){
        mContext = context;
        arrayList = result_list;
    }

    @Override
    public int getCount(){
        return arrayList.size();
    }

    @Override
    public Object getItem(int position){
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        ResultTextAdapter.ViewHolder holder;
        if (convertView == null) {
            holder = new ResultTextAdapter.ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_result_text, null);
            holder.words = convertView.findViewById(R.id.text_tx);
            convertView.setTag(holder);
        } else {
            holder = (ResultTextAdapter.ViewHolder) convertView.getTag();
        }

        TextResult.WordsResult result = arrayList.get(position);
        holder.words.setText(result.words);
        return convertView;
    }

    public final class ViewHolder {
        public TextView words;
    }
}
