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
import com.example.sqlbrite.model.DriverLicenseResult;

import java.util.ArrayList;
import java.util.List;

import static com.example.sqlbrite.util.BitmapUtil.changeBitmapSize;

public class ResultDriverLicenseAdapter extends BaseAdapter {

    private Context mContext;
    private List<DriverLicenseResult> arrayList = new ArrayList<DriverLicenseResult>();

    public ResultDriverLicenseAdapter(Context context, List<DriverLicenseResult> result_list){
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_result_driver_license, null);
            holder.userName = convertView.findViewById(R.id.text_user_name);
            holder.gender = convertView.findViewById(R.id.text_user_gender);
            holder.countryOfCitizenship = convertView.findViewById(R.id.text_country_of_citizenship);
            holder.birthday = convertView.findViewById(R.id.text_date_of_birthday);
            holder.certificateNumber = convertView.findViewById(R.id.text_certificate_number);
            holder.address = convertView.findViewById(R.id.text_user_address);
            holder.imageView = convertView.findViewById(R.id.image_driver_license);
            holder.initialLicenseDate = convertView.findViewById(R.id.text_initial_license_date);
            holder.validityPeriod = convertView.findViewById(R.id.text_validity_period);
            holder.to = convertView.findViewById(R.id.text_to);
            holder.quasiDrivingModel = convertView.findViewById(R.id.text_quasi_driving_model);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        DriverLicenseResult driverLicenseResult = arrayList.get(position);
        holder.imageView.setImageBitmap(changeBitmapSize(IntelligentDetectionActivity.bitmap,300,200));
        holder.userName.setText(driverLicenseResult.words_result.userName.getWords());
        holder.gender.setText(driverLicenseResult.words_result.gender.getWords());
        holder.countryOfCitizenship.setText(driverLicenseResult.words_result.countryOfCitizenship.getWords());
        holder.birthday.setText(driverLicenseResult.words_result.birthday.getWords());
        holder.certificateNumber.setText(driverLicenseResult.words_result.certificateNumber.getWords());
        holder.address.setText(driverLicenseResult.words_result.address.getWords());
        holder.initialLicenseDate.setText(driverLicenseResult.words_result.initialLicenseDate.getWords());
        holder.validityPeriod.setText(driverLicenseResult.words_result.validityPeriod.getWords());
        holder.to.setText(driverLicenseResult.words_result.to.getWords());
        holder.quasiDrivingModel.setText(driverLicenseResult.words_result.quasiDrivingModel.getWords());
        return convertView;
    }

    public final class ViewHolder {
        private ImageView imageView;
        private TextView address;
        private TextView birthday;
        private TextView userName;
        private TextView certificateNumber;
        private TextView gender;
        private TextView countryOfCitizenship;
        private TextView initialLicenseDate;
        private TextView validityPeriod;
        private TextView to;
        private TextView quasiDrivingModel;
    }

}
