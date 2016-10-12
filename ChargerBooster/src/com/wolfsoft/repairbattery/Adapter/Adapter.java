package com.wolfsoft.repairbattery.Adapter;

import com.wolfsoft.repairbattery.Fragment.Pager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;



/**
 * @author Adrián García Lomas
 */
public class Adapter extends FragmentPagerAdapter {
    private static final int COUNT = 1;

    public Adapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return Pager.getInstance();
    }

    @Override
    public int getCount() {
        return COUNT;
    }
}
