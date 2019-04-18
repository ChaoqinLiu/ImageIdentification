package com.example.sqlbrite.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.sqlbrite.R;
import com.example.sqlbrite.app.BaseActivity;
import com.example.sqlbrite.model.Language;
import com.example.sqlbrite.model.TranslateResult;
import com.example.sqlbrite.util.MD5Utils;
import com.google.gson.Gson;
import com.safframework.injectview.annotations.InjectView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class TextTranslationActivity extends BaseActivity {

    private String wordsKeyArr;
    private static String queryStr = "";
    private static final String appid = "20190401000283388";
    private static final String key = "SkTeVqucOItasS35k8tJ";
    private static final String salt = "1435660288";
    private static String sign;
    private String signStr;
    private String dst;
    private static String lan = "en";  //翻译目标语言,默认为英文

    private ProgressDialog progressDialog = null;

    @InjectView(R.id.text_original)
    TextView original;

    @InjectView(R.id.tx_Translation)
    TextView translation;

    @InjectView(R.id.sp_language)
    Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_text_translation);

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

        getTranslationResult();
        initSpinner();
    }

    private void getTranslationResult(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String result = Translation();
                    TranslateResult translateResult = new Gson().fromJson(result,TranslateResult.class);
                    dst = translateResult.getTrans_result().get(0).getDst();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            original.setText(queryStr);
                            translation.setText(dst);
                            original.setMovementMethod(new ScrollingMovementMethod());
                            translation.setMovementMethod(new ScrollingMovementMethod());
                            progressDialog.dismiss();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public static String Translation(){

        StringBuffer buffer = new StringBuffer();

        String requestUrl = "https://fanyi-api.baidu.com/api/trans/vip/translate?q=" + queryStr + "&from=auto&to=" + lan + "&appid=" + appid + "&salt=" + salt + "&sign=" + sign;
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {// 重写系统返回键
            Intent intent = new Intent();
            intent.setClass(TextTranslationActivity.this, MainActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void initSpinner(){
        List<Language> languages = new ArrayList<Language>();
        languages.add(new Language("en", "英文"));
        languages.add(new Language("zh", "中文"));
        languages.add(new Language("yue", "粤语"));
        languages.add(new Language("jp", "日语"));
        languages.add(new Language("kor", "韩语"));
        languages.add(new Language("fra", "法语"));
        languages.add(new Language("spa", "西班牙语"));
        languages.add(new Language("th", "泰语"));
        languages.add(new Language("ara", "阿拉伯语"));
        languages.add(new Language("ru", "俄语"));
        languages.add(new Language("pt", "葡萄牙语"));
        languages.add(new Language("de", "德语"));
        languages.add(new Language("it", "意大利语"));
        languages.add(new Language("vie", "越南语"));
        ArrayAdapter<Language> adapter = new ArrayAdapter<Language>(this,R.layout.item_language_text,languages);
        adapter.setDropDownViewResource(R.layout.item_language_drop_down);
        spinner.setAdapter(adapter);
        spinner.setSelection(0,true);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position==0){
                    return;
                }else {
                    wordsKeyArr = original.getText().toString();
                    lan = ((Language)spinner.getSelectedItem()).getKey();
                    getTranslationResult();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

}
