package com.example.sqlbrite.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.Window;
import android.widget.ListView;
import android.widget.Toast;

import com.example.sqlbrite.R;
import com.example.sqlbrite.adapter.ResultBankCardAdapter;
import com.example.sqlbrite.adapter.ResultIDCardForBackAdapter;
import com.example.sqlbrite.adapter.ResultIDCardForFrontAdapter;
import com.example.sqlbrite.adapter.ResultImageAdapter;
import com.example.sqlbrite.adapter.ResultLicensePlateAdapter;
import com.example.sqlbrite.adapter.ResultTextAdapter;
import com.example.sqlbrite.app.BaseActivity;
import com.example.sqlbrite.fragment.TranslationFragment;
import com.example.sqlbrite.model.BankCardResult;
import com.example.sqlbrite.model.IDCardForBackResult;
import com.example.sqlbrite.model.IDCardForFrontResult;
import com.example.sqlbrite.model.ImageResult;
import com.example.sqlbrite.model.LicensePlateResult;
import com.example.sqlbrite.model.TextResult;
import com.example.sqlbrite.util.FileUtil;
import com.example.sqlbrite.util.GsonUtil;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
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
import java.util.List;

import sun.misc.BASE64Encoder;

public class IntelligentDetectionActivity extends BaseActivity {

    private static String API_KEY;
    private static String SECRET_KEY;
    private String requestUrl;
    private String path;
    private String type_image;
    private String type_text;
    private String type_id_card;
    private String type_bank_card;
    private String type_license_plate;

    private String id_card_side;

    private ResultImageAdapter adapter;
    private ResultTextAdapter tAdapter;
    private ResultIDCardForFrontAdapter idCardAdapter;
    private ResultIDCardForBackAdapter idCardForBackAdapter;
    private ResultBankCardAdapter bankCardAdapter;
    private ResultLicensePlateAdapter licensePlateAdapter;

    private ArrayList<ImageResult.ResultArray> resultBeanList = new ArrayList<ImageResult.ResultArray>();
    private ArrayList<TextResult.WordsResult> resultTextList = new ArrayList<TextResult.WordsResult>();
    private List<IDCardForFrontResult> idCardList = new ArrayList<IDCardForFrontResult>();
    private List<IDCardForBackResult> idCardForBackList = new ArrayList<IDCardForBackResult>();
    private List<BankCardResult> bankResultBeanList = new ArrayList<BankCardResult>();
    private List<LicensePlateResult> licensePlateResultList = new ArrayList<LicensePlateResult>();

    public static Bitmap bitmap;
    private String wordsArray;

    private ProgressDialog progressDialog = null;

    @InjectView(R.id.view_list)
    ListView listView;

