package com.example.livecoaching.RenderEngine;

import android.util.Log;
import android.view.SurfaceHolder;

public class TacticThread extends Thread{
    private boolean running;
    private SurfaceHolder surfaceHolder;
    private TacticPanel tacticPanel;

    private static final String TAG = TacticThread.class.getSimpleName();

    public TacticThread(SurfaceHolder holder, TacticPanel panel){
        super();
        this.surfaceHolder = holder;
        this.tacticPanel = panel;
    }

    public void setRunning(boolean b){
        this.running = b;
    }

    @Override
    public void run(){
        long tickCount = 0L;
        Log.d(TAG,"Starting Game loop");
        while (running){
            tickCount ++;
            // update game state here
            // render state to the screen
        }
        Log.d(TAG, "Game loop executed " + tickCount + " times");
    }
}
