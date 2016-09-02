package com.qwyxand.kerborbitalkalculator;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by Matthew on 9/2/2016.
 */
public class CustomFragmentPagerAdapter extends FragmentPagerAdapter {

    public CustomFragmentPagerAdapter(FragmentManager fm){
        super(fm);
    }

    @Override
    public Fragment getItem(int pos) {
        if (pos == 0)
            return new CalculatorFragment();
        else if (pos == 1)
            return new OrbitalDisplayFragment();
        else
            return null;
    }

    @Override
    public int getCount() {
        return 2;
    }

}
