package com.layer.baselayer;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jbl on 2016-04-25.
 */
public class MapLayer extends TextView {
    List<PointType> list = new ArrayList<PointType>();
    PointType sprite;
    Paint paint;
    public int screenWidth = 600;
    public int screenHeight = 1024;
    float r_width,r_height;
    int layer_w,layer_h;
    float scaleW,scaleH;
    float scale_rectw,scale_recth;//相对地图
    int rect_x,rect_y;
    public MapLayer(Context context) {
        super(context);
        init();
    }

    public MapLayer(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MapLayer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);

        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(5);
        paint.setStyle(Paint.Style.FILL);
        // Important for certain APIs
        setLayerType(LAYER_TYPE_SOFTWARE, paint);
    }

    public void setLimitData(int width, int height,int mine_w,int mine_h) {

         scaleW = (float) width / (float) mine_w;
         scaleH = (float) height / (float)  mine_h;
        layer_w=width;
        layer_h=height;
        r_height=(float)layer_h/scaleW;
        r_width=(float)layer_w/scaleW;
        rect_x=(int)(mine_w-r_width)/2;
        rect_y=(int)(mine_h-r_height)/2;


    }

    public void addSprite(int x, int y) {
        sprite = new PointType();
        sprite.x =Math.round( x/scaleW)+rect_x;
        sprite.y = Math.round(y/scaleW)+rect_y;
    }
    public void moveSprite(int x,int y){
        sprite.x =Math.round( x/scaleW)+rect_x;
        sprite.y = Math.round(y/scaleW)+rect_y;
        this.invalidate();
    }

    public void addAi(int x, int y) {
        PointType pointType = new PointType();
        pointType.x =Math.round( x/scaleW)+rect_x;
        pointType.y = Math.round(y/scaleW)+rect_y;
        list.add(pointType);
    }

    class PointType {
        int x, y;

    }

    @Override
    public void draw(Canvas canvas) {
        Rect rect = new Rect(rect_x, rect_y, rect_x+(int)r_width,rect_y+(int)r_height);
        paint.setShadowLayer(3, 0, 0, Color.WHITE);
        paint.setColor(Color.parseColor("#706ACEE8"));
        paint.setStyle(Paint.Style.FILL);
        canvas.drawRect(rect, paint);
        super.draw(canvas);
        paint.setStyle(Paint.Style.FILL);
        paint.setShadowLayer(3, 0, 0, Color.YELLOW);
        paint.setColor(Color.RED);
        RectF re = new RectF(sprite.x, sprite.y,
                sprite.x + 5, sprite.y + 5);
        canvas.drawOval(re, paint);
        paint.setColor(Color.WHITE);
        for (int i = 0; i < list.size(); i++) {
            PointType pointType = list.get(i);
//    canvas.drawPoint(pointType.x,pointType.y,paint);
            RectF rectF = new RectF(pointType.x, pointType.y,
                    pointType.x + 5, pointType.y + 5);
            canvas.drawOval(rectF, paint);
        }
    }
}
