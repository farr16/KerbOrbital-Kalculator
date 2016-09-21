package com.qwyxand.kerborbitalkalculator;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/** CustomFragmentPagerAdapter
 * Created by Matthew on 9/2/2016.
 *
 * Manages behavior of swiping between tabs.
 */
class CustomFragmentPagerAdapter extends FragmentPagerAdapter {

    private final int PAGE_COUNT = 3;

    private String origin;
    private String destination;
    private float phaseAngle;
    private float ejectionAngle;

    // constructor
    public CustomFragmentPagerAdapter(FragmentManager fm){
        super(fm);

        // Default values for initial view setup
        origin = "";
        destination = "";
        phaseAngle = Float.NEGATIVE_INFINITY;
        ejectionAngle = Float.NEGATIVE_INFINITY;
    }

    @Override
    /** getItem
     * Given a position index, returns the fragment corresponding to that index.
     */
    public Fragment getItem(int pos) {
        if (pos == 0) {
            return new CalculatorFragment();
        }
        else if (pos == 1) {
            return new PhaseAngleDisplayFragment();
        }
        else if (pos == 2) {
            return new EjectionAngleDisplayFragment();
        }
        else
            return null;
    }

    @Override
    /** getItemPosition
     *
     * Returns the position of a given Fragment object. Overwritten to allow for updating of views
     * by the adapter when notifyDatasetChanged() is called, by calling the given object's public
     * update method.
     */
    public int getItemPosition(Object object) {
        if (object instanceof CalculatorFragment)
            ;
        else if (object instanceof PhaseAngleDisplayFragment) {
            ((PhaseAngleDisplayFragment) object).updatePhaseDisplay(origin, destination, true, 0, 0, phaseAngle);
        }
        else if (object instanceof EjectionAngleDisplayFragment) {
            ((EjectionAngleDisplayFragment) object).updateEjectionDisplay(origin, 0 , ejectionAngle);
        }
        return super.getItemPosition(object);
    }

    @Override
    /** getCount
     * Returns the number of tabs in the application interface, currently fixed at three.
     */
    public int getCount() {return PAGE_COUNT;}

    /* Setter methods for the viewpager data set variables
    * */
    public void setOrigin (String orig) {origin = orig;}

    public void setDestination (String dest) {destination = dest;}

    public void setPhaseAngle (float phase) {phaseAngle = phase;}

    public void setEjectionAngle (float eject) {ejectionAngle = eject;}

}
