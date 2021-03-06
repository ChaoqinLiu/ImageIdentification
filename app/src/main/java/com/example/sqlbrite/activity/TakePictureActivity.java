package com.example.sqlbrite.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.view.KeyEvent;
import android.view.Window;

import com.example.sqlbrite.app.BaseActivity;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;


public class TakePictureActivity extends BaseActivity {
    private Uri mImageUri;
    private String mImageName;
    private String mImagePath;
    private File mImageFile;
    private static final int TAKE_PHOTO_RESULT_CODE = 100;  //拍照
    private static final int CROP_PHOTO_RESULT_CODE = 200;  //裁剪
    private static final int RESULT_CODE = 300;  //提交识别

    private String type;
    private String id_card_side;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);

        type = getIntent().getStringExtra("type");
        id_card_side = getIntent().getStringExtra("id_card_side");

        startCamera();

    }

    private void startCamera(){
        Intent intent = new Intent();
        //启动相机
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.addCategory(Intent.CATEGORY_DEFAULT);

        createImageFile();
        //添加权限
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        //获取uri
        if (mImageFile != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                //7.0以上要通过FileProvider将File转化为Uri
                mImageUri = FileProvider.getUriForFile(this,"com.example.sqlbrite.provider",mImageFile);
                grantUriPermission("com.example.sqlbrite.provider",mImageUri,Intent.FLAG_GRANT_READ_URI_PERMISSION);
            } else {
                //7.0以下则直接使用Uri的fromFile方法将File转化为Uri
                mImageUri = Uri.fromFile(mImageFile);
            }
        }

        //将uri加入到额外数据
        intent.putExtra(MediaStore.EXTRA_OUTPUT,mImageUri);

        //将存储图片的uri读写权限授权给相机应用
        List<ResolveInfo> resInfoList = TakePictureActivity.this.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        for (ResolveInfo resolveInfo : resInfoList) {
            String packageName = resolveInfo.activityInfo.packageName;
            TakePictureActivity.this.grantUriPermission(packageName, mImageUri , Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }

        startActivityForResult(intent,TAKE_PHOTO_RESULT_CODE);
    }

    private void createImageFile(){
        mImageName = Calendar.getInstance().getTimeInMillis() + ".jpg";
        //创建文件图片
        mImageFile = new File(Environment.getExternalStorageDirectory(), mImageName);
        //将图片的绝对路径设置给mImagePath
        mImagePath = mImageFile.getAbsolutePath();
        mImageFile.setWritable(true);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TAKE_PHOTO_RESULT_CODE:
                if (resultCode == RESULT_OK) {
                    Intent intent = new Intent("com.android.camera.action.CROP");
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                    }
                    //处理华为裁剪区域是圆形的问题
                    if (Build.MANUFACTURER.equals("HUAWEI")) {
                        intent.putExtra("aspectX", 0.1);
                        intent.putExtra("aspectY", 0.1);
                    } else {
                        intent.putExtra("aspectX", 0.1);
                        intent.putExtra("aspectY", 0.1);
                    }
                    intent.putExtra("crop", true);
                    intent.putExtra("outputX", 795); //裁剪区的宽
                    intent.putExtra("outputY" , 500);//裁剪区的高
                    intent.putExtra("scale", true);  //是否保留比例
                    intent.putExtra("return-data", false);//是否在Intent中返回图片
                    intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());//设置输出图片的格式
                    intent.setDataAndType(mImageUri, "image/*");
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);

                    //将存储图片的uri读写权限授权给剪裁工具应用
                    List<ResolveInfo> resInfoList = TakePictureActivity.this.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
                    for (ResolveInfo resolveInfo : resInfoList) {
                        String packageName = resolveInfo.activityInfo.packageName;
                        TakePictureActivity.this.grantUriPermission(packageName, mImageUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    }

                    startActivityForResult(intent,CROP_PHOTO_RESULT_CODE); //启动裁剪程序
                }
                //如果取消拍照按返回键直接返回MainActivity
                if (resultCode == RESULT_CANCELED) {
                    Intent intent = new Intent();
                    intent.setClass(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                break;
            case CROP_PHOTO_RESULT_CODE:
                if (resultCode == RESULT_OK) {
                    try {
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(mImageUri));
                        //保存到SD卡
                        savePhotoToSD(bitmap);
                        //更新系统图库
                        updateSystemGallery();
                        //将图片地址传递到下一个Activity处理
                        Intent intent = new Intent(TakePictureActivity.this, IntelligentDetectionActivity.class);
                        intent.putExtra("type", type);
                        intent.putExtra("image_path", mImagePath);
                        intent.putExtra("id_card_side",id_card_side);
                        startActivityForResult(intent, RESULT_CODE);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;
            default:
                break;
        }
    }

    private void savePhotoToSD(Bitmap bitmap){
        //创建输出流缓冲区
        BufferedOutputStream os = null;
        try {
            //设置输出流
            os = new BufferedOutputStream(new FileOutputStream(mImageFile));
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,os);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (os != null) {
                try {
                    //不管输出是否异常都要关闭流
                    os.flush();
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void updateSystemGallery(){
        //把文件插入到系统图库
        try {
            MediaStore.Images.Media.insertImage(this.getContentResolver(),mImageFile.getAbsolutePath(),mImageName,null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        //最后通知图库更新
        this.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,Uri.parse("file://" + mImagePath)));
    }

 }

