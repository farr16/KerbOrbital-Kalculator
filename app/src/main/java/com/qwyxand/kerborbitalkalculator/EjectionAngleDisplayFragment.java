package com.qwyxand.kerborbitalkalculator;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Matthew on 9/4/2016.
 *
 * This fragment will display the ejection angle calculated in the orbital maneuver calculations.
 *
 * The ejection angle is the angle between the ship's position at the burn point and the direction
 * of the origin body's orbit.
 */
public class EjectionAngleDisplayFragment extends Fragment {
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_ejection_angle_display, container, false);
    }
}
