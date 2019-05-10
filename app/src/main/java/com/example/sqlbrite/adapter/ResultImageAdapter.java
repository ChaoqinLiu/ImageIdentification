package com.example.sqlbrite.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sqlbrite.R;
import com.example.sqlbrite.activity.IntelligentDetectionActivity;
import com.example.sqlbrite.model.ImageResult.ResultArray;

import java.text.DecimalFormat;
import java.util.ArrayList;

import static com.example.sqlbrite.util.BitmapUtil.changeBitmapSize;

public class ResultImageAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<ResultArray> arrayList = new ArrayList<ResultArray>();

    public ResultImageAdapter(Context context, ArrayList<ResultArray> result_list){
        mContext = context;
        arrayList = result_list;
    }

    @Override
    public int getCount(){
        return arrayList.size();
    }

    @Override
    public Object getItem(int position){ ;
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_result_image, null);
            holder.score = convertView.findViewById(R.id.text_score);
            holder.root = convertView.findViewById(R.id.text_root);
            holder.keyword = convertView.findViewById(R.id.text_keyword);
            holder.imageView = convertView.findViewById(R.id.icon_view);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ResultArray result = arrayList.get(position);
        holder.imageView.setImageBitmap(changeBitmapSize(IntelligentDetectionActivity.bitmap,70,70));
        holder.keyword.setText(result.keyword);
        //将浮点数转换为两位小数点的百分比显示
        double d_score = result.score * 100;
        String score = null;
        DecimalFormat s_score = new DecimalFormat("#.00");
        String r_score = s_score.format(d_score);
        //如果r_score的长度为3位，小数点前没数字则补0
        if (r_score.length() < 4 ) {
            score = "0" + r_score;
        } else {
            score = r_score;
        }
        holder.score.setText(score + "%");
        holder.root.setText(result.root);
        return convertView;
    }

    public final class ViewHolder {
        public TextView score;
        public TextView root;
        public TextView keyword;
        public ImageView imageView;
    }
}
