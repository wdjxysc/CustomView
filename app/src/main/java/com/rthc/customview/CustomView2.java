package com.rthc.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * Created by Administrator on 2016/5/30.
 */
public class CustomView2 extends View{

    public CustomView2(Context context) {
        super(context);
    }

    public CustomView2(Context context, AttributeSet attrs) {
        super(context, attrs);

        setAttrs(attrs);
    }



    public CustomView2(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    /**
     * 文本
     */
    private String mTitleText;
    /**
     * 文本的颜色
     */
    private int mTitleTextColor;
    /**
     * 文本的大小
     */
    private int mTitleTextSize;

    /**
     * 绘制时控制文本绘制的范围
     */
    private Rect mBound;

    /**
     * 画笔
     */
    private Paint mPaint;



    /**
     * 获得我自定义的样式属性
     *
     * @param context
     * @param attrs
     * @param defStyle
     */
    public CustomView2(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);

    }


    private void setAttrs(AttributeSet attrs){
        /**
         * 获得我们所定义的自定义样式属性
         */
        TypedArray a = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.CustomView2, 0, 0);
        int n = a.getIndexCount();
        for (int i = 0; i < n; i++)
        {
            int attr = a.getIndex(i);
            switch (attr)
            {
                case R.styleable.CustomView2_cvTitleText:
                    mTitleText = a.getString(attr);
                    break;
                case R.styleable.CustomView2_cvTitleTextColor:
                    // 默认颜色设置为黑色
                    mTitleTextColor = a.getColor(attr, Color.BLACK);
                    break;
                case R.styleable.CustomView2_cvTitleTextSize:
                    // 默认设置为16sp，TypeValue也可以把sp转化为px
                    mTitleTextSize = a.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_SP, 16, getResources().getDisplayMetrics()));
                    break;
            }

        }
        a.recycle();

        /**
         * 获得绘制文本的宽和高
         */
        mPaint = new Paint();
        mPaint.setTextSize(mTitleTextSize);
        mPaint.setAntiAlias(true);
        // mPaint.setColor(mTitleTextColor);
        mBound = new Rect();
        mPaint.getTextBounds(mTitleText, 0, mTitleText.length(), mBound);




        //添加点击事件
        setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mTitleText = randomText();
                postInvalidate();
            }
        });
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width;
        int height;

        if(widthMode == MeasureSpec.EXACTLY){
            width = widthSize;
        }else{
            mPaint.setTextSize(mTitleTextSize);
            mPaint.getTextBounds(mTitleText, 0, mTitleText.length(), mBound);
            float textWidth = mBound.width();
            int desired = (int) (getPaddingLeft() + textWidth + getPaddingRight());
            width = desired;
        }

        if(heightMode == MeasureSpec.EXACTLY){
            height = heightSize;
        }else {
            mPaint.setTextSize(mTitleTextSize);
            mPaint.getTextBounds(mTitleText,0,mTitleText.length(),mBound);
            float textHeight = mBound.height();

            int desired = (int) (getPaddingTop() + textHeight + getPaddingBottom() + 200);
            height= resolveSizeAndState(desired, heightMeasureSpec, 0);
            //height = desired;
        }

        setMeasuredDimension(width, height);
        //super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }


    @Override
    protected void onDraw(Canvas canvas)
    {
        mPaint.setColor(Color.YELLOW);
        Log.i("log","getMeasuredWidth()--" +getMeasuredWidth() + "     getMeasuredHeight()--" + getMeasuredHeight());
        canvas.drawRect(0, 0, getMeasuredWidth(), getMeasuredHeight(), mPaint);



        mPaint.setColor(mTitleTextColor);
        Log.i("log","getWidth()--" +getWidth() + "     mBound.width()--" + mBound.width());
        Log.i("log","getHeight()--" +getHeight() + "     mBound.height()--" + mBound.height());
        Log.i("log","drawText  x--" +(getWidth() / 2 - mBound.width() / 2) + "     y--" + (getHeight() / 2 + mBound.height() / 2));
        canvas.drawText(mTitleText, getWidth() / 2 - mBound.width() / 2, getHeight() / 2 + mBound.height() / 2, mPaint);
    }


    private String randomText()
    {
        Random random = new Random();
        Set<Integer> set = new HashSet<Integer>();
        while (set.size() < 4)
        {
            int randomInt = random.nextInt(10);
            set.add(randomInt);
        }
        StringBuffer sb = new StringBuffer();
        for (Integer i : set)
        {
            sb.append("" + i);
        }

        return sb.toString();
    }

}
