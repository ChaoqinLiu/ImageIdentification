package com.example.sqlbrite.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentTabHost;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sqlbrite.R;
import com.example.sqlbrite.app.BaseActivity;
import com.example.sqlbrite.fragment.TabHistoryFragment;
import com.example.sqlbrite.fragment.TabHomeFragment;
import com.example.sqlbrite.fragment.TabMeFragment;
import com.jakewharton.rxbinding2.view.RxView;
import com.safframework.injectview.annotations.InjectView;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.concurrent.TimeUnit;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

public class MainActivity extends BaseActivity {

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

    private static final String TYPE_IMAGE = "type_image";  //识别图片，包括动物，植物等等
    private static final String TYPE_TEXT = "type_text";   //文字识别
    private static final String TYPE_ID_CARD = "type_id_card";  //身份证
    private static final String TYPE_BANK_CARD = "type_bank_card";  //银行卡
    private static final String TYPE_LICENSE_PLATE = "type_license_plate";
    private static final String TYPE_DRIVER_LICENSE = "type_driver_license";
    private static final String TYPE_TRAIN_TICKET = "type_train_ticket";
    private static final String TYPE_PASSPORT = "type_passport";
    private static final String TYPE_DRIVING_LICENSE = "type_driving_license";
    private static final String TYPE_BUSINESS_LICENSE = "type_business_license";

    private static final String TAG = "MainActivity";
    private FragmentTabHost tabHost; // 声明一个碎片标签栏对象

    private boolean mIsExit;
    private boolean ClickTypeForIDCard;  //身份证识别点击类型的标识,true为点击，false为长击
    private Class activity;    //目标 Activity

    private LinearLayout id_card_back;
    private LinearLayout id_card_front;
    private TextView icon_back;
    private TextView icon_front;
    private static final String ID_CARD_BACK = "back";  //身份证正面（国徽面）
    private static final String ID_CARD_FRONT = "front";//身份证反面（照片面）

    @InjectView(R.id.text_image)
    TextView text_image;

    @InjectView(R.id.text_tx)
    TextView text_tx;

    @InjectView(R.id.text_driving_license)
    TextView text_driving_license;

    @InjectView(R.id.text_id_card)
    TextView text_id_card;

    @InjectView(R.id.text_business_license)
    TextView text_business_license;

    @InjectView(R.id.text_bank_card)
    TextView text_bank_card;

    @InjectView(R.id.text_train_ticket)
    TextView text_train_ticket;

    @InjectView(R.id.text_driver_license)
    TextView text_driver_license;

    @InjectView(R.id.text_license_plate)
    TextView text_license_plate;

