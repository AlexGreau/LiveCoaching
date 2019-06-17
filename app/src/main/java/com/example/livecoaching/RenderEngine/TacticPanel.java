package com.example.livecoaching.RenderEngine;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.SurfaceHolder;

import com.example.livecoaching.Model.Player;
import com.example.livecoaching.Model.Tactic;
import com.example.livecoaching.R;

import static android.content.ContentValues.TAG;

public class TacticPanel extends SurfaceView implements SurfaceHolder.Callback {
    private TacticThread thread;
    private Player player;
    private Context context;


    public TacticPanel(Context context, AttributeSet attrs){
        super(context);
        init(context);
    }

    private void init(Context context){
        this.context = context;
        getHolder().addCallback(this);
        setFocusable(true);
        thread = new TacticThread(getHolder(), this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        thread.setRunning(true);
        thread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        while (retry) {
            try {
                thread.join();
                retry = false;
            } catch (InterruptedException e){
                // try again to shut down the thread
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (event.getY() > getHeight() - 50) {
                thread.setRunning(false);
                ((Activity)getContext()).finish();
            } else {
                Log.d(TAG, "Coords: x=" + event.getX() + ",y=" + event.getY());
            }
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onDraw(Canvas canvas){
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.GREEN);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.terrain_ultimate);
        canvas.drawBitmap(bitmap,0,0,paint);
    }

    @Override
    protected void onSizeChanged(int w,int h, int oldw,int oldh){
        super.onSizeChanged(w,h,oldw,oldh);

    }
}