    @Override
    @SuppressLint("NewApi")
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_intelligent_detection);

        Intent intent = getIntent();
        path =  intent.getStringExtra("image_path");
        type_image = intent.getStringExtra("type_image");
        type_text = intent.getStringExtra("type_text");
        type_id_card = intent.getStringExtra("type_id_card");
        type_bank_card = intent.getStringExtra("type_bank_card");
        type_license_plate = intent.getStringExtra("type_license_plate");

        id_card_side = intent.getStringExtra("id_card_side");
        //L.i("ImagePath= " + path);

        bitmap = BitmapFactory.decodeFile(path, null);

        progressDialog = ProgressDialog.show(IntelligentDetectionActivity.this,"请稍后", "正在识别中...",true);

        //根据按钮传过来的类型执行相应的图片处理方法
        getImageInformationByType();
    }

    private String initUploadImage(String path){

        String access_token = getAccessToken(API_KEY,SECRET_KEY);
        //L.i("access_token = " + access_token);
        //拼接请求
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
                return bos.toString("utf-8");
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String initUploadIDCardImage(String path){

        String access_token = getAccessToken(API_KEY,SECRET_KEY);
        //L.i("access_token = " + access_token);
        //拼接请求
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
            String param = "id_card_side=" + id_card_side + "&" + "image=" + imageParam;

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
                return bos.toString("utf-8");
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {// 重写系统返回键
            Intent intent = new Intent();
            intent.setClass(IntelligentDetectionActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void getImageInformationByType(){
        if (type_image != null) {
            getImageInformation();
        } else if (type_text != null) {
            getTextImageInformation();
        } else if (type_id_card != null) {
            getIDCardImageInformation();
        } else if (type_bank_card != null) {
            getBankCardImageInformation();
        } else if (type_license_plate != null) {
            getLicensePlateImageInformation();
        }
    }

    //图像识别
    private void getImageInformation(){
        new Thread(new Runnable() {  //开启一个新的线程，防止在主线程中网络请求发生异常
            @Override
            public void run() {
                try {
                    API_KEY = "rcBBY03vg6qf9GDLCwZwMMrZ";
                    SECRET_KEY = "FFNDh7p3AGagLHSuNdEPYazn6zbUUeun";
                    requestUrl = "https://aip.baidubce.com/rest/2.0/image-classify/v2/advanced_general";
                    String resultStr = initUploadImage(path);
                    if (resultStr == null || resultStr.equals(" ")) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(3000);
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            progressDialog.dismiss();
                                            Toast.makeText(IntelligentDetectionActivity.this,"连接服务器失败，请检查您的网络设置",Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                    } else {
                        JsonObject jsonObject = new JsonParser().parse(resultStr).getAsJsonObject();
                        //再转JsonArray 加上数据头
                        JsonArray jsonArray = jsonObject.getAsJsonArray("result");
                        Gson gson = new Gson();

                        //循环遍历
                        for (JsonElement result : jsonArray) {
                            ImageResult.ResultArray resultBean = gson.fromJson(result, new TypeToken<ImageResult.ResultArray>() {}.getType());
                            resultBeanList.add(resultBean);
                        }
                        //在主线程中执行UI操作
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    adapter = new ResultImageAdapter(IntelligentDetectionActivity.this,resultBeanList);
                                    listView.setAdapter(adapter);
                                    progressDialog.dismiss();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    //图文识别
    private void getTextImageInformation() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    API_KEY = "BSvVZBfk78U0P5rp3vkMbvwX";
                    SECRET_KEY = "E2p9DFGo4qHVpWp5yEzy7VAVE7xNdvGp";
                    requestUrl = "https://aip.baidubce.com/rest/2.0/ocr/v1/general_basic";
                    String resultStr = initUploadImage(path);
                    if (resultStr == null || resultStr.equals(" ")) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(3000);
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            progressDialog.dismiss();
                                            Toast.makeText(IntelligentDetectionActivity.this,"连接服务器失败，请检查您的网络设置",Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                    } else {
                        JsonObject jsonObject = new JsonParser().parse(resultStr).getAsJsonObject();
                        JsonArray jsonArray = jsonObject.getAsJsonArray("words_result");
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
                                try {
                                    tAdapter = new ResultTextAdapter(IntelligentDetectionActivity.this,resultTextList);
                                    listView.setAdapter(tAdapter);
                                    progressDialog.dismiss();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        wordsArray = jsonArray.toString();
                        initFragment(); //向TranslationFragment传递数据
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    //身份证识别
    private void getIDCardImageInformation(){
        String Back = "back";
        String Front = "front";
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    API_KEY = "BSvVZBfk78U0P5rp3vkMbvwX";
                    SECRET_KEY = "E2p9DFGo4qHVpWp5yEzy7VAVE7xNdvGp";
                    requestUrl = "https://aip.baidubce.com/rest/2.0/ocr/v1/idcard";
                    String IDCardStr = initUploadIDCardImage(path);
                    if (IDCardStr == null || IDCardStr.equals(" ")) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(3000);
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            progressDialog.dismiss();
                                            Toast.makeText(IntelligentDetectionActivity.this,"连接服务器失败，请检查您的网络设置",Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                    } else {
                        if (id_card_side.equals(Front)) {
                            getIDCardInfoForFront(IDCardStr);
                        } else if (id_card_side.equals(Back)) {
                            getIDCardInfoForBack(IDCardStr);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    //银行卡识别
    private void getBankCardImageInformation(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    API_KEY = "BSvVZBfk78U0P5rp3vkMbvwX";
                    SECRET_KEY = "E2p9DFGo4qHVpWp5yEzy7VAVE7xNdvGp";
                    requestUrl = "https://aip.baidubce.com/rest/2.0/ocr/v1/bankcard";
                    String bankCardStr = initUploadImage(path);
                    if (bankCardStr == null || bankCardStr.equals(" ")) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(3000);
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            progressDialog.dismiss();
                                            Toast.makeText(IntelligentDetectionActivity.this,"连接服务器失败，请检查您的网络设置",Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                    } else {
                        //往实体类 BankCardResult 中添加对象 bankCardResult
                        BankCardResult bankCardResult = GsonUtil.parseJsonWithGson(bankCardStr,BankCardResult.class);
                        bankResultBeanList.add(bankCardResult);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    bankCardAdapter = new ResultBankCardAdapter(IntelligentDetectionActivity.this,bankResultBeanList);
                                    listView.setAdapter(bankCardAdapter);
                                    progressDialog.dismiss();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    //车牌识别
    private void getLicensePlateImageInformation(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    API_KEY = "BSvVZBfk78U0P5rp3vkMbvwX";
                    SECRET_KEY = "E2p9DFGo4qHVpWp5yEzy7VAVE7xNdvGp";
                    requestUrl = "https://aip.baidubce.com/rest/2.0/ocr/v1/license_plate";
                    String LicensePlateStr = initUploadImage(path);
                    if (LicensePlateStr == null || LicensePlateStr.equals(" ")) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(3000);
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            progressDialog.dismiss();
                                            Toast.makeText(IntelligentDetectionActivity.this,"连接服务器失败，请检查您的网络设置",Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                    } else {
                        LicensePlateResult licensePlateResult = GsonUtil.parseJsonWithGson(LicensePlateStr,LicensePlateResult.class);
                        licensePlateResultList.add(licensePlateResult);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    licensePlateAdapter = new ResultLicensePlateAdapter(IntelligentDetectionActivity.this,licensePlateResultList);
                                    listView.setAdapter(licensePlateAdapter);
                                    progressDialog.dismiss();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void getIDCardInfoForFront(String idCardStr){

        IDCardForFrontResult frontResult = GsonUtil.parseJsonWithGson(idCardStr,IDCardForFrontResult.class);
        idCardList.add(frontResult);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    idCardAdapter = new ResultIDCardForFrontAdapter(IntelligentDetectionActivity.this, idCardList);
                    listView.setAdapter(idCardAdapter);
                    progressDialog.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void getIDCardInfoForBack(String idCardStr){

        IDCardForBackResult backResult = GsonUtil.parseJsonWithGson(idCardStr,IDCardForBackResult.class);
        idCardForBackList.add(backResult);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    idCardForBackAdapter = new ResultIDCardForBackAdapter(IntelligentDetectionActivity.this, idCardForBackList);
                    listView.setAdapter(idCardForBackAdapter);
                    progressDialog.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void initFragment(){
        FragmentManager manager = this.getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        TranslationFragment fragment = new TranslationFragment();
        Bundle bundle = new Bundle();
        bundle.putString("words",wordsArray);
        fragment.setArguments(bundle);
        transaction.add(R.id.text_translation,fragment);
        transaction.commit();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
    }

}
