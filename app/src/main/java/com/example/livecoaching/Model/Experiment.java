package com.example.livecoaching.Model;

import android.util.Log;

import com.example.livecoaching.Interfaces.TrialOrganiser;
import com.example.livecoaching.Logs.Logger;

import java.util.ArrayList;

public class Experiment implements TrialOrganiser {
    private final String TAG = "Experiment";
    // values
    private String participantID;
    private int indexInTrials;
    private Logger logger;
    private RouteCalculator routeCalculator;
    private int interactionType;
    private int difficulty;

    private ArrayList<Trial> trials;


    public Experiment(String participantID, Logger simpleLogger) {
        this.participantID = participantID;
        this.logger = simpleLogger;
        this.indexInTrials = 0;
        initTrials();
    }

    public void run(){
        trials.get(0).run();
    }

    public void stop(){
        trials.get(indexInTrials).stop();
        Log.d(TAG, "stopping experiment");
    }
    public void runNextTrial(){
        this.indexInTrials ++;
        initTrials();
    }

    public void initTrials() {
        trials = new ArrayList<>();
        // build all the different trials here
        // 3 difficulties x 3 interaction Types x 4 tries
        Trial trial;
        for (int i = 0; i < 3; i ++){// difficulty first
            for (int j = 1; j < 4; j++){ // interaction type
                for (int k = 0; k < 4; k++){
                    trial = new Trial(this.participantID,j,i,this.logger,this);
                    trials.add(trial);
                }
            }
        }

        Log.d(TAG,"trials size : " + trials.size() + "; example : " + trials.get(trials.size()-1));
    }

    // get and set
    public Logger getLogger(){
        return this.logger;
    }

    @Override
    public void launchNextTrial() {
        indexInTrials ++;
        if (indexInTrials < trials.size()){
            trials.get(indexInTrials).run();
        } else {
            // message main activity for end
        }
    }
}
