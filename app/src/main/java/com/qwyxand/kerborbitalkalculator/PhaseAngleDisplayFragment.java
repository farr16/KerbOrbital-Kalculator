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

    private static final String originLocation = "com.qwyxand.kerborbitalkalculator.origin";
    private static final String destinationLocation = "com.qwyxand.kerborbitalkalculator.destination";
    private static final String phaseAngleLocation = "com.qwyxand.kerborbitalkalculator.phaseAngle";


    private String origin;
    private String destination;
    private float phaseAngle;

    private TextView phaseDisplay;

    public static PhaseAngleDisplayFragment newInstance(String orig, String dest, float phase) {
        Bundle args = new Bundle();
        args.putString(originLocation, orig);
        args.putString(destinationLocation, dest);
        args.putFloat(phaseAngleLocation, phase);

        PhaseAngleDisplayFragment fragment = new PhaseAngleDisplayFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        origin = args.getString(originLocation);
        destination = args.getString(destinationLocation);
        phaseAngle = args.getFloat(phaseAngleLocation);
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
        destination = dest;
        phaseAngle = phase;

        String content = "Origin: " + origin + "\nDestination: " + "\nPhase Angle: " + phaseAngle;
        phaseDisplay.setText(content);
    }
}