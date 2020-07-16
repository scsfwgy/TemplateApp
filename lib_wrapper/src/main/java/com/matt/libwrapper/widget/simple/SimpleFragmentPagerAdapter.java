package com.matt.libwrapper.widget.simple;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

/**
 * ============================================================
 * <p/>
 * 版 权 ：   闪牛投资 版权所有 (c)
 * 作 者 :    clude
 * 创建日期 ：2016-03-30
 * 描 述 ：   常用viewPager适配器
 * ============================================================
 **/
public class SimpleFragmentPagerAdapter extends FragmentPagerAdapter {
    private List<? extends Fragment> fragmentList;
    private List<String> titles;

    public SimpleFragmentPagerAdapter(FragmentManager fm, List<? extends Fragment> fragmentList) {
        super(fm);
        this.fragmentList = fragmentList;
    }

    public SimpleFragmentPagerAdapter(FragmentManager fm, List<? extends Fragment> fragmentList, List<String> titles) {
        super(fm);
        this.fragmentList = fragmentList;
        this.titles = titles;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (titles != null) {
            return titles.get(position);
        } else {
            return super.getPageTitle(position);
        }
    }
}

