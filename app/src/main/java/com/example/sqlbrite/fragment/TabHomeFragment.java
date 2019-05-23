package com.example.sqlbrite.fragment;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.sqlbrite.R;
import com.example.sqlbrite.activity.AlbumSelectionActivity;
import com.example.sqlbrite.activity.MainActivity;
import com.example.sqlbrite.activity.TakePictureActivity;
import com.jakewharton.rxbinding2.view.RxView;
import com.safframework.log.L;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.concurrent.TimeUnit;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

public class TabHomeFragment extends Fragment {

    protected View view;
    protected Context context;

    private static final int TYPE_IMAGE_RESULT_CODE = 10;
    private static final int TYPE_TEXT_RESULT_CODE = 20;
    private static final int ID_CARD_BACK_RESULT_CODE = 40;
    private static final int ID_CARD_FRONT_RESULT_CODE = 50;
    private static final int BANK_CARD_FRONT_RESULT_CODE = 60;
    private static final int LICENSE_PLATE_RESULT_CODE = 70;
    private static final int DRIVER_LICENSE_RESULT_CODE = 80;
    private static final int TRAIN_TICKET_RESULT_CODE = 90;
    private static final int PASSPORT_RESULT_CODE = 100;
    private static final int DRIVING_LICENSE_RESULT_CODE = 110;
    private static final int BUSINESS_LICENSE_RESULT_CODE = 120;

    private static final String TYPE_IMAGE = "type_image";
    private static final String TYPE_TEXT = "type_text";
    private static final String TYPE_ID_CARD = "type_id_card";
    private static final String TYPE_BANK_CARD = "type_bank_card";
    private static final String TYPE_LICENSE_PLATE = "type_license_plate";
    private static final String TYPE_DRIVER_LICENSE = "type_driver_license";
    private static final String TYPE_TRAIN_TICKET = "type_train_ticket";
    private static final String TYPE_PASSPORT = "type_passport";
    private static final String TYPE_DRIVING_LICENSE = "type_driving_license";
    private static final String TYPE_BUSINESS_LICENSE = "type_business_license";

    private boolean ClickTypeForIDCard;  //身份证识别点击类型的标识,true为点击，false为长击
    private Class activity;    //目标 Activity
    private MainActivity mainActivity;

    private LinearLayout id_card_back;
    private LinearLayout id_card_front;
    private TextView icon_back;
    private TextView icon_front;
    private static final String ID_CARD_BACK = "back";  //身份证正面（国徽面）
    private static final String ID_CARD_FRONT = "front";//身份证反面（照片面）

    private TextView text_image;
    private TextView text_tx;
    private TextView text_driving_license;
    private TextView text_id_card;
    private TextView text_business_license;
    private TextView text_bank_card;
    private TextView text_train_ticket;
    private TextView text_driver_license;
    private TextView text_license_plate;
    private TextView text_passport;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        context = (MainActivity) getActivity();
        view = inflater.inflate(R.layout.fragment_tab_home,container,false);

        text_image = view.findViewById(R.id.text_image);
        text_tx = view.findViewById(R.id.text_tx);
        text_id_card = view.findViewById(R.id.text_id_card);
        text_business_license = view.findViewById(R.id.text_business_license);
        text_license_plate = view.findViewById(R.id.text_license_plate);
        text_train_ticket = view.findViewById(R.id.text_train_ticket);
        text_bank_card = view.findViewById(R.id.text_bank_card);
        text_passport = view.findViewById(R.id.text_passport);
        text_driving_license = view.findViewById(R.id.text_driving_license);
        text_driver_license = view.findViewById(R.id.text_driver_license);

        Typeface iconfont = Typeface.createFromAsset(context.getAssets(), "iconfont/iconfont.ttf");
        text_image.setTypeface(iconfont);
        text_tx.setTypeface(iconfont);
        text_id_card.setTypeface(iconfont);
        text_business_license.setTypeface(iconfont);
        text_license_plate.setTypeface(iconfont);
        text_driver_license.setTypeface(iconfont);
        text_train_ticket.setTypeface(iconfont);
        text_bank_card.setTypeface(iconfont);
        text_passport.setTypeface(iconfont);
        text_driving_license.setTypeface(iconfont);

