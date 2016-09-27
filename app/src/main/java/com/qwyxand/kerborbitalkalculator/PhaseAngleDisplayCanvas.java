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

    private float y;
    private float x;
    private float minDim;

    private Path originOrbit;
    private Path destinationOrbit;
    private Path kerbol;

    private Paint orbitPaint;
    private Paint kerbolPaint;

    public PhaseAngleDisplayCanvas(Context c, AttributeSet attrs) {
        super(c, attrs);

        originOrbit = new Path();
        destinationOrbit = new Path();
        kerbol = new Path();

        // Setup paint for drawing orbit circles
        orbitPaint = new Paint();
        orbitPaint.setAntiAlias(true);
        orbitPaint.setColor(ContextCompat.getColor(c, R.color.colorOrbitCircles));
        orbitPaint.setStyle(Paint.Style.STROKE);
        orbitPaint.setStrokeWidth(2f);

        // Setup paint for drawing sun
        kerbolPaint = new Paint();
        kerbolPaint.setAntiAlias(true);
        kerbolPaint.setColor(ContextCompat.getColor(c, R.color.colorKerbolDisplay));

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

        kerbol.reset();
        originOrbit.reset();
        destinationOrbit.reset();

        float bodyRadius = 10f;

        kerbol.addCircle(x, y, bodyRadius, Path.Direction.CW);

        float outerRad = minDim / 2 - bodyRadius;
        float minInnerRad = 4 * bodyRadius;

        float origRad;
        float destRad;

        if (origin.sma < destination.sma) {
            destRad = outerRad;
            destinationOrbit.addCircle(x, y, destRad, Path.Direction.CW);
            origRad = origin.sma/destination.sma * destRad;
            if (origRad < minInnerRad)
                origRad = minInnerRad;
            originOrbit.addCircle(x, y, origRad, Path.Direction.CW);
        }
        else {
            origRad = outerRad;
            originOrbit.addCircle(x, y, origRad, Path.Direction.CW);
            destRad = destination.sma/origin.sma * origRad;
            if (destRad < minInnerRad)
                destRad = minInnerRad;
            originOrbit.addCircle(x, y, destRad, Path.Direction.CW);
        }

        canvas.drawPath(originOrbit, orbitPaint);
        canvas.drawPath(destinationOrbit, orbitPaint);
        canvas.drawPath(kerbol, kerbolPaint);
    }

    // Setter methods for private variables
    public void setOrigin(Body orig) {origin = orig;}

    public void setDestination(Body dest) {destination = dest;}

    public void setPhaseAngle(float phase) {phaseAngle = phase;}

}
