package com.xuhong.smarthome.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * @创建者 xuhong
 * @描述 主界面中ViewPager的适配器
 */
public class MainViewPagerAdapter extends FragmentPagerAdapter {

    List<Fragment> list;
    List<String> title;

    public MainViewPagerAdapter(FragmentManager fm, List<Fragment> list) {
        super(fm);
        this.list = list;
    }

    public MainViewPagerAdapter(FragmentManager fm, List<Fragment> list,List<String> title) {
        super(fm);
        this.list = list;
        this.title = title;
    }
    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getCount() {
        return list.size();
    }


    @Override
    public CharSequence getPageTitle(int position) {
        if(title == null){
            return super.getPageTitle(position);
        }else{
            return title.get(position);
        }
    }
}