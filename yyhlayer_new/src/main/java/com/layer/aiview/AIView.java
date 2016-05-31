package com.layer.aiview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.TextView;

/**
 * Created by jbl on 2016-04-14.
 */
public class AIView extends TextView {
    public int x, y, width, height;
    int i = 0;
    int ceng1 = 10;
    int ceng2 = 20;
    int p_width = 10;
    String oval_bg = "#706ACEE8";
    boolean hitStatus = false;
    Paint p;
    Handler h = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            AIView.this.invalidate();
        }
    };

    public AIView(Context context) {
        super(context);
        init();
    }

    public AIView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AIView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init() {
        p = new Paint(Paint.ANTI_ALIAS_FLAG);
        setLayerType(LAYER_TYPE_SOFTWARE, p);
        this.setTextSize(18);
        this.setTextColor(Color.YELLOW);
        this.setGravity(Gravity.CENTER);
        this.setPadding(20, 0, 20, 0);
        new AiTHread().start();

    }

    public void setHitStatus(boolean hitStatus) {
        this.hitStatus = hitStatus;

    }

    @Override
    protected void onAttachedToWindow() {
        int dis_x, dis_y;//长度
        int foreign_d = (width - 2 * ceng1);
        int dis_ling_rect = ((int) Math.sqrt(width * width + height * height) - foreign_d) / 2;
        dis_x = (int) Math.sqrt(dis_ling_rect * dis_ling_rect / 2);
        dis_y = (int) Math.sqrt(dis_ling_rect * dis_ling_rect / 2);


        topx = x + dis_x - p_width / 2;
        topy = y + dis_y - p_width / 2;
        bottomx = x + width - dis_x + p_width / 2;
        bottomy = y + height - dis_y + p_width / 2;
        super.onAttachedToWindow();
    }

    public int getR() {
        Rect rect = new Rect();
        this.getHitRect(rect);
        return rect.width() / 2;
    }

    @Override
    public void draw(Canvas canvas) {

//        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
//               Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
//               paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
//               canvas.drawRect(0, 0, width, height, paint);
        canvas.drawColor(Color.argb(1, 0, 0, 0));


        if (hitStatus) {
            p.setShadowLayer(7, 0, 0, Color.YELLOW);
        } else {
            p.reset();
        }
        p.setStrokeWidth(p_width);
        p.setColor(Color.parseColor("#1B91AD"));
        p.setStyle(Paint.Style.STROKE);
//               canvas.drawArc(0,0,width,height,30,30,true,p );
        RectF oval = new RectF();                     //RectF对象
        oval.left = 0 + ceng1;                              //左边
        oval.top = 0 + ceng1;                                   //上边
        oval.right = width - ceng1;                             //右边
        oval.bottom = height - ceng1;
        RectF oval2 = new RectF();                     //RectF对象
        oval2.left = 0 + 3 * ceng1;                              //左边
        oval2.top = 0 + 3 * ceng1;                                   //上边
        oval2.right = width - 3 * ceng1;                             //右边
        oval2.bottom = height - 3 * ceng1;
        canvas.save();
        canvas.rotate(i * 5, width / 2, height / 2);
        canvas.drawArc(oval, 120, 60, false, p);
        canvas.drawArc(oval, 240, 60, false, p);
        canvas.drawArc(oval, 0, 60, false, p);
        canvas.restore();
        canvas.save();
        canvas.rotate(-i * 5, width / 2, height / 2);
        canvas.drawArc(oval, 120, 60, false, p);
        canvas.drawArc(oval, 240, 60, false, p);
        canvas.drawArc(oval, 0, 60, false, p);
        canvas.restore();
        p.setColor(Color.parseColor(oval_bg));
        p.setStyle(Paint.Style.FILL);
        canvas.drawOval(oval2, p);
        super.draw(canvas);
    }

    class AiTHread extends Thread {
        @Override
        public void run() {
            while (true) {
                //   Log.d("jia========","ai线程运行中");
                i++;
                try {
                    sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                h.sendEmptyMessage(0);
            }
        }
    }

    int topx, topy, bottomx, bottomy;

    public int getTopX() {
        return topx;
    }

    public int getTopY() {
        return topy;
    }

    public int getBottomX() {
        return bottomx;
    }

    public int getBottomY() {
        return bottomy;
    }
}
