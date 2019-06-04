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
import com.example.sqlbrite.model.TrainTicketResult;

import java.util.ArrayList;
import java.util.List;

import static com.example.sqlbrite.utils.BitmapUtil.changeBitmapSize;

public class ResultTrainTicketAdapter extends BaseAdapter {

    private Context mContext;
    private List<TrainTicketResult> arrayList = new ArrayList<TrainTicketResult>();

    public ResultTrainTicketAdapter(Context context, List<TrainTicketResult> result_list){
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_result_train_ticket, null);
            holder.starting_station = convertView.findViewById(R.id.text_starting_station);
            holder.destination_station = convertView.findViewById(R.id.text_destination_station);
            holder.passenger_name = convertView.findViewById(R.id.text_passenger_name);
            holder.seat_category = convertView.findViewById(R.id.text_seat_category);
            holder.departure_date = convertView.findViewById(R.id.text_departure_date);
            holder.ticket_rates = convertView.findViewById(R.id.text_ticket_rates);
            holder.imageView = convertView.findViewById(R.id.image_train_ticket);
            holder.ticket_num = convertView.findViewById(R.id.text_ticket_num);
            holder.train_num = convertView.findViewById(R.id.text_train_num);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        TrainTicketResult trainTicketResult = arrayList.get(position);
        holder.imageView.setImageBitmap(changeBitmapSize(IntelligentDetectionActivity.bitmap,300,189));
        holder.starting_station.setText(trainTicketResult.words_result.getStartingStation());
        holder.destination_station.setText(trainTicketResult.words_result.getDestinationstation());
        holder.passenger_name.setText(trainTicketResult.words_result.getPassengerName());
        holder.seat_category.setText(trainTicketResult.words_result.getSeatCategory());
        holder.departure_date.setText(trainTicketResult.words_result.getDepartureDate());
        holder.ticket_rates.setText(trainTicketResult.words_result.getTicketRates());
        holder.ticket_num.setText(trainTicketResult.words_result.getTicketNum());
        holder.train_num.setText(trainTicketResult.words_result.getTrainNum());
        return convertView;
    }

    public final class ViewHolder {
        private ImageView imageView;
        private TextView starting_station;
        private TextView destination_station;
        private TextView passenger_name;
        private TextView seat_category;
        private TextView departure_date;
        private TextView ticket_rates;
        private TextView ticket_num;
        private TextView train_num;
    }

}
