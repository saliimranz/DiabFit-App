package com.example.diabfitapp.nutrition.food;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class CircularProgressView extends View {

    private Paint progressPaint;
    private Paint backgroundPaint;
    private float progress = 0;
    private float max = 100;
    private boolean isOverTarget = false;

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
        progressPaint.setStrokeWidth(20);
        progressPaint.setAntiAlias(true);

        backgroundPaint = new Paint();
        backgroundPaint.setColor(Color.GRAY);
        backgroundPaint.setStyle(Paint.Style.STROKE);
        backgroundPaint.setStrokeWidth(20);
        backgroundPaint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = getWidth();
        int height = getHeight();
        int radius = Math.min(width, height) / 2 - 20;

        // Draw background circle
        canvas.drawCircle(width / 2, height / 2, radius, backgroundPaint);

        // Set the progress paint color based on overTarget state
        if (isOverTarget) {
            progressPaint.setColor(Color.RED);
        } else {
            progressPaint.setColor(Color.GREEN);
        }

        // Draw progress arc
        float sweepAngle = 360 * (progress / max);
        canvas.drawArc(20, 20, width - 20, height - 20, -90, sweepAngle, false, progressPaint);
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
