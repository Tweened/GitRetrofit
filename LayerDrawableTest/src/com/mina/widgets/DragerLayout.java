package com.mina.widgets;

import com.mina.layerdrawabletest.R;
import com.mina.v2.TestActivity;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

/**
 * Created by mina on 2017/4/26.
 */

public class DragerLayout extends LinearLayout{

    private Context mContext;
    private int mNum;
    private int[]mRes;

    private DragerView[] mChildren;
    private Rect[] mRectPosition;

    private boolean layoutOnce = false;

    private TestActivity.OnUpdateListener mUpdate;

    public DragerLayout(Context context) {
        this(context, null);
    }

    public DragerLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DragerLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        init(attrs);
    }

    private void init(AttributeSet attrs){
        TypedArray a = mContext.obtainStyledAttributes(attrs, R.styleable.dragger_layout_style);
        mNum = a.getInteger(R.styleable.dragger_layout_style_optionsNum, 0);
        a.recycle();

        mChildren = new DragerView[mNum];
        mRectPosition = new Rect[mNum];
    }

    public void setDrawableResources(int[] res){
        this.mRes = res;
    }

    public int[] getDrawableResources(){
        int[] res = new int[mNum];
        for(int i=0; i<mNum; i++){
            res[i] = (Integer) mChildren[i].getTag();
        }
        return res;
    }

    public void setmUpdate(TestActivity.OnUpdateListener mUpdate){
        this.mUpdate = mUpdate;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if(!layoutOnce){
            for(int i=0; i<mNum; i++){
                DragerView child = (DragerView) getChildAt(i);
                mChildren[i] = child;
                child.setRects(mRectPosition);
                child.setPosition(i);
                child.setTag(mRes[i]);

                child.setLayoutRect(new Rect(
                        child.getLeft(),
                        child.getTop(),
                        child.getRight(), child.getBottom()));

                mRectPosition[i] = new Rect(
                        child.getLeft(),
                        child.getTop(),
                        child.getRight(), child.getBottom());

                child.setExchangeListener(new OnExchangeListener() {
                    @Override
                    public void onExchangeStart(int from, int to) {

                        DragerView childTo = mChildren[to];
                        DragerView childFrom = mChildren[from];

                        childTo.setPosition(from);
                        childFrom.setPosition(to);

                        Rect r = childFrom.getLayoutRect();
                        childFrom.setLayoutRect(childTo.getLayoutRect());
                        childTo.setLayoutRect(r);

                        childTo.layout(r.left, r.top, r.right, r.bottom);

                        mChildren[to] = childFrom;
                        mChildren[from] = childTo;
                    }

                    @Override
                    public void onExchangeEnd(int position) {
                        if(mUpdate!=null){
                            mUpdate.onUpdate(getDrawableResources());
                        }
                    }
                });
            }
            layoutOnce = true;
        }
    }

    interface OnExchangeListener{
        void onExchangeStart(int from, int to);
        void onExchangeEnd(int position);
    }
}
