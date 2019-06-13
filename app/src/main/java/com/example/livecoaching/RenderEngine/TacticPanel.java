package com.example.livecoaching.RenderEngine;

import android.content.Context;
import android.view.SurfaceView;
import android.view.SurfaceHolder;

import com.example.livecoaching.Model.Tactic;

public class TacticPanel extends SurfaceView implements SurfaceHolder.Callback {
    private TacticThread thread;

    public TacticPanel(Context context){
        super(context);
        getHolder().addCallback(this);
        thread = new TacticThread();
        setFocusable(true);
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
}
