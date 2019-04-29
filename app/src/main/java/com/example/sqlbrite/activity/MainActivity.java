package com.example.sqlbrite.activity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sqlbrite.R;
import com.example.sqlbrite.app.BaseActivity;
import com.jakewharton.rxbinding2.view.RxView;
import com.safframework.injectview.annotations.InjectView;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.concurrent.TimeUnit;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

public class MainActivity extends BaseActivity {

    private static final int TYPE_IMAGE_RESULT_CODE = 10;
    private static final int TYPE_TEXT_RESULT_CODE = 20;

    private static final String TYPE_IMAGE = "type_image";  //识别图片，包括动物，植物等等
    private static final String TYPE_TEXT = "type_text";   //文字识别

    private boolean mIsExit;

    @InjectView(R.id.text_image)
    TextView text_image;

    @InjectView(R.id.text_tx)
    TextView text_tx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Typeface iconfont = Typeface.createFromAsset(getAssets(), "iconfont/iconfont.ttf");
        text_image.setTypeface(iconfont);
        text_tx.setTypeface(iconfont);

        initHomeView();

    }

    private void initHomeView(){

        RxPermissions rxPermissions = new RxPermissions(this);

        /**
         * 拍照作为图像来源
         */
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

        /**
         * 相册图片作为图像来源
         */
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

}
