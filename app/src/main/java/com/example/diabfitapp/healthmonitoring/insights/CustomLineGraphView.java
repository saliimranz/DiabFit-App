package com.example.diabfitapp.healthmonitoring.insights;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.example.diabfitapp.healthmonitoring.sugerlog.SugarLog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CustomLineGraphView extends View {
    private Paint linePaint;
    private Paint pointPaint;
    private Paint gridPaint;
    private Paint textPaint;
    private List<SugarLog> dataPoints;

    private SimpleDateFormat inputDateFormat;
    private SimpleDateFormat outputDateFormat;

    public CustomLineGraphView(Context context) {
        super(context);
        init();
    }

    public CustomLineGraphView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        linePaint = new Paint();
        linePaint.setColor(Color.BLUE);
        linePaint.setStrokeWidth(5f);
        linePaint.setStyle(Paint.Style.STROKE);

        pointPaint = new Paint();
        pointPaint.setColor(Color.RED);
        pointPaint.setStrokeWidth(10f);
        pointPaint.setStyle(Paint.Style.FILL);

        gridPaint = new Paint();
        gridPaint.setColor(Color.LTGRAY);
        gridPaint.setStrokeWidth(2f);

        textPaint = new Paint();
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(30f);

        dataPoints = new ArrayList<>();

        inputDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        outputDateFormat = new SimpleDateFormat("MMM dd", Locale.getDefault());
    }

    public void setDataPoints(List<SugarLog> dataPoints) {
        this.dataPoints = dataPoints;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (dataPoints.isEmpty()) return;

        float width = getWidth();
        float height = getHeight();
        float maxDataPoint = getMaxSugarLevel(dataPoints);
        float minDataPoint = getMinSugarLevel(dataPoints);

        // Draw grid
        for (int i = 1; i < 10; i++) {
            float y = height * i / 10;
            canvas.drawLine(0, y, width, y, gridPaint);
        }

        // Draw line graph
        float prevX = 0;
        float prevY = height * (1 - (dataPoints.get(0).getSugarLevel() - minDataPoint) / (maxDataPoint - minDataPoint));

        for (int i = 1; i < dataPoints.size(); i++) {
            float x = width * i / (dataPoints.size() - 1);
            float y = height * (1 - (dataPoints.get(i).getSugarLevel() - minDataPoint) / (maxDataPoint - minDataPoint));

            canvas.drawLine(prevX, prevY, x, y, linePaint);
            canvas.drawCircle(x, y, 5f, pointPaint);

            prevX = x;
            prevY = y;
        }

        // Draw X-axis (Time)
        for (int i = 0; i < dataPoints.size(); i += Math.max(1, dataPoints.size() / 10)) {
            float x = width * i / (dataPoints.size() - 1);
            String time = formatTime(dataPoints.get(i).getTime());
            canvas.drawText(time, x, height - 20, textPaint);
        }

        // Draw Y-axis (Sugar Levels)
        for (int i = 1; i <= 10; i++) {
            float y = height * (1 - i / 10f);
            float sugarLevel = minDataPoint + (maxDataPoint - minDataPoint) * i / 10;
            canvas.drawText(String.format(Locale.getDefault(), "%.1f", sugarLevel), 10, y, textPaint);
        }
    }

    private float getMaxSugarLevel(List<SugarLog> dataPoints) {
        float max = Float.MIN_VALUE;
        for (SugarLog dataPoint : dataPoints) {
            if (dataPoint.getSugarLevel() > max) {
                max = dataPoint.getSugarLevel();
            }
        }
        return max;
    }

    private float getMinSugarLevel(List<SugarLog> dataPoints) {
        float min = Float.MAX_VALUE;
        for (SugarLog dataPoint : dataPoints) {
            if (dataPoint.getSugarLevel() < min) {
                min = dataPoint.getSugarLevel();
            }
        }
        return min;
    }

    private String formatTime(String time) {
        try {
            return outputDateFormat.format(inputDateFormat.parse(time));
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }
}
