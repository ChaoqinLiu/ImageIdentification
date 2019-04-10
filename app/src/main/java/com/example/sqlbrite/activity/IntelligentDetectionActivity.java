package com.example.sqlbrite.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sqlbrite.R;
import com.example.sqlbrite.adapter.ResultAdapter;
import com.example.sqlbrite.adapter.ResultTextAdapter;
import com.example.sqlbrite.app.BaseActivity;
import com.example.sqlbrite.model.Result;
import com.example.sqlbrite.model.TextResult;
import com.example.sqlbrite.util.FileUtil;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.jakewharton.rxbinding2.view.RxView;
import com.safframework.injectview.annotations.InjectView;
import com.safframework.log.L;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import sun.misc.BASE64Encoder;


public class IntelligentDetectionActivity extends BaseActivity {

    private static final String API_KEY_IMAGE = "rcBBY03vg6qf9GDLCwZwMMrZ";
    private static final String SECRET_KEY_IMAGE = "FFNDh7p3AGagLHSuNdEPYazn6zbUUeun";
    private static final String API_KEY_TEXT = "BSvVZBfk78U0P5rp3vkMbvwX";
    private static final String SECRET_KEY_TEXT = "E2p9DFGo4qHVpWp5yEzy7VAVE7xNdvGp";
    private String path;
    private String type_image;
    private String type_text;
    private ResultAdapter adapter;
    private ResultTextAdapter tAdapter;

    private ArrayList<Result.ResultArray> resultBeanList = new ArrayList<Result.ResultArray>();
    private ArrayList<TextResult.WordsResult> resultTextList = new ArrayList<TextResult.WordsResult>();

    public static Bitmap bitmap;
    private String wordsArray;

    private static final int TRANSLATION_RESULT_CODE = 1;

    private ProgressDialog progressDialog = null;
    private Handler handler = new Handler();

    @InjectView(R.id.view_list)
    ListView listView;

    @InjectView(R.id.back)
    TextView back;

    @InjectView(R.id.text_translation)
    TextView text_translation;

    @Override
    @SuppressLint("NewApi")
    protected void onCreate(Bundle savedInstanceState) {

        //防止主线程中进行网络请求异常（但不建议这样）
        //StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        //StrictMode.setThreadPolicy(policy);

        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_intelligent_detection);

        Typeface iconfont = Typeface.createFromAsset(getAssets(), "iconfont/iconfont.ttf");
        back.setTypeface(iconfont);

        Intent intent = getIntent();
        path =  intent.getStringExtra("image_path");
        type_image = intent.getStringExtra("type_image");
        type_text = intent.getStringExtra("type_text");
        L.i("ImagePath= " + path);

        bitmap = BitmapFactory.decodeFile(path, null);

        progressDialog = ProgressDialog.show(IntelligentDetectionActivity.this,"请稍后", "正在识别中...",true);
        handler.postDelayed(mCloseDialog,1500);

