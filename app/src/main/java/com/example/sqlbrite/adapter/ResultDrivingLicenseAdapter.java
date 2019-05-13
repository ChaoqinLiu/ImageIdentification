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
import com.example.sqlbrite.model.DrivingLicenseResult;

import java.util.ArrayList;
import java.util.List;

import static com.example.sqlbrite.util.BitmapUtil.changeBitmapSize;

public class ResultDrivingLicenseAdapter extends BaseAdapter {

    private Context mContext;
    private List<DrivingLicenseResult> arrayList = new ArrayList<DrivingLicenseResult>();

    public ResultDrivingLicenseAdapter(Context context, List<DrivingLicenseResult> result_list){
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_result_driving_license, null);
            holder.address = convertView.findViewById(R.id.text_owner_address);
            holder.numberPlateNumber = convertView.findViewById(R.id.text_number_plate_number);
            holder.vehicleType = convertView.findViewById(R.id.text_vehicle_type);
            holder.owner = convertView.findViewById(R.id.text_owner);
            holder.natureOfUse = convertView.findViewById(R.id.text_nature_of_use);
            holder.brandModelNumber = convertView.findViewById(R.id.text_brand_model_number);
            holder.imageView = convertView.findViewById(R.id.image_driving_license);
            holder.engineNumber = convertView.findViewById(R.id.text_engine_number);
            holder.vehicleIdentificationNumber = convertView.findViewById(R.id.text_vehicle_identification_number);
            holder.registrationDate = convertView.findViewById(R.id.text_registration_date);
            holder.issuingCertificateOfDate = convertView.findViewById(R.id.text_issuing_certificate_of_date);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        DrivingLicenseResult drivingLicenseResult = arrayList.get(position);
        holder.imageView.setImageBitmap(changeBitmapSize(IntelligentDetectionActivity.bitmap,300,200));
        holder.address.setText(drivingLicenseResult.words_result.address.getWords());
        holder.numberPlateNumber.setText(drivingLicenseResult.words_result.numberPlateNumber.getWords());
        holder.vehicleType.setText(drivingLicenseResult.words_result.vehicleType.getWords());
        holder.owner.setText(drivingLicenseResult.words_result.owner.getWords());
        holder.natureOfUse.setText(drivingLicenseResult.words_result.natureOfUse.getWords());
        holder.brandModelNumber.setText(drivingLicenseResult.words_result.brandModelNumber.getWords());
        holder.engineNumber.setText(drivingLicenseResult.words_result.engineNumber.getWords());
        holder.vehicleIdentificationNumber.setText(drivingLicenseResult.words_result.vehicleIdentificationNumber.getWords());
        holder.registrationDate.setText(drivingLicenseResult.words_result.registrationDate.getWords());
        holder.issuingCertificateOfDate.setText(drivingLicenseResult.words_result.issuingCertificateOfDate.getWords());
        return convertView;
    }

    public final class ViewHolder {
        private ImageView imageView;
        private TextView address;
        private TextView numberPlateNumber;
        private TextView vehicleType;
        private TextView owner;
        private TextView natureOfUse;
        private TextView brandModelNumber;
        private TextView engineNumber;
        private TextView vehicleIdentificationNumber;
        private TextView registrationDate;
        private TextView issuingCertificateOfDate;
    }

}
