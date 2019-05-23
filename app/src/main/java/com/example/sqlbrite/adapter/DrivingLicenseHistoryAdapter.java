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
import com.example.sqlbrite.model.DrivingLicenseHistory.DrivingLicenseHistoryArray;

import java.util.ArrayList;
import java.util.List;

import static com.example.sqlbrite.util.BitmapUtil.changeBitmapSize;

public class DrivingLicenseHistoryAdapter extends BaseAdapter {

    private Context mContext;
    private List<DrivingLicenseHistoryArray> arrayList = new ArrayList<DrivingLicenseHistoryArray>();
    private Bitmap mBitmap;

    public DrivingLicenseHistoryAdapter(Context context, List<DrivingLicenseHistoryArray> result_list){
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_result_driving_license_history, null);
            holder.id = convertView.findViewById(R.id.text_id);
            holder.owner = convertView.findViewById(R.id.text_owner);
            holder.address = convertView.findViewById(R.id.text_address);
            holder.imageView = convertView.findViewById(R.id.image_view_driving_license);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        DrivingLicenseHistoryArray result = arrayList.get(position);
        byte[] bytes = result.bytes;
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
        holder.imageView.setImageBitmap(changeBitmapSize(bitmap,70,70));
        holder.id.setText(String.valueOf(result.id));
        holder.owner.setText(result.owner);
        holder.address.setText(result.address);
        return convertView;
    }

    public final class ViewHolder {
        private TextView id;
        private TextView address;
        private TextView owner;
        private ImageView imageView;
    }
}
