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
import com.example.sqlbrite.model.IDCardForBackResult;

import java.util.ArrayList;
import java.util.List;

import static com.example.sqlbrite.util.BitmapUtil.changeBitmapSize;

public class ResultIDCardForBackAdapter extends BaseAdapter {

    private Context mContext;
    private List<IDCardForBackResult> arrayList = new ArrayList<IDCardForBackResult>();

    public ResultIDCardForBackAdapter(Context context, List<IDCardForBackResult> result_list){
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_result_id_card_for_back, null);
            holder.dateOfIssue = convertView.findViewById(R.id.text_dateOfIssue);
            holder.issuingAuthority = convertView.findViewById(R.id.text_issuingAuthority);
            holder.expirationDate = convertView.findViewById(R.id.text_expirationDate);
            holder.imageView = convertView.findViewById(R.id.image_id_card_back);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        IDCardForBackResult result = arrayList.get(position);
        holder.imageView.setImageBitmap(changeBitmapSize(IntelligentDetectionActivity.bitmap));
        holder.dateOfIssue.setText(result.words_result.dateOfIssue.getWords());
        holder.issuingAuthority.setText(result.words_result.issuingAuthority.getWords());
        holder.expirationDate.setText(result.words_result.expirationDate.getWords());
        return convertView;
    }

    public final class ViewHolder {
        private ImageView imageView;
        private TextView dateOfIssue;
        private TextView issuingAuthority;
        private TextView expirationDate;
    }

}
