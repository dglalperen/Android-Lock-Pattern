package com.example.androidlockpattern;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class PatternLockView extends View {
    private static final int DOT_COUNT = 3;
    private static final int DOT_RADIUS = 50;
    private List<Dot> dots;
    private Path patternPath;
    private Paint dotPaint, pathPaint, selectedDotPaint, currentSegmentPaint;
    private List<Integer> currentPattern;
    private float lastPathX, lastPathY;
    private boolean isDrawing;

    private long startTime, endTime;

    public PatternLockView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    private int getColorBasedOnTotalTime() {
        long currentTime = isDrawing ? System.currentTimeMillis() : endTime;
        long totalTimeElapsed = currentTime - startTime;

        if (totalTimeElapsed < 1500) {
            return Color.RED;
        } else if (totalTimeElapsed < 2500) {
            return Color.YELLOW;
        } else {
            return Color.BLUE;
        }
    }


    private void initialize() {
        dots = new ArrayList<>();
        patternPath = new Path();
        currentPattern = new ArrayList<>();
        isDrawing = false;

        // Initialize paints
        dotPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        dotPaint.setColor(Color.BLACK);
        dotPaint.setStyle(Paint.Style.FILL);

        selectedDotPaint = new Paint(dotPaint);
        selectedDotPaint.setColor(Color.RED);

        pathPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        pathPaint.setColor(Color.BLUE);
        pathPaint.setStyle(Paint.Style.STROKE);
        pathPaint.setStrokeWidth(24);
        pathPaint.setStrokeJoin(Paint.Join.ROUND);
        pathPaint.setStrokeCap(Paint.Cap.ROUND);

        currentSegmentPaint = new Paint(pathPaint);
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        int padding = DOT_RADIUS * 3;
        float cellWidth = (float) (w - 2 * padding) / (DOT_COUNT - 1);
        float cellHeight = (float) (h - 2 * padding) / (DOT_COUNT - 1);

        dots.clear();
        for (int i = 0; i < DOT_COUNT; i++) {
            for (int j = 0; j < DOT_COUNT; j++) {
                float centerX = padding + j * cellWidth;
                float centerY = padding + i * cellHeight;
                dots.add(new Dot(i * DOT_COUNT + j, centerX, centerY));
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // Draw each dot
        for (Dot dot : dots) {
            Paint paint = dot.selected ? selectedDotPaint : dotPaint;
            canvas.drawCircle(dot.centerX, dot.centerY, DOT_RADIUS, paint);
        }

        // Set the dynamic color for the current path
        currentSegmentPaint.setColor(getColorBasedOnTotalTime());

        // Draw the pattern path
        canvas.drawPath(patternPath, currentSegmentPaint);

        if (isDrawing && currentPattern.size() > 1) {
            Dot lastDot = dots.get(currentPattern.get(currentPattern.size() - 1));
            canvas.drawLine(lastPathX, lastPathY, lastDot.centerX, lastDot.centerY, currentSegmentPaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startTime = System.currentTimeMillis();  // Start timing
                handleActionDown(event.getX(), event.getY());
                break;
            case MotionEvent.ACTION_MOVE:
                handleActionMove(event.getX(), event.getY());
                break;
            case MotionEvent.ACTION_UP:
                endTime = System.currentTimeMillis();  // End timing
                handleActionUp();
                break;
        }
        invalidate();
        return true;
    }

    public long getDrawingTime() {
        return endTime - startTime;
    }

    private void handleActionDown(float x, float y) {
        resetPattern();
        isDrawing = true;
        addDotToPattern(x, y);
    }

    private void handleActionMove(float x, float y) {
        addDotToPattern(x, y);
    }

    private void handleActionUp() {
        isDrawing = false;
    }

    private void addDotToPattern(float x, float y) {
        for (Dot dot : dots) {
            if (!dot.selected && isWithinDot(dot, x, y)) {
                dot.selected = true;
                currentPattern.add(dot.id);

                if (currentPattern.size() == 1) {
                    patternPath.moveTo(dot.centerX, dot.centerY);
                } else {
                    patternPath.lineTo(dot.centerX, dot.centerY);
                }
                lastPathX = dot.centerX;
                lastPathY = dot.centerY;
                break;
            }
        }
    }


    private boolean isWithinDot(Dot dot, float x, float y) {
        float dx = x - dot.centerX;
        float dy = y - dot.centerY;
        return Math.sqrt(dx * dx + dy * dy) <= DOT_RADIUS;
    }

    public List<Integer> getPattern() {
        return new ArrayList<>(currentPattern);
    }

    public void resetPattern() {
        currentPattern.clear();
        patternPath.reset();
        for (Dot dot : dots) {
            dot.selected = false;
        }
    }


}

