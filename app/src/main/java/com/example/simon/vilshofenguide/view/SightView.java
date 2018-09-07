package com.example.simon.vilshofenguide.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.example.simon.vilshofenguide.pathfinding.Sight;

/**
 * Created by Simon on 02.03.2016.
 */
public class SightView extends View{

    private Sight sightToShow;
    private Paint sightTextPaint;

    private void initPaints(){
        this.sightTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    protected  void onDraw(Canvas c){
        super.onDraw(c);
        c.drawText(sightToShow.toString(), 0, 0, sightTextPaint);
    }

    public void setSightToShow(Sight sight){
        this.sightToShow = sight;
        this.invalidate();
        this.requestLayout();
    }

    public SightView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
}
