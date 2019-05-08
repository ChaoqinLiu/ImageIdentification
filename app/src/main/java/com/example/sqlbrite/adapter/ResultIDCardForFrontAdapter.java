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
import com.example.sqlbrite.model.IDCardForFrontResult;
import java.util.ArrayList;

public class ResultIDCardForFrontAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<IDCardForFrontResult> arrayList = new ArrayList<IDCardForFrontResult>();

    public ResultIDCardForFrontAdapter(Context context, ArrayList<IDCardForFrontResult> result_list){
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
        holder.userName.setText(result.getUserName());
        holder.gender.setText(result.getGender());
        holder.nationality.setText(result.getNationality());
        holder.birthday.setText(result.getBirthday());
        holder.idNumber.setText(result.getIdNumber());
        holder.address.setText(result.getAddress());
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
