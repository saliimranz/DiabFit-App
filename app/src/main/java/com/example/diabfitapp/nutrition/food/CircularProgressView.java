package com.example.diabfitapp.nutrition.food;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class CircularProgressView extends View {

    private Paint progressPaint;
    private Paint backgroundPaint;
    private float progress = 0;
    private float max = 100;
    private boolean isOverTarget = false;
    private int strokeWidth = 20;

    public CircularProgressView(Context context) {
        super(context);
        init();
    }

    public CircularProgressView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CircularProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        progressPaint = new Paint();
        progressPaint.setStyle(Paint.Style.STROKE);
        progressPaint.setStrokeWidth(strokeWidth);
        progressPaint.setAntiAlias(true);

        backgroundPaint = new Paint();
        backgroundPaint.setColor(Color.GRAY);
        backgroundPaint.setStyle(Paint.Style.STROKE);
        backgroundPaint.setStrokeWidth(strokeWidth);
        backgroundPaint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = getWidth();
        int height = getHeight();
        int radius = Math.min(width, height) / 2 - strokeWidth;

        // Center of the circle
        float cx = width / 2;
        float cy = height / 2;

        // Draw background circle
        canvas.drawCircle(cx, cy, radius, backgroundPaint);

        // Set the progress paint color based on overTarget state
        if (isOverTarget) {
            progressPaint.setColor(Color.RED);
        } else {
            progressPaint.setColor(Color.GREEN);
        }

        // Define the bounds for the arc
        RectF arcBounds = new RectF(cx - radius, cy - radius, cx + radius, cy + radius);

        // Draw progress arc
        float sweepAngle = 360 * (progress / max);
        canvas.drawArc(arcBounds, -90, sweepAngle, false, progressPaint);
    }

    public void setProgress(float progress, float max) {
        this.progress = progress;
        this.max = max;
        invalidate();
    }

    public void setOverTarget(boolean isOverTarget) {
        this.isOverTarget = isOverTarget;
        invalidate();
    }
}
