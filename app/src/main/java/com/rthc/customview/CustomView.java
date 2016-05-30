package com.rthc.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Administrator on 2016/5/30.
 */
public class CustomView extends View {

    private int shapeColor;

    private boolean displayShapeName;

    public CustomView(Context context) {
        super(context);
    }

    public CustomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setupAttributes(attrs);

        setupPaint();
    }

    public CustomView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setupAttributes(attrs);

        setupPaint();

    }

    public CustomView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        setupAttributes(attrs);

        setupPaint();
    }

    private void setupAttributes(AttributeSet attrs){
        // 提取自定义属性到TypedArray对象中
        TypedArray a = getContext().getTheme().obtainStyledAttributes(attrs,
                R.styleable.CustomView, 0, 0);

        // 将属性赋值给成员变量
        try {
            shapeColor = a.getColor(R.styleable.CustomView_shapeColor,
                    Color.BLACK);
            displayShapeName = a.getBoolean(
                    R.styleable.CustomView_displayShapeName, false);
        } finally {
            // TypedArray对象是共享的必须被重复利用。
            a.recycle();
        }
    }


    //getset
    public boolean isDisplayingShapeName() {
        return displayShapeName;
    }

    public void setDisplayingShapeName(boolean state) {
        this.displayShapeName = state;
        invalidate();//重绘
        requestLayout();//重新调整布局
    }

    public int getShapeColor() {
        return shapeColor;
    }

    public void setShapeColor(int color) {
        this.shapeColor = color;
        invalidate();//重绘
        requestLayout();//重新调整布局
    }



    private int shapeWidth = 100;
    private int shapeHeight = 100;
    private int textXOffset = 0;
    private int textYOffset = 30;
    private Paint paintShape;



    private void setupPaint() {
        paintShape = new Paint();
        paintShape.setStyle(Paint.Style.FILL);
        paintShape.setColor(shapeColor);
        paintShape.setTextSize(30);
        paintShape.setAntiAlias(true);//抗锯齿
        //canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG|Paint.FILTER_BITMAP_FLAG));//不能用paint时 使用该方法
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //简单定义文本边距
        int textPadding = 10;
        int contentWidth = shapeWidth;

        //使用测量模式获得宽度
        int minw = contentWidth + getPaddingLeft() + getPaddingRight();
        int w = resolveSizeAndState(minw, widthMeasureSpec, 0);

        // 同宽度
        int minh = shapeHeight + getPaddingBottom() + getPaddingTop();
        //如果现实图形名，则加上文字高度
        if (displayShapeName) {
            minh += textYOffset + textPadding;
        }
        int h = resolveSizeAndState(minh, heightMeasureSpec, 0);

        // 测量完成后必须调用setMeasuredDimension方法
        // 之后可以通过getMeasuredWidth 和 getMeasuredHeight 方法取出高度和宽度
        setMeasuredDimension(w, h);

        //super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }


    private String[] shapeValues = { "square", "circle", "triangle" };
    private int currentShapeIndex = 0;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean result = super.onTouchEvent(event);

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            currentShapeIndex ++;
            if (currentShapeIndex > (shapeValues.length - 1)) {
                currentShapeIndex = 0;
            }
            postInvalidate();
            //invalidate();
            return true;
        }
        return result;

        //return super.onTouchEvent(event);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        String shapeSelected = shapeValues[currentShapeIndex];

        if (shapeSelected.equals("square")) {
            canvas.drawRect(0, 0, shapeWidth, shapeHeight, paintShape);
            textXOffset = 0;
        } else if (shapeSelected.equals("circle")) {
            canvas.drawCircle(shapeWidth / 2, shapeHeight / 2, shapeWidth / 2, paintShape);
            textXOffset = 12;
        } else if (shapeSelected.equals("triangle")) {
            canvas.drawPath(getTrianglePath(), paintShape);
            textXOffset = 0;
        }
        if (displayShapeName) {
            canvas.drawText(shapeSelected, 0 + textXOffset, shapeHeight + textYOffset, paintShape);
        }
    }

    protected Path getTrianglePath() {
        Point p1 = new Point(0, shapeHeight), p2 = null, p3 = null;
        p2 = new Point(p1.x + shapeWidth, p1.y);
        p3 = new Point(p1.x + (shapeWidth / 2), p1.y - shapeHeight);
        Path path = new Path();
        path.moveTo(p1.x, p1.y);
        path.lineTo(p2.x, p2.y);
        path.lineTo(p3.x, p3.y);
        return path;
    }


    public String getSelectedShape() {
        return shapeValues[currentShapeIndex];
    }


    @Override
    public Parcelable onSaveInstanceState() {
        // 新建一个Bundle
        Bundle bundle = new Bundle();
        // 保存view基本的状态，调用父类方法即可
        bundle.putParcelable("instanceState", super.onSaveInstanceState());
        // 保存我们自己的数据
        bundle.putInt("currentShapeIndex", this.currentShapeIndex);
        // 当然还可以继续保存其他数据
        // 返回bundle对象
        return bundle;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        // 判断该对象是否是我们保存的
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            // 把我们自己的数据恢复
            this.currentShapeIndex = bundle.getInt("currentShapeIndex");
            // 可以继续恢复之前的其他数据
            // 恢复view的基本状态
            state = bundle.getParcelable("instanceState");
        }
        // 如果不是我们保存的对象，则直接调用父类的方法进行恢复
        super.onRestoreInstanceState(state);
    }
}
