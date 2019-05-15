package com.example.sqlbrite.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.sqlbrite.R;
import com.example.sqlbrite.adapter.TabFragmentPagerAdapter;
import com.example.sqlbrite.app.BaseActivity;

public class MainActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener,
        ViewPager.OnPageChangeListener {

    private boolean mIsExit;

    public static final int PAGE_ONE = 0;
    public static final int PAGE_TWO = 1;
    public static final int PAGE_THREE = 2;

    private RadioGroup tabBar;
    private RadioButton home;
    private RadioButton mine;
    private RadioButton history;
    private TabFragmentPagerAdapter tabFragmentPagerAdapter;
    private ViewPager viewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tabFragmentPagerAdapter = new TabFragmentPagerAdapter(getSupportFragmentManager());
        initView();
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

}
