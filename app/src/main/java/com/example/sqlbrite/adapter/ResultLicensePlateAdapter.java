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
import com.example.sqlbrite.model.LicensePlateResult;

import java.util.ArrayList;
import java.util.List;

import static com.example.sqlbrite.util.BitmapUtil.changeBitmapSize;

public class ResultLicensePlateAdapter extends BaseAdapter {

    private Context mContext;
    private List<LicensePlateResult> arrayList = new ArrayList<LicensePlateResult>();

    private String licensePlateColor;

    public ResultLicensePlateAdapter(Context context, List<LicensePlateResult> result_list){
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_result_license_palte, null);
            holder.color = convertView.findViewById(R.id.text_license_plate_color);
            holder.number = convertView.findViewById(R.id.text_bank_license_plate_number);
            holder.imageView = convertView.findViewById(R.id.image_license_plate);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        LicensePlateResult licensePlateResult = arrayList.get(position);
        holder.imageView.setImageBitmap(changeBitmapSize(IntelligentDetectionActivity.bitmap,300,189));
        try {
            String colorType = licensePlateResult.words_result.getColor();
            if (colorType.contains("yellow")) {
                licensePlateColor = "黄色";
            } else if (colorType.contains("blue")) {
                licensePlateColor = "蓝色";
            } else if (colorType.contains("green")) {
                licensePlateColor = "绿色";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        holder.color.setText(licensePlateColor);
        holder.number.setText(licensePlateResult.words_result.getNumber());
        return convertView;
    }

    public final class ViewHolder {
        private TextView color;
        private TextView number;
        private ImageView imageView;
    }
}
