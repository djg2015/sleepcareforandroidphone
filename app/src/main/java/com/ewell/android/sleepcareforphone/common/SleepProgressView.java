package com.ewell.android.sleepcareforphone.common;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by lillix on 7/27/16.
 */
public class SleepProgressView extends View {


    private float maxCount;
    private float deepsleepCount;
    private float lightsleepCount;
    private float awakeCount;
    private String score;
    private Paint mPaint;
    private Paint mTextPaint;
    private Paint mSubtextPaint;
    private int mWidth, mHeight;


    private int circle_purple = Color.parseColor("#5c82f5");
    private int circle_green = Color.parseColor("#4be0d3");
    private int circle_lightgreen = Color.parseColor("#7df93c");
    private int circle_blue = Color.parseColor("#56a3fd");
    private int circle_default = Color.parseColor("#44f5f5f5");

    public SleepProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public SleepProgressView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SleepProgressView(Context context) {
        this(context, null);
    }

    private void init(Context context) {
        mPaint = new Paint();
        mTextPaint = new Paint();
        mSubtextPaint = new Paint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        initPaint();

       // mTextPaint.setTextSize(48);
        canvas.drawText(score, mWidth / 2, mHeight / 2, mTextPaint);
        canvas.drawText("睡眠得分", mWidth / 2, mHeight / 2+40, mSubtextPaint);


        RectF rectBlackBg = new RectF(10, 10, mWidth - 10, mHeight - 10);
        canvas.drawArc(rectBlackBg, 0, 360, false, mPaint);

        float section1 = deepsleepCount / maxCount;
        float section2 = lightsleepCount/maxCount;
        float section3 = awakeCount/maxCount;
        mPaint.setColor(circle_purple);
        canvas.drawArc(rectBlackBg, 90, section1 * 360, false, mPaint);
        mPaint.setColor(circle_green);
        canvas.drawArc(rectBlackBg, 90+section1 * 360, section2 * 360, false, mPaint);
        mPaint.setColor(circle_lightgreen);
        canvas.drawArc(rectBlackBg, 90+section1 * 360+section2*360, section3 * 360, false, mPaint);
mPaint.setColor(circle_default);
        canvas.drawArc(rectBlackBg, 90+section1 * 360+section2*360+section3*360,  360, false, mPaint);

    }

    private void initPaint() {
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth((float) 16.0);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setColor(Color.TRANSPARENT);

        mTextPaint.setAntiAlias(true);
        mTextPaint.setStrokeWidth((float) 5.0);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setTextSize(48);
        mTextPaint.setColor(circle_blue);

        mSubtextPaint.setAntiAlias(true);
        mSubtextPaint.setStrokeWidth((float) 3.0);
        mSubtextPaint.setTextAlign(Paint.Align.CENTER);
        mSubtextPaint.setTextSize(18);
        mSubtextPaint.setColor(Color.GRAY);

    }

    private int dipToPx(int dip) {
        float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dip * scale + 0.5f * (dip >= 0 ? 1 : -1));
    }





    public void setScore(String score) {
        this.score = score;
        invalidate();
    }


    public void setMaxCount(float maxCount) {

        this.maxCount = maxCount;
    }

    public void setDeepsleepCount(float deepsleepCount) {
        this.deepsleepCount = deepsleepCount;
       // invalidate();
    }
    public void setLightsleepCount(float lightsleepCount) {
        this.lightsleepCount = lightsleepCount;
       // invalidate();
    }
    public void setAwakeCount(float awakeCount) {
        this.awakeCount = awakeCount;
      //  invalidate();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);

        if (heightSpecMode == MeasureSpec.AT_MOST
                || heightSpecMode == MeasureSpec.UNSPECIFIED) {
            mHeight = dipToPx(15);
        }

        else {
          	mHeight = heightSpecSize;
          //  mHeight = mWidth;

        }

        if (widthSpecMode == MeasureSpec.EXACTLY
                || widthSpecMode == MeasureSpec.AT_MOST) {
            mWidth = mHeight;

        } else {
            mWidth = 0;
        }


        setMeasuredDimension(mWidth, mHeight);
    }
}
