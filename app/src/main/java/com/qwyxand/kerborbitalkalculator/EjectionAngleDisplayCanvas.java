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
    private Paint shipPaint;

    private RectF bounds;

    private Path trianglePath;

    public EjectionAngleDisplayCanvas(Context c, AttributeSet attrs) {
        super(c, attrs);

        orbitPaint = new Paint();
        orbitPaint.setAntiAlias(true);
        orbitPaint.setColor(ContextCompat.getColor(c, R.color.colorOrbitCircles));
        orbitPaint.setStyle(Paint.Style.STROKE);
        orbitPaint.setStrokeWidth(2f);

        arrowPaint = new Paint();
        arrowPaint.setAntiAlias(true);
        arrowPaint.setColor(ContextCompat.getColor(c, R.color.colorArrowDisplay));

        shipPaint = new Paint();
        shipPaint.setAntiAlias(true);
        shipPaint.setColor(ContextCompat.getColor(c, R.color.colorShipDisplay));

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

        trianglePath = new Path();
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

        // Draw display of the ejection angle in degrees
        angleRad *= 1.3f;
        float textX = x + (float) Math.cos(Math.toRadians(textAngleOffset)) * angleRad;
        float textY = y + (float) Math.sin(Math.toRadians(textAngleOffset)) * angleRad;
        canvas.drawText(ejectionAngle + "°", textX, textY, labelPaint);

        // Draw circle representing the origin body
        float originRad = minDim/8;
        originPaint.setColor(origin.color);
        originPaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(x, y, originRad, originPaint);

        canvas.drawText(origin.name, x, y, originLabelPaint);

        // Draw circle representing player ship's orbit around the origin body
        float orbitRad = originRad * 1.75f;
        canvas.drawCircle(x,y,orbitRad, orbitPaint);

        // Draw triangle representing the player's craft in the view
        float triSize = 15f;
        float triCenX = (float) (x + Math.cos(Math.toRadians(ejectDisplayAngle)) * orbitRad);
        float triCenY = (float) (y +Math.sin(Math.toRadians(ejectDisplayAngle)) * orbitRad);
        drawRotatedTriangle(canvas, triCenX, triCenY, triSize, ejectDisplayAngle, shipPaint);

        // Draw arrow showing the direction of the ship's orbit around the origin body
        float arcRadius = orbitRad * 1.25f;
        bounds.set(x-arcRadius, y-arcRadius, x+arcRadius, y+arcRadius);
        float coverage = -30f;
        // Draw arrow arc
        arrowPaint.setStyle(Paint.Style.STROKE);
        canvas.drawArc(bounds, ejectDisplayAngle - coverage/2, coverage, false, arrowPaint);
        triCenX = (float) (x + arcRadius*Math.cos(Math.toRadians(ejectDisplayAngle + coverage/2)));
        triCenY = (float) (y + arcRadius*Math.sin(Math.toRadians(ejectDisplayAngle + coverage/2)));
        triSize = 10f;
        // Draw arrow head
        arrowPaint.setStyle(Paint.Style.FILL);
        drawRotatedTriangle(canvas, triCenX, triCenY, triSize, ejectDisplayAngle+coverage/2, arrowPaint);
    }

    /**
     * drawRotatedTriangle
     *
     * Helper method for drawing a rotated triangle, used to draw the triangle representing the player
     * ship and the head of the arrow representing the direction of the ship's orbit
     *
     * @param c canvas on which the triangle is drawn
     * @param x x coordinate of the center-point of the triangle
     * @param y y coordinate of the center-point of the triangle
     * @param size half the height of the triangle
     * @param angle angle the triangle should be rotated by
     * @param paint paint used to draw the triangle, must be a fill-styled paint
     */
    private void drawRotatedTriangle(Canvas c, float x, float y, float size, float angle, Paint paint) {
        trianglePath.reset();

        trianglePath.moveTo(x, y - size/2);
        trianglePath.lineTo(x - size/2, y + size/2);
        trianglePath.lineTo(x + size/2, y + size/2);
        c.rotate(angle, x, y);
        c.drawPath(trianglePath, paint);
        c.rotate(-angle, x, y);
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
