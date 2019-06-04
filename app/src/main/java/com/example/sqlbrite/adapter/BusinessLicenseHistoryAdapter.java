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
import com.example.sqlbrite.model.BusinessLicenseHistory.BusinessLicenseHistoryArray;

import java.util.ArrayList;
import java.util.List;

import static com.example.sqlbrite.utils.BitmapUtil.changeBitmapSize;

public class BusinessLicenseHistoryAdapter extends BaseAdapter {

    private Context mContext;
    private List<BusinessLicenseHistoryArray> arrayList = new ArrayList<BusinessLicenseHistoryArray>();

    public BusinessLicenseHistoryAdapter(Context context, List<BusinessLicenseHistoryArray> result_list){
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_result_business_license_history, null);
            holder.id = convertView.findViewById(R.id.text_id);
            holder.companyName = convertView.findViewById(R.id.text_company_name);
            holder.legalPerson = convertView.findViewById(R.id.text_legal_person);
            holder.imageView = convertView.findViewById(R.id.image_view_business_license);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        BusinessLicenseHistoryArray result = arrayList.get(position);
        byte[] bytes = result.bytes;
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
        holder.imageView.setImageBitmap(changeBitmapSize(bitmap,70,70));
        holder.id.setText(String.valueOf(result.id));
        holder.companyName.setText(result.companyName);
        holder.legalPerson.setText(result.legalPerson);
        bitmap.recycle();
        return convertView;
    }

    public final class ViewHolder {
        private TextView id;
        private TextView companyName;
        private TextView legalPerson;
        private ImageView imageView;
    }
}
