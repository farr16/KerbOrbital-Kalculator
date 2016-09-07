package com.qwyxand.kerborbitalkalculator;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

/** CalculatorFragment
 * Created by Matthew on 9/2/2016.
 *
 * This fragment will contain code for initializing GUI elements for the calculator interface and
 * getting input from user input about the orbital transfer to be performed.
 */
public class CalculatorFragment extends Fragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View calcView = inflater.inflate(R.layout.fragment_calculator, container, false);

        // Initialize array of planets to be used in origin and destination spinners
        final String[] planets = {"Moho", "Eve", "Kerbin", "Duna", "Dres", "Jool", "Eeloo"};

        // Obtain references to layout spinners in code
        final Spinner origin = (Spinner) calcView.findViewById(R.id.originSelector);
        final Spinner destination = (Spinner) calcView.findViewById(R.id.destinationSelector);

        // Obtain references to layout buttons in code
        final Button calculateButton = (Button) calcView.findViewById(R.id.calculateButton);
        final Button resetButton = (Button) calcView.findViewById(R.id.resetButton);

        // Obtain reference to layout EditText in code
        final EditText parkingOrbitEntry = (EditText) calcView.findViewById(R.id.parkingOrbitEntry);

        // Obtain reference to labels we will be editing in code
        final TextView warningMessageDisplay = (TextView) calcView.findViewById(R.id.warningMessageDisplay);
        final TextView phaseAngleDisplay = (TextView) calcView.findViewById(R.id.phaseAngleDisplay);
        final TextView ejectionAngleDisplay = (TextView) calcView.findViewById(R.id.ejectionAngleDisplay);
        final TextView ejectionVelocityDisplay= (TextView) calcView.findViewById(R.id.ejectionVelocityDisplay);
        final TextView ejectionBurnDeltaVDisplay = (TextView) calcView.findViewById(R.id.ejectionBurnDeltaVDisplay);

        // Set array adapters to populate spinners with choices
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_spinner_item, planets);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        origin.setAdapter(adapter);
        destination.setAdapter(adapter);

        // Set onClick behaviors for layout buttons
        calculateButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                boolean flag = false;
                String warnings = "";
                int parkingOrbit = -1;

                String orig = origin.getSelectedItem().toString();
                String dest = destination.getSelectedItem().toString();

                if (orig.equals(dest)) {
                    warnings += getString(R.string.planet_selection_warning);
                    flag = true;
                }

                try {
                    parkingOrbit = Integer.parseInt(parkingOrbitEntry.getText().toString());
                }
                catch (NumberFormatException e) {
                    warnings += getString(R.string.parking_orbit_input_warning);
                    flag = true;
                }

                warningMessageDisplay.setText(warnings);

                if (flag) {
                    return;
                }

                System.out.println(orig);
                System.out.println(dest);
                System.out.println("Parking orbit: " + parkingOrbit);

            }
        });
        resetButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                phaseAngleDisplay.setText("");
                ejectionAngleDisplay.setText("");
                ejectionVelocityDisplay.setText("");
                ejectionBurnDeltaVDisplay.setText("");
                parkingOrbitEntry.setText("");
                warningMessageDisplay.setText("");
            }
        });


        return calcView;
    }
}
