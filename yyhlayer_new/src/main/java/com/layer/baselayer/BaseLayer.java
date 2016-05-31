package com.layer.baselayer;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.layer.sprite.SpriteView;
import com.layer.aiview.AIView;
import com.layer.aiview.AIViewSurface;
import com.layer.arcmenu.ArcMenuGroup;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Created by jbl on 2016-04-14.
 */
public class BaseLayer extends FrameLayout {
    List<AIView> list = new ArrayList<AIView>();
    SpriteView view;
    ArcMenuGroup arcMenuGroup;
    boolean ani_end = true;
    public int bg_width, bg_height;
    int arc_x, arc_y;

    public BaseLayer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BaseLayer(Context context) {
        super(context);
    }

    public BaseLayer(Context context, AttributeSet attrs, int d) {
        super(context, attrs, d);
    }

    public void addSprite(SpriteView v, int x, int y) {
        view = v;
        addView(v);

    }

    public void addArcMenu(ArcMenuGroup v, int x, int y, LayoutParams lp) {

        arcMenuGroup = v;
        arc_x = x - lp.width / 2;
        arc_y = y - lp.height / 2;
        addView(v, lp);

    }

    public void moveSprite(int x, int y) {
//       view.correctTarget(x, y);
        ObjectAnimator o3 = view.correctTarget(x, y);
        int height = view.getMeasuredHeight();
        int width = view.getMeasuredWidth();
        Log.d("translation===", "" + width + "===" + width + "===height" + height + "==getX" + view.getX());
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(view, "Y", y - height / 2);
        objectAnimator.setDuration(600);
        objectAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        ObjectAnimator objectAnimator2 = ObjectAnimator.ofFloat(view, "X", x - width / 2);
        objectAnimator2.setDuration(600);
        objectAnimator2.setInterpolator(new AccelerateDecelerateInterpolator());

        AnimatorSet set = new AnimatorSet();
        set.play(o3).before(objectAnimator);
        set.play(objectAnimator).with(objectAnimator2);
        set.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                ani_end = false;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                checkHit();
                view.reset();
                ani_end = true;
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        if (ani_end) {
            set.start();
        }

    }

    public void checkHit() {
        Rect spri = new Rect();
        view.getHitRect(spri);
        if (arcMenuGroup != null) {
            arcMenuGroup.removeAllViews();
            this.removeView(arcMenuGroup);
            arcMenuGroup = null;
        }
        for (int i = 0; i < list.size(); i++) {
            AIView aiView = list.get(i);
            Rect aire = new Rect();
            aiView.getHitRect(aire);
            if (aire.contains(spri.centerX(), spri.centerY())) {
                Log.d("碰撞检测===", "精灵碰撞");
                Toast.makeText(this.getContext(), aiView.getText(), Toast.LENGTH_SHORT).show();
                if (iHitEvent != null) {
                    iHitEvent.hitTarget(aiView);
                }
                aiView.setHitStatus(true);
            } else {

                aiView.setHitStatus(false);
            }

        }
    }

    public void addAi(AIViewSurface v) {
        addView(v);
//        list.add(v);
    }

    public void addAi(AIView v) {
        addView(v);
        list.add(v);
//        Collections.sort(list, new SortByXY());
    }

