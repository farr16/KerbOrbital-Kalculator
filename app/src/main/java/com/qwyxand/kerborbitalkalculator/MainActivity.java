package com.qwyxand.kerborbitalkalculator;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

public class MainActivity extends FragmentActivity implements CalculatorFragment.OnCalculationListener{

    private CustomFragmentPagerAdapter adapter;

    /** onCalculation
     *
     * Listener for fragment communication from the CalculatorFragment to the other fragments.
     * Called when the calculate button is pressed in the CalculatorFragment. Puts the info
     * passed by the fragment into the adapter and calls notifyDataSetChanged, resulting in the
     * PhaseAngleDisplayFragment and EjectionAngleDisplayFragment views being updated.
     *
     * @param orig The origin planet selected in the CalculatorFragment view.
     * @param dest The destination planet selected in the CalculatorFragment view.
     * @param phase The phase angle calculated by the calculator.
     * @param eject The ejection angle calculated by the calculator.
     */
    public void onCalculation(String orig, String dest, float phase, float eject) {
        adapter.setOrigin(orig);
        adapter.setDestination(dest);
        adapter.setPhaseAngle(phase);
        adapter.setEjectionAngle(eject);

        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the content of the activity to use the activity_main.xml layout file
        setContentView(R.layout.activity_main);

        // Find the view pager that will allow the user to swipe between fragments
        ViewPager viewPager = (ViewPager) findViewById(R.id.ViewPager);
        viewPager.setOffscreenPageLimit(2);

        // Create an adapter which will take an index and give the fragment to be displayed
        adapter = new CustomFragmentPagerAdapter(getSupportFragmentManager());

        // Set the created adapter to be the adapter for the viewPager
        viewPager.setAdapter(adapter);
    }
}
