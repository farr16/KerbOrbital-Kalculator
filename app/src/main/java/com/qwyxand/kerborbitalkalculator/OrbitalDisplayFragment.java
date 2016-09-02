package com.qwyxand.kerborbitalkalculator;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/** OrbitalDisplayFragment
 * Created by Matthew on 9/2/2016.
 *
 * This fragment will display the results of the orbital maneuver calculations.
 */
public class OrbitalDisplayFragment extends Fragment {
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_orbital_display, container, false);
    }
}