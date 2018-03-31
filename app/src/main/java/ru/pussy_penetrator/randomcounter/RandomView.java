package ru.pussy_penetrator.randomcounter;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;

import java.util.Random;

/**
 * Created by Sex_predator on 09.12.2016.
 */
public class RandomView extends View {

    //private final long  ANIMATION_TIME    = 25_000; //25s
    private final long  SHADING_TIME      = 10_000; //10s
    //private final int   MIN_SPIN_COUNT    = 100;
    private final float VERTICAL_DISTANCE = 20;

    private final int[] COLORS = new int[] {Color.rgb(200, 0, 0), Color.rgb(0, 150, 0),
                                            getResources().getColor(R.color.colorPrimary),
                                            Color.rgb(30, 30, 30),
                                            //Color.rgb(255, 255, 0),
                                            getResources().getColor(R.color.colorAccent),
                                            Color.rgb(255, 100, 0)};

    private long mAnimationTime = 25_000; //25s
    private int  mMinSpinCount  = 100;

    private int[] mNumberColors;

    private Paint mTextPaint;
    private Paint mLinePaint;
    private Paint mTrianglePaint;
    private Paint mShadePaint;

    private Interpolator mInterpolator = new DecelerateInterpolator(1.5f);
    private Path mTrianglePath;

    private long mStartTime = 0;
    private float   mTextHeight;
    private int     mSpinCount;
    private boolean mAnimationEnded;

    private int mRandomNumber   = -1;
    private int mRandomMaxBound = -1;

    private AnimationListener mAnimationListener;

    public RandomView(Context context) {
        super(context);
        initPaints();
    }

