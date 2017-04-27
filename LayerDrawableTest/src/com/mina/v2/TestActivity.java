package com.mina.v2;

import com.mina.layerdrawabletest.R;
import com.mina.widgets.DragerLayout;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.widget.ImageView;



/**
 * Created by mina on 2017/4/26.
 */

public class TestActivity extends Activity {

    private ImageView mImage;
    private DragerLayout mOptionsLayout;
    private Drawable mDrawables[];
    private LayerDrawable mLayerDrawable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	this.setContentView(R.layout.activity_test_drag_layout);
        mImage = (ImageView) this.findViewById(R.id.layer_drawable_iv);
        mOptionsLayout = (DragerLayout) this.findViewById(R.id.options_drawable_dl);
        init();
    }

    private void init(){
        mDrawables = new Drawable[4];

        mDrawables[0] = getResources().getDrawable(R.drawable.blue_rect_cubic);
        mDrawables[1] = getResources().getDrawable(R.drawable.green_rect_cubic);
        mDrawables[2] = getResources().getDrawable(R.drawable.red_rect_cubic);
        mDrawables[3] = getResources().getDrawable(R.drawable.black_rect_cubic);

        mLayerDrawable = new LayerDrawable(mDrawables);

        mImage.setImageDrawable(mLayerDrawable);

        mOptionsLayout.setDrawableResources(new int[]{
                R.drawable.black_rect_cubic,
                R.drawable.red_rect_cubic,
                R.drawable.green_rect_cubic,
                R.drawable.blue_rect_cubic
        });

        mOptionsLayout.setmUpdate(new OnUpdateListener(){

            @Override
            public void onUpdate(int[] res) {
                for(int i=0, j=res.length-1; i<res.length&&j>=0; i++, j--){
                    mDrawables[i] = getResources().getDrawable(res[j]);
                }
                mLayerDrawable = new LayerDrawable(mDrawables);

                mImage.setImageDrawable(mLayerDrawable);
            }
        });

    }

    public interface OnUpdateListener{
        void onUpdate(int [] res);
    }
}
