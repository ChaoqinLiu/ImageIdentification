package com.example.sqlbrite.activity;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sqlbrite.R;
import com.example.sqlbrite.app.BaseActivity;
import com.example.sqlbrite.database.IdentificationDatabaseHelper;
import com.example.sqlbrite.utils.MD5Utils;
import com.jakewharton.rxbinding2.view.RxView;
import com.safframework.injectview.annotations.InjectView;
import com.safframework.log.L;
import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import rx.Observable;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

import static com.example.sqlbrite.database.IdentificationDatabaseHelper.CURRENTVERSION;
import static com.example.sqlbrite.utils.ValidatorUtil.isEmail;
import static com.example.sqlbrite.utils.ValidatorUtil.isPhone;


public class LoginActivity extends BaseActivity{

    private IdentificationDatabaseHelper dbHelper;
    private BriteDatabase briteDatabase;
    private SqlBrite sqlBrite;

    private CountDownTimer countDownTimer;
    private EventHandler handler;

    ArrayList<String> nameList = new ArrayList<String>();

    private EditText edit_nickname;
    private EditText edit_account;
    private EditText edit_password;
    private EditText edit_rePassword;
    private Button register;
    private Button cancel;

    private String nickname;
    private String account;
    private String password;
    private String rePassword;

    private String name;

    @InjectView(R.id.text_user)
    TextView text_user;

    @InjectView(R.id.text_password)
    TextView text_password;

    @InjectView(R.id.text_quick_login)
    TextView text_quick_login;

    @InjectView(R.id.text_register)
    TextView text_register;

    @InjectView(R.id.button_login)
    Button button_login;

    @InjectView(R.id.edit_password)
    EditText editPassword;

    @InjectView(R.id.edit_user)
    EditText editUser;

