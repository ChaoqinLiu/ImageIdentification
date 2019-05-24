package com.example.sqlbrite.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sqlbrite.R;
import com.example.sqlbrite.activity.DisplayHistoryActivity;
import com.example.sqlbrite.model.ImageHistory.ImageHistoryArray;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import static com.example.sqlbrite.util.BitmapUtil.changeBitmapSize;

public class ImageHistoryAdapter extends BaseAdapter {

    private Context mContext;
    private List<ImageHistoryArray> arrayList = new ArrayList<ImageHistoryArray>();

    public ImageHistoryAdapter(Context context, List<ImageHistoryArray> result_list){
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
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_result_image_history, null);
            holder.id = convertView.findViewById(R.id.text_image);
            holder.score = convertView.findViewById(R.id.text_score);
            holder.root = convertView.findViewById(R.id.text_root);
            holder.keyword = convertView.findViewById(R.id.text_keyword);
            holder.imageView = convertView.findViewById(R.id.icon_view);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ImageHistoryArray result = arrayList.get(position);
        byte[] bytes = result.bytes;
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
        holder.imageView.setImageBitmap(changeBitmapSize(bitmap,70,70));
        holder.id.setText(String.valueOf(result.id));
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
        private TextView id;
        private TextView score;
        private TextView root;
        private TextView keyword;
        private ImageView imageView;
    }
}
