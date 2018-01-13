package com.example.hermon.framentpager;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class MyFragmentPageAdaptor extends FragmentPagerAdapter {
    public MyFragmentPageAdaptor(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position){
            case 0:
                fragment = new FragmentMain();
                break;
            case 1:
                fragment = new FragmentDetails();
                break;
            case 2:
                fragment = new FragmentMain();
                break;
        }
        return fragment;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (position==0) {
            return "Main page";
        } else {
            return "Details";
        }
    }

    @Override
    public int getCount() {
        return 2;
    }
}