    @InjectView(R.id.text_forget_password)
    TextView text_forget_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        Typeface iconfont = Typeface.createFromAsset(getAssets(), "iconfont/iconfont.ttf");
        text_user.setTypeface(iconfont);
        text_password.setTypeface(iconfont);
        initView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        dbHelper = IdentificationDatabaseHelper.getInstance(this,CURRENTVERSION);
        sqlBrite = SqlBrite.create();
        briteDatabase = sqlBrite.wrapDatabaseHelper(dbHelper, Schedulers.io());
    }

    @Override
    protected void onStop() {
        super.onStop();
        dbHelper.closeLink();
    }

    private void initView(){
        RxView.clicks(text_quick_login)
                .throttleFirst(600,TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        initQuickLogin();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        L.i(throwable.getMessage());
                    }
                });

        RxView.clicks(text_register)
                .throttleFirst(600,TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        initRegister();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        L.i(throwable.getMessage());
                    }
                });

        RxView.clicks(button_login)
                .throttleFirst(600,TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        initLogin();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        L.i(throwable.getMessage());
                    }
                });

        RxView.clicks(text_forget_password)
                .throttleFirst(600,TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        initRetrievePassword();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        L.i(throwable.getMessage());
                    }
                });

        //如果SharedPreferences里存在用户信息则直接进入
        /*SharedPreferences preferences = getSharedPreferences("user",MODE_PRIVATE);
        String user = preferences.getString("account","");
        String passwprd = preferences.getString("password","");
        if (!(TextUtils.isEmpty(user) && TextUtils.isEmpty(passwprd))) {
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
            finish();
        }*/
    }

    private void initQuickLogin (){
        final Dialog dialog = new Dialog(this,R.style.DialogTheme);
        View view = View.inflate(this,R.layout.dialog_quick_login,null);
        dialog.setContentView(view);

        Window window = dialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();

        LinearLayout phone_quick_login = dialog.findViewById(R.id.phone_quick_login);
        LinearLayout qq_quick_login = dialog.findViewById(R.id.qq_quick_login);
        LinearLayout weichat_quick_login = dialog.findViewById(R.id.weichat_quick_login);
        TextView icon_phone = dialog.findViewById(R.id.icon_phone);
        TextView icon_qq = dialog.findViewById(R.id.icon_qq);
        TextView icon_weichat = dialog.findViewById(R.id.icon_weichat);
        TextView cancel = dialog.findViewById(R.id.cancel);

        Typeface iconfont = Typeface.createFromAsset(getAssets(), "iconfont/iconfont.ttf");
        icon_phone.setTypeface(iconfont);
        icon_qq.setTypeface(iconfont);
        icon_weichat.setTypeface(iconfont);

        RxView.clicks(cancel)
                .throttleFirst(600,TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        dialog.dismiss();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        L.i(throwable.getMessage());
                    }
                });

        RxView.clicks(phone_quick_login)
                .throttleFirst(600,TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        initPhoneQuickLogin();
                        dialog.dismiss();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        L.i(throwable.getMessage());
                    }
                });

        RxView.clicks(qq_quick_login)
                .throttleFirst(600,TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        initQQLogin();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        L.i(throwable.getMessage());
                    }
                });

        RxView.clicks(weichat_quick_login)
                .throttleFirst(600,TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        initWeichatLogin();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        L.i(throwable.getMessage());
                    }
                });
    }

    private void initRegister(){
        final Dialog dialog = new Dialog(this,R.style.DialogTheme);
        //设置布局
        View view = View.inflate(this,R.layout.dialog_register,null);
        dialog.setContentView(view);

        Window window = dialog.getWindow();
        //设置弹出位置
        window.setGravity(Gravity.CENTER);
        //设置对话框大小
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();
        dialog.setCancelable(false);

        edit_nickname = dialog.findViewById(R.id.nickname);
        edit_account = dialog.findViewById(R.id.account);
        edit_password = dialog.findViewById(R.id.password);
        edit_rePassword = dialog.findViewById(R.id.rePassword);
        register = dialog.findViewById(R.id.button_register);
        cancel = dialog.findViewById(R.id.button_cancel);

        RxView.clicks(register)
                .throttleFirst(600,TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        nickname = edit_nickname.getText().toString().trim();
                        account = edit_account.getText().toString().trim();
                        password = edit_password.getText().toString().trim();
                        rePassword = edit_rePassword.getText().toString().trim();

                        Observable<SqlBrite.Query> observable = briteDatabase.createQuery("user","SELECT name FROM user WHERE name=?",new String(account));
                        observable.subscribe(new Action1<SqlBrite.Query>() {
                            @Override
                            public void call(SqlBrite.Query query) {
                                Cursor cursor = query.run();
                                if (cursor.getCount() != 0) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(LoginActivity.this, "账号已注册，请直接登陆", Toast.LENGTH_SHORT).show();
                                            dialog.dismiss();
                                            return;
                                        }
                                    });
                                } else {
                                    if (TextUtils.isEmpty(nickname)) {
                                        Toast.makeText(LoginActivity.this,"昵称不能为空",Toast.LENGTH_SHORT).show();
                                        return;
                                    }

                                    if (!(isPhone(account) || isEmail(account))) {
                                        Toast.makeText(LoginActivity.this,"账号格式不正确，请重新输入",Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                    if (password.length() < 6) {
                                        Toast.makeText(LoginActivity.this,"密码不能小于6位",Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                    if (!(rePassword.equals(password))) {
                                        Toast.makeText(LoginActivity.this,"两次输入的密码必须一致",Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                    String pwd = MD5Utils.MD5Encode(password,"UTF-8");
                                    ContentValues contentValues = new ContentValues();
                                    contentValues.put("nickname", nickname);
                                    contentValues.put("name", account);
                                    contentValues.put("password", pwd);
                                    briteDatabase.insert("user", contentValues);
                                    Toast.makeText(LoginActivity.this,"注册成功",Toast.LENGTH_SHORT).show();
                                    saveUserDataToSharedPreferences(account, pwd);
                                    dialog.dismiss();
                                    cursor.close();
                                    briteDatabase.close();
                                }
                            }
                        }, new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {
                                L.i(throwable.getMessage());
                            }
                        });

                    }

                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        L.i(throwable.getMessage());
                    }
                });

        RxView.clicks(cancel)
                .throttleFirst(600,TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception{
                        dialog.dismiss();
                    }
                });

    }

    private void saveUserDataToSharedPreferences(String account,String password){
        SharedPreferences.Editor editor = getSharedPreferences("user",MODE_PRIVATE).edit();
        editor.putString("account",account);
        editor.putString("password",password);
        editor.commit();
    }

    private void initLogin() {
        Observable<SqlBrite.Query> observable = briteDatabase.createQuery("user","SELECT name, password FROM user");
        observable.subscribe(new Action1<SqlBrite.Query>() {
            @Override
            public void call(SqlBrite.Query query) {
                String user = editUser.getText().toString().trim();
                String pwd = MD5Utils.MD5Encode(editPassword.getText().toString(), "UTF-8").trim();
                Cursor cursor = query.run();
                if (cursor.getCount() != 0) {
                    while (cursor.moveToNext()) {
                        account = cursor.getString(cursor.getColumnIndex("name"));
                        password = cursor.getString(cursor.getColumnIndex("password"));
                    }
                    if (account.equals(user) && password.equals(pwd)) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(LoginActivity.this, "登陆成功", Toast.LENGTH_SHORT).show();
                            }
                        });
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(LoginActivity.this, "账号或密码不正确，请重新输入", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
                cursor.close();
                briteDatabase.close();
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                L.i(throwable.getMessage());
            }
        });
    }

    private void initPhoneQuickLogin(){
        final Dialog dialog = new Dialog(this,R.style.DialogTheme);
        //设置布局
        View view = View.inflate(this,R.layout.dialog_phone_quick_login,null);
        dialog.setContentView(view);

        Window window = dialog.getWindow();
        window.setGravity(Gravity.CENTER);
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);
        dialog.show();

        EditText phoneNumber = dialog.findViewById(R.id.phone_number);
        EditText verificationCode = dialog.findViewById(R.id.verification_code);
        Button buttonSend = dialog.findViewById(R.id.button_send);
        Button buttonLogin = dialog.findViewById(R.id.button_login);
        Button cancel = dialog.findViewById(R.id.button_cancel);

        RxView.clicks(buttonSend)
                .throttleFirst(600,TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        String phone = phoneNumber.getText().toString().trim();
                        if (!isPhone(phone)) {
                            Toast.makeText(LoginActivity.this, "手机号码格式不正确，请重新输入", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        SMSSDK.getVerificationCode("86", phone);
                        verificationCode.requestFocus();
                        countDownTimer = new CountDownTimer(60000, 1000) {
                            @Override
                            public void onTick(long millisUntilFinished) {
                                buttonSend.setText(String.valueOf(millisUntilFinished / 1000));
                                buttonSend.setEnabled(false);
                            }

                            @Override
                            public void onFinish() {
                                buttonSend.setText("获取");
                                countDownTimer = null;
                                buttonSend.setEnabled(true);
                            }
                        }.start();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        L.i(throwable.getMessage());
                    }
                });

        RxView.clicks(buttonLogin)
                .throttleFirst(600,TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        handler = new EventHandler(){
                            public void afterEvent(int event, int result, Object data){
                                Message message = new Message();
                                message.arg1 = event;
                                message.arg2 = result;
                                message.obj = data;
                                new Handler(Looper.getMainLooper(), new Handler.Callback() {
                                    @Override
                                    public boolean handleMessage(Message msg) {
                                        int event = message.arg1;
                                        int result = message.arg2;
                                        Object data = message.obj;
                                        if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                                            if (result == SMSSDK.RESULT_COMPLETE) {
                                                HashMap<String,Object> phoneMap = (HashMap<String, Object>) data;
                                                String phone = (String) phoneMap.get("phone");
                                                saveUserDataToSharedPreferences(phone," ");
                                                Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                                                startActivity(intent);
                                                finish();
                                            } else {
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Toast.makeText(LoginActivity.this,"验证码不正确",Toast.LENGTH_SHORT).show();
                                                        ((Throwable) data).printStackTrace();
                                                        return;
                                                    }
                                                });
                                            }
                                        }
                                        return false;
                                    }
                                }).sendMessage(message);
                            }
                        };
                        SMSSDK.registerEventHandler(handler);
                        String code = verificationCode.getText().toString().trim();
                        String phone = phoneNumber.getText().toString().trim();
                        if (TextUtils.isEmpty(code)) {
                            Toast.makeText(LoginActivity.this, "验证码不能为空", Toast.LENGTH_SHORT).show();
                            return;
                        } else {
                            SMSSDK.submitVerificationCode("86", phone, code);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        L.i(throwable.getMessage());
                    }
                });

        RxView.clicks(cancel)
                .throttleFirst(600,TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        dialog.dismiss();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        L.i(throwable.getMessage());
                    }
                });
    }

    private void initRetrievePassword() {
        final Dialog dialog = new Dialog(this,R.style.DialogTheme);
        //设置布局
        View view = View.inflate(this,R.layout.dialog_retrieve_password,null);
        dialog.setContentView(view);

        Window window = dialog.getWindow();
        window.setGravity(Gravity.CENTER);
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);
        dialog.show();

        EditText phoneNumber = dialog.findViewById(R.id.phone_number);
        EditText verificationCode = dialog.findViewById(R.id.verification_code);
        Button buttonSend = dialog.findViewById(R.id.button_send);
        Button buttonLogin = dialog.findViewById(R.id.button_login);
        Button cancel = dialog.findViewById(R.id.button_cancel);

        RxView.clicks(buttonSend)
                .throttleFirst(600,TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        String phone = phoneNumber.getText().toString().trim();
                        if (!isPhone(phone)) {
                            Toast.makeText(LoginActivity.this, "手机号码格式不正确，请重新输入", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        SMSSDK.getVerificationCode("86", phone);
                        verificationCode.requestFocus();
                        countDownTimer = new CountDownTimer(60000, 1000) {
                            @Override
                            public void onTick(long millisUntilFinished) {
                                buttonSend.setText(String.valueOf(millisUntilFinished / 1000));
                                buttonSend.setEnabled(false);
                            }

                            @Override
                            public void onFinish() {
                                buttonSend.setText("获取");
                                countDownTimer = null;
                                buttonSend.setEnabled(true);
                            }
                        }.start();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        L.i(throwable.getMessage());
                    }
                });

        RxView.clicks(buttonLogin)
                .throttleFirst(600,TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        handler = new EventHandler(){
                            public void afterEvent(int event, int result, Object data){
                                Message message = new Message();
                                message.arg1 = event;
                                message.arg2 = result;
                                message.obj = data;
                                new Handler(Looper.getMainLooper(), new Handler.Callback() {
                                    @Override
                                    public boolean handleMessage(Message msg) {
                                        int event = message.arg1;
                                        int result = message.arg2;
                                        Object data = message.obj;
                                        if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                                            if (result == SMSSDK.RESULT_COMPLETE) {
                                                HashMap<String,Object> phoneMap = (HashMap<String, Object>) data;
                                                String phone = (String) phoneMap.get("phone");
                                                initUpdatePassword(phone);
                                                dialog.dismiss();
                                            } else {
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Toast.makeText(LoginActivity.this,"验证码不正确",Toast.LENGTH_SHORT).show();
                                                        ((Throwable) data).printStackTrace();
                                                        return;
                                                    }
                                                });
                                            }
                                        }
                                        return false;
                                    }
                                }).sendMessage(message);
                            }
                        };
                        SMSSDK.registerEventHandler(handler);
                        String code = verificationCode.getText().toString().trim();
                        String phone = phoneNumber.getText().toString().trim();
                        if (TextUtils.isEmpty(code)) {
                            Toast.makeText(LoginActivity.this, "验证码不能为空", Toast.LENGTH_SHORT).show();
                            return;
                        } else {
                            SMSSDK.submitVerificationCode("86", phone, code);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        L.i(throwable.getMessage());
                    }
                });

        RxView.clicks(cancel)
                .throttleFirst(600,TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        dialog.dismiss();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        L.i(throwable.getMessage());
                    }
                });
    }

    private void initUpdatePassword(String phone) {
        final Dialog dialog = new Dialog(this,R.style.DialogTheme);
        //设置布局
        View view = View.inflate(this,R.layout.dialog_update_password,null);
        dialog.setContentView(view);

        Window window = dialog.getWindow();
        window.setGravity(Gravity.CENTER);
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false); //禁止点击弹框区域外关闭
        dialog.show();

        EditText password = dialog.findViewById(R.id.password);
        EditText rePassword = dialog.findViewById(R.id.rePassword);
        Button buttonUpdate = dialog.findViewById(R.id.button_update);
        Button cancel = dialog.findViewById(R.id.button_cancel);

        RxView.clicks(buttonUpdate)
                .throttleFirst(600,TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        String pwd = password.getText().toString().trim();
                        String rePwd = rePassword.getText().toString().trim();
                        if (pwd.length() < 6) {
                            Toast.makeText(LoginActivity.this,"密码不能小于6位",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (!(rePwd.equals(pwd))) {
                            Toast.makeText(LoginActivity.this, "两次输入的密码不一致", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Observable<SqlBrite.Query> observable = briteDatabase.createQuery("user", "SELECT name FROM user WHERE name=?",new String(phone));
                        observable.subscribe(new Action1<SqlBrite.Query>() {
                            @Override
                            public void call(SqlBrite.Query query) {
                                Cursor cursor = query.run();
                                if (cursor.getCount() == 0) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(LoginActivity.this, "手机号尚未注册", Toast.LENGTH_SHORT).show();
                                            dialog.dismiss();
                                            return;
                                        }
                                    });
                                }
                                cursor.close();
                                briteDatabase.close();
                            }
                        }, new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {
                                L.i(throwable.getMessage());
                            }
                        });
                        ContentValues values = new ContentValues();
                        String password = MD5Utils.MD5Encode(pwd,"UTF-8");
                        values.put("password", password);
                        briteDatabase.update("user", values, "name=?", new String(phone));
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(LoginActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                                return;
                            }
                        });
                        briteDatabase.close();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        L.i(throwable.getMessage());
                    }
                });

        RxView.clicks(cancel)
                .throttleFirst(600,TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        dialog.dismiss();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        L.i(throwable.getMessage());
                    }
                });
    }

    private void initQQLogin(){}

    private void initWeichatLogin(){}

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterEventHandler(handler);
    }

}

