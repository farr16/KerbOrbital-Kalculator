package com.qwyxand.kerborbitalkalculator;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
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
    private Path angleDisplayLines;

    private Paint orbitPaint;
    private Paint kerbolPaint;
    private Paint originPaint;
    private Paint destinationPaint;
    private Paint angleDisplayPaint;
    private Paint textPaint;

    public PhaseAngleDisplayCanvas(Context c, AttributeSet attrs) {
        super(c, attrs);

        // Setup path variables which will store draw shapes
        originOrbit = new Path();
        destinationOrbit = new Path();
        kerbolDraw = new Path();
        originDraw = new Path();
        destinationDraw = new Path();
        angleDisplayLines = new Path();

        // Setup paint for drawing orbit circles
        orbitPaint = new Paint();
        orbitPaint.setAntiAlias(true);
        orbitPaint.setColor(ContextCompat.getColor(c, R.color.colorOrbitCircles));
        orbitPaint.setStyle(Paint.Style.STROKE);
        orbitPaint.setStrokeWidth(2f);

        // Setup paint for drawing angle lines
        angleDisplayPaint = new Paint();
        angleDisplayPaint.setAntiAlias(true);
        angleDisplayPaint.setColor(ContextCompat.getColor(c, R.color.colorAngleLines));
        angleDisplayPaint.setStyle(Paint.Style.STROKE);
        angleDisplayPaint.setStrokeWidth(2f);

        // Setup paint for drawing sun
        kerbolPaint = new Paint();
        kerbolPaint.setAntiAlias(true);
        kerbolPaint.setColor(ContextCompat.getColor(c, R.color.colorKerbolDisplay));

        // Setup paint for drawing origin and destination (without color)
        originPaint = new Paint();
        originPaint.setAntiAlias(true);
        destinationPaint = new Paint();
        destinationPaint.setAntiAlias(true);

        textPaint = new Paint();
        textPaint.setColor(Color.BLACK);

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

        // Reset paths so any previous contents aren't drawn again
        kerbolDraw.reset();
        originOrbit.reset();
        destinationOrbit.reset();
        originDraw.reset();
        destinationDraw.reset();
        angleDisplayLines.reset();

        // If one of the values required to draw the view isn't set up, return without drawing
        if (origin == null || destination == null || phaseAngle == Float.NEGATIVE_INFINITY) {
            return;
        }

        // TODO: Replace with different sizes for different display densities/sizes
        float bodyRadius = 10f;

        // Draw the central body in the center of the screen
        kerbolDraw.addCircle(x, y, bodyRadius, Path.Direction.CW);

        float outerRad = minDim/2 - bodyRadius*2;
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

        // Calculate sin and cos for phase angle, which will be used to place the destination planet
        // And to draw the angle display
        float phaseCos = (float) Math.cos(Math.toRadians(phaseAngle));
        float phaseSin = (float) Math.sin(Math.toRadians(phaseAngle));

        // Place the destination planet on its orbit offset from the origin planet by the phase angle
        float destX = (x + phaseCos*destRad);
        float destY = (y - phaseSin*destRad);
        destinationDraw.addCircle(destX, destY, bodyRadius, Path.Direction.CW);

        angleDisplayLines.moveTo(x + phaseCos*minDim/2, y - phaseSin*minDim/2);
        angleDisplayLines.lineTo(x,y);
        angleDisplayLines.lineTo(x + minDim/2, y);

        canvas.drawPath(originOrbit, orbitPaint);
        canvas.drawPath(destinationOrbit, orbitPaint);
        canvas.drawPath(angleDisplayLines, angleDisplayPaint);
        canvas.drawPath(kerbolDraw, kerbolPaint);
        canvas.drawPath(originDraw, originPaint);
        canvas.drawPath(destinationDraw, destinationPaint);
        float offset = (origRad == outerRad) ? -bodyRadius*2 : 0;
        canvas.drawText(origin.name, x+origRad+offset, y+bodyRadius*2, textPaint);
        canvas.drawText("Kerbol", x - bodyRadius, y + bodyRadius*2, textPaint);
        canvas.drawText(destination.name, destX, destY+bodyRadius*2, textPaint);
    }

    // Setter methods for private variables
    public void setOrigin(Body orig) {origin = orig;}

    public void setDestination(Body dest) {destination = dest;}

    public void setPhaseAngle(float phase) {phaseAngle = phase;}

}
