package com.qwyxand.kerborbitalkalculator;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/** CalculatorFragment
 * Created by Matthew on 9/2/2016.
 *
 * This fragment will contain code for initializing GUI elements for the calculator interface and
 * getting input from user input about the orbital transfer to be performed.
 */
public class CalculatorFragment extends Fragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_calculator, container, false);
    }
}
