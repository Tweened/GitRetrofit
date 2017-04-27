package com.mina.widgets;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * 鐢ㄤ簬瀹炵幇鎷栨嫿View鐨勫姩鐢�
 * Created by mina on 2017/4/26.
 */

public class DragerView extends ImageView {

    private int lastX;
    private int lastY;

    private Rect[] mRects;
    private Rect mLayoutRect;
    private DragerLayout.OnExchangeListener lis;
    private int position;

    public DragerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DragerView(Context context) {
        super(context);
    }

    public DragerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int startX = (int) event.getX();
        int startY = (int) event.getY();
        ScaleAnimation mScale  = new ScaleAnimation(
                1.0f, 1.1f, 1.0f, 1.1f, getLayoutParams().width/2, getLayoutParams().height/2);
        mScale.setDuration(200);
        mScale.setFillAfter(true);

        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                this.setAlpha(0.5f);
                this.startAnimation(mScale);
                lastX = startX;
                lastY = startY;
                break;
            case MotionEvent.ACTION_MOVE:
                int offsetX = startX - lastX;
                int offsetY = startY - lastY;
                layout(getLeft()+offsetX, getTop()+offsetY, getRight()+offsetX, getBottom()+offsetY);
                /*鑻ュ叾浠栧尯鍩熷寘鍚玿y鐐癸紝鍒欒繘琛屽洖璋�*/
                checkBounds(getLeft()+offsetX+getWidth()/2, getTop()+offsetY+getHeight()/2);
                break;
            case MotionEvent.ACTION_UP:
                this.setAlpha(1f);
                this.clearAnimation();
                rePosition();
                break;
        }
        return true;
    }

    private void checkBounds(int x, int y){
        if(mRects==null) return;
        for(int i=0; i<mRects.length; i++){
            if(i==position) continue;
            Rect r = mRects[i];
            if(r.contains(x, y)){
                if(lis!=null){
                    lis.onExchangeStart(position, i);
                }
            }
        }
    }

    private void rePosition(){
        Rect r = mRects[position];
        setLayoutRect(r);
        layout(r.left, r.top, r.right, r.bottom);
        if(lis!=null){
            lis.onExchangeEnd(position);
        }
    }

    public void setExchangeListener(DragerLayout.OnExchangeListener l){
        this.lis = l;
    }

    public void setRects(Rect[] rects){
        this.mRects = rects;
    }

    public void setLayoutRect(Rect rect){
        this.mLayoutRect = rect;
    }

    public Rect getLayoutRect(){
        return mLayoutRect;
    }

    public void setPosition(int position){
        this.position = position;
    }

    public int getPosition(){
        return position;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

    }
}
