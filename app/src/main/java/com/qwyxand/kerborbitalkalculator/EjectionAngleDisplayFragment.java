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

    private Body origin = null;
    private float ejectionAngle = Float.NEGATIVE_INFINITY;

    // For now, a TextView is used to display the calculated phase angle
    // Later, this will be drawn graphically
    private TextView ejectionDisplay;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_ejection_angle_display, container, false);

        ejectionDisplay = (TextView) view.findViewById(R.id.ejectionAngleDisplayFragmentLabel);

        String content;

        if (origin != null) {
            content = "Origin: " + origin.name + "\nEjection Angle: " + ejectionAngle
                     + "\nOrigin Color: " + Integer.toHexString(origin.color);
        }
        else {
            content = getString(R.string.ejection_angle_display_fragment_label);
        }

        ejectionDisplay.setText(content);

        return view;
    }

    /** updateEjectionDisplay
     *
     * Updates the view for the EjectionDisplayFragment.
     * Called in the adapter's getItemPosition method. Updates the view when the Calculate button
     * is pressed in the Calculator Fragment
     *
     * @param orig The origin planet selected in the CalculatorFragment view
     * @param eject The ejection angle calculated by the calculator
     */
    public void updateEjectionDisplay(Body orig, float eject) {
        origin = orig;
        ejectionAngle = eject;

        String content;
        if (origin != null) {
            content = "Origin: " + origin.name + "\nEjection Angle: " + ejectionAngle
                            + "\nColor:" + Integer.toHexString(origin.color);
        }
        else {
            content = getString(R.string.ejection_angle_display_fragment_label);
        }

        ejectionDisplay.setText(content);

    }
}
