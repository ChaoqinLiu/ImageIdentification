package com.example.sqlbrite.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.example.sqlbrite.activity.MainActivity;
import com.example.sqlbrite.fragment.TabHistoryFragment;
import com.example.sqlbrite.fragment.TabHomeFragment;
import com.example.sqlbrite.fragment.TabMineFragment;

public class TabFragmentPagerAdapter extends FragmentPagerAdapter {

    private final int PAGER_COUNT = 3;
    private TabHomeFragment tabHomeFragment;
    private TabMineFragment tabMineFragment;
    private TabHistoryFragment tabHistoryFragment;


    public TabFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
        tabHomeFragment = new TabHomeFragment();
        tabMineFragment = new TabMineFragment();
        tabHistoryFragment = new TabHistoryFragment();
    }


    @Override
    public int getCount() {
        return PAGER_COUNT;
    }

    @Override
    public Object instantiateItem(ViewGroup vg, int position) {
        return super.instantiateItem(vg, position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        System.out.println("position Destory" + position);
        super.destroyItem(container, position, object);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case MainActivity.PAGE_ONE:
                fragment = tabHomeFragment;
                break;
            case MainActivity.PAGE_TWO:
                fragment = tabHistoryFragment;
                break;
            case MainActivity.PAGE_THREE:
                fragment = tabMineFragment;
                break;
        }
        return fragment;
    }

}
