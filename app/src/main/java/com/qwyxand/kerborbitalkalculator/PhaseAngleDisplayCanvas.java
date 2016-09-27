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
    private Path kerbolDraw;
    private Path originDraw;
    private Path destinationDraw;

    private Paint orbitPaint;
    private Paint kerbolPaint;
    private Paint originPaint;
    private Paint destinationPaint;

    public PhaseAngleDisplayCanvas(Context c, AttributeSet attrs) {
        super(c, attrs);

        // Setup path variables which will store draw shapes
        originOrbit = new Path();
        destinationOrbit = new Path();
        kerbolDraw = new Path();
        originDraw = new Path();
        destinationDraw = new Path();

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

        // Setup paint for drawing origin and destination (without color)
        originPaint = new Paint();
        originPaint.setAntiAlias(true);
        destinationPaint = new Paint();
        destinationPaint.setAntiAlias(true);

    }

    @Override
    /** onSizeChanged
     * Overridden to store coordinates of the center of the canvas whenever the canvas is resized,
     * and to calculate which dimension is smaller so drawn circles don't exceed the size of the
     * canvas.
     */
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        x = w/2;
        y = h/2;
        minDim = (h>w) ? w : h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // If one of the values required to draw the view isn't set up, return without drawing
        if (origin == null || destination == null || phaseAngle == Float.NEGATIVE_INFINITY)
            return;

        // Reset paths so any previous contents aren't drawn again
        kerbolDraw.reset();
        originOrbit.reset();
        destinationOrbit.reset();
        originDraw.reset();
        destinationDraw.reset();

        // TODO: Replace with different sizes for different display densities/sizes
        float bodyRadius = 10f;

        // Draw the central body in the center of the screen
        kerbolDraw.addCircle(x, y, bodyRadius, Path.Direction.CW);

        float outerRad = minDim / 2 - bodyRadius;
        float minInnerRad = 4 * bodyRadius;

        float origRad;
        float destRad;

        // Setup the paths for drawing each orbit circle
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

        originPaint.setAntiAlias(true);
        originPaint.setColor(origin.color);

        destinationPaint.setAntiAlias(true);
        destinationPaint.setColor(destination.color);

        // Place the origin planet along the vertical center of the screen on its orbit
        originDraw.addCircle(x+origRad, y, bodyRadius, Path.Direction.CW);

        // Place the destination planet on its orbit offset from the origin planet by the phase angle
        float destX = (float) (x + Math.cos(Math.toRadians(phaseAngle))*destRad);
        float destY = (float) (y - Math.sin(Math.toRadians(phaseAngle))*destRad);
        destinationDraw.addCircle(destX, destY, bodyRadius, Path.Direction.CW);
        
        canvas.drawPath(originOrbit, orbitPaint);
        canvas.drawPath(destinationOrbit, orbitPaint);
        canvas.drawPath(kerbolDraw, kerbolPaint);
        canvas.drawPath(originDraw, originPaint);
        canvas.drawPath(destinationDraw, destinationPaint);
    }

    // Setter methods for private variables
    public void setOrigin(Body orig) {origin = orig;}

    public void setDestination(Body dest) {destination = dest;}

    public void setPhaseAngle(float phase) {phaseAngle = phase;}

}
