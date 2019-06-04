package com.example.sqlbrite.activity;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sqlbrite.R;
import com.example.sqlbrite.app.BaseActivity;
import com.example.sqlbrite.database.IdentificationDatabaseHelper;
import com.example.sqlbrite.model.Language;
import com.example.sqlbrite.model.TranslateResult;
import com.example.sqlbrite.utils.BitmapUtil;
import com.example.sqlbrite.utils.MD5Utils;
import com.google.gson.Gson;
import com.safframework.injectview.annotations.InjectView;
import com.safframework.log.L;
import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import rx.schedulers.Schedulers;


public class TextTranslationActivity extends BaseActivity {

    private String wordsKeyArr;
    private String queryStr = "";
    private String path;
    private static final String appid = "20190401000283388";
    private static final String key = "SkTeVqucOItasS35k8tJ";
    private static final String salt = "1435660288";
    private static String sign;
    private String signStr;
    private String dst;
    private static String lan = "en";  //翻译目标语言,默认为英文

    private IdentificationDatabaseHelper dbHelper;
    private BriteDatabase briteDatabase;
    private SqlBrite sqlBrite;

    private ProgressDialog progressDialog = null;

    private ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
    private ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(1);
    private ExecutorService fixedThreadPool = Executors.newFixedThreadPool(3);


    @InjectView(R.id.text_original)
    EditText original;

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
        path = intent.getStringExtra("path");
        try {
            JSONArray jsonArray = new JSONArray(wordsKeyArr);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String words = jsonObject.getString("words");
                queryStr += words;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        signStr = appid + queryStr + salt + key;
        sign = MD5Utils.MD5Encode(signStr,"UTF-8");

        progressDialog = ProgressDialog.show(TextTranslationActivity.this,"请稍后", "正在翻译中...",true);

        getTranslationResult();
        initSpinner();
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

    private void getTranslationResult(){
        fixedThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    String result = Translation();
                    L.i(result);
                    if (result == null || result.equals("")) {
                        scheduledThreadPool.schedule(new Runnable() {
                            @Override
                            public void run() {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        progressDialog.dismiss();
                                        Toast.makeText(TextTranslationActivity.this,"获取数据失败,请重新识别或检查网络设置",Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        },2,TimeUnit.SECONDS);
                    } else {
                        TranslateResult translateResult = new Gson().fromJson(result,TranslateResult.class);
                        dst = translateResult.getTrans_result().get(0).getDst();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                original.setText(queryStr);
                                translation.setText(dst);
                                progressDialog.dismiss();
                                singleThreadExecutor.execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        saveTranslationData();
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

    private void saveTranslationData() {
        try {
            ContentValues values = new ContentValues();
            values.put("original", queryStr);
            values.put("translation",dst);
            Bitmap bitmap = BitmapUtil.openBitmap(path);
            final ByteArrayOutputStream os = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
            values.put("pic", os.toByteArray());
            briteDatabase.insert("translation", values);
            bitmap.recycle();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String Translation(){

        StringBuffer buffer = new StringBuffer();

        String requestUrl = "https://fanyi-api.baidu.com/api/trans/vip/translate?q=" + queryStr + "&from=auto&to=" + lan + "&appid=" + appid + "&salt=" + salt + "&sign=" + sign;

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
            finish();
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
                queryStr = original.getText().toString();
                signStr = appid + queryStr + salt + key;
                sign = MD5Utils.MD5Encode(signStr,"UTF-8");
                lan = ((Language)spinner.getSelectedItem()).getKey();
                getTranslationResult();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        scheduledThreadPool.shutdown();
        singleThreadExecutor.shutdown();
        fixedThreadPool.shutdown();
    }

}