        //根据按钮传过来的类型执行相应的图片处理方法
        getTypeView();
        //返回主页
        ReturnToHome();
        //翻译
        initTranslation();
    }

    //处理type_image类型的图片
    private void initUploadImage(String path){

        String access_token = getAccessToken(API_KEY_IMAGE,SECRET_KEY_IMAGE);
        //L.i("access_token = " + access_token);

        //拼接请求
        String requestUrl = "https://aip.baidubce.com/rest/2.0/image-classify/v2/advanced_general";
        String contentType = "application/x-www-form-urlencoded";
        String urls = requestUrl + "?access_token=" + access_token;
        //L.i("urls =" + urls);

        try {
            URL url = new URL(urls);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setDoInput(true);
            con.setDoOutput(true);
            con.setUseCaches(false);
            con.setRequestProperty("Content-Type",contentType);
            con.setRequestProperty("Connection", "Keep-Alive");
            con.setRequestProperty("User-Agent","Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.100 Safari/537.36");

            OutputStreamWriter out = new OutputStreamWriter(con.getOutputStream(), "utf-8");

            //将图片转换为byte[]再进行base64编码
            byte[] bytes  = FileUtil.readFileByBytes(path);
            BASE64Encoder encoder = new BASE64Encoder();
            String str = encoder.encode(bytes);
            //URLEncoder 使用指定的编码机制将字符串转换为 application/x-www-form-urlencoded 格式
            String imageParam = URLEncoder.encode(str);
            //API请求参数 image
            String param = "image=" + imageParam;

            out.write(param);
            out.flush();
            out.close();

            if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedInputStream bis = new BufferedInputStream(con.getInputStream());
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                int len;
                byte[] arr = new byte[1024];
                while ((len = bis.read(arr)) != -1) {
                    bos.write(arr, 0, len);
                    bos.flush();
                }
                bos.close();
                //L.i("result = " + bos.toString("utf-8"));
                String resultStr = bos.toString("utf-8");
                JsonObject jsonObject = new JsonParser().parse(resultStr).getAsJsonObject();
                //再转JsonArray 加上数据头
                JsonArray jsonArray = jsonObject.getAsJsonArray("result");
                Gson gson = new Gson();

                //循环遍历
                for (JsonElement result : jsonArray) {
                    Result.ResultArray resultBean = gson.fromJson(result, new TypeToken<Result.ResultArray>() {}.getType());
                    resultBeanList.add(resultBean);
                }
                //在主线程中执行UI操作
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter = new ResultAdapter(IntelligentDetectionActivity.this,resultBeanList);
                        listView.setAdapter(adapter);
                    }
                });
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //处理type_text类型的图片
    private void initTextImage(String path){
        String access_token = getAccessToken(API_KEY_TEXT,SECRET_KEY_TEXT);
        L.i("access_token = " + access_token);

        //拼接请求
        String requestUrl = "https://aip.baidubce.com/rest/2.0/ocr/v1/general_basic";
        String contentType = "application/x-www-form-urlencoded";
        String urls = requestUrl + "?access_token=" + access_token;
        L.i("urls =" + urls);

        try {
            URL url = new URL(urls);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setDoInput(true);
            con.setDoOutput(true);
            con.setUseCaches(false);
            con.setRequestProperty("Content-Type",contentType);
            con.setRequestProperty("Connection", "Keep-Alive");
            con.setRequestProperty("User-Agent","Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.100 Safari/537.36");

            OutputStreamWriter out = new OutputStreamWriter(con.getOutputStream(), "utf-8");

            //将图片转换为byte[]再进行base64编码
            byte[] bytes  = FileUtil.readFileByBytes(path);
            BASE64Encoder encoder = new BASE64Encoder();
            String str = encoder.encode(bytes);
            //URLEncoder 使用指定的编码机制将字符串转换为 application/x-www-form-urlencoded 格式
            String imageParam = URLEncoder.encode(str);
            //API请求参数 image
            String param = "image=" + imageParam;

            out.write(param);
            out.flush();
            out.close();

            if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedInputStream bis = new BufferedInputStream(con.getInputStream());
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                int len;
                byte[] arr = new byte[1024];
                while ((len = bis.read(arr)) != -1) {
                    bos.write(arr, 0, len);
                    bos.flush();
                }
                bos.close();
                L.i("result = " + bos.toString("utf-8"));
                String resultStr = bos.toString("utf-8");
                JsonObject jsonObject = new JsonParser().parse(resultStr).getAsJsonObject();
                //再转JsonArray 加上数据头
                JsonArray jsonArray = jsonObject.getAsJsonArray("words_result");
                //L.i("jsonArray= " + jsonArray.toString());
                Gson gson = new Gson();

                //循环遍历
                for (JsonElement result : jsonArray) {
                    TextResult.WordsResult resultTextBean = gson.fromJson(result, new TypeToken<TextResult.WordsResult>() {}.getType());
                    resultTextList.add(resultTextBean);
                }
                //在主线程中执行UI操作
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tAdapter = new ResultTextAdapter(IntelligentDetectionActivity.this,resultTextList);
                        listView.setAdapter(tAdapter);
                    }
                });
                wordsArray = jsonArray.toString();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //翻译操作
    private void initTranslation() {
        RxView.clicks(text_translation)
                .throttleFirst(600, TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception{
                        Intent intent = new Intent(IntelligentDetectionActivity.this, TextTranslationActivity.class);
                        intent.putExtra("translationArr", wordsArray);
                        startActivityForResult(intent, TRANSLATION_RESULT_CODE);
                    }
                });
    }

    //获取AccessToken
    public static String getAccessToken(String api_key, String secret_key){
        try {
            String url = "https://aip.baidubce.com/oauth/2.0/token"+"?grant_type=client_credentials"+"&client_id="+api_key+"&client_secret="+secret_key+"&";
            String result = sendPost(url);
            JSONObject jsonObject = new JSONObject(result);
            String access_token = jsonObject.getString("access_token");
            return access_token;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 发送POST请求
     */
    public static String sendPost(String urlStr){
        try {
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5 * 1000);
            conn.setReadTimeout(5 * 1000);
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setRequestProperty("charset", "utf-8");
            //conn.setRequestProperty("User-Agent","Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.100 Safari/537.36");
            OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream(), "utf-8");
            String param = "";
            out.write(new String(param.getBytes("utf-8")));
            out.flush();
            out.close();
            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                int len;
                byte[] arr = new byte[1024];
                while ((len = bis.read(arr)) != -1) {
                    bos.write(arr, 0, len);
                    bos.flush();
                }
                bos.close();
                return bos.toString("utf-8");
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Runnable mCloseDialog = new Runnable() {
        @Override
        public void run() {
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        }
    };

    private void ReturnToHome(){
        RxView.clicks(back)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        Intent intent = new Intent(IntelligentDetectionActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        L.i("onError = " + throwable.getMessage());
                    }
                });
    }

    private void getTypeView(){
        if (type_image != null) {
            new Thread(new Runnable() {  //开启一个新的线程，防止在主线程中网络请求发生异常
                @Override
                public void run() {
                    initUploadImage(path);
                }
            }).start();
        }

        else if (type_text != null) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    initTextImage(path);
                }
            }).start();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    text_translation.setText("翻译");
                }
            });
        }
    }

}
