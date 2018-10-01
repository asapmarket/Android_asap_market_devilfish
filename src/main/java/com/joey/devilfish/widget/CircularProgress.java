package com.joey.devilfish.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.View;

import com.joey.devilfish.R;

/**
 * 文件描述
 * Date: 2017/7/22
 *
 * @author xusheng
 */

public class CircularProgress extends View {

    /**
     * 默认进度条的半径
     */
    private int circleRadius = 24;
    /**
     * 默认进度条的宽度
     */
    private int barWidth = 3;
    /**
     * 默认进度条的长度
     */
    private final int barLength = 0;
    /**
     * 默认进度条是否追到尾部
     */
    private final int barMaxLength = 365;
    private boolean fillRadius = true;
    private double timeStartGrowing = 0;
    /**
     * 转动一圈所需时间
     */
    private double barSpinCycleTime = 500;
    private float barExtraLength = 0;
    private boolean barGrowingFromFront = true;
    private long pausedTimeWithoutGrowing = 0;
    /**
     * 暂停不进行增量时间 --暂时设置成0
     */
    private final long pauseGrowingTime = 0;
    /**
     * 进度条的默认颜色
     */
    private int barColor = 0xFFAAAAAA;
    private Paint barPaint = new Paint();
    private RectF circleBounds = new RectF();
    /**
     * 进度条的转速
     */
    private float spinSpeed = 500.0f;
    /**
     * 进度条的执行最后一帧动画的时间
     */
    private long lastTimeAnimated = 0;
    /**
     * 当前的进度条的位置
     */
    private float mProgress = 0.0f;
    /**
     * 进度条下一个节点要定位的地方
     */
    private float mTargetProgress = 0.0f;
    /**
     * 是在旋转中？可在外面控制是否需要旋转
     */
    private boolean isSpinning = false;
    /**
     * 回调Callback
     */
    private ProgressCallback callback;

    public CircularProgress(Context context) {
        super(context);
    }

    public CircularProgress(Context context, AttributeSet attrs) {
        super(context, attrs);
        parseAttributes(context.obtainStyledAttributes(attrs,
                R.styleable.ProgressCircular));
    }

    public CircularProgress(Context context, AttributeSet attrs, int paramInt) {
        super(context, attrs, paramInt);
        parseAttributes(context.obtainStyledAttributes(attrs, R.styleable.ProgressCircular));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int viewWidth = circleRadius + this.getPaddingLeft()
                + this.getPaddingRight();
        int viewHeight = circleRadius + this.getPaddingTop()
                + this.getPaddingBottom();
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int width;
        int height;

        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else if (widthMode == MeasureSpec.AT_MOST) {
            width = Math.min(viewWidth, widthSize);
        } else {
            width = viewWidth;
        }
        if (heightMode == MeasureSpec.EXACTLY
                || widthMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else if (heightMode == MeasureSpec.AT_MOST) {
            height = Math.min(viewHeight, heightSize);
        } else {
            height = viewHeight;
        }
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        setupBounds(w, h);
        setupPaints();
        invalidate();
    }

    private void setupPaints() {
        barPaint.setColor(barColor);
        barPaint.setAntiAlias(true);
        barPaint.setStyle(Paint.Style.STROKE);
        barPaint.setStrokeCap(Paint.Cap.ROUND);
        barPaint.setStrokeWidth(barWidth);
    }

    private void setupBounds(int layout_width, int layout_height) {
        int paddingTop = getPaddingTop();
        int paddingBottom = getPaddingBottom();
        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();
        if (!fillRadius) {
            int minValue = Math.min(layout_width - paddingLeft - paddingRight,
                    layout_height - paddingBottom - paddingTop);
            int circleDiameter = (int) Math.min(minValue, circleRadius * 2
                    - barWidth * 2);
            int xOffset = (layout_width - paddingLeft - paddingRight - circleDiameter)
                    / 2 + paddingLeft;
            int yOffset = (layout_height - paddingTop - paddingBottom - circleDiameter)
                    / 2 + paddingTop;
            circleBounds = new RectF(xOffset + barWidth, yOffset + barWidth,
                    xOffset + circleDiameter - barWidth, yOffset
                    + circleDiameter - barWidth);
        } else {
            circleBounds = new RectF(paddingLeft + barWidth, paddingTop
                    + barWidth, layout_width - paddingRight - barWidth,
                    layout_height - paddingBottom - barWidth);
        }
    }

    private void parseAttributes(TypedArray a) {
        circleRadius = (int) a
                .getDimension(
                        R.styleable.ProgressCircular_matProg_circleRadius,
                        circleRadius);
        barWidth = (int) a.getDimension(
                R.styleable.ProgressCircular_matProg_barWidth, barWidth);
        fillRadius = a.getBoolean(
                R.styleable.ProgressCircular_matProg_fillRadius, false);
        float baseSpinSpeed = a.getFloat(
                R.styleable.ProgressCircular_matProg_spinSpeed,
                spinSpeed / 360.0f);
        spinSpeed = baseSpinSpeed * 360;
        barSpinCycleTime = a.getInt(
                R.styleable.ProgressCircular_matProg_barSpinCycleTime,
                (int) barSpinCycleTime);
        barColor = a.getColor(R.styleable.ProgressCircular_matProg_barColor,
                barColor);
        spin();
        a.recycle();
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        boolean mustInvalidate = false;
        if (isSpinning) {
            mustInvalidate = true;
            long deltaTime = (SystemClock.uptimeMillis() - lastTimeAnimated);
            float deltaNormalized = deltaTime * spinSpeed / 1000.0f;
            updateBarLength(deltaTime);
            mProgress += deltaNormalized;
            if (mProgress > 360) {
                mProgress -= 360f;
            }
            lastTimeAnimated = SystemClock.uptimeMillis();
            float from = mProgress - 90;
            float length = barLength + barExtraLength;
            if (isInEditMode()) {
                from = 0;
                length = 135;
            }

            canvas.drawArc(circleBounds, from, length, false, barPaint);
        } else {
            float oldProgress = mProgress;
            if (mProgress != mTargetProgress) {
                mustInvalidate = true;

                float deltaTime = (float) (SystemClock.uptimeMillis() - lastTimeAnimated) / 1000;
                float deltaNormalized = deltaTime * spinSpeed;

                mProgress = Math.min(mProgress + deltaNormalized,
                        mTargetProgress);
                lastTimeAnimated = SystemClock.uptimeMillis();
            }

            if (oldProgress != mProgress) {
                runCallback();
            }

            float offset = 0.0f;
            float progress = mProgress;
            float factor = 2.0f;
            offset = (float) (1.0f - Math.pow(1.0f - mProgress / 360.0f,
                    2.0f * factor)) * 360.0f;
            progress = (float) (1.0f - Math.pow(1.0f - mProgress / 360.0f,
                    factor)) * 360.0f;

            if (isInEditMode()) {
                progress = 360;
            }

            canvas.drawArc(circleBounds, offset - 90, progress, false, barPaint);
        }

        if (mustInvalidate) {
            invalidate();
        }
    }

    @Override
    protected void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);