        initHomeView();
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mainActivity = (MainActivity) context;
    }

    private void initHomeView(){
        RxPermissions rxPermissions = new RxPermissions(mainActivity);

        //拍照作为图像来源
        RxView.clicks(text_image)
                .throttleFirst(600,TimeUnit.MILLISECONDS)
                .compose(rxPermissions.ensure(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE))
                .subscribe(new Consumer<Boolean>() {

                    @Override
                    public void accept(@NonNull Boolean granted) throws Exception {

                        if (granted) {
                            Intent intent = new Intent(context, TakePictureActivity.class);
                            intent.putExtra("type_image", TYPE_IMAGE);
                            startActivityForResult(intent, TYPE_IMAGE_RESULT_CODE);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        L.i(throwable.getMessage());
                    }
                });

        //相册图片作为图像来源
        RxView.longClicks(text_image)
                .throttleFirst(600,TimeUnit.MILLISECONDS)
                .compose(rxPermissions.ensure(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE))
                .subscribe(new Consumer<Boolean>() {

                    @Override
                    public void accept(@NonNull Boolean granted) throws Exception {

                        if (granted) {
                            Intent intent = new Intent(context, AlbumSelectionActivity.class);
                            intent.putExtra("type_image", TYPE_IMAGE);
                            startActivityForResult(intent, TYPE_IMAGE_RESULT_CODE);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        L.i(throwable.getMessage());
                    }
                });

        RxView.clicks(text_tx)
                .throttleFirst(600,TimeUnit.MILLISECONDS)
                .compose(rxPermissions.ensure(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE))
                .subscribe(new Consumer<Boolean>() {

                    @Override
                    public void accept(@NonNull Boolean granted) throws Exception {

                        if (granted) {
                            Intent intent = new Intent(context, TakePictureActivity.class);
                            intent.putExtra("type_text", TYPE_TEXT);
                            startActivityForResult(intent, TYPE_IMAGE_RESULT_CODE);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        L.i(throwable.getMessage());
                    }
                });

        RxView.longClicks(text_tx)
                .throttleFirst(600,TimeUnit.MILLISECONDS)
                .compose(rxPermissions.ensure(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE))
                .subscribe(new Consumer<Boolean>() {

                    @Override
                    public void accept(@NonNull Boolean granted) throws Exception {

                        if (granted) {
                            Intent intent = new Intent(context, AlbumSelectionActivity.class);
                            intent.putExtra("type_text", TYPE_TEXT);
                            startActivityForResult(intent, TYPE_TEXT_RESULT_CODE);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        L.i(throwable.getMessage());
                    }
                });

        //身份证识别
        RxView.clicks(text_id_card)
                .throttleFirst(600,TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        ClickTypeForIDCard = true;
                        showIDCardOptionDialog();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        L.i(throwable.getMessage());
                    }
                });

        RxView.longClicks(text_id_card)
                .throttleFirst(600,TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        ClickTypeForIDCard = false;
                        showIDCardOptionDialog();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        L.i(throwable.getMessage());
                    }
                });

        RxView.clicks(text_bank_card)
                .throttleFirst(600,TimeUnit.MILLISECONDS)
                .compose(rxPermissions.ensure(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE))
                .subscribe(new Consumer<Boolean>() {

                    @Override
                    public void accept(@NonNull Boolean granted) throws Exception {

                        if (granted) {
                            Intent intent = new Intent(context, TakePictureActivity.class);
                            intent.putExtra("type_bank_card", TYPE_BANK_CARD);
                            startActivityForResult(intent, BANK_CARD_FRONT_RESULT_CODE);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        L.i(throwable.getMessage());
                    }
                });

        RxView.longClicks(text_bank_card)
                .throttleFirst(600,TimeUnit.MILLISECONDS)
                .compose(rxPermissions.ensure(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE))
                .subscribe(new Consumer<Boolean>() {

                    @Override
                    public void accept(@NonNull Boolean granted) throws Exception {

                        if (granted) {
                            Intent intent = new Intent(context, AlbumSelectionActivity.class);
                            intent.putExtra("type_bank_card", TYPE_BANK_CARD);
                            startActivityForResult(intent, BANK_CARD_FRONT_RESULT_CODE);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        L.i(throwable.getMessage());
                    }
                });

        RxView.clicks(text_license_plate)
                .throttleFirst(600,TimeUnit.MILLISECONDS)
                .compose(rxPermissions.ensure(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE))
                .subscribe(new Consumer<Boolean>() {

                    @Override
                    public void accept(@NonNull Boolean granted) throws Exception {

                        if (granted) {
                            Intent intent = new Intent(context, TakePictureActivity.class);
                            intent.putExtra("type_license_plate", TYPE_LICENSE_PLATE);
                            startActivityForResult(intent, LICENSE_PLATE_RESULT_CODE);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        L.i(throwable.getMessage());
                    }
                });

        RxView.longClicks(text_license_plate)
                .throttleFirst(600,TimeUnit.MILLISECONDS)
                .compose(rxPermissions.ensure(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE))
                .subscribe(new Consumer<Boolean>() {

                    @Override
                    public void accept(@NonNull Boolean granted) throws Exception {

                        if (granted) {
                            Intent intent = new Intent(context, AlbumSelectionActivity.class);
                            intent.putExtra("type_license_plate", TYPE_LICENSE_PLATE);
                            startActivityForResult(intent, LICENSE_PLATE_RESULT_CODE);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        L.i(throwable.getMessage());
                    }
                });

        RxView.clicks(text_driver_license)
                .throttleFirst(600,TimeUnit.MILLISECONDS)
                .compose(rxPermissions.ensure(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE))
                .subscribe(new Consumer<Boolean>() {

                    @Override
                    public void accept(@NonNull Boolean granted) throws Exception {

                        if (granted) {
                            Intent intent = new Intent(context, TakePictureActivity.class);
                            intent.putExtra("type_driver_license", TYPE_DRIVER_LICENSE);
                            startActivityForResult(intent, DRIVER_LICENSE_RESULT_CODE);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        L.i(throwable.getMessage());
                    }
                });

        RxView.longClicks(text_driver_license)
                .throttleFirst(600,TimeUnit.MILLISECONDS)
                .compose(rxPermissions.ensure(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE))
                .subscribe(new Consumer<Boolean>() {

                    @Override
                    public void accept(@NonNull Boolean granted) throws Exception {

                        if (granted) {
                            Intent intent = new Intent(context, AlbumSelectionActivity.class);
                            intent.putExtra("type_driver_license", TYPE_DRIVER_LICENSE);
                            startActivityForResult(intent, DRIVER_LICENSE_RESULT_CODE);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        L.i(throwable.getMessage());
                    }
                });

        RxView.clicks(text_train_ticket)
                .throttleFirst(600,TimeUnit.MILLISECONDS)
                .compose(rxPermissions.ensure(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE))
                .subscribe(new Consumer<Boolean>() {

                    @Override
                    public void accept(@NonNull Boolean granted) throws Exception {

                        if (granted) {
                            Intent intent = new Intent(context, TakePictureActivity.class);
                            intent.putExtra("type_train_ticket", TYPE_TRAIN_TICKET);
                            startActivityForResult(intent, TRAIN_TICKET_RESULT_CODE);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        L.i(throwable.getMessage());
                    }
                });

        RxView.longClicks(text_train_ticket)
                .throttleFirst(600,TimeUnit.MILLISECONDS)
                .compose(rxPermissions.ensure(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE))
                .subscribe(new Consumer<Boolean>() {

                    @Override
                    public void accept(@NonNull Boolean granted) throws Exception {

                        if (granted) {
                            Intent intent = new Intent(context, AlbumSelectionActivity.class);
                            intent.putExtra("type_train_ticket", TYPE_TRAIN_TICKET);
                            startActivityForResult(intent, TRAIN_TICKET_RESULT_CODE);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        L.i(throwable.getMessage());
                    }
                });

        RxView.clicks(text_passport)
                .throttleFirst(600,TimeUnit.MILLISECONDS)
                .compose(rxPermissions.ensure(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE))
                .subscribe(new Consumer<Boolean>() {

                    @Override
                    public void accept(@NonNull Boolean granted) throws Exception {

                        if (granted) {
                            Intent intent = new Intent(context, TakePictureActivity.class);
                            intent.putExtra("type_passport", TYPE_PASSPORT);
                            startActivityForResult(intent, PASSPORT_RESULT_CODE);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        L.i(throwable.getMessage());
                    }
                });

        RxView.longClicks(text_passport)
                .throttleFirst(600,TimeUnit.MILLISECONDS)
                .compose(rxPermissions.ensure(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE))
                .subscribe(new Consumer<Boolean>() {

                    @Override
                    public void accept(@NonNull Boolean granted) throws Exception {

                        if (granted) {
                            Intent intent = new Intent(context, AlbumSelectionActivity.class);
                            intent.putExtra("type_hong_kong_and_macau_pass", TYPE_PASSPORT);
                            startActivityForResult(intent, PASSPORT_RESULT_CODE);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        L.i(throwable.getMessage());
                    }
                });

        RxView.clicks(text_driving_license)
                .throttleFirst(600,TimeUnit.MILLISECONDS)
                .compose(rxPermissions.ensure(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE))
                .subscribe(new Consumer<Boolean>() {

                    @Override
                    public void accept(@NonNull Boolean granted) throws Exception {

                        if (granted) {
                            Intent intent = new Intent(context, TakePictureActivity.class);
                            intent.putExtra("type_driving_license", TYPE_DRIVING_LICENSE);
                            startActivityForResult(intent, DRIVING_LICENSE_RESULT_CODE);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        L.i(throwable.getMessage());
                    }
                });

        RxView.longClicks(text_driving_license)
                .throttleFirst(600,TimeUnit.MILLISECONDS)
                .compose(rxPermissions.ensure(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE))
                .subscribe(new Consumer<Boolean>() {

                    @Override
                    public void accept(@NonNull Boolean granted) throws Exception {

                        if (granted) {
                            Intent intent = new Intent(context, AlbumSelectionActivity.class);
                            intent.putExtra("type_driving_license", TYPE_DRIVING_LICENSE);
                            startActivityForResult(intent, DRIVING_LICENSE_RESULT_CODE);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        L.i(throwable.getMessage());
                    }
                });

        RxView.clicks(text_business_license)
                .throttleFirst(600,TimeUnit.MILLISECONDS)
                .compose(rxPermissions.ensure(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE))
                .subscribe(new Consumer<Boolean>() {

                    @Override
                    public void accept(@NonNull Boolean granted) throws Exception {

                        if (granted) {
                            Intent intent = new Intent(context, TakePictureActivity.class);
                            intent.putExtra("type_business_license", TYPE_BUSINESS_LICENSE);
                            startActivityForResult(intent, BUSINESS_LICENSE_RESULT_CODE);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        L.i(throwable.getMessage());
                    }
                });

        RxView.longClicks(text_business_license)
                .throttleFirst(600,TimeUnit.MILLISECONDS)
                .compose(rxPermissions.ensure(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE))
                .subscribe(new Consumer<Boolean>() {

                    @Override
                    public void accept(@NonNull Boolean granted) throws Exception {

                        if (granted) {
                            Intent intent = new Intent(context, AlbumSelectionActivity.class);
                            intent.putExtra("type_business_license", TYPE_BUSINESS_LICENSE);
                            startActivityForResult(intent, BUSINESS_LICENSE_RESULT_CODE);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        L.i(throwable.getMessage());
                    }
                });
    }

    private void showIDCardOptionDialog(){
        RxPermissions rxPermissions = new RxPermissions(mainActivity);

        if (ClickTypeForIDCard == true) {
            activity = TakePictureActivity.class;
        } else if (ClickTypeForIDCard == false) {
            activity = AlbumSelectionActivity.class;
        }

        //使用Dialog、设置style
        final Dialog dialog = new Dialog(context,R.style.DialogTheme);
        //设置布局
        View view = View.inflate(context,R.layout.dialog_id_card_option_layout,null);
        dialog.setContentView(view);

        Window window = dialog.getWindow();
        //设置弹出位置
        window.setGravity(Gravity.BOTTOM);
        //设置对话框大小
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();

        Typeface iconfont = Typeface.createFromAsset(context.getAssets(), "iconfont/iconfont.ttf");
        icon_back = dialog.findViewById(R.id.icon_back);
        icon_front = dialog.findViewById(R.id.icon_front);
        icon_back.setTypeface(iconfont);
        icon_front.setTypeface(iconfont);

        id_card_back = dialog.findViewById(R.id.tv_id_card_back);
        id_card_front = dialog.findViewById(R.id.tv_id_card_front);

        RxView.clicks(id_card_back)
                .throttleFirst(600,TimeUnit.MILLISECONDS)
                .compose(rxPermissions.ensure(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE))
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        if (aBoolean) {
                            Intent intent = new Intent(context, activity);
                            intent.putExtra("id_card_side", ID_CARD_BACK);
                            intent.putExtra("type_id_card", TYPE_ID_CARD);
                            startActivityForResult(intent, ID_CARD_BACK_RESULT_CODE);
                            dialog.dismiss();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        L.i(throwable.getMessage());
                    }
                });

        RxView.clicks(id_card_front)
                .throttleFirst(600,TimeUnit.MILLISECONDS)
                .compose(rxPermissions.ensure(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE))
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        if (aBoolean) {
                            Intent intent = new Intent(context, activity);
                            intent.putExtra("id_card_side", ID_CARD_FRONT);
                            intent.putExtra("type_id_card", TYPE_ID_CARD);
                            startActivityForResult(intent, ID_CARD_FRONT_RESULT_CODE);
                            dialog.dismiss();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        L.i(throwable.getMessage());
                    }
                });

    }

}
