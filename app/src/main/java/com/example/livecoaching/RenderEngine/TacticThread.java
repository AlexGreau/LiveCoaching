package com.example.livecoaching.RenderEngine;

public class TacticThread extends Thread{
    private boolean running;
    public void setRunning(boolean b){
        this.running = b;
    }

    @Override
    public void run(){
        while (running){
            // update game state here
            // render state to the screen
        }
    }
}
