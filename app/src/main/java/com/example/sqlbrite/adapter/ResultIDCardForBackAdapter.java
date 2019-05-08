package com.example.sqlbrite.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
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

public class ResultIDCardForBackAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<IDCardForBackResult> arrayList = new ArrayList<IDCardForBackResult>();

    public ResultIDCardForBackAdapter(Context context, ArrayList<IDCardForBackResult> result_list){
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
        holder.dateOfIssue.setText(result.getDateOfIssue());
        holder.issuingAuthority.setText(result.getIssuingAuthority());
        holder.expirationDate.setText(result.getExpirationDate());
        return convertView;
    }

    public final class ViewHolder {
        private ImageView imageView;
        private TextView dateOfIssue;
        private TextView issuingAuthority;
        private TextView expirationDate;
    }

    private Bitmap changeBitmapSize(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        //设置图片大小
        int newWidth=300;
        int newHeight=185;
        //计算压缩的比率
        float scaleWidth = ((float)newWidth)/width;
        float scaleHeight=((float)newHeight)/height;
        //获取想要缩放的matrix
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth,scaleHeight);
        //获取新的bitmap
        bitmap = Bitmap.createBitmap(bitmap,0,0,width,height,matrix,true);
        bitmap.getWidth();
        bitmap.getHeight();
        return bitmap;
    }
}
