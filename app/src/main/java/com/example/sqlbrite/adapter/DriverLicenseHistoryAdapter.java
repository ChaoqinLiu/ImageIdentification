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
import com.example.sqlbrite.model.DriverLicenseHistory.DriverLicenseHistoryArray;

import java.util.ArrayList;
import java.util.List;

import static com.example.sqlbrite.utils.BitmapUtil.changeBitmapSize;

public class DriverLicenseHistoryAdapter extends BaseAdapter {

    private Context mContext;
    private List<DriverLicenseHistoryArray> arrayList = new ArrayList<DriverLicenseHistoryArray>();

    public DriverLicenseHistoryAdapter(Context context, List<DriverLicenseHistoryArray> result_list){
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_result_driver_license_history, null);
            holder.id = convertView.findViewById(R.id.text_id);
            holder.name = convertView.findViewById(R.id.text_name);
            holder.number = convertView.findViewById(R.id.text_number);
            holder.imageView = convertView.findViewById(R.id.image_view_driver_license);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        DriverLicenseHistoryArray result = arrayList.get(position);
        byte[] bytes = result.bytes;
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
        holder.imageView.setImageBitmap(changeBitmapSize(bitmap,70,70));
        holder.id.setText(String.valueOf(result.id));
        holder.name.setText(result.name);
        holder.number.setText(result.number);
        bitmap.recycle();
        return convertView;
    }

    public final class ViewHolder {
        private TextView id;
        private TextView name;
        private TextView number;
        private ImageView imageView;
    }
}
