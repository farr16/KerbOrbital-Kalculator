package com.qwyxand.kerborbitalkalculator;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/** CustomFragmentPagerAdapter
 * Created by Matthew on 9/2/2016.
 *
 * Manages behavior of swiping between tabs.
 */
public class CustomFragmentPagerAdapter extends FragmentPagerAdapter {

    // constructor
    public CustomFragmentPagerAdapter(FragmentManager fm){
        super(fm);
    }

    @Override
    /** getItem
     * Given a position index, returns the fragment corresponding to that index.
     */
    public Fragment getItem(int pos) {
        if (pos == 0)
            return new CalculatorFragment();
        else if (pos == 1)
            return new PhaseAngleDisplayFragment();
        else if (pos == 2)
            return new EjectionAngleDisplayFragment();
        else
            return null;
    }

    @Override
    /** getCount
     * Returns the number of tabs in the application interface, currently fixed at three.
     */
    public int getCount() {
        return 3;
    }

}