    class SortByXY implements Comparator {
        @Override
        public int compare(Object o, Object t1) {
            AIView ai1=(AIView)o;
            AIView ai2=(AIView)t1;
            if((ai1.y)>(ai2.y)){
                return 1;
            }else   if((ai1.x+ai1.y)<(ai2.x+ai2.y)){
                return -1;
            }
            return 0;
        }
    }
//    @Override

    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        layoutArcMenu();
        layoutAI();
        layoutSprite();
    }

    public void layoutSprite() {
        int height = view.getMeasuredHeight();
        int width = view.getMeasuredWidth();
        view.layout(view.x, view.y, view.x + width, view.y + height);
        Log.d("translation===", "layoutSprite");


    }

    public void layoutArcMenu() {
        if (arcMenuGroup != null) {
            int height = arcMenuGroup.getMeasuredHeight();
            int width = arcMenuGroup.getMeasuredWidth();
            arcMenuGroup.layout(arc_x, arc_y, arc_x + width, arc_y + height);
        }

        Log.d("translation===", "layoutSprite");


    }

    @Override
    protected void onDraw(Canvas canvas) {
        Paint p = new Paint();
        p.setStyle(Paint.Style.FILL);

        for (int i = 0; i < list.size(); i++) {
            p.setColor(Color.WHITE);

            p.setStrokeWidth(6);
            if (i < list.size() - 1) {
                AIView v = list.get(i);
                Rect rect=new Rect();
                v.getHitRect(rect);
                int x = rect.centerX();
                int y = rect.centerY();

                AIView v2 = list.get(i + 1);
                Rect rect2=new Rect();
                v2.getHitRect(rect2);
                int x2 = rect2.centerX();
                int y2 = rect2.centerY();

                double A=getA(x, y, x2, y2);
//                 int a[]=   limitsquare(x-x2,y-y2,(int)A);
                int []loc1=calXY(x, y,x2,y2, v.getR());
                int []loc2=calXY(x2,y2,x,y,v2.getR());
                canvas.drawLine(loc1[0], loc1[1], loc2[0], loc2[1], p);
              //  canvas.drawLine(x, y, x2, y2, p);
                p.setStrokeWidth(4);
                p.setColor(Color.BLACK);
              //  canvas.drawLine(x, y, x2, y2, p);
                canvas.drawLine(loc1[0], loc1[1], loc2[0], loc2[1], p);
            }
        }
        super.onDraw(canvas);
    }
    public int[] calXY(int x0,int y0,int x1,int y1,int r) {
        int s[] = new int[2];
        double dis=Math.sqrt((x1-x0)*(x1-x0)+(y1-y0)*(y1-y0));
        int disx=x1-x0;
        int disy=y1-y0;
//        int x = (int) (x0 + r * Math.cos(angle*Math.PI/180));
//        int y = (int) (y0 + r * Math.sin(angle*Math.PI/180)) ;
        int x =(int)(x0+r*disx/dis) ;
        int y = (int)(y0+r*disy/dis) ;
        s[0] = x;
        s[1] = y;
        return s;

    }
    public double getA(int x1,int y1,int x2,int y2){
        double dis=(x1-x2)*(x1-x2)+(y1-y2)*(y1-y2);
        double c=Math.sqrt (dis);
        double a=x2-x1;
        double cosa=a/c;
        double A=Math.acos(cosa);
        return A*180/Math.PI;
    }
//    public int[] limitsquare(int dx, int dy,int ang) {
//        int a[]=new int[2];
//        int angel;
//        if (dx > 0 && dy < 0) {
//            angel = Math.abs(ang);
//            a[0]=180-angel;
//            a[1]=360-angel;
//            //    1
//        } else if (dx < 0 && dy < 0) {
//            angel = Math.abs(ang);
//            a[0]=angel;
//            a[1]=180+angel;
//            //   2
//        } else if (dx < 0 && dy > 0) {
//            angel = Math.abs(ang);
//            a[0]=angel+90;
//            a[1]=360-angel;
//            //3
//        } else if (dx > 0 && dy > 0) {
//
//            angel = Math.abs(ang);
//            a[0]=angel;
//            a[1]=180+angel;
//            //4
//        }
//        return a;
//    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

    }

    public void layoutAI() {
        for (int i = 0; i < list.size(); i++) {
            AIView v = list.get(i);
            int x = v.x;
            int y = v.y;
            int height = v.width;
            int width = v.height;
            v.layout(x, y, x + width, y + height);
            Log.d("wwww====", "height===" + height + "width===" + width);

        }
    }

    IHitEvent iHitEvent;

    public void addHitEvent(IHitEvent iHitEvent) {
        this.iHitEvent = iHitEvent;
    }

    public interface IHitEvent {
        public void hitTarget(AIView v);
    }
}

