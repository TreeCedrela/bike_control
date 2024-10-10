package com.example.myapplication;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;


public class Houbbbb extends View {
    public int currentHighlightedIndex;
    public int[] lineHeights = new int[12];
    private int lineWidth;
    private int spacing;
    private int totalLineWidth;
    private int totalSpacing;
    private int totalWidth;
    private int startX;
    private int startY;

    public Houbbbb(Context context) {
        super(context);
        init();
    }

    public Houbbbb(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public Houbbbb(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        int baseHeight = 50;
        for (int i = 0; i < lineHeights.length; i++) {
            lineHeights[i] = baseHeight + i * 15;
        }
        currentHighlightedIndex = lineHeights.length - 1;
        lineWidth = 10;
        spacing = 20;
        calculateDimensions();
    }

    public void setLineWidth(int width) {
        lineWidth = width;
        invalidate();
    }

    private void calculateDimensions() {
        totalLineWidth = lineWidth * lineHeights.length;
        totalSpacing = spacing * (lineHeights.length - 1);
        totalWidth = totalLineWidth + totalSpacing;
        startX = (getWidth() - totalWidth) / 2;
        startY = getHeight() / 2 - lineHeights[0] / 2; // 调整起始 Y 坐标，使竖线在同一水平线上
    }

    @Override
    protected void onDraw(Canvas canvas) {
        calculateDimensions();

        for (int i = 0; i < lineHeights.length; i++) {
            if (i == currentHighlightedIndex) {
                canvas.drawLine(startX, startY, startX, startY + lineHeights[i], PaintFactory.getHighlightPaint());
            } else {
                Paint normalPaint = PaintFactory.getNormalPaint();
                normalPaint.setStrokeWidth(lineWidth);
                canvas.drawLine(startX, startY, startX, startY + lineHeights[i], normalPaint);
            }
            startX += lineWidth + spacing;
        }
    }
}

class  PaintFactory {
    // 正常状态下竖线的画笔
    public static Paint getNormalPaint() {
        Paint paint = new Paint();
        paint.setColor(Color.GRAY);
        paint.setStrokeWidth(10);
        return paint;
    }

    // 高亮状态下竖线的画笔
    public static Paint getHighlightPaint() {
        Paint paint = new Paint();
        paint.setColor(Color.YELLOW);
        paint.setStrokeWidth(10);
        return paint;
    }
}


