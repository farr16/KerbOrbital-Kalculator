package com.qwyxand.kerborbitalkalculator;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Matthew on 9/26/2016.
 *
 * Handles drawing the graphical representation of the phase angle between the origin and destination
 * bodies.
 */

public class PhaseAngleDisplayCanvas extends View {

    private Body origin;
    private Body destination;
    private float phaseAngle;

    private float bodyRadius = 7.5f;
    private float y;
    private float x;
    private float minDim;

    private Path originOrbit;
    private Path destinationOrbit;

    Paint orbitPaint;

    public PhaseAngleDisplayCanvas(Context c, AttributeSet attrs) {
        super(c, attrs);

        originOrbit = new Path();
        destinationOrbit = new Path();

        // Setup paint for drawing orbit circles
        orbitPaint = new Paint();
        orbitPaint.setAntiAlias(true);
        orbitPaint.setColor(ContextCompat.getColor(c, R.color.colorOrbitCircles));
        orbitPaint.setStyle(Paint.Style.STROKE);
        orbitPaint.setStrokeWidth(2f);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        x = w/2;
        y = h/2;
        minDim = (h>w) ? w : h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (origin == null || destination == null || phaseAngle == Float.NEGATIVE_INFINITY)
            return;

        originOrbit.reset();
        destinationOrbit.reset();

        float outerRad = minDim / 2 - bodyRadius;
        float minInnerRad = 4 * bodyRadius;
        float innerRad;

        if (origin.sma < destination.sma) {
            destinationOrbit.addCircle(x, y, outerRad, Path.Direction.CW);
            innerRad = origin.sma/destination.sma * outerRad;
            if (innerRad < minInnerRad)
                innerRad = minInnerRad;
            originOrbit.addCircle(x, y, innerRad, Path.Direction.CW);
        }
        else {
            originOrbit.addCircle(x, y, outerRad, Path.Direction.CW);
            innerRad = destination.sma/origin.sma * outerRad;
            if (innerRad < minInnerRad)
                innerRad = minInnerRad;
            originOrbit.addCircle(x, y, innerRad, Path.Direction.CW);
        }

        canvas.drawPath(originOrbit, orbitPaint);
        canvas.drawPath(destinationOrbit, orbitPaint);
    }

    // Setter methods for private variables
    public void setOrigin(Body orig) {origin = orig;}

    public void setDestination(Body dest) {destination = dest;}

    public void setPhaseAngle(float phase) {phaseAngle = phase;}

}
