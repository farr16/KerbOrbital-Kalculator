package com.qwyxand.kerborbitalkalculator;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.v4.content.ContextCompat;
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

    private float y;
    private float x;
    private float minDim;

    private Paint orbitPaint;
    private Paint originPaint;
    private Paint originLabelPaint;
    private Paint labelPaint;
    private Paint angleDisplayPaint;
    private Paint arrowPaint;

    private RectF bounds;

    private Path path;

    public EjectionAngleDisplayCanvas(Context c, AttributeSet attrs) {
        super(c, attrs);

        orbitPaint = new Paint();
        orbitPaint.setAntiAlias(true);
        orbitPaint.setColor(ContextCompat.getColor(c, R.color.colorOrbitCircles));
        orbitPaint.setStyle(Paint.Style.STROKE);
        orbitPaint.setStrokeWidth(2f);

        arrowPaint = new Paint();
        arrowPaint.setAntiAlias(true);
        arrowPaint.setColor(ContextCompat.getColor(c, R.color.colorOrbitCircles)); // Replace with own color later

        originPaint = new Paint();
        originPaint.setAntiAlias(true);

        originLabelPaint = new Paint();
        originLabelPaint.setColor(Color.WHITE);
        originLabelPaint.setTextAlign(Paint.Align.CENTER);

        labelPaint = new Paint();
        labelPaint.setColor(Color.BLACK);
        labelPaint.setTextAlign(Paint.Align.CENTER);

        angleDisplayPaint = new Paint();
        angleDisplayPaint.setAntiAlias(true);
        angleDisplayPaint.setColor(ContextCompat.getColor(c, R.color.colorAngleLines));
        angleDisplayPaint.setStyle(Paint.Style.STROKE);
        angleDisplayPaint.setStrokeWidth(2f);

        bounds = new RectF (0f, 0f, 0f, 0f);

        path = new Path();
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

        // If one of the values required to draw the view isn't set up, return without drawing
        if (origin == null || ejectionAngle == Float.NEGATIVE_INFINITY) {
            canvas.drawText("Ejection angle has not been calculated", x, y, labelPaint);
            return;
        }

        float angleRad = minDim/2;

        /* This float stores the angle (in degrees) used when drawing the angle to the screen, as
         * opposed to the variable ejectionAngle which stores angle from the origin's prograde or
         * retrograde  */
        float ejectDisplayAngle;

        float textAngleOffset;

        if (inner) {
            ejectDisplayAngle = ejectionAngle-90;
            textAngleOffset = 270 + ejectionAngle/2;
            //Draw first line in arc display in direction of origin's prograde vector
            canvas.drawLine(x, y-angleRad, x, y, angleDisplayPaint);
        }
        else {
            ejectDisplayAngle = ejectionAngle+90;
            textAngleOffset = 90 + ejectionAngle/2;
            //draw first line in arc display in direction of origin's retrograde vector
            canvas.drawLine(x, y+angleRad, x, y, angleDisplayPaint);
        }
        float endPointX = x + (float) Math.cos(Math.toRadians(ejectDisplayAngle)) * angleRad;
        float endPointY = y + (float) Math.sin(Math.toRadians(ejectDisplayAngle)) * angleRad;
        canvas.drawLine(x, y, endPointX, endPointY, angleDisplayPaint);

        angleRad *= 0.66f;
        bounds.set(x-angleRad, y-angleRad, x+angleRad, y+angleRad);
        canvas.drawArc(bounds, ejectDisplayAngle, -ejectionAngle, false, angleDisplayPaint);

        angleRad *= 1.3f;
        float textX = x + (float) Math.cos(Math.toRadians(textAngleOffset)) * angleRad;
        float textY = y + (float) Math.sin(Math.toRadians(textAngleOffset)) * angleRad;
        canvas.drawText(ejectionAngle + "°", textX, textY, labelPaint);

        float originRad = minDim/8;
        originPaint.setColor(origin.color);
        originPaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(x, y, originRad, originPaint);

        canvas.drawText(origin.name, x, y, originLabelPaint);

        float orbitRad = originRad * 1.75f;
        canvas.drawCircle(x,y,orbitRad, orbitPaint);

        // Draw arrow showing the direction of the ship's orbit around the origin body
        path.reset();
        float arcRadius = orbitRad * 1.25f;
        bounds.set(x-arcRadius, y-arcRadius, x+arcRadius, y+arcRadius);
        float coverage = -30f;

        arrowPaint.setStyle(Paint.Style.STROKE);
        canvas.drawArc(bounds, ejectDisplayAngle - coverage/2, coverage, false, arrowPaint);
        float triCenX = (float) (x + arcRadius*Math.cos(Math.toRadians(ejectDisplayAngle + coverage/2)));
        float triCenY = (float) (y + arcRadius*Math.sin(Math.toRadians(ejectDisplayAngle + coverage/2)));
        float triSize = 10f;
        path.moveTo(triCenX, triCenY - triSize/2);
        path.lineTo(triCenX - triSize/2, triCenY + triSize/2);
        path.lineTo(triCenX + triSize/2, triCenY + triSize/2);
        arrowPaint.setStyle(Paint.Style.FILL);
        canvas.rotate(ejectDisplayAngle + coverage/2, triCenX, triCenY);
        canvas.drawPath(path, arrowPaint);
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