    @InjectView(R.id.text_passport)
    TextView text_passport;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Typeface iconfont = Typeface.createFromAsset(getAssets(), "iconfont/iconfont.ttf");
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
        //startTabFragment();

    }

    private void initHomeView(){
        RxPermissions rxPermissions = new RxPermissions(this);

         //拍照作为图像来源
        RxView.clicks(text_image)
                .throttleFirst(600,TimeUnit.MILLISECONDS)
                .compose(rxPermissions.ensure(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE))
                .subscribe(new Consumer<Boolean>() {

                    @Override
                    public void accept(@NonNull Boolean granted) throws Exception {

                        if (granted) {
                            Intent intent = new Intent(MainActivity.this, TakePictureActivity.class);
                            intent.putExtra("type_image", TYPE_IMAGE);
                            startActivityForResult(intent, TYPE_IMAGE_RESULT_CODE);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        System.out.println("onError()" + throwable.getMessage());
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
                            Intent intent = new Intent(MainActivity.this, AlbumSelectionActivity.class);
                            intent.putExtra("type_image", TYPE_IMAGE);
                            startActivityForResult(intent, TYPE_IMAGE_RESULT_CODE);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        System.out.println("onError()" + throwable.getMessage());
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
                            Intent intent = new Intent(MainActivity.this, TakePictureActivity.class);
                            intent.putExtra("type_text", TYPE_TEXT);
                            startActivityForResult(intent, TYPE_IMAGE_RESULT_CODE);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        System.out.println("onError()" + throwable.getMessage());
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
                            Intent intent = new Intent(MainActivity.this, AlbumSelectionActivity.class);
                            intent.putExtra("type_text", TYPE_TEXT);
                            startActivityForResult(intent, TYPE_TEXT_RESULT_CODE);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        System.out.println("onError()" + throwable.getMessage());
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
                        System.out.println("onError()" + throwable.getMessage());
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
                        System.out.println("onError()" + throwable.getMessage());
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
                            Intent intent = new Intent(MainActivity.this, TakePictureActivity.class);
                            intent.putExtra("type_bank_card", TYPE_BANK_CARD);
                            startActivityForResult(intent, BANK_CARD_FRONT_RESULT_CODE);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        System.out.println("onError()" + throwable.getMessage());
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
                            Intent intent = new Intent(MainActivity.this, AlbumSelectionActivity.class);
                            intent.putExtra("type_bank_card", TYPE_BANK_CARD);
                            startActivityForResult(intent, BANK_CARD_FRONT_RESULT_CODE);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        System.out.println("onError()" + throwable.getMessage());
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
                            Intent intent = new Intent(MainActivity.this, TakePictureActivity.class);
                            intent.putExtra("type_license_plate", TYPE_LICENSE_PLATE);
                            startActivityForResult(intent, LICENSE_PLATE_RESULT_CODE);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        System.out.println("onError()" + throwable.getMessage());
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
                            Intent intent = new Intent(MainActivity.this, AlbumSelectionActivity.class);
                            intent.putExtra("type_license_plate", TYPE_LICENSE_PLATE);
                            startActivityForResult(intent, LICENSE_PLATE_RESULT_CODE);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        System.out.println("onError()" + throwable.getMessage());
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
                            Intent intent = new Intent(MainActivity.this, TakePictureActivity.class);
                            intent.putExtra("type_driver_license", TYPE_DRIVER_LICENSE);
                            startActivityForResult(intent, DRIVER_LICENSE_RESULT_CODE);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        System.out.println("onError()" + throwable.getMessage());
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
                            Intent intent = new Intent(MainActivity.this, AlbumSelectionActivity.class);
                            intent.putExtra("type_driver_license", TYPE_DRIVER_LICENSE);
                            startActivityForResult(intent, DRIVER_LICENSE_RESULT_CODE);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        System.out.println("onError()" + throwable.getMessage());
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
                            Intent intent = new Intent(MainActivity.this, TakePictureActivity.class);
                            intent.putExtra("type_train_ticket", TYPE_TRAIN_TICKET);
                            startActivityForResult(intent, TRAIN_TICKET_RESULT_CODE);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        System.out.println("onError()" + throwable.getMessage());
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
                            Intent intent = new Intent(MainActivity.this, AlbumSelectionActivity.class);
                            intent.putExtra("type_train_ticket", TYPE_TRAIN_TICKET);
                            startActivityForResult(intent, TRAIN_TICKET_RESULT_CODE);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        System.out.println("onError()" + throwable.getMessage());
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
                            Intent intent = new Intent(MainActivity.this, TakePictureActivity.class);
                            intent.putExtra("type_passport", TYPE_PASSPORT);
                            startActivityForResult(intent, PASSPORT_RESULT_CODE);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        System.out.println("onError()" + throwable.getMessage());
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
                            Intent intent = new Intent(MainActivity.this, AlbumSelectionActivity.class);
                            intent.putExtra("type_hong_kong_and_macau_pass", TYPE_PASSPORT);
                            startActivityForResult(intent, PASSPORT_RESULT_CODE);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        System.out.println("onError()" + throwable.getMessage());
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
                            Intent intent = new Intent(MainActivity.this, TakePictureActivity.class);
                            intent.putExtra("type_driving_license", TYPE_DRIVING_LICENSE);
                            startActivityForResult(intent, DRIVING_LICENSE_RESULT_CODE);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        System.out.println("onError()" + throwable.getMessage());
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
                            Intent intent = new Intent(MainActivity.this, AlbumSelectionActivity.class);
                            intent.putExtra("type_driving_license", TYPE_DRIVING_LICENSE);
                            startActivityForResult(intent, DRIVING_LICENSE_RESULT_CODE);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        System.out.println("onError()" + throwable.getMessage());
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
                            Intent intent = new Intent(MainActivity.this, TakePictureActivity.class);
                            intent.putExtra("type_business_license", TYPE_BUSINESS_LICENSE);
                            startActivityForResult(intent, BUSINESS_LICENSE_RESULT_CODE);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        System.out.println("onError()" + throwable.getMessage());
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
                            Intent intent = new Intent(MainActivity.this, AlbumSelectionActivity.class);
                            intent.putExtra("type_business_license", TYPE_BUSINESS_LICENSE);
                            startActivityForResult(intent, BUSINESS_LICENSE_RESULT_CODE);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        System.out.println("onError()" + throwable.getMessage());
                    }
                });
    }

    private void showIDCardOptionDialog(){
        RxPermissions rxPermissions = new RxPermissions(this);

        if (ClickTypeForIDCard == true) {
            activity = TakePictureActivity.class;
        } else if (ClickTypeForIDCard == false) {
            activity = AlbumSelectionActivity.class;
        }

        //使用Dialog、设置style
        final Dialog dialog = new Dialog(this,R.style.DialogTheme);
        //设置布局
        View view = View.inflate(this,R.layout.dialog_id_card_option_layout,null);
        dialog.setContentView(view);

        Window window = dialog.getWindow();
        //设置弹出位置
        window.setGravity(Gravity.BOTTOM);
        //设置对话框大小
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();

        Typeface iconfont = Typeface.createFromAsset(getAssets(), "iconfont/iconfont.ttf");
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
                            Intent intent = new Intent(MainActivity.this, activity);
                            intent.putExtra("id_card_side", ID_CARD_BACK);
                            intent.putExtra("type_id_card", TYPE_ID_CARD);
                            startActivityForResult(intent, ID_CARD_BACK_RESULT_CODE);
                            dialog.dismiss();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        System.out.println("onError()" + throwable.getMessage());
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
                            Intent intent = new Intent(MainActivity.this, activity);
                            intent.putExtra("id_card_side", ID_CARD_FRONT);
                            intent.putExtra("type_id_card", TYPE_ID_CARD);
                            startActivityForResult(intent, ID_CARD_FRONT_RESULT_CODE);
                            dialog.dismiss();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        System.out.println("onError()" + throwable.getMessage());
                    }
                });

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //判断用户是否点击了“返回键”
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mIsExit) {
                this.finish();
            } else {
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                mIsExit = true;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mIsExit = false;
                    }
                }, 2000);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void startTabFragment(){
        Bundle bundle = new Bundle(); // 创建一个包裹对象
        bundle.putString("tag", TAG); // 往包裹中存入名叫tag的标记
        // 从布局文件中获取名叫tabhost的碎片标签栏
        tabHost = findViewById(android.R.id.tabhost);
        // 把实际的内容框架安装到碎片标签栏
        tabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);
        // 往标签栏添加第一个标签，其中内容视图展示TabFirstFragment
        tabHost.addTab(getTabView(R.string.str_home, R.drawable.tab_home_selector),
                TabHomeFragment.class, bundle);
        // 往标签栏添加第二个标签，其中内容视图展示TabSecondFragment
        tabHost.addTab(getTabView(R.string.str_history, R.drawable.tab_history_selector),
                TabHistoryFragment.class, bundle);
        // 往标签栏添加第三个标签，其中内容视图展示TabThirdFragment
        tabHost.addTab(getTabView(R.string.str_me, R.drawable.tab_me_selector),
                TabMeFragment.class, bundle);
        // 不显示各标签之间的分隔线
        tabHost.getTabWidget().setShowDividers(LinearLayout.SHOW_DIVIDER_NONE);
    }

    // 根据字符串和图标的资源编号，获得对应的标签规格
    private TabHost.TabSpec getTabView(int textId, int imgId) {
        // 根据资源编号获得字符串对象
        String text = getResources().getString(textId);
        // 根据资源编号获得图形对象
        Drawable drawable = getResources().getDrawable(imgId);
        // 设置图形的四周边界。这里必须设置图片大小，否则无法显示图标
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        // 根据布局文件item_tabbar.xml生成标签按钮对象
        View item_tabbar = getLayoutInflater().inflate(R.layout.item_tabbar, null);
        TextView tv_item = item_tabbar.findViewById(R.id.tv_item_tabbar);
        tv_item.setText(text);
        // 在文字上方显示标签的图标
        tv_item.setCompoundDrawables(null, drawable, null, null);
        // 生成并返回该标签按钮对应的标签规格
        return tabHost.newTabSpec(text).setIndicator(item_tabbar);
    }

}
