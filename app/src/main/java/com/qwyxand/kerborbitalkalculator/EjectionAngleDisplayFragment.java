package com.qwyxand.kerborbitalkalculator;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Matthew on 9/4/2016.
 *
 * This fragment will display the ejection angle calculated in the orbital maneuver calculations.
 *
 * The ejection angle is the angle between the ship's position at the burn point and the direction
 * of the origin body's orbit.
 */
public class EjectionAngleDisplayFragment extends Fragment {

    private static final String originLocation = "com.qwyxand.kerborbitalkalculator.origin";
    private static final String ejectionAngleLocation = "com.qwyxand.kerborbitalkalculator.ejectionAngle";

    private String origin;
    private float ejectionAngle;

    private TextView ejectionDisplay;

    public static EjectionAngleDisplayFragment newInstance(String orig, float phase) {

        Bundle args = new Bundle();
        args.putString(originLocation, orig);
        args.putFloat(ejectionAngleLocation, phase);

        EjectionAngleDisplayFragment fragment = new EjectionAngleDisplayFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        origin = args.getString(originLocation);
        ejectionAngle = args.getFloat(ejectionAngleLocation);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_ejection_angle_display, container, false);

        ejectionDisplay = (TextView) view.findViewById(R.id.ejectionAngleDisplayFragmentLabel);
        String content = "Origin: " + origin + "\nEjection Angle: " + ejectionAngle;

        ejectionDisplay.setText(content);

        return view;
    }

    public void updateEjectionDisplay(String orig, float eject) {
        origin = orig;
        ejectionAngle = eject;

        String content = "Origin: " + origin + "\nEjection Angle: " + ejectionAngle;
        ejectionDisplay.setText(content);

    }
}
