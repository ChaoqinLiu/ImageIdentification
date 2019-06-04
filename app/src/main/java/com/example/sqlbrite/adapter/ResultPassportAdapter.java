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
import com.example.sqlbrite.model.PassportResult;

import java.util.ArrayList;
import java.util.List;

import static com.example.sqlbrite.utils.BitmapUtil.changeBitmapSize;

public class ResultPassportAdapter extends BaseAdapter {

    private Context mContext;
    private List<PassportResult> arrayList = new ArrayList<PassportResult>();

    public ResultPassportAdapter(Context context, List<PassportResult> result_list){
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_result_passport, null);
            holder.imageView = convertView.findViewById(R.id.image_passport);
            holder.countryCode = convertView.findViewById(R.id.text_country_code);
            holder.passportIssuanceLocation = convertView.findViewById(R.id.text_passport_issuance_location);
            holder.validUntil = convertView.findViewById(R.id.text_valid_until);
            holder.passportNumber = convertView.findViewById(R.id.text_vehicle_passport_number);
            holder.passportDateOfIssue = convertView.findViewById(R.id.text_passport_date_of_issue);
            holder.birthPlace = convertView.findViewById(R.id.text_birth_place);
            holder.ownerName = convertView.findViewById(R.id.text_owner_name);
            holder.pinyinOfName = convertView.findViewById(R.id.text_pinyin_of_name);
            holder.ownerBirthday = convertView.findViewById(R.id.text_owner_birthday);
            holder.sex = convertView.findViewById(R.id.text_sex);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        PassportResult passportResult = arrayList.get(position);
        holder.imageView.setImageBitmap(changeBitmapSize(IntelligentDetectionActivity.bitmap,300,200));
        holder.countryCode.setText(passportResult.words_result.countryCode.getWords());
        holder.passportIssuanceLocation.setText(passportResult.words_result.passportIssuanceLocation.getWords());
        holder.validUntil.setText(passportResult.words_result.validUntil.getWords());
        holder.passportNumber.setText(passportResult.words_result.passportNumber.getWords());
        holder.passportDateOfIssue.setText(passportResult.words_result.passportDateOfIssue.getWords());
        holder.birthPlace.setText(passportResult.words_result.birthPlace.getWords());
        holder.ownerName.setText(passportResult.words_result.ownerName.getWords());
        holder.pinyinOfName.setText(passportResult.words_result.pinyinOfName.getWords());
        holder.ownerBirthday.setText(passportResult.words_result.ownerBirthday.getWords());
        holder.sex.setText(passportResult.words_result.sex.getWords());
        return convertView;
    }

    public final class ViewHolder {
        private ImageView imageView;
        private TextView countryCode;
        private TextView passportIssuanceLocation;
        private TextView validUntil;
        private TextView passportNumber;
        private TextView passportDateOfIssue;
        private TextView birthPlace;
        private TextView ownerName;
        private TextView pinyinOfName;
        private TextView ownerBirthday;
        private TextView sex;
    }

}
