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
import com.example.sqlbrite.model.IDCardForFrontResult;
import java.util.ArrayList;
import java.util.List;

import static com.example.sqlbrite.util.BitmapUtil.changeBitmapSize;

public class ResultIDCardForFrontAdapter extends BaseAdapter {

    private Context mContext;
    private List<IDCardForFrontResult> arrayList = new ArrayList<IDCardForFrontResult>();

    public ResultIDCardForFrontAdapter(Context context, List<IDCardForFrontResult> result_list){
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_result_id_card_for_front, null);
            holder.userName = convertView.findViewById(R.id.text_name);
            holder.gender = convertView.findViewById(R.id.text_gender);
            holder.nationality = convertView.findViewById(R.id.text_nationality);
            holder.birthday = convertView.findViewById(R.id.text_birthday);
            holder.idNumber = convertView.findViewById(R.id.text_idNumber);
            holder.address = convertView.findViewById(R.id.text_address);
            holder.imageView = convertView.findViewById(R.id.image_id_card);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        IDCardForFrontResult result = arrayList.get(position);
        holder.imageView.setImageBitmap(changeBitmapSize(IntelligentDetectionActivity.bitmap));
        holder.userName.setText(result.words_result.userName.getWords());
        holder.gender.setText(result.words_result.gender.getWords());
        holder.nationality.setText(result.words_result.nationality.getWords());
        holder.birthday.setText(result.words_result.birthday.getWords());
        holder.idNumber.setText(result.words_result.idNumber.getWords());
        holder.address.setText(result.words_result.address.getWords());
        return convertView;
    }

    public final class ViewHolder {
        private ImageView imageView;
        private TextView address;
        private TextView birthday;
        private TextView userName;
        private TextView idNumber;
        private TextView gender;
        private TextView nationality;
    }

}
