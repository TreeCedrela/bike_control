package com.example.autobike;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public  class Qianbbbb extends View {

    private Paint paint;
    private int centerX,centerY;
    private int baseRadius;
    private int ringSpacing;//圆环间距

    // 新增：用于记录当前高亮的圆环索引，0表示最内圈，1表示中间圈，2表示最外圈
    private int currentHighlightedRing = 0;

    // 新增：定义三个圆环是否高亮的状态变量
    private boolean isQianbo1Highlighted = false;
    private boolean isQianbo2Highlighted = false;
    private boolean isQianbo3Highlighted = true;

    public Qianbbbb(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);//设置为圆环
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        int width = getWidth();
        int height = getHeight();

        centerX = width / 2;
        centerY = height / 2;
        // 设置基础半径和圆环间距
        baseRadius = width / 8;
        ringSpacing = width / 12;


    }
    



    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setStrokeWidth(10);
        int innerRadius=baseRadius;
        int middleRadius=baseRadius+ringSpacing;
        int outerRadius=baseRadius+ringSpacing*2;
        // 绘制最内圈圆环
        paint.setColor(isQianbo1Highlighted? Color.YELLOW : Color.GRAY);
        canvas.drawCircle(centerX, centerY, innerRadius, paint);

        // 绘制中间圈圆环
        paint.setColor(isQianbo2Highlighted? Color.YELLOW : Color.GRAY);
        canvas.drawCircle(centerX, centerY, middleRadius, paint);

        // 绘制最外圈圆环
        paint.setColor(isQianbo3Highlighted? Color.YELLOW : Color.GRAY);
        canvas.drawCircle(centerX, centerY, outerRadius, paint);
    }

    public void setNextInnerRingHighlighted() {
        if (currentHighlightedRing > 0) {
            currentHighlightedRing--;
        }
        updateRingHighlightStatus();
    }

    public void setNextOuterRingHighlighted() {
        if (currentHighlightedRing < 2) {
            currentHighlightedRing++;
        }
        updateRingHighlightStatus();
    }

    private void updateRingHighlightStatus() {
        currentHighlightedRing = (currentHighlightedRing + 1) % 3;

        isQianbo1Highlighted = currentHighlightedRing == 0;
        isQianbo2Highlighted = currentHighlightedRing == 1;
        isQianbo3Highlighted = currentHighlightedRing == 2;

        invalidate();
    }

    public boolean isQianbo1Highlighted() {
        return isQianbo1Highlighted;
    }
    public boolean isQianbo2Highlighted() {
        return isQianbo2Highlighted;
    }

    public boolean isQianbo3Highlighted() {
        return isQianbo3Highlighted;
    }
}

