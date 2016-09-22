package com.qwyxand.kerborbitalkalculator;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.math.BigDecimal;
import java.math.RoundingMode;

/** CalculatorFragment
 * Created by Matthew on 9/2/2016.
 *
 * This fragment will contain code for initializing GUI elements for the calculator interface and
 * getting input from user input about the orbital transfer to be performed.
 */
public class CalculatorFragment extends Fragment implements View.OnClickListener{

    Body[] bodies;

    private Spinner origin;
    private Spinner destination;
    private EditText parkingOrbitEntry;

    private TextView warningMessageDisplay;
    private TextView phaseAngleDisplay;
    private TextView ejectionAngleDisplay;
    private TextView ejectionVelocityDisplay;
    private TextView ejectionBurnDeltaVDisplay;

    OnCalculationListener mCallback;

    public interface OnCalculationListener {
        void onCalculation(String orig, String dest, float phase, float eject, boolean inner, int origColor, int destColor);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View calcView = inflater.inflate(R.layout.fragment_calculator, container, false);

        String[] planets = {"Moho", "Eve", "Kerbin", "Duna", "Dres", "Jool", "Eeloo"};
        bodies = initBodies();

        // Obtain references to layout spinners in code
        origin = (Spinner) calcView.findViewById(R.id.originSelector);
        destination = (Spinner) calcView.findViewById(R.id.destinationSelector);

        // Obtain references to layout buttons in code
        final Button calculateButton = (Button) calcView.findViewById(R.id.calculateButton);
        final Button resetButton = (Button) calcView.findViewById(R.id.resetButton);

        // Obtain reference to layout EditText in code
        parkingOrbitEntry = (EditText) calcView.findViewById(R.id.parkingOrbitEntry);

        // Obtain reference to labels we will be editing in code
        warningMessageDisplay = (TextView) calcView.findViewById(R.id.warningMessageDisplay);
        phaseAngleDisplay = (TextView) calcView.findViewById(R.id.phaseAngleDisplay);
        ejectionAngleDisplay = (TextView) calcView.findViewById(R.id.ejectionAngleDisplay);
        ejectionVelocityDisplay= (TextView) calcView.findViewById(R.id.ejectionVelocityDisplay);
        ejectionBurnDeltaVDisplay = (TextView) calcView.findViewById(R.id.ejectionBurnDeltaVDisplay);

        // Set array adapters to populate spinners with choices
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_spinner_item, planets);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        origin.setAdapter(adapter);
        destination.setAdapter(adapter);

        calculateButton.setOnClickListener(this);
        resetButton.setOnClickListener(this);

