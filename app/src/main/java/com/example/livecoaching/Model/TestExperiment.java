package com.example.livecoaching.Model;

import android.location.Location;

import com.example.livecoaching.Interfaces.ExperimentVisualizer;
import com.example.livecoaching.Logs.Logger;

public class TestExperiment extends Experiment {

    public TestExperiment(Logger simpleLogger, ExperimentVisualizer visu) {
        super("testRun", simpleLogger,visu);
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
