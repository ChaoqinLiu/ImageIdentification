package com.example.sqlbrite.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentValues;
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
import com.example.sqlbrite.adapter.ResultBusinessLicenseAdapter;
import com.example.sqlbrite.adapter.ResultDriverLicenseAdapter;
import com.example.sqlbrite.adapter.ResultDrivingLicenseAdapter;
import com.example.sqlbrite.adapter.ResultIDCardForBackAdapter;
import com.example.sqlbrite.adapter.ResultIDCardForFrontAdapter;
import com.example.sqlbrite.adapter.ResultImageAdapter;
import com.example.sqlbrite.adapter.ResultLicensePlateAdapter;
import com.example.sqlbrite.adapter.ResultPassportAdapter;
import com.example.sqlbrite.adapter.ResultTextAdapter;
import com.example.sqlbrite.adapter.ResultTrainTicketAdapter;
import com.example.sqlbrite.app.BaseActivity;
import com.example.sqlbrite.database.IdentificationDatabaseHelper;
import com.example.sqlbrite.fragment.TranslationFragment;
import com.example.sqlbrite.model.BankCardResult;
import com.example.sqlbrite.model.BusinessLicenseResult;
import com.example.sqlbrite.model.DriverLicenseResult;
import com.example.sqlbrite.model.DrivingLicenseResult;
import com.example.sqlbrite.model.IDCardForBackResult;
import com.example.sqlbrite.model.IDCardForFrontResult;
import com.example.sqlbrite.model.ImageResult;
import com.example.sqlbrite.model.LicensePlateResult;
import com.example.sqlbrite.model.PassportResult;
import com.example.sqlbrite.model.TextResult;
import com.example.sqlbrite.model.TrainTicketResult;
import com.example.sqlbrite.utils.BitmapUtil;
import com.example.sqlbrite.utils.FileUtil;
import com.example.sqlbrite.utils.GsonUtil;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.safframework.injectview.annotations.InjectView;
import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;

import org.json.JSONArray;
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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import rx.schedulers.Schedulers;
import sun.misc.BASE64Encoder;

public class IntelligentDetectionActivity extends BaseActivity {

    private static String API_KEY;
    private static String SECRET_KEY;
    private String requestUrl;

    private String type;
    private String id_card_side;
    private String path;
    private final String type_image = "type_image";
    private final String type_text = "type_text";
    private final String type_id_card = "type_id_card";
    private final String type_bank_card = "type_bank_card";
    private final String type_license_plate = "type_license_plate";
    private final String type_driver_license = "type_driver_license";
    private final String type_train_ticket = "type_train_ticket";
    private final String type_passport = "type_passport";
    private final String type_driving_license = "type_driving_license";
    private final String type_business_license = "type_business_license";

    private ResultImageAdapter adapter;
    private ResultTextAdapter tAdapter;
    private ResultIDCardForFrontAdapter idCardAdapter;
    private ResultIDCardForBackAdapter idCardForBackAdapter;
    private ResultBankCardAdapter bankCardAdapter;
    private ResultLicensePlateAdapter licensePlateAdapter;
    private ResultDriverLicenseAdapter driverLicenseAdapter;
    private ResultTrainTicketAdapter trainTicketAdapter;
    private ResultDrivingLicenseAdapter drivingLicenseAdapter;
    private ResultPassportAdapter passportAdapter;
    private ResultBusinessLicenseAdapter businessLicenseAdapter;

    private List<ImageResult.ResultArray> resultBeanList = new ArrayList<ImageResult.ResultArray>();
    private List<TextResult.WordsResult> resultTextList = new ArrayList<TextResult.WordsResult>();
    private List<IDCardForFrontResult> idCardList = new ArrayList<IDCardForFrontResult>();
    private List<IDCardForBackResult> idCardForBackList = new ArrayList<IDCardForBackResult>();
    private List<BankCardResult> bankResultBeanList = new ArrayList<BankCardResult>();
    private List<LicensePlateResult> licensePlateResultList = new ArrayList<LicensePlateResult>();
    private List<DriverLicenseResult> driverLicenseResultList = new ArrayList<DriverLicenseResult>();
    private List<TrainTicketResult> trainTicketResultList = new ArrayList<TrainTicketResult>();
    private List<DrivingLicenseResult> drivingLicenseResultList = new ArrayList<DrivingLicenseResult>();
    private List<PassportResult> passportResultList = new ArrayList<PassportResult>();
    private List<BusinessLicenseResult> businessLicenseResultList = new ArrayList<BusinessLicenseResult>();

