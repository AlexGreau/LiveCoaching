package com.example.livecoaching.Model;

import android.util.Log;

import com.example.livecoaching.Interfaces.TrialOrganiser;
import com.example.livecoaching.Logs.Logger;

import java.util.ArrayList;

public class Experiment implements TrialOrganiser {
    private final String TAG = "Experiment";
    // values
    private String participantID;
    private Logger logger;

    private int currentDifficulty;
    private int currentInteractionType;
    private int currentIndex;
    private final int maxTrialIndexPerCombo = 3;
    private final int maxDifficultyIndex = 2;
    private final int maxInteractionIndex = 2;

    private int indexInTrials;

    private ArrayList<Trial> trials;


    public Experiment(String participantID, Logger simpleLogger) {
        this.participantID = participantID;
        this.logger = simpleLogger;
        this.indexInTrials = 0;
        initTrials();
        initCurrents();
    }

    public void run() {
        trials.get(0).run();
    }

    public void stop() {
        trials.get(indexInTrials).stop();
        Log.d(TAG, "stopping experiment");
    }

    public void initTrials() {
        trials = new ArrayList<>();
        // build all the different trials here
        // 3 difficulties x 3 interaction Types x 4 tries
        Trial firstTrial = new Trial(participantID, currentInteractionType, currentDifficulty, this.logger, this);
        trials.add(firstTrial);
        Log.d(TAG, "trials size : " + trials.size() + "; example : " + trials.get(trials.size() - 1).getParticipantID() + ", " + trials.get(trials.size() - 1).getDifficulty());
    }

    public void createNextTrial() {
        Trial nextTrial;
        currentIndex++;
        if (currentIndex > maxTrialIndexPerCombo) {
            currentIndex = 0;
            // incrementer difficulte
            currentDifficulty++;
            if (currentDifficulty > maxDifficultyIndex) {
                currentDifficulty = 0;
                currentInteractionType++;
                if (currentInteractionType > maxInteractionIndex) {
                    return;
                }
            }
        }
        nextTrial = new Trial(participantID, currentInteractionType, currentDifficulty, this.logger, this);
        trials.add(nextTrial);
    }

    // get and set
    public Logger getLogger() {
        return this.logger;
    }

    @Override
    public void launchNextTrial() {
        indexInTrials++;
        createNextTrial();
        if (indexInTrials < trials.size()) {
            trials.get(indexInTrials).run();
        } else {
            // message main activity for end
            // todo : actualize main activity UI on the run
        }
    }

    public void initCurrents() {
        currentIndex = 0;
        currentDifficulty = 0;
        currentInteractionType = 0;
    }
}
