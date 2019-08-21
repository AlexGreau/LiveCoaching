package com.example.livecoaching.Model;

import android.location.Location;

import com.example.livecoaching.Logs.Logger;

public class TestExperiment extends Experiment {

    public TestExperiment(String participantID, Logger simpleLogger) {
        super("testRun", simpleLogger);
    }

    @Override
    public void simpleLogIt(Trial trial){
        return;
    }

    @Override
    public void completeLogIt(Trial trial, Location loc, long time, int partOfroute){
        return;
    }

    @Override
    public void initCurrents(){
        setCurrentDifficulty(1);
        setCurrentInteractionType(2);
    }
}
