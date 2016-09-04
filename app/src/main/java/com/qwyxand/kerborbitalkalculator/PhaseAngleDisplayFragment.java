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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_phase_angle_display, container, false);
    }
}