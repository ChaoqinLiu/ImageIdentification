package com.example.sqlbrite.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.ScrollingMovementMethod;
import android.view.Window;
import android.widget.TextView;

import com.example.sqlbrite.R;
import com.example.sqlbrite.app.BaseActivity;
import com.example.sqlbrite.model.TranslateResult;
import com.example.sqlbrite.util.MD5Utils;
import com.google.gson.Gson;
import com.jakewharton.rxbinding2.view.RxView;
import com.safframework.injectview.annotations.InjectView;
import com.safframework.log.L;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

public class TextTranslationActivity extends BaseActivity {

    private static final int TRANSLATION_RESULT_CODE = 1;

    private String wordsKeyArr;
    private String signUrl;
    private static String queryStr = "";
    private static String query;
    private static final String appid = "20190401000283388";
    private static final String key = "SkTeVqucOItasS35k8tJ";
    private static final String salt = "1435660288";
    private static String sign;
    private String signStr;
    private String dst;

    private ProgressDialog progressDialog = null;
    private Handler handler = new Handler();

    @InjectView(R.id.text_back)
    TextView back;

    @InjectView(R.id.text_original)
    TextView original;

    @InjectView(R.id.tx_Translation)
    TextView translation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_text_translation);

        Typeface iconfont = Typeface.createFromAsset(getAssets(), "iconfont/iconfont.ttf");
        back.setTypeface(iconfont);

        Intent intent = getIntent();
        wordsKeyArr = intent.getStringExtra("translationArr");
        //L.i("wordsKeyArr" + wordsKeyArr);
        try {
            JSONArray jsonArray = new JSONArray(wordsKeyArr);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String words = jsonObject.getString("words");
                queryStr += words;
                //L.i("queryStr= " + queryStr);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        signStr = appid + queryStr + salt + key;
        //L.i("signStr =" + signStr);
        sign = MD5Utils.MD5Encode(signStr,"UTF-8");
        //L.i(sign);

        progressDialog = ProgressDialog.show(TextTranslationActivity.this,"请稍后", "正在翻译中...",true);
        handler.postDelayed(mCloseDialog,1500);

        new Thread(new Runnable() {
            @Override
            public void run() {
                String result = Translation();
                TranslateResult translateResult = new Gson().fromJson(result,TranslateResult.class);
                dst = translateResult.getTrans_result().get(0).getDst();
                //L.i("dst = " + dst);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        original.setText(queryStr);
                        translation.setText(dst);
                        original.setMovementMethod(new ScrollingMovementMethod());
                        translation.setMovementMethod(new ScrollingMovementMethod());
                    }
                });
            }
        }).start();

        initView();

    }

    private static String Translation(){

        StringBuffer buffer = new StringBuffer();

        String requestUrl = "https://fanyi-api.baidu.com/api/trans/vip/translate?q=" + queryStr + "&from=auto&to=en&appid=" + appid + "&salt=" + salt + "&sign=" + sign;
        //L.i("requestUrl =" + requestUrl);

        try {
            URL url = new URL(requestUrl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setDoInput(true);
            con.setDoOutput(false);
            con.setUseCaches(false);
            con.connect();

            if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = con.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream,"utf-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                String str = null;
                while ((str = bufferedReader.readLine()) != null) {
                    buffer.append(str);
                }
                bufferedReader.close();
                inputStreamReader.close();
                //释放资源
                inputStream.close();
                inputStream = null;
                con.disconnect();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return buffer.toString();
    }

    private void initView(){
        RxView.clicks(back)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        Intent intent = new Intent(TextTranslationActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        L.i("onError = " + throwable.getMessage());
                    }
                });
    }

    private Runnable mCloseDialog = new Runnable() {
        @Override
        public void run() {
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        }
    };


}
