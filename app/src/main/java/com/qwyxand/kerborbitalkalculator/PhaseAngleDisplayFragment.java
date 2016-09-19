package com.qwyxand.kerborbitalkalculator;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/** PhaseAngleDisplayFragment
 * Created by Matthew on 9/4/2016.
 *
 * This fragment will display the phase angle calculated in the orbital maneuver calculations.
 *
 * The phase angle is the angle between the origin body and the destination body, with its
 * vertex located at the body the origin and destination are orbiting.
 */
public class PhaseAngleDisplayFragment extends Fragment {

    private String origin = "";
    private String destination = "";
    private float phaseAngle = Float.NEGATIVE_INFINITY;

    private TextView phaseDisplay;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_phase_angle_display, container, false);

        phaseDisplay = (TextView) view.findViewById(R.id.phaseAngleDisplayFragmentLabel);
        String content = "Origin: " + origin + "\nDestination: " + destination + "\nPhase Angle: "
                        + phaseAngle;

        phaseDisplay.setText(content);

        return view;
    }

    public void updatePhaseDisplay(String orig, String dest, float phase) {

        origin = orig;
        phaseAngle = phase;
        destination = dest;

        String content = "Origin: " + origin + "\nDestination: " + destination + "\nPhase Angle: "
                + phaseAngle;
        phaseDisplay.setText(content);
    }
}