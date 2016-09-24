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

    private Body origin;
    private Body destination;
    private float phaseAngle = Float.NEGATIVE_INFINITY;

    // For now, a TextView is used to display the calculated phase angle
    // Later, this will be drawn graphically
    private TextView phaseDisplay;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_phase_angle_display, container, false);

        phaseDisplay = (TextView) view.findViewById(R.id.phaseAngleDisplayFragmentLabel);

        String content;

        if (origin!=null && destination!=null) {
            boolean innerOrbit = origin.sma < destination.sma;
            content = "Origin: " + origin.name + "\nDestination: " + destination.name + "\nPhase Angle: "
                    + phaseAngle + "\nInner: " + innerOrbit + "\nOrigin Color: " + Integer.toHexString(origin.color)
                    + "\nDestination Color: " + Integer.toHexString(destination.color);
        }
        else
            content = getString(R.string.phase_angle_display_fragment_label);

        phaseDisplay.setText(content);

        return view;
    }

    /** updatePhaseDisplay
     *
     * Updates the view for the PhaseDisplayFragment.
     * Called in the adapter's getItemPosition method. Updates the view when the Calculate button
     * is pressed in the Calculator Fragment
     *
     * @param orig The origin planet selected in the CalculatorFragment view
     * @param dest The destination planet selected in the CalculatorFragment view
     * @param phase The phase angle calculated by the calculator
     */
    public void updatePhaseDisplay(Body orig, Body dest, float phase) {

        origin = orig;
        destination = dest;
        phaseAngle = phase;
        destination = dest;

        if (origin!=null && destination!=null) {
            boolean innerOrbit = origin.sma < destination.sma;
            String content = "Origin: " + origin.name + "\nDestination: " + destination.name + "\nPhase Angle: "
                            + phaseAngle + "\nInner: " + innerOrbit + "\nOrigin Color: " + Integer.toHexString(origin.color)
                            + "\nDestination Color: " + Integer.toHexString(destination.color);

            phaseDisplay.setText(content);
        }
    }
}