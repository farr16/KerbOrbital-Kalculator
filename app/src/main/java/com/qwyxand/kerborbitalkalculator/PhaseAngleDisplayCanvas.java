package com.qwyxand.kerborbitalkalculator;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
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

    private RectF bounds;

    private Paint orbitPaint;
    private Paint parentPaint;
    private Paint originPaint;
    private Paint destinationPaint;
    private Paint angleDisplayPaint;
    private Paint bodyLabelPaint;
    private Paint arcLabelPaint;

    public PhaseAngleDisplayCanvas(Context c, AttributeSet attrs) {
        super(c, attrs);

        // Setup RectF for bounds on the angle display arc with default values
        bounds = new RectF(-1f, -1f, 1f, 1f);

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
         parentPaint = new Paint();
         parentPaint.setAntiAlias(true);
         parentPaint.setColor(ContextCompat.getColor(c, R.color.colorKerbolDisplay));

        // Setup paint for drawing origin and destination (without color)
        originPaint = new Paint();
        originPaint.setAntiAlias(true);
        destinationPaint = new Paint();
        destinationPaint.setAntiAlias(true);

        // Setup paint for drawing text labels
        bodyLabelPaint = new Paint();
        bodyLabelPaint.setColor(Color.BLACK);
        bodyLabelPaint.setTextAlign(Paint.Align.CENTER);
        arcLabelPaint = new Paint();
        arcLabelPaint.setColor(ContextCompat.getColor(c, R.color.colorAngleLines));
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
        if (origin == null || destination == null || phaseAngle == Float.NEGATIVE_INFINITY) {
            canvas.drawText("Phase angle has not been calculated", x, y , bodyLabelPaint);
            return;
        }

        // TODO: Replace with different sizes for different display densities/sizes
        float bodyRadius = 10f;

        float outerRad = minDim/2 - bodyRadius*2;
        float innerRad;
        float minInnerRad = 4 * bodyRadius;

        float origRad;
        float destRad;

        // Determine which radius is smaller, scale that orbit's display size relative to the larger one
        if (origin.sma < destination.sma) {
            destRad = outerRad;
            origRad = origin.sma/destination.sma * destRad;
            if (origRad < minInnerRad)
                origRad = minInnerRad;
            innerRad = origRad;
        }
        else {
            origRad = outerRad;
            destRad = destination.sma/origin.sma * origRad;
            if (destRad < minInnerRad)
                destRad = minInnerRad;
            innerRad = destRad;
        }

        // Draw the origin and destination orbits
        canvas.drawCircle(x, y, destRad, orbitPaint);
        canvas.drawCircle(x, y, origRad, orbitPaint);

        originPaint.setColor(origin.color);
        destinationPaint.setColor(destination.color);

        // Calculate sin and cos for phase angle, which will be used to place the destination planet
        // And to draw the angle display
        float phaseCos = (float) Math.cos(Math.toRadians(phaseAngle));
        float phaseSin = (float) Math.sin(Math.toRadians(phaseAngle));

        // Calculate location along its orbit for the destination planet
        float destX = (x + phaseCos*destRad);
        float destY = (y - phaseSin*destRad);

        // Draw the angle display lines and the angle display arc
        canvas.drawLine(x + phaseCos*minDim/2, y - phaseSin*minDim/2, x, y, angleDisplayPaint);
        float angleRad = (innerRad < outerRad - innerRad) ? (innerRad + outerRad)/2 : innerRad/2;
        bounds.set(x-angleRad, y-angleRad , x+angleRad, y+angleRad);
        canvas.drawLine(x, y, x+minDim/2, y, angleDisplayPaint);
        canvas.drawArc(bounds, 0f, -phaseAngle, false, angleDisplayPaint);

        // Draw the parent, origin, and destination bodies
        canvas.drawCircle(x, y, bodyRadius, parentPaint);
        canvas.drawCircle(x + origRad, y, bodyRadius, originPaint);
        canvas.drawCircle(destX, destY, bodyRadius, destinationPaint);

        // Draw text labels for the origin, parent, and destination bodies
        // If origin is on the outer orbit, offset name tag slightly so it doesn't go off canvas
        float offset = (origRad == outerRad) ? -bodyRadius*2 : 0;
        canvas.drawText(origin.name, x+origRad+offset, y+bodyRadius*2, bodyLabelPaint);
        canvas.drawText("Kerbol", x - bodyRadius, y + bodyRadius*2, bodyLabelPaint);
        canvas.drawText(destination.name, destX, destY+bodyRadius*2, bodyLabelPaint);

        // Draw angle arc display text
        float angleDisplayX = (angleRad*1.1f) * (float) Math.cos(Math.toRadians(phaseAngle/2));
        float angleDisplayY = (angleRad*1.1f) * (float) Math.sin(Math.toRadians(phaseAngle/2));
        canvas.drawText(phaseAngle + "°", x +angleDisplayX,  y - angleDisplayY, arcLabelPaint);
    }

    // Setter methods for private variables
    public void setOrigin(Body orig) {origin = orig;}

    public void setDestination(Body dest) {destination = dest;}

    public void setPhaseAngle(float phase) {phaseAngle = phase;}

}