        if (visibility == VISIBLE) {
            lastTimeAnimated = SystemClock.uptimeMillis();
        }
    }

    private void updateBarLength(long deltaTimeInMilliSeconds) {
        if (pausedTimeWithoutGrowing >= pauseGrowingTime) {
            timeStartGrowing += deltaTimeInMilliSeconds;

            if (timeStartGrowing > barSpinCycleTime) {
                timeStartGrowing -= barSpinCycleTime;
                pausedTimeWithoutGrowing = 0;
                barGrowingFromFront = !barGrowingFromFront;
            }

            float distance = (float) Math.cos((timeStartGrowing
                    / barSpinCycleTime + 1)
                    * Math.PI) / 2 + 0.5f;
            float destLength = (barMaxLength - barLength);

            if (barGrowingFromFront) {
                barExtraLength = distance * destLength;
            } else {
                float newLength = destLength * (1 - distance);
                mProgress += (barExtraLength - newLength);
                barExtraLength = newLength;
            }
        } else {
            pausedTimeWithoutGrowing += deltaTimeInMilliSeconds;
        }
    }

    /**
     * 停止运行
     *
     * @author 刘浩
     * @date 2015年8月28日 下午4:26:41
     */
    public void stopSpinning() {
        isSpinning = false;
        mProgress = 0.0f;
        mTargetProgress = 0.0f;
        invalidate();
    }

    /**
     * 开始运行
     *
     * @author 刘浩
     * @date 2015年8月28日 下午4:26:41
     */
    public void startSpinning() {
        isSpinning = true;
        mProgress = 0.0f;
        mTargetProgress = 0.0f;
        invalidate();
    }

    public void spin() {
        lastTimeAnimated = SystemClock.uptimeMillis();
        isSpinning = true;
        invalidate();
    }

    /**
     * 监听app在windows是否显示，来控制进度条是否刷新 (non-Javadoc)
     *
     * @author 刘浩
     * @date 2015年8月28日 下午4:06:40
     * @see android.view.View#onWindowVisibilityChanged(int)
     */
    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        if (visibility == View.VISIBLE) {
            startSpinning();
        } else {
            stopSpinning();
        }
        super.onWindowVisibilityChanged(visibility);
    }

    private void runCallback() {
        if (callback != null) {
            float normalizedProgress = (float) Math
                    .round(mProgress * 100 / 360.0f) / 100;
            callback.onProgressUpdate(normalizedProgress);
        }
    }

    public void setProgress(float progress) {
        if (isSpinning) {
            mProgress = 0.0f;
            isSpinning = false;
            runCallback();
        }

        if (progress > 1.0f) {
            progress -= 1.0f;
        } else if (progress < 0) {
            progress = 0;
        }

        if (progress == mTargetProgress) {
            return;
        }
        if (mProgress == mTargetProgress) {
            lastTimeAnimated = SystemClock.uptimeMillis();
        }

        mTargetProgress = Math.min(progress * 360.0f, 360.0f);

        invalidate();
    }

    public interface ProgressCallback {
        public void onProgressUpdate(float progress);
    }
}
