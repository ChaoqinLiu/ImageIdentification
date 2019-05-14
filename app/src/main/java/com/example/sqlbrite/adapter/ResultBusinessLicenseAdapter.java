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
import com.example.sqlbrite.model.BusinessLicenseResult;

import java.util.ArrayList;
import java.util.List;

import static com.example.sqlbrite.util.BitmapUtil.changeBitmapSize;

public class ResultBusinessLicenseAdapter extends BaseAdapter {

    private Context mContext;
    private List<BusinessLicenseResult> arrayList = new ArrayList<BusinessLicenseResult>();

    public ResultBusinessLicenseAdapter(Context context, List<BusinessLicenseResult> result_list){
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_result_business_license, null);
            holder.imageView = convertView.findViewById(R.id.image_business_license);
            holder.registeredCapital = convertView.findViewById(R.id.text_registered_capital);
            holder.socialCreditCode = convertView.findViewById(R.id.text_social_credit_code);
            holder.companyName = convertView.findViewById(R.id.text_company_name);
            holder.legalPerson = convertView.findViewById(R.id.text_legal_person);
            holder.documentNumber = convertView.findViewById(R.id.text_document_number);
            holder.formation = convertView.findViewById(R.id.text_formation);
            holder.dateOfEstablishment = convertView.findViewById(R.id.text_date_of_establishment);
            holder.companyAddress = convertView.findViewById(R.id.text_company_address);
            holder.businessScope = convertView.findViewById(R.id.text_business_scope);
            holder.typeOfCompany = convertView.findViewById(R.id.text_type_of_company);
            holder.businessLicenseValidityPeriod = convertView.findViewById(R.id.text_business_license_validity_period);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        BusinessLicenseResult businessLicenseResult = arrayList.get(position);
        holder.imageView.setImageBitmap(changeBitmapSize(IntelligentDetectionActivity.bitmap,300,200));
        holder.registeredCapital.setText(businessLicenseResult.words_result.registeredCapital.getWords());
        holder.socialCreditCode.setText(businessLicenseResult.words_result.socialCreditCode.getWords());
        holder.companyName.setText(businessLicenseResult.words_result.companyName.getWords());
        holder.legalPerson.setText(businessLicenseResult.words_result.legalPerson.getWords());
        holder.documentNumber.setText(businessLicenseResult.words_result.documentNumber.getWords());
        holder.formation.setText(businessLicenseResult.words_result.formation.getWords());
        holder.dateOfEstablishment.setText(businessLicenseResult.words_result.dateOfEstablishment.getWords());
        holder.companyAddress.setText(businessLicenseResult.words_result.companyAddress.getWords());
        holder.businessScope.setText(businessLicenseResult.words_result.businessScope.getWords());
        holder.typeOfCompany.setText(businessLicenseResult.words_result.typeOfCompany.getWords());
        holder.businessLicenseValidityPeriod.setText(businessLicenseResult.words_result.businessLicenseValidityPeriod.getWords());
        return convertView;
    }

    public final class ViewHolder {
        private ImageView imageView;
        private TextView registeredCapital;
        private TextView socialCreditCode;
        private TextView companyName;
        private TextView legalPerson;
        private TextView documentNumber;
        private TextView formation;
        private TextView dateOfEstablishment;
        private TextView companyAddress;
        private TextView businessScope;
        private TextView typeOfCompany;
        private TextView businessLicenseValidityPeriod;
    }

}
