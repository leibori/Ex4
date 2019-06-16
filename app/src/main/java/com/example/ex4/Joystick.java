package com.example.ex4;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

public class Joystick extends SurfaceView implements SurfaceHolder.Callback, View.OnTouchListener {

    private float centerX;
    private float centerY;
    private float baseRadius;
    private float topRadius;
    private JoystickListener joystickCallback;

    private void setupDimestions() {
        centerX = getWidth() / 2;
        centerY = getHeight() / 2;
        baseRadius = Math.min(getWidth(), getHeight()) / 3;
        topRadius = Math.min(getWidth(), getHeight()) / 5;
    }

    public Joystick(Context context) {
        super(context);
        getHolder().addCallback(this);
        setOnTouchListener(this);
        if (context instanceof JoystickListener)
            joystickCallback = (JoystickListener) context;
    }

    public Joystick(Context context, AttributeSet attributes) {
        super(context, attributes);
        getHolder().addCallback(this);
        setOnTouchListener(this);
    }

    public Joystick(Context context, AttributeSet attributes, int style) {
        super(context, attributes, style);
        getHolder().addCallback(this);
        setOnTouchListener(this);
    }

    private void drawJoystick(float newX, float newY) {
        if (getHolder().getSurface().isValid()) {
            Canvas myCanvas = this.getHolder().lockCanvas();
            Paint colors = new Paint();
            myCanvas.drawColor(Color.CYAN, PorterDuff.Mode.CLEAR);
            colors.setColor(Color.GRAY);
            myCanvas.drawCircle(centerX, centerY, baseRadius, colors);
            colors.setColor(Color.GREEN);
            myCanvas.drawCircle(newX, newY, topRadius, colors);
            getHolder().unlockCanvasAndPost(myCanvas);
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        setupDimestions();
        drawJoystick(centerX, centerY);
    }

    public boolean onTouch(View view, MotionEvent event) {
        if (view.equals(this)) {
            if (event.getAction() != event.ACTION_UP) {
                float displacement = (float) Math.sqrt(Math.pow(event.getX() - centerX, 2) +
                        Math.pow(event.getY() - centerY, 2));
                if (displacement <= baseRadius)
                    drawJoystick(event.getX(), event.getY());
                else {
                    float ratio = baseRadius / displacement;
                    float constrainedX = centerX + (event.getX() - centerX) * ratio;
                    float constrainedY = centerY + (event.getY() - centerY) * ratio;
                    drawJoystick(constrainedX, constrainedY);
                    joystickCallback.OnJoystickMoved((constrainedX - centerX) / baseRadius,
                            (constrainedY - centerY) / baseRadius, getId());
                }
            } else
                drawJoystick(centerX, centerY);
        }
        return true;
    }

    public interface JoystickListener {
        void OnJoystickMoved(float xPrecent, float yPrecent, int id);
    }
}
