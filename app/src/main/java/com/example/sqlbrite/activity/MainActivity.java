package com.example.sqlbrite.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.sqlbrite.R;
import com.example.sqlbrite.adapter.TabFragmentPagerAdapter;
import com.example.sqlbrite.app.BaseActivity;
import com.example.sqlbrite.fragment.TabMineFragment;
import com.facebook.stetho.Stetho;
import com.safframework.log.L;

public class MainActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener,
        ViewPager.OnPageChangeListener {

    private boolean mIsExit;

    public static final int PAGE_ONE = 0;
    public static final int PAGE_TWO = 1;
    public static final int PAGE_THREE = 2;
    private static final int USER_RESULT_CODE = 40;
    private static final int FLAG_RESULT_CODE = 5;

    private RadioGroup tabBar;
    private RadioButton home;
    private RadioButton mine;
    private RadioButton history;
    private TabFragmentPagerAdapter tabFragmentPagerAdapter;
    private ViewPager viewPager;

    private String flag;
    private static String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tabFragmentPagerAdapter = new TabFragmentPagerAdapter(getSupportFragmentManager());
        Stetho.initializeWithDefaults(this);
        initView();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        flag = getIntent().getStringExtra("flag");
        if (flag != null) {
            viewPager.setCurrentItem(PAGE_TWO);
        }
        type = getIntent().getStringExtra("type");
        if (type.equals("type_user")) {
            viewPager.setCurrentItem(PAGE_THREE);
        }

    }

    private void initView(){
        tabBar = (RadioGroup) findViewById(R.id.rg_tab_bar);
        home = (RadioButton) findViewById(R.id.bt_home);
        mine = (RadioButton) findViewById(R.id.bt_mine);
        history = (RadioButton) findViewById(R.id.bt_history);
        viewPager = (ViewPager) findViewById(R.id.viewpager);

        tabBar.setOnCheckedChangeListener(this);
        viewPager.setAdapter(tabFragmentPagerAdapter);
        viewPager.setCurrentItem(0);
        viewPager.addOnPageChangeListener(this);
        home.setChecked(true);
    }

    @Override
    public void onCheckedChanged(RadioGroup group,int checkedId){
        switch (checkedId) {
            case R.id.bt_home:
                viewPager.setCurrentItem(PAGE_ONE);
                break;
            case R.id.bt_history:
                viewPager.setCurrentItem(PAGE_TWO);
                break;
            case R.id.bt_mine:
                viewPager.setCurrentItem(PAGE_THREE);
                break;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (state == 2) {
            int currentItemPosition = viewPager.getCurrentItem();
            switch (currentItemPosition) {
                case PAGE_ONE:
                    home.setChecked(true);
                    break;
                case PAGE_TWO:
                    history.setChecked(true);
                    break;
                case PAGE_THREE:
                    mine.setChecked(true);
                    break;
            }
        }
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.share, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if (item.getItemId() == R.id.share) {
            Intent intent = new Intent(Intent.ACTION_SEND);
            //intent.setType("text/plain");
            //intent.putExtra(Intent.EXTRA_TEXT, "这是一段分享的文字");
            startActivity(Intent.createChooser(intent, "分享"));
        }
        return super.onOptionsItemSelected(item);
    }

}