    public RandomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaints();
    }

    public RandomView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaints();
    }

    public void setAnimationTime(long animationTime) {
        mAnimationTime = animationTime;
    }

    public void setMinSpinCount(int minSpinCount) {
        mMinSpinCount = minSpinCount;
    }

    public void setAnimationListener(
            AnimationListener animationListener) {
        mAnimationListener = animationListener;
    }

    private void initPaints() {
        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setColor(getResources().getColor(R.color.colorAccent));
        mTextPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));

        mLinePaint = new Paint();
        mLinePaint.setAntiAlias(true);
        mLinePaint.setColor(Color.BLACK);
        mLinePaint.setStrokeWidth(1.5f);

        mTrianglePaint = new Paint();
        mTrianglePaint.setAntiAlias(true);
        mTrianglePaint.setColor(Color.BLACK);

        mShadePaint = new Paint();
        mShadePaint.setAntiAlias(true);
        mShadePaint.setStyle(Paint.Style.FILL);
        mShadePaint.setColor(Color.BLACK);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        if (mRandomMaxBound != -1)
            setDesirableTextSize(String.valueOf(mRandomMaxBound));
    }

    private long getTime() {
        return System.nanoTime() / 1_000_000;
    }

    public void setRandomNumber(int randomNumber, int randomMaxBound) {
        mRandomNumber = randomNumber;
        mRandomMaxBound = randomMaxBound;
        setDesirableTextSize(String.valueOf(mRandomMaxBound));

        //init random colors
        Random random = new Random();
        mNumberColors = new int[mRandomMaxBound];
        for (int i = 0; i < mRandomMaxBound; i++) {
            int color = COLORS[random.nextInt(COLORS.length)];
            while (i != 0 && color == mNumberColors[i - 1])
                color = COLORS[random.nextInt(COLORS.length)];

            mNumberColors[i] = color;
        }

        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mRandomNumber == -1)
            return;

        if (mStartTime == 0) {
            if (mAnimationEnded) {
                drawAnimation(canvas, 1);
                drawShade(canvas, 1);
            } else
                drawAnimation(canvas, 0);
            return;
        }

        long time = getTime() - mStartTime;

        if (time <= mAnimationTime) {
            drawAnimation(canvas, mInterpolator.getInterpolation((float) time / mAnimationTime));
            invalidate();
            return;
        } else {
            //end of animation
            drawAnimation(canvas, 1);

            if (mAnimationListener != null) {
                mAnimationListener.onAnimationEnd();
                mAnimationListener = null;
            }
        }

        //shading
        time -= mAnimationTime;
        if (time <= SHADING_TIME) {
            drawShade(canvas, (float) time / SHADING_TIME);
            invalidate();
        } else {
            //end of shading
            drawShade(canvas, 1);
            mAnimationEnded = true;
            mStartTime = 0;
        }
    }

    public void startAnimation() {
        //count spin
        mSpinCount = (mMinSpinCount / mRandomMaxBound + 1) * mRandomMaxBound + mRandomNumber;

        //start
        mAnimationEnded = false;
        mStartTime = getTime();
        invalidate();
    }

    private void drawShade(Canvas canvas, float factor) {
        float width = canvas.getWidth();
        float height = canvas.getHeight();
        float yUp = (height - mTextHeight - VERTICAL_DISTANCE) / 2;
        float yDown = (height + mTextHeight + VERTICAL_DISTANCE) / 2;

        mShadePaint.setAlpha((int) (factor * 255));
        canvas.drawRect(0, 0, width, yUp, mShadePaint);
        canvas.drawRect(0, yDown, width, height, mShadePaint);
    }

    private void drawAnimation(Canvas canvas, float factor) {
        float width = canvas.getWidth();
        float height = canvas.getHeight();
        float centerX = width / 2;
        float centerY = height / 2 - (mTextPaint.descent() + mTextPaint.ascent()) / 2;

        //draw all numbers (no optimization)
        float translateY = factor * mSpinCount * mTextHeight;
        float stripeHeight = mRandomMaxBound * mTextHeight;
        translateY -= (int) ((translateY + centerY) / stripeHeight) * stripeHeight;

        for (int i = 1; i <= mRandomMaxBound; i++) {
            //change color
            mTextPaint.setColor(mNumberColors[i - 1]);

            //draw numbers
            float y = centerY + translateY + (i - 1) * mTextHeight;
            canvas.drawText(String.valueOf(i), centerX, y, mTextPaint);

            y = centerY + translateY + (i - 1 - mRandomMaxBound) * mTextHeight;
            canvas.drawText(String.valueOf(i), centerX, y, mTextPaint);
        }

        //draw horizontal lines
        float yUp = (height - mTextHeight - VERTICAL_DISTANCE) / 2;
        float yDown = (height + mTextHeight + VERTICAL_DISTANCE) / 2;
        canvas.drawLine(0, yUp, width, yUp, mLinePaint);
        canvas.drawLine(0, yDown, width, yDown, mLinePaint);

        //draw triangle
        float a = VERTICAL_DISTANCE * 3;

        if (mTrianglePath == null) {
            mTrianglePath = new Path();
            mTrianglePath.moveTo(0, (height - a) / 2);
            mTrianglePath.lineTo((float) (Math.sqrt(3) / 2 * a), height / 2);
            mTrianglePath.lineTo(0, (height + a) / 2);
            mTrianglePath.lineTo(0, (height - a) / 2);
        }

        canvas.drawPath(mTrianglePath, mTrianglePaint);
    }

    private void setDesirableTextSize(String s) {
        float width = getWidth() * 0.9f;
        float height = getHeight() / (float) Math.min(4, mRandomMaxBound);

        if (width == 0 || height == 0) {
            mTextPaint.setTextSize(0);
            return;
        }

        float hi = 600;
        float lo = 2;
        final float threshold = 0.5f; // How close we have to be

        Paint.FontMetrics fontMetrics;
        while ((hi - lo) > threshold) {
            float size = (hi + lo) / 2;
            mTextPaint.setTextSize(size);
            fontMetrics = mTextPaint.getFontMetrics();
            if (mTextPaint.measureText(s) >= width ||
                Math.abs(fontMetrics.ascent) + Math.abs(fontMetrics.descent) >= height)
                hi = size; // too big
            else
                lo = size; // too small
        }
        // Use lo so that we undershoot rather than overshoot
        mTextPaint.setTextSize(lo);

        //set text height
        fontMetrics = mTextPaint.getFontMetrics();
        mTextHeight = Math.abs(fontMetrics.ascent) + Math.abs(fontMetrics.descent);
        mTextHeight += VERTICAL_DISTANCE;
    }

    public interface AnimationListener {

        public void onAnimationEnd();
    }
}
