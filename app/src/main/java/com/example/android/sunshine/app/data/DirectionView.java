package com.example.android.sunshine.app.data;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;

import com.example.android.sunshine.app.R;

/**
 * Created by Laurie on 3/23/2017.
 */

public class DirectionView extends View {

    static private int MaxWidth = 100;
    static private int MaxHeight = 100;

    private Path mCircle;
    private Path mDirection;
    private Paint mPaint;
    private float mDegrees = -1;
    private String mDirStr;

    //constructor through code
    public DirectionView(Context context) {
        super(context);
        initCanvas();
    }

    //constructor through resources
    public DirectionView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initCanvas();
    }

    //constructor through inflation
    public DirectionView(Context context, AttributeSet attrs, int defaultStyle) {
        super(context, attrs, defaultStyle);
        initCanvas();
    }

    private void initCanvas() {
        //initialize drawings when drawings gets complex so it reduces the chance of
        //having poor performance and sluggish UI
        mCircle = new Path();
        mDirection = new Path();
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    public void setDirection(float degrees) {
        mDegrees = degrees;
        invalidate();

        final AccessibilityManager accessibilityManager = (AccessibilityManager)
                getContext().getSystemService(Context.ACCESSIBILITY_SERVICE);

        if (accessibilityManager.isEnabled()) {
            sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED);
        }
    }

    private void setCirclePaint() {
        int color = getResources().getColor(R.color.sunshine_light_blue);
        mPaint.setColor(color);
        mPaint.setStyle(Paint.Style.FILL);
    }

    private void buildCirclePath() {
        int width = getWidth();
        int height = getHeight();

//        int color = getResources().getColor(R.color.sunshine_light_blue);

//        mPaint.setColor(color);
//        mPaint.setStyle(Paint.Style.FILL);
        mCircle.addCircle(width / 2, height / 2, Math.min(width, height) / 2, Path.Direction.CCW);
    }

    private void buildDirectionPath() {

        int width = getWidth();
        int height = getHeight();

        mPaint.setColor(Color.WHITE);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(5);

        mDirection.reset();
        mDirection.moveTo(width / 2, height / 2);

        if (mDegrees >= 337.5 || mDegrees < 22.5) {
            mDirStr = "compass direction North";
            mDirection.lineTo(width / 2, 0);
        } else if (mDegrees >= 22.5 && mDegrees < 67.5) {
            mDirStr = "compass direction North East";
            mDirection.lineTo(width, 0);
        } else if (mDegrees >= 67.5 && mDegrees < 112.5) {
            mDirStr = "compass direction East";
            mDirection.lineTo(width, height / 2);
        } else if (mDegrees >= 112.5 && mDegrees < 157.5) {
            mDirStr = "compass direction South East";
            mDirection.lineTo(width, height);
        } else if (mDegrees >= 157.5 && mDegrees < 202.5) {
            mDirStr = "compass direction South";
            mDirection.lineTo(width / 2, height);
        } else if (mDegrees >= 202.5 && mDegrees < 247.5) {
            mDirStr = "compass direction South West";
            mDirection.lineTo(0, height);
        } else if (mDegrees >= 247.5 && mDegrees < 292.5) {
            mDirStr = "compass direction West";
            mDirection.lineTo(0, height / 2);
        } else if (mDegrees >= 292.5 || mDegrees < 22.5) {
            mDirStr = "compass direction North West";
            mDirection.lineTo(0, 0);
        }

    }

    @Override
    protected void onMeasure(int wMeasureSpec, int hMeasureSpec) {
        super.onMeasure(wMeasureSpec, hMeasureSpec);
        int wMode = MeasureSpec.getMode(wMeasureSpec);
        int wSize = MeasureSpec.getSize(wMeasureSpec);
        int width = MaxWidth;

        if (wMode == MeasureSpec.EXACTLY) {
            width = wSize;
        } else if (wMode == MeasureSpec.EXACTLY) {
            width = (wSize < MaxWidth) ? wSize : MaxWidth;
        }

        int hMode = MeasureSpec.getMode(hMeasureSpec);
        int hSize = MeasureSpec.getSize(hMeasureSpec);
        int height = MaxHeight;
        if (hMode == MeasureSpec.EXACTLY) {
            height = hSize;
        } else if (hMode == MeasureSpec.EXACTLY) {
            height = (hSize < MaxHeight) ? hSize : MaxHeight;
        }

        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //mDegrees is negative when view is first constructed
        if (mDegrees != -1) {
            //set the paint for circle
            setCirclePaint();
            //if mCircle object is not drawn
            if (mCircle.isEmpty()) {
                //draw the mCircle object
                buildCirclePath();
            }
            //draw the pattern of mCircle and mPaint(for the circle) onto screen
            canvas.drawPath(mCircle, mPaint);
            //set the mDirection and mPaint object for the line
            buildDirectionPath();
            //draw the mDirection and mPaint onto screen
            canvas.drawPath(mDirection, mPaint);

        }
    }

    @Override
    public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent ev) {
        ev.getText().add(mDirStr);
        return true;
    }
}

