package com.layer.aiview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by jbl on 2016-04-14.
 */
public class AIViewSurface extends SurfaceView implements SurfaceHolder.Callback {
    public int x,y,width,height;
    SurfaceHolder holder;
    int i=0;
    int ceng1=10;
    int ceng2=20;
    public AIViewSurface(Context context) {
        super(context);
        init();
    }
    public AIViewSurface(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    public AIViewSurface(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    public void init(){
        width=200;
        height=200;
        holder=getHolder();
        holder.setFormat(PixelFormat.RGBA_8888);
        holder.addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        new AiTHread().start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }
    class AiTHread extends Thread{
        @Override
        public void run() {
           while(true){
               Log.d("jia========", "ai线程运行中");
               i++;
               try {
                   sleep(500);
               } catch (InterruptedException e) {
                   e.printStackTrace();
               }
               Canvas canvas=holder.lockCanvas();

               canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
               Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
               paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
               canvas.drawRect(0, 0, width, height, paint);
               canvas.drawColor(Color.argb(1,0,0,0));
               canvas.rotate(i * 10, width / 2, height / 2);
               Paint p=new Paint(Paint.ANTI_ALIAS_FLAG);
               p.setStrokeWidth(20);
               p.setColor(Color.parseColor("#1B91AD"));
               p.setStyle(Paint.Style.STROKE);
//               canvas.drawArc(0,0,width,height,30,30,true,p );
               RectF oval=new RectF();                     //RectF对象
               oval.left=0+ceng1;                              //左边
               oval.top=0+ceng1;                                   //上边
               oval.right=width-ceng1;                             //右边
               oval.bottom=height-ceng1;
               canvas.drawArc(oval,120,40,false,p);
               canvas.drawArc(oval,240,40,false,p);
               canvas.drawArc(oval,0,40,false,p);
               if(canvas!=null){
                   holder.unlockCanvasAndPost(canvas);
               }
           }
        }
    }
}
