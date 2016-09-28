package com.qwyxand.kerborbitalkalculator;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Matthew on 9/28/2016.
 *
 * Handles drawing the graphical representation of the ejection angle between the craft's ejection
 * burn and the origin body's prograde or retrograde.
 */

public class EjectionAngleDisplayCanvas extends View {

    private Body origin;
    private float ejectionAngle;
    private boolean inner;

    public EjectionAngleDisplayCanvas(Context c, AttributeSet attrs) {
        super(c, attrs);
    }

    public void setOrigin(Body orig) {
        origin = orig;
    }

    public void setEjectionAngle(float eject) {
        ejectionAngle = eject;
    }

    public void setInner(boolean in) {
        inner = in;
    }

}
