package com.qwyxand.kerborbitalkalculator;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

public class MainActivity extends FragmentActivity
    implements CalculatorFragment.OnCalculationListener{


    public void onCalculation(String orig, String dest, float phase, float eject) {
        String origin = orig;
        String destination = dest;
        float phaseAngle = phase;
        float ejectionAngle = eject;

        System.out.println(origin);
        System.out.println(destination);
        System.out.println("Phase Angle: " + phaseAngle);
        System.out.println("Ejection Angle: " + ejectionAngle);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the content of the activity to use the activity_main.xml layout file
        setContentView(R.layout.activity_main);

        // Find the view pager that will allow the user to swipe between fragments
        ViewPager viewPager = (ViewPager) findViewById(R.id.ViewPager);

        // Create an adapter which will take an index and give the fragment to be displayed
        CustomFragmentPagerAdapter adapter = new CustomFragmentPagerAdapter(getSupportFragmentManager());

        // Set the created adapter to be the adapter for the viewPager
        viewPager.setAdapter(adapter);
    }
}
