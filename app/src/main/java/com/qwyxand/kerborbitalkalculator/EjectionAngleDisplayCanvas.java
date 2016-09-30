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
    private Paint arcLabelPaint;

    private RectF bounds;

    public EjectionAngleDisplayCanvas(Context c, AttributeSet attrs) {
        super(c, attrs);

        orbitPaint = new Paint();
        orbitPaint.setAntiAlias(true);
        orbitPaint.setColor(ContextCompat.getColor(c, R.color.colorOrbitCircles));
        orbitPaint.setStyle(Paint.Style.STROKE);
        orbitPaint.setStrokeWidth(2f);

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

        arcLabelPaint = new Paint();
        arcLabelPaint.setColor(ContextCompat.getColor(c, R.color.colorAngleLines));
        arcLabelPaint.setTextAlign(Paint.Align.CENTER);

        bounds = new RectF (0f, 0f, 0f, 0f);
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

        if (inner) {
            canvas.drawLine(x, y-angleRad, x, y, angleDisplayPaint);
            float endPointX = x + (float) Math.cos(Math.toRadians(ejectionAngle-90)) * angleRad;
            float endPointY = y + (float) Math.sin(Math.toRadians(ejectionAngle-90)) * angleRad;
            canvas.drawLine(x, y, endPointX, endPointY, angleDisplayPaint);

            angleRad *= 0.66f;
            bounds.set(x-angleRad, y-angleRad, x+angleRad, y+angleRad);
            canvas.drawArc(bounds, ejectionAngle-90, -ejectionAngle, false, angleDisplayPaint);

            angleRad *= 1.2f;
            float textX = x + (float) Math.cos(Math.toRadians(270 + ejectionAngle/2)) * angleRad;
            float textY = y + (float) Math.sin(Math.toRadians(270 + ejectionAngle/2)) * angleRad;
            canvas.drawText(ejectionAngle + "°", textX, textY, arcLabelPaint);
        }
        else {
            canvas.drawLine(x, y+angleRad, x, y, angleDisplayPaint);
            float endPointX = x + (float) Math.cos(Math.toRadians(ejectionAngle+90)) * angleRad;
            float endPointY = y + (float) Math.sin(Math.toRadians(ejectionAngle+90)) * angleRad;
            canvas.drawLine(x, y, endPointX, endPointY, angleDisplayPaint);

            angleRad *= 0.66f;
            bounds.set(x-angleRad, y-angleRad, x+angleRad, y+angleRad);
            canvas.drawArc(bounds, ejectionAngle+90, -ejectionAngle, false, angleDisplayPaint);

            angleRad *= 1.2f;
            float textX = x + (float) Math.cos(Math.toRadians(90 + ejectionAngle/2)) * angleRad;
            float textY = y + (float) Math.sin(Math.toRadians(90 + ejectionAngle/2)) * angleRad;
            canvas.drawText(ejectionAngle + "°", textX, textY, arcLabelPaint);
        }

        float originRad = minDim/8;
        originPaint.setColor(origin.color);
        originPaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(x, y, originRad, originPaint);

        canvas.drawText(origin.name, x, y, originLabelPaint);

        float orbitRad = originRad * 1.75f;
        canvas.drawCircle(x,y,orbitRad, orbitPaint);
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
