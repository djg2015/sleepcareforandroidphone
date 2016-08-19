package com.ewell.android.sleepcareforphone.common;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

/***
 * 自定义圆弧进度条
 *
 * @author liujing
 */
public class ProgressView extends View {

    //分段颜色
    private int currentTextColor = Color.BLACK;
    private float maxCount;
    private float currentCount;
    private int score;
    private Paint mPaint;
    private Paint mTextPaint;
    private Paint mSubtextPaint;
    private int mWidth, mHeight;

    private int innerWidth, innerHeight;
    private Paint innerPaint;
    private float avgCount;
    private int screenwidth;

    private Paint defaultPaint, defaultinnerPaint;

    private int circle_blue = Color.parseColor("#56a3fd");
    private int circle_green = Color.parseColor("#4be0d3");
    private int circle_red = Color.parseColor("#fe4f4a");
    private int circle_purple = Color.parseColor("#c164b1");
    private int circle_default = Color.parseColor("#44f5f5f5");

    private int[] SECTION_COLORS = {circle_blue, circle_purple, circle_red};

private String progressviewType;


    public ProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public ProgressView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProgressView(Context context) {
        this(context, null);
    }

    private void init(Context context) {
        mPaint = new Paint();
        mTextPaint = new Paint();
        mSubtextPaint = new Paint();
        innerPaint = new Paint();
defaultPaint = new Paint();
        defaultinnerPaint = new Paint();


        WindowManager manager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);
        screenwidth = outMetrics.widthPixels;

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        initPaint();
        RectF rectBlackBg = new RectF(25, 25, mWidth - 25, mHeight - 25);
        canvas.drawArc(rectBlackBg, 0, 360, false, mPaint);
        mPaint.setColor(Color.BLACK);
        mTextPaint.setTextSize(50);
        canvas.drawText(score + "", mWidth / 2, mHeight / 2, mTextPaint);
        canvas.drawText("次/分", mWidth / 2, mHeight / 2 + 30, mSubtextPaint);

        float innersection = avgCount / maxCount;
        innerPaint.setColor(circle_green);
        float tempwidth = (screenwidth - 790) / 20.7f;
        RectF innerBg = new RectF(40, 40, innerWidth + tempwidth, innerHeight + tempwidth);
        canvas.drawArc(innerBg, 90, innersection * 360, false, innerPaint);
        canvas.drawArc(innerBg, 90+innersection * 360,  360, false, defaultinnerPaint);

        float section = currentCount / maxCount;
        if (section <= 1.0f / 3.0f) {
            if (section != 0.0f) {
                mPaint.setColor(circle_blue);
            } else {
                mPaint.setColor(Color.TRANSPARENT);
            }
        } else {
            int count = (section <= 1.0f / 3.0f * 2) ? 2 : 3;
            int[] colors = new int[count];
            System.arraycopy(SECTION_COLORS, 0, colors, 0, count);
            float[] positions = new float[count];
            if (count == 2) {
                positions[0] = 0.0f;
                positions[1] = 1.0f - positions[0];
            } else {
                positions[0] = 0.0f;
                positions[1] = (maxCount / 3) / currentCount;
                positions[2] = 1.0f - positions[0] * 2;
            }
            positions[positions.length - 1] = 1.0f;
            LinearGradient shader = new LinearGradient(3, 3, (mWidth - 3) * section,
                    mHeight - 3, colors, null,
                    Shader.TileMode.MIRROR);
            mPaint.setShader(shader);
        }
        canvas.drawArc(rectBlackBg, 90, section * 360, false, mPaint);
        canvas.drawArc(rectBlackBg, 90+section * 360, 360, false, defaultPaint);
    }

    private void initPaint() {
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth((float) 16.0);
        mPaint.setStyle(Style.STROKE);
        mPaint.setStrokeCap(Cap.ROUND);
        mPaint.setColor(Color.TRANSPARENT);

        mTextPaint.setAntiAlias(true);
        mTextPaint.setStrokeWidth((float) 5.0);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setTextSize(50);
        mTextPaint.setColor(currentTextColor);

        mSubtextPaint.setAntiAlias(true);
        mSubtextPaint.setStrokeWidth((float) 3.0);
        mSubtextPaint.setTextAlign(Paint.Align.CENTER);
        mSubtextPaint.setTextSize(18);
        mSubtextPaint.setColor(Color.GRAY);

        innerPaint.setAntiAlias(true);
        innerPaint.setStrokeWidth((float) 10.0);
        innerPaint.setStyle(Style.STROKE);
        innerPaint.setStrokeCap(Cap.ROUND);
        innerPaint.setColor(Color.TRANSPARENT);

        defaultinnerPaint.setAntiAlias(true);
        defaultinnerPaint.setStrokeWidth((float) 10.0);
        defaultinnerPaint.setStyle(Style.STROKE);
        defaultinnerPaint.setStrokeCap(Cap.ROUND);
        defaultinnerPaint.setColor(circle_default);


        defaultPaint.setAntiAlias(true);
        defaultPaint.setStrokeWidth((float) 16.0);
        defaultPaint.setStyle(Style.STROKE);
        defaultPaint.setStrokeCap(Cap.ROUND);
        defaultPaint.setColor(circle_default);
    }

    private int dipToPx(int dip) {
        float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dip * scale + 0.5f * (dip >= 0 ? 1 : -1));
    }

    public int getScore() {
        return score;
    }

    public void setAvgCount(float avgcount) {

        this.avgCount = avgcount;

    }

    public float getMaxCount() {
        return maxCount;
    }

    public float getCurrentCount() {
        return currentCount;
    }

    public void setScore(int score) {
        this.score = score;

       if(progressviewType.equals("心率")) {
           if (score > 80) {
               currentTextColor = circle_red;

           } else if (score >= 40 && score <= 80) {
               currentTextColor = circle_blue;

           } else {
               currentTextColor = circle_red;

           }
       }
        else if(progressviewType.equals("呼吸")){
           if (score > 40) {
               currentTextColor = circle_red;

           } else if (score >= 15 && score <= 40) {
               currentTextColor = circle_blue;

           } else {
               currentTextColor = circle_red;

           }
       }
        invalidate();
    }

    //设置类型:心率或呼吸
    public void setProgressviewType(String type){

        this.progressviewType = type;
    }

    /***
     * 设置最大的进度值
     *
     * @param maxCount
     */
    public void setMaxCount(float maxCount) {
        this.maxCount = maxCount;
    }

    /***
     * 设置当前的进度值
     *
     * @param currentCount
     */
    public void setCurrentCount(float currentCount) {
        this.currentCount = currentCount > maxCount ? maxCount : currentCount;
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);
        if (widthSpecMode == MeasureSpec.EXACTLY
                || widthSpecMode == MeasureSpec.AT_MOST) {
            mWidth = widthSpecSize / 7 * 3;
            innerWidth = mWidth / 10 * 9;

        } else {
            mWidth = 0;
        }
        if (heightSpecMode == MeasureSpec.AT_MOST
                || heightSpecMode == MeasureSpec.UNSPECIFIED) {
            mHeight = dipToPx(15);
        } else {
            //	mHeight = heightSpecSize;
            mHeight = mWidth;
            innerHeight = innerWidth;

        }
        setMeasuredDimension(mWidth, mHeight);
    }

}
