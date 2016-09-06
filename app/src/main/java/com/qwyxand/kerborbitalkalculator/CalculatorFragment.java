package com.qwyxand.kerborbitalkalculator;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

/** CalculatorFragment
 * Created by Matthew on 9/2/2016.
 *
 * This fragment will contain code for initializing GUI elements for the calculator interface and
 * getting input from user input about the orbital transfer to be performed.
 */
public class CalculatorFragment extends Fragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View calcView = inflater.inflate(R.layout.fragment_calculator, container, false);

        // Initialize array of planets to be used in origin and destination spinners
        String[] planets = {"Moho", "Eve", "Kerbin", "Duna", "Dres", "Jool", "Eeloo"};

        // Obtain references to layout spinners in code
        Spinner origin = (Spinner) calcView.findViewById(R.id.originSelector);
        Spinner destination = (Spinner) calcView.findViewById(R.id.destinationSelector);

        // Set array adapters to populate spinners with choices
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_spinner_item, planets);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        origin.setAdapter(adapter);
        destination.setAdapter(adapter);

        return calcView;
    }
}
