package com.example.sqlbrite.fragment;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.sqlbrite.R;
import com.example.sqlbrite.activity.MainActivity;

public class TabHistoryFragment extends Fragment {

    protected View view;
    protected Context context;

    private LinearLayout layout_image;
    private TextView view_image;
    private TextView icon_image;
    private LinearLayout layout_text;
    private TextView view_text;
    private TextView icon_text;
    private LinearLayout layout_driving_license;
    private TextView view_driving_license;
    private TextView icon_driving_license;
    private LinearLayout layout_id_card;
    private TextView view_id_card;
    private TextView icon_id_card;
    private LinearLayout layout_business_license;
    private TextView view_business_license;
    private TextView icon_business_license;
    private LinearLayout layout_bank_card;
    private TextView view_bank_card;
    private TextView icon_bank_card;
    private LinearLayout layout_driver_license;
    private TextView view_driver_license;
    private TextView icon_driver_license;
    private LinearLayout layout_license_plate;
    private TextView view_license_plate;
    private TextView icon_license_plate;
    private LinearLayout layout_passport;
    private TextView view_passport;
    private TextView icon_passport;
    private LinearLayout layout_train_ticket;
    private TextView view_train_ticket;
    private TextView icon_train_ticket;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        context = (MainActivity) getActivity();
        view = inflater.inflate(R.layout.fragment_tab_history,container,false);

        layout_image = view.findViewById(R.id.layout_image);
        view_image = view.findViewById(R.id.view_image);
        icon_image = view.findViewById(R.id.icon_image);
        layout_text = view.findViewById(R.id.layout_text);
        view_text = view.findViewById(R.id.view_text);
        icon_text = view.findViewById(R.id.icon_text);
        layout_driving_license = view.findViewById(R.id.layout_driving_license);
        view_driving_license = view.findViewById(R.id.view_driving_license);
        icon_driving_license = view.findViewById(R.id.icon_driving_license);
        layout_id_card = view.findViewById(R.id.layout_id_card);
        view_id_card = view.findViewById(R.id.view_id_card);
        icon_id_card = view.findViewById(R.id.icon_id_card);
        layout_business_license = view.findViewById(R.id.layout_business_license);
        view_business_license = view.findViewById(R.id.view_business_license);
        icon_business_license = view.findViewById(R.id.icon_business_license);
        layout_bank_card = view.findViewById(R.id.layout_bank_card);
        view_bank_card = view.findViewById(R.id.view_bank_card);
        icon_bank_card = view.findViewById(R.id.icon_bank_card);
        layout_driver_license = view.findViewById(R.id.layout_driver_license);
        view_driver_license = view.findViewById(R.id.view_driver_license);
        icon_driver_license = view.findViewById(R.id.icon_driver_license);
        layout_license_plate = view.findViewById(R.id.layout_license_plate);
        view_license_plate = view.findViewById(R.id.view_license_plate);
        icon_license_plate = view.findViewById(R.id.icon_license_plate);
        layout_passport = view.findViewById(R.id.layout_passport);
        view_passport = view.findViewById(R.id.view_passport);
        icon_passport = view.findViewById(R.id.icon_passport);
        layout_train_ticket = view.findViewById(R.id.layout_train_ticket);
        view_train_ticket = view.findViewById(R.id.view_train_ticket);
        icon_train_ticket = view.findViewById(R.id.icon_train_ticket);

        Typeface iconfont = Typeface.createFromAsset(context.getAssets(), "iconfont/iconfont.ttf");
        view_image.setTypeface(iconfont);
        icon_image.setTypeface(iconfont);
        view_text.setTypeface(iconfont);
        icon_text.setTypeface(iconfont);
        view_driving_license.setTypeface(iconfont);
        icon_driving_license.setTypeface(iconfont);
        view_id_card.setTypeface(iconfont);
        icon_id_card.setTypeface(iconfont);
        view_business_license.setTypeface(iconfont);
        icon_business_license.setTypeface(iconfont);
        view_bank_card.setTypeface(iconfont);
        icon_bank_card.setTypeface(iconfont);
        view_driver_license.setTypeface(iconfont);
        icon_driver_license.setTypeface(iconfont);
        view_license_plate.setTypeface(iconfont);
        icon_license_plate.setTypeface(iconfont);
        view_passport.setTypeface(iconfont);
        icon_passport.setTypeface(iconfont);
        view_train_ticket.setTypeface(iconfont);
        icon_train_ticket.setTypeface(iconfont);

        return view;
    }


}
