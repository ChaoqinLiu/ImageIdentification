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
import com.example.sqlbrite.model.IDCardForBackHistory.IDCardForBackHistoryArray;

import java.util.ArrayList;
import java.util.List;

import static com.example.sqlbrite.util.BitmapUtil.changeBitmapSize;

public class IDCardForBackHistoryAdapter extends BaseAdapter {

    private Context mContext;
    private List<IDCardForBackHistoryArray> arrayList = new ArrayList<IDCardForBackHistoryArray>();

    public IDCardForBackHistoryAdapter(Context context, List<IDCardForBackHistoryArray> result_list){
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_result_id_card_for_back_history, null);
            holder.id = convertView.findViewById(R.id.text_id);
            holder.issuingAuthority = convertView.findViewById(R.id.text_issuingAuthority);
            holder.dateOfIssue = convertView.findViewById(R.id.text_dateOfIssue);
            holder.expirationDate = convertView.findViewById(R.id.text_expirationDate);
            holder.imageView = convertView.findViewById(R.id.image_view_id_card_back);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        IDCardForBackHistoryArray result = arrayList.get(position);
        byte[] bytes = result.bytes;
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
        holder.imageView.setImageBitmap(changeBitmapSize(bitmap,70,70));
        holder.id.setText(String.valueOf(result.id));
        holder.issuingAuthority.setText(result.issuingAuthority);
        holder.dateOfIssue.setText(result.dateOfIssue);
        holder.expirationDate.setText(result.expirationDate);
        return convertView;
    }

    public final class ViewHolder {
        private TextView id;
        private TextView issuingAuthority;
        private TextView dateOfIssue;
        private TextView expirationDate;
        private ImageView imageView;
    }
}
