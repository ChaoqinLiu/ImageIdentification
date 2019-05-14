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
import com.example.sqlbrite.model.BankCardResult;

import java.util.ArrayList;
import java.util.List;

import static com.example.sqlbrite.util.BitmapUtil.changeBitmapSize;

public class ResultBankCardAdapter extends BaseAdapter {

    private Context mContext;
    private List<BankCardResult> arrayList = new ArrayList<BankCardResult>();

    private String bankCardType;

    public ResultBankCardAdapter(Context context, List<BankCardResult> result_list){
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_result_bank_card, null);
            holder.bank_card_number = convertView.findViewById(R.id.text_bank_card_number);
            holder.valid_date = convertView.findViewById(R.id.text_valid_date);
            holder.bank_name = convertView.findViewById(R.id.text_bank_name);
            holder.bank_card_type = convertView.findViewById(R.id.text_bank_card_type);
            holder.imageView = convertView.findViewById(R.id.image_bank_card);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        BankCardResult bankResult = arrayList.get(position);
        holder.imageView.setImageBitmap(changeBitmapSize(IntelligentDetectionActivity.bitmap,300,189));
        holder.bank_card_number.setText(bankResult.result.getBankCardNumber());
        holder.bank_name.setText(bankResult.result.getBankName());
        holder.valid_date.setText(bankResult.result.getValidDate());
        try {
            int bank_card_type = Integer.parseInt(bankResult.result.getBankCardType());
            if (bank_card_type == 0) {
                bankCardType = "不能识别";
            } else if (bank_card_type == 1) {
                bankCardType = "借记卡";
            } else if (bank_card_type == 2) {
                bankCardType = "信用卡";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        holder.bank_card_type.setText(bankCardType);
        return convertView;
    }

    public final class ViewHolder {
        private TextView bank_card_number;
        private TextView valid_date;
        private TextView bank_name;
        private TextView bank_card_type;
        private ImageView imageView;
    }
}