    private String imageStr;

    public static Bitmap bitmap;
    private String wordsArray;
    private String textStr = "";

    private IdentificationDatabaseHelper dbHelper;
    private BriteDatabase briteDatabase;
    private SqlBrite sqlBrite;

    private ProgressDialog progressDialog = null;

    private ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
    private ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(1);
    private ExecutorService fixedThreadPool = Executors.newFixedThreadPool(5);

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
        type = intent.getStringExtra("type");
        id_card_side = intent.getStringExtra("id_card_side");

        bitmap = BitmapFactory.decodeFile(path, null);

        progressDialog = ProgressDialog.show(IntelligentDetectionActivity.this,"请稍后", "正在识别中...",true);

        //根据按钮传过来的类型执行相应的图片处理方法
        getImageInformationByType();
    }

    @Override
    protected void onStart() {
        super.onStart();
        dbHelper = IdentificationDatabaseHelper.getInstance(this,16);
        sqlBrite = SqlBrite.create();
        briteDatabase = sqlBrite.wrapDatabaseHelper(dbHelper, Schedulers.io());
    }

    @Override
    protected void onStop() {
        super.onStop();
        dbHelper.closeLink();
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
        switch (type) {
            case type_image:
                getImageInformation();
                break;
            case type_text:
                getTextImageInformation();
                break;
            case type_id_card:
                getIDCardImageInformation();
                break;
            case type_bank_card:
                getBankCardImageInformation();
                break;
            case type_license_plate:
                getLicensePlateImageInformation();
                break;
            case type_driver_license:
                getDriverLicenseImageInformation();
                break;
            case type_train_ticket:
                getTrainTicketImageInformation();
                break;
            case type_passport:
                getPassportImageInformation();
                break;
            case type_driving_license:
                getDrivingLicenseImageInformation();
                break;
            case type_business_license:
                getBusinessLicenseImageInformation();
                break;
             default:
                 break;
        }
    }

    //图像识别
    private void getImageInformation(){
        fixedThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    API_KEY = "rcBBY03vg6qf9GDLCwZwMMrZ";
                    SECRET_KEY = "FFNDh7p3AGagLHSuNdEPYazn6zbUUeun";
                    requestUrl = "https://aip.baidubce.com/rest/2.0/image-classify/v2/advanced_general";
                    imageStr = initUploadImage(path);
                    if (imageStr == null || imageStr.equals("")) {
                        scheduledThreadPool.schedule(new Runnable() {
                            @Override
                            public void run() {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        progressDialog.dismiss();
                                        Toast.makeText(IntelligentDetectionActivity.this,"连接服务器失败，请检查您的网络设置",Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        },2,TimeUnit.SECONDS);
                    } else {
                        JsonObject jsonObject = new JsonParser().parse(imageStr).getAsJsonObject();
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
                                adapter = new ResultImageAdapter(IntelligentDetectionActivity.this,resultBeanList);
                                listView.setAdapter(adapter);
                                progressDialog.dismiss();
                                //开启一条线程执行插入数据操作，防止发生堵塞
                                singleThreadExecutor.execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        saveImageData();
                                    }
                                });
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //图文识别
    private void getTextImageInformation() {
        fixedThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    API_KEY = "BSvVZBfk78U0P5rp3vkMbvwX";
                    SECRET_KEY = "E2p9DFGo4qHVpWp5yEzy7VAVE7xNdvGp";
                    requestUrl = "https://aip.baidubce.com/rest/2.0/ocr/v1/general_basic";
                    String resultTextStr = initUploadImage(path);
                    if (resultTextStr == null || resultTextStr.equals("")) {
                        scheduledThreadPool.schedule(new Runnable() {
                            @Override
                            public void run() {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        progressDialog.dismiss();
                                        Toast.makeText(IntelligentDetectionActivity.this,"连接服务器失败，请检查您的网络设置",Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        },2,TimeUnit.SECONDS);
                    } else {
                        JsonObject jsonObject = new JsonParser().parse(resultTextStr).getAsJsonObject();
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
                                tAdapter = new ResultTextAdapter(IntelligentDetectionActivity.this,resultTextList);
                                listView.setAdapter(tAdapter);
                                progressDialog.dismiss();
                            }
                        });
                        wordsArray = jsonArray.toString();
                        singleThreadExecutor.execute(new Runnable() {
                            @Override
                            public void run() {
                                saveTextData();
                            }
                        });
                        initFragment(); //向TranslationFragment传递数据
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //身份证识别
    private void getIDCardImageInformation(){
        String Back = "back";
        String Front = "front";
        fixedThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    API_KEY = "BSvVZBfk78U0P5rp3vkMbvwX";
                    SECRET_KEY = "E2p9DFGo4qHVpWp5yEzy7VAVE7xNdvGp";
                    requestUrl = "https://aip.baidubce.com/rest/2.0/ocr/v1/idcard";
                    String IDCardStr = initUploadIDCardImage(path);
                    if (IDCardStr == null || IDCardStr.equals("")) {
                        scheduledThreadPool.schedule(new Runnable() {
                            @Override
                            public void run() {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        progressDialog.dismiss();
                                        Toast.makeText(IntelligentDetectionActivity.this,"连接服务器失败，请检查您的网络设置",Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        },2,TimeUnit.SECONDS);
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
        });
    }

    //银行卡识别
    private void getBankCardImageInformation(){
        fixedThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    API_KEY = "BSvVZBfk78U0P5rp3vkMbvwX";
                    SECRET_KEY = "E2p9DFGo4qHVpWp5yEzy7VAVE7xNdvGp";
                    requestUrl = "https://aip.baidubce.com/rest/2.0/ocr/v1/bankcard";
                    String bankCardStr = initUploadImage(path);
                    if (bankCardStr == null || bankCardStr.equals("")) {
                        scheduledThreadPool.schedule(new Runnable() {
                            @Override
                            public void run() {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        progressDialog.dismiss();
                                        Toast.makeText(IntelligentDetectionActivity.this,"连接服务器失败，请检查您的网络设置",Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        },2,TimeUnit.SECONDS);
                    } else {
                        //往实体类 BankCardResult 中添加对象 bankCardResult
                        BankCardResult bankCardResult = GsonUtil.parseJsonWithGson(bankCardStr,BankCardResult.class);
                        bankResultBeanList.add(bankCardResult);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                bankCardAdapter = new ResultBankCardAdapter(IntelligentDetectionActivity.this,bankResultBeanList);
                                listView.setAdapter(bankCardAdapter);
                                progressDialog.dismiss();
                                singleThreadExecutor.execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        saveBankCardData();
                                    }
                                });
                            }
                        });
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //车牌识别
    private void getLicensePlateImageInformation(){
        fixedThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    API_KEY = "BSvVZBfk78U0P5rp3vkMbvwX";
                    SECRET_KEY = "E2p9DFGo4qHVpWp5yEzy7VAVE7xNdvGp";
                    requestUrl = "https://aip.baidubce.com/rest/2.0/ocr/v1/license_plate";
                    String LicensePlateStr = initUploadImage(path);
                    if (LicensePlateStr == null || LicensePlateStr.equals("")) {
                        scheduledThreadPool.schedule(new Runnable() {
                            @Override
                            public void run() {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        progressDialog.dismiss();
                                        Toast.makeText(IntelligentDetectionActivity.this,"连接服务器失败，请检查您的网络设置",Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        },2,TimeUnit.SECONDS);
                    } else {
                        LicensePlateResult licensePlateResult = GsonUtil.parseJsonWithGson(LicensePlateStr,LicensePlateResult.class);
                        licensePlateResultList.add(licensePlateResult);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                licensePlateAdapter = new ResultLicensePlateAdapter(IntelligentDetectionActivity.this,licensePlateResultList);
                                listView.setAdapter(licensePlateAdapter);
                                progressDialog.dismiss();
                                singleThreadExecutor.execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        saveLicensePlateData();
                                    }
                                });
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //驾驶证识别
    private void getDriverLicenseImageInformation() {
        fixedThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    API_KEY = "BSvVZBfk78U0P5rp3vkMbvwX";
                    SECRET_KEY = "E2p9DFGo4qHVpWp5yEzy7VAVE7xNdvGp";
                    requestUrl = "https://aip.baidubce.com/rest/2.0/ocr/v1/driving_license";
                    String DriverLicenseStr = initUploadImage(path);
                    if (DriverLicenseStr == null || DriverLicenseStr.equals("")) {
                        scheduledThreadPool.schedule(new Runnable() {
                            @Override
                            public void run() {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        progressDialog.dismiss();
                                        Toast.makeText(IntelligentDetectionActivity.this,"连接服务器失败，请检查您的网络设置",Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        },2,TimeUnit.SECONDS);
                    } else {
                        DriverLicenseResult driverLicenseResult = GsonUtil.parseJsonWithGson(DriverLicenseStr,DriverLicenseResult.class);
                        driverLicenseResultList.add(driverLicenseResult);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                driverLicenseAdapter = new ResultDriverLicenseAdapter(IntelligentDetectionActivity.this, driverLicenseResultList);
                                listView.setAdapter(driverLicenseAdapter);
                                progressDialog.dismiss();
                                singleThreadExecutor.execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        saveDriverLicenseData();
                                    }
                                });
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //火车票识别
    private void getTrainTicketImageInformation(){
        fixedThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    API_KEY = "BSvVZBfk78U0P5rp3vkMbvwX";
                    SECRET_KEY = "E2p9DFGo4qHVpWp5yEzy7VAVE7xNdvGp";
                    requestUrl = "https://aip.baidubce.com/rest/2.0/ocr/v1/train_ticket";
                    String TrainTicketStr = initUploadImage(path);
                    if (TrainTicketStr == null || TrainTicketStr.equals(" ")) {
                        scheduledThreadPool.schedule(new Runnable() {
                            @Override
                            public void run() {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        progressDialog.dismiss();
                                        Toast.makeText(IntelligentDetectionActivity.this,"连接服务器失败，请检查您的网络设置",Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        },2,TimeUnit.SECONDS);
                    } else {

                        TrainTicketResult trainTicketResult = GsonUtil.parseJsonWithGson(TrainTicketStr,TrainTicketResult.class);
                        trainTicketResultList.add(trainTicketResult);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                trainTicketAdapter = new ResultTrainTicketAdapter(IntelligentDetectionActivity.this,trainTicketResultList);
                                listView.setAdapter(trainTicketAdapter);
                                progressDialog.dismiss();
                                singleThreadExecutor.execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        saveTrainTicketData();
                                    }
                                });
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //护照识别
    private void getPassportImageInformation(){
        fixedThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    API_KEY = "BSvVZBfk78U0P5rp3vkMbvwX";
                    SECRET_KEY = "E2p9DFGo4qHVpWp5yEzy7VAVE7xNdvGp";
                    requestUrl = "https://aip.baidubce.com/rest/2.0/ocr/v1/passport";
                    String PassportStr = initUploadImage(path);
                    if (PassportStr == null || PassportStr.equals("")) {
                        scheduledThreadPool.schedule(new Runnable() {
                            @Override
                            public void run() {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        progressDialog.dismiss();
                                        Toast.makeText(IntelligentDetectionActivity.this,"连接服务器失败，请检查您的网络设置",Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        },2,TimeUnit.SECONDS);
                    } else {

                        PassportResult passportResult = GsonUtil.parseJsonWithGson(PassportStr,PassportResult.class);
                        passportResultList.add(passportResult);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                passportAdapter = new ResultPassportAdapter(IntelligentDetectionActivity.this,passportResultList);
                                listView.setAdapter(passportAdapter);
                                progressDialog.dismiss();
                                singleThreadExecutor.execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        savePassportData();
                                    }
                                });
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //行驶证识别
    private void getDrivingLicenseImageInformation(){
        fixedThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    API_KEY = "BSvVZBfk78U0P5rp3vkMbvwX";
                    SECRET_KEY = "E2p9DFGo4qHVpWp5yEzy7VAVE7xNdvGp";
                    requestUrl = "https://aip.baidubce.com/rest/2.0/ocr/v1/vehicle_license";
                    String DrivingLicenseStr = initUploadImage(path);
                    if (DrivingLicenseStr == null || DrivingLicenseStr.equals("")) {
                        scheduledThreadPool.schedule(new Runnable() {
                            @Override
                            public void run() {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        progressDialog.dismiss();
                                        Toast.makeText(IntelligentDetectionActivity.this,"连接服务器失败，请检查您的网络设置",Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        },2,TimeUnit.SECONDS);
                    } else {

                        DrivingLicenseResult drivingLicenseResult = GsonUtil.parseJsonWithGson(DrivingLicenseStr,DrivingLicenseResult.class);
                        drivingLicenseResultList.add(drivingLicenseResult);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                drivingLicenseAdapter = new ResultDrivingLicenseAdapter(IntelligentDetectionActivity.this,drivingLicenseResultList);
                                listView.setAdapter(drivingLicenseAdapter);
                                progressDialog.dismiss();
                                singleThreadExecutor.execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        saveDrivingLicenseData();
                                    }
                                });
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }

    //营业执照识别
    private void getBusinessLicenseImageInformation() {
        fixedThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    API_KEY = "BSvVZBfk78U0P5rp3vkMbvwX";
                    SECRET_KEY = "E2p9DFGo4qHVpWp5yEzy7VAVE7xNdvGp";
                    requestUrl = "https://aip.baidubce.com/rest/2.0/ocr/v1/business_license";
                    String BusinessLicenseStr = initUploadImage(path);
                    if (BusinessLicenseStr == null || BusinessLicenseStr.equals("")) {
                        scheduledThreadPool.schedule(new Runnable() {
                            @Override
                            public void run() {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        progressDialog.dismiss();
                                        Toast.makeText(IntelligentDetectionActivity.this,"连接服务器失败，请检查您的网络设置",Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        },2,TimeUnit.SECONDS);
                    } else {

                        BusinessLicenseResult businessLicenseResult = GsonUtil.parseJsonWithGson(BusinessLicenseStr,BusinessLicenseResult.class);
                        businessLicenseResultList.add(businessLicenseResult);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                businessLicenseAdapter = new ResultBusinessLicenseAdapter(IntelligentDetectionActivity.this,businessLicenseResultList);
                                listView.setAdapter(businessLicenseAdapter);
                                progressDialog.dismiss();
                                singleThreadExecutor.execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        saveBusinessLicenseData();
                                    }
                                });
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
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
                    singleThreadExecutor.execute(new Runnable() {
                        @Override
                        public void run() {
                            saveIDCardDataForFront();
                        }
                    });
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
                    singleThreadExecutor.execute(new Runnable() {
                        @Override
                        public void run() {
                            saveIDCardDataForBack();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void saveIDCardDataForFront() {
        try {
            for (int i = 0; i < idCardList.size(); i++) {
                ContentValues values = new ContentValues();
                String address = idCardList.get(i).words_result.address.getWords();
                String birthday = idCardList.get(i).words_result.birthday.getWords();
                String name = idCardList.get(i).words_result.userName.getWords();
                String number = idCardList.get(i).words_result.idNumber.getWords();
                String gender = idCardList.get(i).words_result.gender.getWords();
                String nationality = idCardList.get(i).words_result.nationality.getWords();
                values.put("address",address);
                values.put("birthday",birthday);
                values.put("name",name);
                values.put("number",number);
                values.put("gender",gender);
                values.put("nationality",nationality);
                Bitmap bitmap = BitmapUtil.openBitmap(path);
                final ByteArrayOutputStream os = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
                values.put("pic", os.toByteArray());
                briteDatabase.insert("front_id_card",values);
                bitmap.recycle();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveIDCardDataForBack() {
        try {
            for (int i = 0; i < idCardForBackList.size(); i++) {
                ContentValues values = new ContentValues();
                String dateOfIssue = idCardForBackList.get(i).words_result.dateOfIssue.getWords();
                String issuingAuthority = idCardForBackList.get(i).words_result.issuingAuthority.getWords();
                String expirationDate = idCardForBackList.get(i).words_result.expirationDate.getWords();
                values.put("dateOfIssue",dateOfIssue);
                values.put("issuingAuthority",issuingAuthority);
                values.put("expirationDate",expirationDate);
                Bitmap bitmap = BitmapUtil.openBitmap(path);
                final ByteArrayOutputStream os = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
                values.put("pic", os.toByteArray());
                briteDatabase.insert("back_id_card",values);
                bitmap.recycle();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveImageData() {
        try {
            for (int i = 0; i < resultBeanList.size(); i++) {
                ContentValues values = new ContentValues();
                float score = resultBeanList.get(i).score;
                String root = resultBeanList.get(i).root;
                String keyword = resultBeanList.get(i).keyword;
                values.put("score", score);
                values.put("root", root);
                values.put("keyword", keyword);
                Bitmap bitmap = BitmapUtil.openBitmap(path);
                final ByteArrayOutputStream os = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
                values.put("pic", os.toByteArray());
                briteDatabase.insert("image", values);
                bitmap.recycle();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveDrivingLicenseData() {
        try {
            for (int i = 0; i < drivingLicenseResultList.size(); i++) {
                ContentValues values = new ContentValues();
                String address = drivingLicenseResultList.get(i).words_result.address.getWords();
                String numberPlateNumber = drivingLicenseResultList.get(i).words_result.numberPlateNumber.getWords();
                String vehicleType = drivingLicenseResultList.get(i).words_result.vehicleType.getWords();
                String owner = drivingLicenseResultList.get(i).words_result.owner.getWords();
                String natureOfUse = drivingLicenseResultList.get(i).words_result.natureOfUse.getWords();
                String brandModelNumber = drivingLicenseResultList.get(i).words_result.brandModelNumber.getWords();
                String engineNumber = drivingLicenseResultList.get(i).words_result.engineNumber.getWords();
                String vehicleIdentificationNumber = drivingLicenseResultList.get(i).words_result.vehicleIdentificationNumber.getWords();
                String registrationDate = drivingLicenseResultList.get(i).words_result.registrationDate.getWords();
                String issuingCertificateOfDate = drivingLicenseResultList.get(i).words_result.issuingCertificateOfDate.getWords();
                values.put("address",address);
                values.put("numberPlateNumber",numberPlateNumber);
                values.put("vehicleType",vehicleType);
                values.put("owner",owner);
                values.put("natureOfUse",natureOfUse);
                values.put("brandModelNumber",brandModelNumber);
                values.put("engineNumber",engineNumber);
                values.put("vehicleIdentificationNumber",vehicleIdentificationNumber);
                values.put("registrationDate",registrationDate);
                values.put("issuingCertificateOfDate",issuingCertificateOfDate);
                Bitmap bitmap = BitmapUtil.openBitmap(path);
                final ByteArrayOutputStream os = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
                values.put("pic", os.toByteArray());
                briteDatabase.insert("driving_license",values);
                bitmap.recycle();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveDriverLicenseData() {
        try {
            for (int i = 0; i < driverLicenseResultList.size(); i++) {
                ContentValues values = new ContentValues();
                String address = driverLicenseResultList.get(i).words_result.address.getWords();
                String birthday = driverLicenseResultList.get(i).words_result.birthday.getWords();
                String name = driverLicenseResultList.get(i).words_result.userName.getWords();
                String certificateNumber = driverLicenseResultList.get(i).words_result.certificateNumber.getWords();
                String gender = driverLicenseResultList.get(i).words_result.gender.getWords();
                String countryOfCitizenship = driverLicenseResultList.get(i).words_result.countryOfCitizenship.getWords();
                String quasiDrivingModel = driverLicenseResultList.get(i).words_result.quasiDrivingModel.getWords();
                String initialLicenseDate = driverLicenseResultList.get(i).words_result.initialLicenseDate.getWords();
                String validityPeriod = driverLicenseResultList.get(i).words_result.validityPeriod.getWords();
                String deadline = driverLicenseResultList.get(i).words_result.to.getWords();
                values.put("address",address);
                values.put("birthday",birthday);
                values.put("name",name);
                values.put("certificateNumber",certificateNumber);
                values.put("gender",gender);
                values.put("countryOfCitizenship",countryOfCitizenship);
                values.put("quasiDrivingModel",quasiDrivingModel);
                values.put("initialLicenseDate",initialLicenseDate);
                values.put("validityPeriod",validityPeriod);
                values.put("deadline",deadline);
                Bitmap bitmap = BitmapUtil.openBitmap(path);
                final ByteArrayOutputStream os = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
                values.put("pic", os.toByteArray());
                briteDatabase.insert("driver_license",values);
                bitmap.recycle();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveLicensePlateData() {
        try {
            for (int i = 0; i < licensePlateResultList.size(); i++) {
                ContentValues values = new ContentValues();
                String color = licensePlateResultList.get(i).words_result.getColor();
                String number = licensePlateResultList.get(i).words_result.getNumber();
                values.put("color",color);
                values.put("number",number);
                Bitmap bitmap = BitmapUtil.openBitmap(path);
                final ByteArrayOutputStream os = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
                values.put("pic", os.toByteArray());
                briteDatabase.insert("license_plate",values);
                bitmap.recycle();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveBankCardData() {
        try {
            for (int i = 0; i < bankResultBeanList.size(); i++) {
                ContentValues values = new ContentValues();
                String bankCardNumber = bankResultBeanList.get(i).result.getBankCardNumber();
                String validDate = bankResultBeanList.get(i).result.getValidDate();
                String bankName = bankResultBeanList.get(i).result.getBankName();
                String bankCardType = bankResultBeanList.get(i).result.getBankCardType();
                values.put("bankCardNumber",bankCardNumber);
                values.put("validDate",validDate);
                values.put("bankName",bankName);
                values.put("bankCardType",bankCardType);
                Bitmap bitmap = BitmapUtil.openBitmap(path);
                final ByteArrayOutputStream os = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
                values.put("pic", os.toByteArray());
                briteDatabase.insert("bank_card",values);
                bitmap.recycle();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveTextData() {
        try {
            JSONArray jsonArray = new JSONArray(wordsArray);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String words = jsonObject.getString("words");
                textStr += words;
            }
            ContentValues values = new ContentValues();
            values.put("words", textStr);
            Bitmap bitmap = BitmapUtil.openBitmap(path);
            final ByteArrayOutputStream os = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
            values.put("pic", os.toByteArray());
                briteDatabase.insert("text", values);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveBusinessLicenseData() {
        try {
            for (int i = 0; i < businessLicenseResultList.size(); i++) {
                ContentValues values = new ContentValues();
                String registeredCapital = businessLicenseResultList.get(i).words_result.registeredCapital.getWords();
                String socialCreditCode = businessLicenseResultList.get(i).words_result.socialCreditCode.getWords();
                String companyName = businessLicenseResultList.get(i).words_result.companyName.getWords();
                String legalPerson = businessLicenseResultList.get(i).words_result.legalPerson.getWords();
                String documentNumber = businessLicenseResultList.get(i).words_result.documentNumber.getWords();
                String formation = businessLicenseResultList.get(i).words_result.formation.getWords();
                String dateOfEstablishment = businessLicenseResultList.get(i).words_result.dateOfEstablishment.getWords();
                String companyAddress = businessLicenseResultList.get(i).words_result.companyAddress.getWords();
                String businessScope = businessLicenseResultList.get(i).words_result.businessScope.getWords();
                String typeOfCompany = businessLicenseResultList.get(i).words_result.typeOfCompany.getWords();
                String ValidityPeriod = businessLicenseResultList.get(i).words_result.businessLicenseValidityPeriod.getWords();
                values.put("registeredCapital",registeredCapital);
                values.put("companyName",companyName);
                values.put("socialCreditCode",socialCreditCode);
                values.put("legalPerson",legalPerson);
                values.put("documentNumber",documentNumber);
                values.put("formation",formation);
                values.put("dateOfEstablishment",dateOfEstablishment);
                values.put("companyAddress",companyAddress);
                values.put("businessScope",businessScope);
                values.put("typeOfCompany",typeOfCompany);
                values.put("ValidityPeriod",ValidityPeriod);
                Bitmap bitmap = BitmapUtil.openBitmap(path);
                final ByteArrayOutputStream os = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
                values.put("pic", os.toByteArray());
                briteDatabase.insert("business_license",values);
                bitmap.recycle();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveTrainTicketData() {
        try {
            for (int i = 0; i < trainTicketResultList.size(); i++) {
                ContentValues values = new ContentValues();
                String startingStation = trainTicketResultList.get(i).words_result.getStartingStation();
                String destinationStation = trainTicketResultList.get(i).words_result.getDestinationstation();
                String seatCategory = trainTicketResultList.get(i).words_result.getSeatCategory();
                String ticketRates = trainTicketResultList.get(i).words_result.getTicketRates();
                String ticketNum = trainTicketResultList.get(i).words_result.getTicketNum();
                String trainNum = trainTicketResultList.get(i).words_result.getTrainNum();
                String name = trainTicketResultList.get(i).words_result.getPassengerName();
                String date = trainTicketResultList.get(i).words_result.getDepartureDate();
                values.put("startingStation",startingStation);
                values.put("destinationStation",destinationStation);
                values.put("seatCategory",seatCategory);
                values.put("ticketRates",ticketRates);
                values.put("ticketNum",ticketNum);
                values.put("trainNum",trainNum);
                values.put("name",name);
                values.put("date",date);
                Bitmap bitmap = BitmapUtil.openBitmap(path);
                final ByteArrayOutputStream os = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
                values.put("pic", os.toByteArray());
                briteDatabase.insert("train_ticket",values);
                bitmap.recycle();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void savePassportData() {
        try {
            for (int i = 0; i < passportResultList.size(); i++) {
                ContentValues values = new ContentValues();
                String countryCode = passportResultList.get(i).words_result.countryCode.getWords();
                String passportIssuanceLocation = passportResultList.get(i).words_result.passportIssuanceLocation.getWords();
                String validUntil = passportResultList.get(i).words_result.validUntil.getWords();
                String passportNumber = passportResultList.get(i).words_result.passportNumber.getWords();
                String passportDateOfIssue = passportResultList.get(i).words_result.passportDateOfIssue.getWords();
                String birthPlace = passportResultList.get(i).words_result.birthPlace.getWords();
                String name = passportResultList.get(i).words_result.ownerName.getWords();
                String pinyinOfName = passportResultList.get(i).words_result.pinyinOfName.getWords();
                String birthday = passportResultList.get(i).words_result.ownerBirthday.getWords();
                String sex = passportResultList.get(i).words_result.sex.getWords();
                values.put("countryCode",countryCode);
                values.put("passportIssuanceLocation",passportIssuanceLocation);
                values.put("validUntil",validUntil);
                values.put("passportNumber",passportNumber);
                values.put("passportDateOfIssue",passportDateOfIssue);
                values.put("birthPlace",birthPlace);
                values.put("name",name);
                values.put("pinyinOfName",pinyinOfName);
                values.put("birthday",birthday);
                values.put("sex",sex);
                Bitmap bitmap = BitmapUtil.openBitmap(path);
                final ByteArrayOutputStream os = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
                values.put("pic", os.toByteArray());
                briteDatabase.insert("passport",values);
                bitmap.recycle();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initFragment(){
        FragmentManager manager = this.getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        TranslationFragment fragment = new TranslationFragment();
        Bundle bundle = new Bundle();
        bundle.putString("words",wordsArray);
        bundle.putString("path",path);
        fragment.setArguments(bundle);
        transaction.add(R.id.text_translation,fragment);
        transaction.commit();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        singleThreadExecutor.shutdown();
        scheduledThreadPool.shutdown();
        fixedThreadPool.shutdown();
        bitmap.recycle();
    }

}
