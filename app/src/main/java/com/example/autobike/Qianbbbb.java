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
    private int innerRadius,outerRadius;



    private boolean isQianbo1Highlighted=true;//前拨1是否高亮
    private boolean isQianbo2Highlighted=false;//前拨2是否高亮

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
        innerRadius=width/8;
        outerRadius=width/4;

    }

    //设置前拨1高亮
    public void setQianbo1Highlighted(boolean highlighted){
        isQianbo1Highlighted=highlighted;
        invalidate();
    }

    //设置前拨2高亮
    public void setQianbo2Highlighted(boolean highlighted){
        isQianbo2Highlighted=highlighted;
        invalidate();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setStrokeWidth(10);
        paint.setColor(isQianbo1Highlighted?Color.YELLOW:Color.GRAY);
       canvas.drawCircle(centerX,centerY,innerRadius,paint);

       paint.setColor(isQianbo2Highlighted?Color.YELLOW:Color.GRAY);
       canvas.drawCircle(centerX,centerY,outerRadius,paint);
    }
}

