package com.layer.sprite;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;


/**
 * Created by jbl on 2016-04-14.
 */
public class SpriteView extends ImageView {
    public int x, y;
    int angel;
    int last_angel;
    public SpriteView(Context context) {
        super(context);
    }

    public SpriteView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SpriteView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @SuppressLint("NewApi")
    public ObjectAnimator correctTarget(int targetx, int targety) {
        int curx, cury;
        curx = (int) this.getX()+this.getWidth()/2;
        cury = (int) this.getY()+this.getHeight()/2;
        int dx, dy;
        dy = targety - cury;
        dx = targetx - curx;

        double tana = (double) (dy) / (double) (dx);
        int a = (int) (Math.atan(tana) * 180 / Math.PI);
        angel = a;
        limitsquare(dx, dy);
        if(dx==0&&dy>0){
            angel=180;
        }else if(dx==0&&dy<0){
            angel=0;
        }
        if(dy==0&&dx>0){
            angel=90;
        }else if(dy==0&&dx<0){
            angel=-90;
        }
        this.invalidate();
        Log.d("jia==========", "angel===" + angel + "===Rotation" + this.getRotation());
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(this, "rotation", this.getRotation(), angel);
        objectAnimator.setDuration(600);
        objectAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        return objectAnimator;
    }

    public void reset() {

//        this.setRotation(0);

    }

    public void limitsquare(int dx, int dy) {
        if (dx > 0 && dy < 0) {
            angel = 90-Math.abs(angel);
            //    1
        } else if (dx < 0 && dy < 0) {
            angel = -(90-Math.abs(angel));
            //   2
        } else if (dx < 0 && dy > 0) {
            angel = -90-Math.abs(angel);
            //3
        } else if (dx > 0 && dy > 0) {

            angel = 90+Math.abs(angel) ;
            //4
        }
    }

}