        return calcView;
    }

    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.calculateButton){
            boolean flag = false;
            String warnings = "";
            int parkingOrbit = -1;

            // Get user inputs for the origin and destination
            int origIndex = origin.getSelectedItemPosition();
            int destIndex = destination.getSelectedItemPosition();

            if (origIndex == destIndex) {
                warnings += getString(R.string.planet_selection_warning);
                flag = true;
            }

            // Get user input for the parking orbit radius
            try {
                parkingOrbit = Integer.parseInt(parkingOrbitEntry.getText().toString());
            }
            catch (NumberFormatException e) {
                warnings += getString(R.string.parking_orbit_input_warning);
                flag = true;
            }

            warningMessageDisplay.setText(warnings);

            // If planet inputs or parking orbit inputs aren't valid, don't perform calculations
            if (flag) {
                return;
            }

            performCalculations(origIndex, destIndex, parkingOrbit);
        }
        else if (id == R.id.resetButton) {
            phaseAngleDisplay.setText("");
            ejectionAngleDisplay.setText("");
            ejectionVelocityDisplay.setText("");
            ejectionBurnDeltaVDisplay.setText("");
            parkingOrbitEntry.setText("");
            warningMessageDisplay.setText("");
        }
    }

    private void performCalculations(int origIndex, int destIndex, int parkingOrbit) {
        Body orig = bodies[origIndex];
        Body dest = bodies[destIndex];
        Body cent = bodies[7];

        // Calculate the phase angle between the two planets at the start of the maneuver
        double tTransfer = Math.PI * Math.sqrt( Math.pow(orig.sma + dest.sma, 3) / (8 * cent.mu) );
        double angTravel = Math.sqrt(cent.mu/dest.sma) * tTransfer/dest.sma * 180/Math.PI;
        double phase = (180 - angTravel) % 360;

        // Calculate the ejection velocity
        double parkR = orig.radius + parkingOrbit; // Radius of parking orbit from center of origin
        // Distance from center of orbital system on point of exit from origin's sphere of influence
        float exitR = orig.sma + orig.soi;
        // Sphere of influence exit velocity
        double exitV = Math.sqrt(cent.mu/exitR) * (Math.sqrt(2*dest.sma / (exitR+dest.sma)) - 1);
        double ejectVNum = parkR * (orig.soi*exitV*exitV - 2*orig.mu) + 2*orig.soi*orig.mu;
        double ejectVDen = parkR * orig.soi;
        double ejectV = Math.sqrt(ejectVNum/ejectVDen);

        // Calculate the deltaV required for the exit burn
        double deltaV = (ejectV - Math.sqrt(orig.mu/parkR));

        // Calculate the angle for the exit burn
        double eta = ejectV * ejectV / 2 - orig.mu / parkR;
        double h = parkR * ejectV;
        double e = Math.sqrt(1 + (2*eta*h*h)/(orig.mu*orig.mu));
        double ejectDeg;
        if (e < 1){
            double a = -orig.mu/(2 * eta);
            double l = a * (1 - e*e);
            double nu = Math.acos((l-orig.soi) / (e*orig.soi));
            double phi = Math.atan2( (e * Math.sin(nu)), (1 + e*Math.cos(nu)));
            ejectDeg = (90 - (phi * 180/Math.PI) + (nu * 180/Math.PI)) % 360;
        }
        else {
            double ejectRad = Math.acos(1/e);
            ejectDeg = (180 - ejectRad * 180/Math.PI) % 360;
        }

        phase = new BigDecimal(phase).setScale(2, RoundingMode.HALF_UP).doubleValue();
        String phaseText = " " + phase;
        phaseAngleDisplay.setText(phaseText);

        ejectV *= 1000; //scale up to account for displaying v in m/s instead of km/s
        ejectV = new BigDecimal(ejectV).setScale(2, RoundingMode.HALF_UP).doubleValue();
        String ejectVText = " " + ejectV;
        ejectionVelocityDisplay.setText(ejectVText);

        deltaV *= 1000; //scale up to account for displaying v in m/s instead of km/s
        deltaV = new BigDecimal(deltaV).setScale(2, RoundingMode.HALF_UP).doubleValue();
        String deltaVText = " " + deltaV;
        ejectionBurnDeltaVDisplay.setText(deltaVText);

        ejectDeg = new BigDecimal(ejectDeg).setScale(2, RoundingMode.HALF_UP).doubleValue();
        String ejectDegText = " " + ejectDeg;
        ejectionAngleDisplay.setText(ejectDegText);

        boolean inner = origIndex < destIndex;

        mCallback.onCalculation(orig.name, dest.name, (float) phase, (float) ejectDeg, inner, orig.color, dest.color);
    }

    @Override
    /** onAttach
     * Fragment lifecycle method called when the fragment is first associated with its activity
     * Overriden to check that the activity the fragment is attached to implements its listener
     */
    public void onAttach(Context context) {
        super.onAttach(context);

        Activity activity = null;

        if (context instanceof Activity)
            activity = (Activity) context;

        try {
            mCallback = (OnCalculationListener) activity;
        }
        catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + "must implement OnCalculationListener");
        }
    }

    /** initBodies
     * Initializes the array containing information about each of the bodies in the Kerbol system
     * used in the orbital transfer calculations.
     * @return bodies
     */
    private Body[] initBodies() {
        Body[] bodies = new Body[8];
        Context context = getContext();
        bodies[0] = new Body("Moho", 168.60938f, 5263138.304f, 250, 9646.630f, ContextCompat.getColor(context, R.color.colorMohoDisplay));
        bodies[1] = new Body("Eve", 8171.7302f, 9832684.544f, 700, 85109.365f, ContextCompat.getColor(context, R.color.colorEveDisplay));
        bodies[2] = new Body("Kerbin", 3531.6f, 13599840.256f, 600, 84159.286f, ContextCompat.getColor(context, R.color.colorKerbinDisplay));
        bodies[3] = new Body("Duna", 301.36321f, 20726155.264f, 320, 47921.949f, ContextCompat.getColor(context, R.color.colorDunaDisplay));
        bodies[4] = new Body("Dres", 21.484489f, 40839358.203f, 138, 32832.840f, ContextCompat.getColor(context, R.color.colorDresDisplay));
        bodies[5] = new Body("Jool", 282528.0f, 68773560.320f, 6000, 2455985.2f, ContextCompat.getColor(context, R.color.colorJoolDisplay));
        bodies[6] = new Body("Eeloo", 74.410815f, 90118820.000f, 210, 119082.94f, ContextCompat.getColor(context, R.color.colorEelooDisplay));
        bodies[7] = new Body("Kerbol", 1172332800f, 0f, 261600, Float.POSITIVE_INFINITY, ContextCompat.getColor(context, R.color.colorKerbolDisplay));

        return bodies;
    }
}

/** Body
 * Stores information about one of the bodies in the Kerbol system (either a planet or the star Kerbol)
 * Class stores final variables, essentially acting as a C struct
 *
 * name stores the name of the body
 * mu stores the body's standard gravitational parameter
 * sma stores the body's semi-major axis
 * radius stores the body's radius
 * soi stores the body's sphere of influence
 * color stores the color the body will be drawn in on the display views
 */
class Body {
    final String name;
    final float mu;
    final float sma;
    final int radius;
    final float soi;
    final int color;

    Body(String _name, float _mu, float _sma, int _radius, float _soi, int _color) {
        name = _name;
        mu = _mu;
        sma = _sma;
        radius = _radius;
        soi = _soi;
        color = _color;
    }
}
