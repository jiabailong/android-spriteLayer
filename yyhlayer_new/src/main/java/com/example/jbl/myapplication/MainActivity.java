package com.example.jbl.myapplication;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.jbl.yyhlayer_new.R;
import com.layer.aiview.AIView;
import com.layer.baselayer.BaseLayer;
import com.layer.baselayer.MapLayer;
import com.layer.baselayer.SmoothLayout;
import com.layer.sprite.SpriteView;
import com.layer.arcmenu.ArcMenuGroup;
import com.layer.arcmenu.ArcMenuItem;

public class MainActivity extends Activity implements SmoothLayout.UpEvent,BaseLayer.IHitEvent,
        ArcMenuGroup.ArcMenuItemClick,View.OnClickListener {
    SpriteView spriteView;
    BaseLayer bsBaseLayer;
    SmoothLayout smoothLayout;
    MapLayer mapLayer;
    int aiview_width=200,aiview_height=200;
    int sprite_initx=500,sprite_inity=500;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout frameLayout = new FrameLayout(this);


        bsBaseLayer=createBaseLayer();

        smoothLayout= linkSmooth(bsBaseLayer);

        frameLayout.addView(smoothLayout);

        mapLayer= addMapLayer(frameLayout);

        setContentView(frameLayout);
        addAiView(bsBaseLayer);
        addSprite(bsBaseLayer);
        Button b=new Button(this);
        b.setBackgroundResource(R.drawable.point_fx);
        b.setOnClickListener(this);
        FrameLayout.LayoutParams lp=new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT);
        lp.gravity=Gravity.BOTTOM|Gravity.RIGHT;
        frameLayout.addView(b,lp);



    }
    public  SmoothLayout linkSmooth(BaseLayer bsBaseLayer){
        SmoothLayout   smooth = new SmoothLayout(this);
        smooth.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT
                , ViewGroup.LayoutParams.MATCH_PARENT));

        smooth.addView(bsBaseLayer);
        smooth.addUpEvent(this);
        return smooth;
    }
    public BaseLayer createBaseLayer(){
        BitmapDrawable bg = (BitmapDrawable) getResources().getDrawable(R.drawable.bg);
        Bitmap bmp = bg.getBitmap();
        BaseLayer   bs = new BaseLayer(this);
        bs.addHitEvent(this);
        bs.setBackgroundResource(R.drawable.bg);
        bs.setLayoutParams(new ViewGroup.LayoutParams(bmp.getWidth()
                , bmp.getHeight()));
        bs.bg_width=bmp.getWidth();
        bs.bg_height=bmp.getHeight();
        return bs ;
    }
    public MapLayer addMapLayer(FrameLayout frameLayout){
        MapLayer mpLayer = new MapLayer(this);

        mpLayer.setBackgroundColor(Color.parseColor("#50000000"));
//        mapLayer.setBackgroundDrawable(colorDrawable);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(mpLayer.screenWidth / 4,
                mpLayer.screenHeight / 5);
        mpLayer.setLimitData(bsBaseLayer.bg_width, bsBaseLayer.bg_height, mpLayer.screenWidth / 4
                , mpLayer.screenHeight / 5);
        lp.gravity = Gravity.BOTTOM | Gravity.LEFT;
        lp.leftMargin = 10;
        lp.bottomMargin = 10;
        frameLayout.addView(mpLayer, lp);
        return mpLayer;
    }
    public void addSprite(BaseLayer bsBaseLayer){
        spriteView = new SpriteView(this);
        spriteView.setImageResource(R.drawable.car);
        spriteView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        spriteView.x = sprite_initx;
        spriteView.y = sprite_inity;
        mapLayer.addSprite(300, 300);
        bsBaseLayer.addSprite(spriteView, sprite_initx, sprite_inity);
    }
    public void addAiView(BaseLayer bsBaseLayer){



        AIView aiView1 = createAIView(300,100);
        AIView aiView2 = createAIView(10,500);
        AIView aiView3 = createAIView(600,500);
        AIView aiView4 = createAIView(300,300);
        bsBaseLayer.addAi(aiView1);
        bsBaseLayer.addAi(aiView2);
        bsBaseLayer.addAi(aiView3);
        bsBaseLayer.addAi(aiView4);
        mapLayer.addAi(aiView1.x, aiView1.y);
        mapLayer.addAi(aiView2.x, aiView2.y);
        mapLayer.addAi(aiView3.x, aiView3.y);
        mapLayer.addAi(aiView4.x, aiView4.y);
    }

    public AIView createAIView(int x,int y){
        AIView aiView = new AIView(this);

        aiView.x = x;
        aiView.y = y;
        aiView.width = aiview_width;
        aiView.height = aiview_height;
        aiView.setText("Supermarket");
        aiView.setLayoutParams(new ViewGroup.LayoutParams(aiview_width, aiview_height));
        return aiView;
    }

    int firstx, firsty;


    @Override
    public void actionUp(MotionEvent ev, SmoothLayout smooth, int distan_x, int distan_y) {
        int x = (int) ev.getX();
        int y = (int) ev.getY();
        if (distan_x < 10 && distan_y < 10) {
            bsBaseLayer.moveSprite(x + smoothLayout.getScrollX(), y + smoothLayout.getScrollY());
            mapLayer.moveSprite(x + smoothLayout.getScrollX(), y + smoothLayout.getScrollY());
        }

    }

    @Override
    public void hitTarget(AIView v) {
        ArcMenuGroup arcMenuGroup=new ArcMenuGroup(this);
        getArcItemData(arcMenuGroup);
        arcMenuGroup.addArcClickListener(this);
        int width=v.getWidth()+200;
        int height=v.getHeight()+200;
        FrameLayout.LayoutParams lp=new FrameLayout.LayoutParams(width,height);
        int x=v.x+v.getWidth()/2;
        int y=v.y+v.getHeight()/2;
        bsBaseLayer.addArcMenu(arcMenuGroup,x,y,lp);
    }
public void getArcItemData(ArcMenuGroup arcMenuGroup ){
    FrameLayout.LayoutParams lps=new FrameLayout.LayoutParams(100,100);
    arcMenuGroup.addArcItem(createArcItem("2"), lps);
    arcMenuGroup.addArcItem(createArcItem("21"), lps);
    arcMenuGroup.addArcItem(createArcItem("22"), lps);
    arcMenuGroup.addArcItem(createArcItem("23"), lps);
    arcMenuGroup.addArcItem(createArcItem("24"), lps);
}
    public ArcMenuItem createArcItem(String s){

        ArcMenuItem arcMenuItem=   new ArcMenuItem(this);
        arcMenuItem.setText(s);
        return arcMenuItem;
    }
    @Override
    public void arcItemClick(View v) {
        ArcMenuItem arcMenuItem= (ArcMenuItem) v;
        Toast.makeText(arcMenuItem.getContext(),
                arcMenuItem.getText()+"====",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View view) {

    }
}
