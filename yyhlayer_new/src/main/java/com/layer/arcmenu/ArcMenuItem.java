package com.layer.arcmenu;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.TextView;

/**
 * Created by jbl on 2016-04-14.
 */
public class ArcMenuItem extends TextView {
    public int x, y;
    Paint p;
    int ceng1=10;
    int ceng2=20;
    int p_width=3;
    public ArcMenuItem(Context context) {
        super(context);
        init();
    }

    public ArcMenuItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ArcMenuItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init() {
        p=new Paint(Paint.ANTI_ALIAS_FLAG);
        setLayerType(LAYER_TYPE_SOFTWARE, p);
        this.setTextColor(Color.WHITE);
        this.setGravity(Gravity.CENTER);
    }

    @Override
    public void draw(Canvas canvas) {
            p.setShadowLayer(7, 0, 0, Color.YELLOW);
        p.setStrokeWidth(p_width);
        p.setColor(Color.parseColor("#1B91AD"));
        p.setStyle(Paint.Style.STROKE);
//               canvas.drawArc(0,0,width,height,30,30,true,p );
        RectF oval=new RectF();                     //RectF对象
        oval.left=0+ceng1;                              //左边
        oval.top=0+ceng1;                                   //上边
        oval.right=this.getMeasuredWidth()-ceng1;                             //右边
        oval.bottom=this.getMeasuredHeight()-ceng1;
//        p.setStyle(Paint.Style.FILL);
        canvas.drawOval(oval, p);
        p.setStyle(Paint.Style.FILL);
        RectF oval2=new RectF();                     //RectF对象
        oval2.left=0+ceng2;                              //左边
        oval2.top=0+ceng2;                                   //上边
        oval2.right=this.getMeasuredWidth()-ceng2;                             //右边
        oval2.bottom=this.getMeasuredHeight()-ceng2;
//        p.setStyle(Paint.Style.FILL);
        canvas.drawOval(oval2, p);
        super.draw(canvas);
    }
}
