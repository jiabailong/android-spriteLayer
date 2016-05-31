package com.layer.baselayer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.FrameLayout;
import android.widget.Scroller;

/**
 * Zooming view. Sc
 */
public class SmoothLayout extends FrameLayout {
    Scroller scroller;
    boolean canmove = false;
UpEvent upEvent;
    public SmoothLayout(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        init(context);
    }

    public SmoothLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        init(context);
    }

    public SmoothLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
        init(context);
    }

    public void init(Context c) {
        scroller = new Scroller(c);
        ViewTreeObserver viewTreeObserver = getViewTreeObserver();
        viewTreeObserver
                .addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

                    @Override
                    public void onGlobalLayout() {
                        int x = SmoothLayout.this.getMeasuredWidth() - SmoothLayout.this.getChildAt(0).getMeasuredWidth();
                        int y = SmoothLayout.this.getMeasuredHeight() - SmoothLayout.this.getChildAt(0).getMeasuredHeight();
                        scrollTo(-x / 2, -y / 2);
                        SmoothLayout.this.getViewTreeObserver()
                                .removeGlobalOnLayoutListener(this);
                        // TODO Auto-generated method stub
                        // int img_height=img.getHeight();
                        // img.layout(0, sy, img_width, sy+img_height);
                        //
                        // sc.layout(0, img_height+sy,
                        // sc.getWidth(),sc.getHeight()+sy);

                        // LayoutParams lp= (LayoutParams) sc.getLayoutParams();
                        // lp.height=lp.height+img_height;
                        // sc.setLayoutParams(lp);
                        // Log.d("view初始化完成", "view初始化完成=="+img_height);
                    }
                });

//		scrollTo(x, y);

    }

    @SuppressLint("NewApi")
    public void smoothBy(int dx, int dy) {
        scroller.startScroll((int) getX(), (int) getY(), dx, dy, 500);
    }
public void addUpEvent(UpEvent upEvent){
    this.upEvent=upEvent;
}
    @Override
    public boolean onTouchEvent( MotionEvent ev) {
        // single touch
        if (ev.getPointerCount() == 1) {
            boolean b = getChildAt(0).dispatchTouchEvent(ev);
            Log.i("jia==", getScrollX() + "单指==" + b);
            if (!b) {
                // getChildAt(0).dispatchTouchEvent(ev);
                moving(ev);
            } else {
                return true;
            }

        }

        //Log.i("jia==", getScrollX() + "==");
        // redraw
        getRootView().invalidate();
        invalidate();

        return true;
    }

    float lastYPosition, lastXPositon;
    float x, y;
    VelocityTracker velocityTracker = VelocityTracker.obtain();

    @SuppressLint("NewApi")
    public void moving(MotionEvent ev) {

        velocityTracker.addMovement(ev);
        velocityTracker.computeCurrentVelocity(100);
        switch (ev.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                canmove = true;
                x = ev.getX();
                y = ev.getY();
                lastXPositon = x;
                lastYPosition = y;
                // Log.i("jia==",
                // lastXPositon+"===down==lastXPositon===lastYPositon"+lastYPosition);
                break;

            case MotionEvent.ACTION_MOVE:
                int curX = (int) ev.getX();
                int curY = (int) ev.getY();
                int dx = (int) (lastXPositon - curX);
                int dy = (int) (lastYPosition - curY);
//			Log.i("jia==",
//					this.getMeasuredWidth() + "===smoothwidth==smoothheight==="
//							+ this.getMeasuredHeight());
//			Log.i("jia==", getScrollX() + "===smoothScrollX==smoothScrollY==="
//					+ getScrollY());
//			Log.i("jia==", getChildAt(0).getMeasuredWidth()
//					+ "===childwidth==childheight==="
//					+ getChildAt(0).getMeasuredHeight());
                lastXPositon = curX;
                lastYPosition = curY;

                if (canmove) {
                    // dx>0向右滑动，dx<0向左滑动
                    //dy>0向下滑动，dy<0向上滑动

                    if ((getScrollX() + dx) < 0) {
                        dx -= (getScrollX() + dx);
                    } else {

                    }
                    if ((getScrollY() + dy) < 0) {
                        dy -= (getScrollY() + dy);
                    }
                    if (getChildAt(0).getMeasuredWidth() <= this.getMeasuredWidth()) {
                        dx = 0;
                    } else {
                        int mx = getChildAt(0).getMeasuredWidth()
                                - this.getMeasuredWidth() - getScrollX();

                        if (mx - dx < 0) {
                            dx -= dx - mx;
                        }
                    }
                    if (getChildAt(0).getMeasuredHeight() <= this.getMeasuredHeight()) {
                        dy = 0;
                    } else {
                        int hy = getChildAt(0).getMeasuredHeight()
                                - this.getMeasuredHeight() - getScrollY();
                        if (hy - dy < 0) {
                            dy -= dy - hy;
                        }
                    }
                    scrollBy(dx, dy);
                }

                //
                break;

            case MotionEvent.ACTION_OUTSIDE:
            case MotionEvent.ACTION_UP:
            if(upEvent!=null){
              int   u_x =(int) ev.getX();
                int u_y = (int)ev.getY();
                int distan_x=(int)(u_x-x);
                int distan_y=(int)(u_y-y);
                upEvent.actionUp(ev,this,distan_x,distan_y);
            }
                canmove = false;
                break;
        }
    }
//	public void initCenter(){
//		int x=SmoothLayout.this.getMeasuredWidth()-SmoothLayout.this.getChildAt(0).getMeasuredWidth();
//		int y=SmoothLayout.this.getMeasuredHeight()-SmoothLayout.this.getChildAt(0).getMeasuredHeight();
//		
//		scrollTo(-x/2, -y/2);
////		
//	}

    public void goToCenter() {
        int x = SmoothLayout.this.getMeasuredWidth() - SmoothLayout.this.getChildAt(0).getMeasuredWidth();
        int y = SmoothLayout.this.getMeasuredHeight() - SmoothLayout.this.getChildAt(0).getMeasuredHeight();

        int pw, ph, sw, sh, sx, sy;
        pw = SmoothLayout.this.getMeasuredWidth();
        ph = SmoothLayout.this.getMeasuredHeight();
        sw = SmoothLayout.this.getChildAt(0).getMeasuredWidth();
        sh = SmoothLayout.this.getChildAt(0).getMeasuredHeight();
        sy = this.getScrollY();
        sx = this.getScrollX();
        scroller.startScroll(sx, sy, -sx - x / 2, -sy - y / 2, 2000);
//		scrollTo(-x/2, -y/2);

//	int dis_y,dis_x;
//	if(sh-sy<ph){
//		dis_x=sx/2-x/2;
//		scrollTo(-x/2, -y/2);
//	}
//	
//	if(sw-sx<pw){
//		dis_y=sy/2-y/2;
//		scrollTo(-x/2, -y/2);
//	}


    }

    @Override
    public void computeScroll() {
        if (scroller.computeScrollOffset()) {
            // View v=getChildAt(0);
            // hitview.scrollTo(scroller.getCurrX(), scroller.getCurrY());
            scrollTo(scroller.getCurrX(), scroller.getCurrY());
            invalidate();
        }
        super.computeScroll();
    }

    public interface UpEvent{
        public void actionUp(MotionEvent ev, SmoothLayout smooth, int distan_x, int distan_y);
    }
}
