package com.example.livecoaching.Model;

import android.location.Location;
import android.util.Log;

import com.example.livecoaching.Communication.Server;
import com.example.livecoaching.Interfaces.Decoder;
import com.example.livecoaching.Interfaces.ExperimentVisualizer;
import com.example.livecoaching.Interfaces.TrialOrganiser;
import com.example.livecoaching.Logs.Logger;

import java.util.ArrayList;

public class Experiment implements TrialOrganiser, Decoder {
    private final String TAG = "Experiment";
    // values
    private String participantID;
    private Logger logger;
    private Server server;

    private int currentDifficulty;
    private int currentInteractionType;
    private int currentIndex;
    private final int maxTrialIndexPerCombo = 3;
    private final int maxDifficultyIndex = 2;
    private final int maxInteractionIndex = 2;

    private int indexInTrials;

    private ArrayList<Trial> trials;

    private boolean isStartingRunningLog = true;

    private ExperimentVisualizer visualizer;


    public Experiment(String participantID, Logger simpleLogger, ExperimentVisualizer visu) {
        this.participantID = participantID;
        this.visualizer = visu;
        this.logger = simpleLogger;
        this.indexInTrials = 0;
        initCurrents();
        initTrials();
    }

    public void run() {
        server = new Server(this);
    }

    public void stop() {
        trials.get(indexInTrials).stop();
        this.visualizer.handleEndOfExperiment();
        Log.d(TAG, "stopping experiment");
    }

    public void initTrials() {
        trials = new ArrayList<>();
        // build all the different trials here
        // 3 difficulties x 3 interaction Types x 4 tries
        Trial firstTrial = new Trial(participantID, currentInteractionType, currentDifficulty, this);
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
        nextTrial = new Trial(participantID, currentInteractionType, currentDifficulty, this);
        trials.add(nextTrial);
    }

    // get and set
    public Logger getLogger() {
        return this.logger;
    }

    @Override
    public void launchNextTrial() {
        isStartingRunningLog = true;
        indexInTrials++;
        createNextTrial();
        if (indexInTrials < trials.size()) {
            trials.get(indexInTrials);
        } else {
            // message main activity for end
            // todo : actualize main activity UI on the run
        }
    }

    @Override
    public String decodeMessage(String msg) {
        Trial concernedTrial = trials.get(indexInTrials);
        String replyMsg = "";
        // split message
        String[] extracts = msg.split("_");
        long time = Long.parseLong(extracts[extracts.length - 1]);
        String[] parts = extracts[0].split(":");
        String senderState = parts[0];
        int partOfroute = 0;
        // interpret results
        if (senderState.equals("Ready")) {
            replyMsg = "continue:" + concernedTrial.getInteractionType();
            if (parts.length >= 2) {
                logger.getLogsArray().clear();
                completeLogIt(concernedTrial, concernedTrial.parseInfos(parts[1]), time, partOfroute);
                concernedTrial.initRouteCalculator(concernedTrial.getActualLocation());
            }
        } else if (senderState.equals("Running")) {
            if (isStartingRunningLog) {
                concernedTrial.setStartingTime(time);
                isStartingRunningLog = false;
            }
            System.out.println("detected " + senderState);
            replyMsg = "";
            if (parts.length >= 2) {
                partOfroute = Integer.parseInt(parts[2]);
                completeLogIt(concernedTrial, concernedTrial.parseInfos(parts[1]), time, partOfroute);
            }
        } else if (senderState.equals("Stop")) {
            System.out.println("detected " + senderState);
            if (parts.length >= 2) {
                concernedTrial.calculateTotalTimeUntil(time);
                concernedTrial.calculateTheoricDistance();
                completeLogIt(concernedTrial, concernedTrial.parseInfos(parts[1]), time, partOfroute);
                simpleLogIt(concernedTrial);
            }
            replyMsg = "";
            stop();
        } else if (senderState.equals("End")) {
            replyMsg = "reset";
        } else if (senderState.equals("Asking")) {
            completeLogIt(concernedTrial, concernedTrial.parseInfos(parts[1]), time, partOfroute);
            replyMsg = "route:" + format(concernedTrial.getRouteCalculator().getActualRoute());
        }

        return replyMsg;
    }

    private String format(ArrayList<Location> locs) {
        // formats the array of location into a sendable message
        String res = "";
        for (Location loc : locs) {
            res += loc.getLatitude() + "-" + loc.getLongitude() + ";";
        }
        return res;
    }

    public void initCurrents() {
        currentIndex = 0;
        currentDifficulty = 0;
        currentInteractionType = 0;
    }

    public void completeLogIt(Trial trial, Location loc, long time, int partOfroute) {
        logger.writeCompleteLog(participantID,
                trial.getInteractionString(trial.getInteractionType()),
                trial.getDifficulty(),
                indexInTrials,
                partOfroute,
                loc,
                time);
    }

    public void simpleLogIt(Trial trial) {
        // launch calculations of data
        // log these datas
        logger.writeSimpleLog(participantID,
                trial.getInteractionString(trial.getInteractionType()),
                trial.getDifficulty(),
                indexInTrials,
                trial.getTheoricDistance(),
                trial.getTotalTime(),
                trial.getTotalDistance()
        );
    }

    public void updateTimeTotal(Trial trial, long time) {
        long before = trial.getTotalTime();

    }

    public int getCurrentDifficulty() {
        return currentDifficulty;
    }

    public void setCurrentDifficulty(int currentDifficulty) {
        this.currentDifficulty = currentDifficulty;
    }

    public int getCurrentInteractionType() {
        return currentInteractionType;
    }

    public void setCurrentInteractionType(int currentInteractionType) {
        this.currentInteractionType = currentInteractionType;
    }

    public int getCurrentIndex() {
        return currentIndex;
    }

    public void setCurrentIndex(int currentIndex) {
        this.currentIndex = currentIndex;
    }
}
