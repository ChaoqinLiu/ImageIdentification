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
import com.example.sqlbrite.model.TextHistory.TextHistoryArray;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import static com.example.sqlbrite.util.BitmapUtil.changeBitmapSize;

public class TextHistoryAdapter extends BaseAdapter {

    private Context mContext;
    private List<TextHistoryArray> arrayList = new ArrayList<TextHistoryArray>();
    private Bitmap mBitmap;

    public TextHistoryAdapter(Context context, List<TextHistoryArray> result_list){
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_result_text_history, null);
            holder.id = convertView.findViewById(R.id.text_id);
            holder.words = convertView.findViewById(R.id.text_words);
            holder.imageView = convertView.findViewById(R.id.image_view_text);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        TextHistoryArray result = arrayList.get(position);
        byte[] bytes = result.bytes;
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
        holder.imageView.setImageBitmap(changeBitmapSize(bitmap,70,70));
        holder.id.setText(String.valueOf(result.id));
        holder.words.setText(result.words);
        return convertView;
    }

    public final class ViewHolder {
        private TextView id;
        private TextView words;
        private ImageView imageView;
    }
}
