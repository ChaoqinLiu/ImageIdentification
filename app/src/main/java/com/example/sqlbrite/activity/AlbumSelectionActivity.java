package com.example.sqlbrite.activity;

import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.view.KeyEvent;
import android.view.Window;

import com.example.sqlbrite.app.BaseActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Calendar;

public class AlbumSelectionActivity extends BaseActivity {

    private static final int ALBUM_SELECTION_RESULT_CODE = 10;  //相册选择图片
    private static final int CROP_PHOTO_RESULT_CODE = 20;       //裁剪
    private static final int RESULT_CODE = 30;
    private File mOutImage;
    private Uri uritempFile;
    private Bitmap mBitmap;
    private String imagePath;

    private String type_image;
    private String type_text;
    private String type_id_card;
    private String type_bank_card;
    private String type_license_plate;
    private String type_driver_license;
    private String type_train_ticket;
    private String type_hong_kong_and_macau_pass;
    private String type_driving_license;

    private String id_card_side;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);

        type_image = getIntent().getStringExtra("type_image");
        type_text = getIntent().getStringExtra("type_text");
        type_bank_card = getIntent().getStringExtra("type_bank_card");
        type_id_card = getIntent().getStringExtra("type_id_card");
        id_card_side = getIntent().getStringExtra("id_card_side");
        type_license_plate = getIntent().getStringExtra("type_license_plate");
        type_driver_license = getIntent().getStringExtra("type_driver_license");
        type_train_ticket = getIntent().getStringExtra("type_train_ticket");
        type_hong_kong_and_macau_pass = getIntent().getStringExtra("type_hong_kong_and_macau_pass");
        type_driving_license = getIntent().getStringExtra("type_driving_license");

        chooseAlbum();
    }

    private void chooseAlbum(){

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, ALBUM_SELECTION_RESULT_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){

        switch (requestCode) {
            case ALBUM_SELECTION_RESULT_CODE:
                if (data != null) {
                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
                        imagePath = handlerImageOnKitKat(data);
                    } else {
                        imagePath = handlerImageBeforeKitKat(data);
                    }
                    mOutImage = new File(imagePath);
                    setCropPhoto();
                 }
                 break;
            case CROP_PHOTO_RESULT_CODE:
               if (resultCode == RESULT_OK) {
                   try {
                       mBitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uritempFile));
                       //L.i("ImagePath = " + imagePath);
                       //将图片路径传到下一个Activity处理
                       Intent intent = new Intent(AlbumSelectionActivity.this, IntelligentDetectionActivity.class);
                       intent.putExtra("type_image", type_image);
                       intent.putExtra("type_text", type_text);
                       intent.putExtra("type_bank_card",type_bank_card);
                       intent.putExtra("type_license_plate",type_license_plate);
                       intent.putExtra("type_driver_license",type_driver_license);
                       intent.putExtra("type_train_ticket",type_train_ticket);
                       intent.putExtra("type_hong_kong_and_macau_pass",type_hong_kong_and_macau_pass);
                       intent.putExtra("type_driving_license",type_driving_license);
                       intent.putExtra("type_id_card", type_id_card);
                       intent.putExtra("image_path", imagePath);
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

    @TargetApi(19)
    private String handlerImageOnKitKat(Intent data) {
        String imagePath = null;
        Uri uri = data.getData();
        if (DocumentsContract.isDocumentUri(this, uri)) {
            //如果是document类型Uri，则通过document id处理
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1]; //解析出数字格式的id
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            } else if ("content".equalsIgnoreCase(uri.getScheme())) {
                //如果不是document类型的Uri,则使用普通方法处理
                imagePath = getImagePath(uri, null);
            }
        }
        return imagePath;
    }

    private String handlerImageBeforeKitKat(Intent data){
        Uri uri = data.getData();
        String imagePath = getImagePath(uri, null);

        return imagePath;
    }

    private String getImagePath(Uri uri, String selection) {
        String path = null;
        //通过Uri和selection来获取真实的图片路径
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    private void setCropPhoto() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //如果是7.0剪裁图片 同理 需要把uri包装
            // 通过FileProvider创建一个content类型的Uri
            Uri inputUri = FileProvider.getUriForFile(AlbumSelectionActivity.this, "com.example.sqlbrite.provider", mOutImage);

            startCropPhoto(inputUri);//设置输入类型
            } else {
            Uri inputUri = Uri.fromFile(mOutImage);
            startCropPhoto(inputUri);
        }
    }

    private void startCropPhoto(Uri uri){

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
        //miui系统 特殊处理 return-data的方式只适用于小图。
        if (Build.MANUFACTURER.contains("Xiaomi")){
            //裁剪后的图片Uri路径，uritempFile为Uri类变量
            uritempFile = Uri.parse("file://" + "/" + Environment.getExternalStorageDirectory().getPath() + "/" + Calendar.getInstance().getTimeInMillis() + ".jpg");
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uritempFile);
        }else {
            uritempFile = uri;
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uritempFile);
        }
        intent.putExtra("scale", true);  //是否保留比例
        intent.putExtra("return-data", false);//是否在Intent中返回图片
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());//设置输出图片的格式
        intent.setDataAndType(uri, "image/*");

        startActivityForResult(intent,CROP_PHOTO_RESULT_CODE); //启动裁剪程序
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {// 重写系统返回键
            Intent intent = new Intent();
            intent.setClass(AlbumSelectionActivity.this, MainActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
