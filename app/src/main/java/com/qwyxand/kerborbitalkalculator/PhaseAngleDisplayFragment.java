package com.qwyxand.kerborbitalkalculator;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

    private PhaseAngleDisplayCanvas phaseAngleDisplayCanvas;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_phase_angle_display, container, false);

        phaseAngleDisplayCanvas = (PhaseAngleDisplayCanvas) view.findViewById(R.id.phaseAngleDisplayCanvas);

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

        phaseAngleDisplayCanvas.setOrigin(origin);
        phaseAngleDisplayCanvas.setDestination(destination);
        phaseAngleDisplayCanvas.setPhaseAngle(phaseAngle);

        phaseAngleDisplayCanvas.invalidate();
    }
}