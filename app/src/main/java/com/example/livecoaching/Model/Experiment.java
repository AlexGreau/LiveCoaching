package com.example.livecoaching.Model;

import android.location.Location;
import android.util.Log;

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

    private int currentDifficulty;
    private int currentInteractionType;
    private int currentIndex;
    private final int maxTrialIndexPerCombo = 3;
    private final int maxDifficultyIndex = 2;
    private final int maxInteractionIndex = 2;
    private final int maxIndex = maxTrialIndexPerCombo * maxDifficultyIndex * maxInteractionIndex;

    private int indexInTrials;
    private ArrayList<Trial> trials;
    private boolean isStartingRunningLog = true;
    private ExperimentVisualizer visualizer;
    private boolean experimentIsRunning;
    private boolean trialIsRunning = false;

    private String replyMsg = "";


    public Experiment(String participantID, Logger simpleLogger, ExperimentVisualizer visu) {
        this.participantID = participantID;
        this.visualizer = visu;
        this.logger = simpleLogger;
        this.indexInTrials = 0;
        initCurrents();
        initTrials();
    }

    public void run() {
        experimentIsRunning = true;
    }

    public void stopCurrentTrial() {
        visualizer.handleEndOfTrial(indexInTrials, trials.get(indexInTrials));
        trialIsRunning = false;
        trials.get(indexInTrials).stop();
    }

    public void skipToNextTrial() {
        trialIsRunning = false;
    }

    public void endExp() {
        experimentIsRunning = false;
        replyMsg = "stop";
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
        currentIndex++;
        if (currentIndex > maxTrialIndexPerCombo) {
            currentIndex = 0;
            currentDifficulty++;
            if (currentDifficulty > maxDifficultyIndex) {
                currentDifficulty = 0;
                currentInteractionType++;
                if (currentInteractionType > maxInteractionIndex) {
                    endExp();
                    return;
                }
            }
        }
        Trial nextTrial = new Trial(participantID, currentInteractionType, currentDifficulty, this);
        trials.add(nextTrial);
    }

    // get and set
    public Logger getLogger() {
        return this.logger;
    }

    @Override
    public void launchNextTrial() {
        isStartingRunningLog = true;
        experimentIsRunning = true;
        indexInTrials++;
        if (indexInTrials <= maxIndex) {
            createNextTrial();
        } else {
            indexInTrials--;
            this.experimentIsRunning = false;
            endExp();
        }
    }

    @Override
    public String decodeMessage(String msg) {
        Trial concernedTrial = trials.get(indexInTrials);
        replyMsg = "";
        if (!this.experimentIsRunning) {
            replyMsg = "stop";
            return replyMsg;
        }
        // split message
        String[] extracts = msg.split("_");
        long time = Long.parseLong(extracts[extracts.length - 1]);
        String[] parts = extracts[0].split(":");
        String senderState = parts[0];
        int partOfroute = 0;
        int direction = 0;
        // interpret results
        if ( senderState.equals("Ready")) {
            replyMsg = "continue:" + concernedTrial.getInteractionType();
            trialIsRunning = true;
            if (parts.length >= 2) {
                logger.getLogsArray().clear();
                visualizer.handleStartOfTrial();
                completeLogIt(concernedTrial, concernedTrial.parseInfos(parts[1]), time, partOfroute);
                concernedTrial.initRouteCalculator(concernedTrial.getActualLocation());
            }
        } else if (senderState.equals("Running")) {
            System.out.println("detected " + senderState);
            replyMsg = "";
            if (isStartingRunningLog) {
                concernedTrial.setStartingTime(time);
                concernedTrial.calculateTheoricDistance();
                isStartingRunningLog = false;
            }
            if (parts.length >= 2) {
                partOfroute = Integer.parseInt(parts[2]);
                concernedTrial.actualizeNextCP(partOfroute);
                completeLogIt(concernedTrial, concernedTrial.parseInfos(parts[1]), time, partOfroute);
                direction = Integer.parseInt(parts[3]);
                concernedTrial.setDirection(direction);
                showProgressOnScreen(concernedTrial);
            }
            if (!trialIsRunning) {
                replyMsg = "stop";
            }
        } else if (senderState.equals("Stop")) {
            trialIsRunning = false;
            System.out.println("detected " + senderState);
            if (parts.length >= 2) {
                concernedTrial.setSuccess(Boolean.parseBoolean(parts[2]));
                concernedTrial.calculateTotalTimeUntil(time);
                completeLogIt(concernedTrial, concernedTrial.parseInfos(parts[1]), time, partOfroute);
                simpleLogIt(concernedTrial);
            }
            replyMsg = "";
            stopCurrentTrial();
            launchNextTrial();
        } else if (senderState.equals("Asking")) {
            completeLogIt(concernedTrial, concernedTrial.parseInfos(parts[1]), time, partOfroute);
            replyMsg = "route:" + format(concernedTrial.getRouteCalculator().getActualRoute());
        }

        return replyMsg;
    }

    public void showProgressOnScreen(Trial trial) {
        visualizer.handleTrialPrinting(indexInTrials, trial);
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
        logger.writeSimpleLog(participantID,
                trial.getInteractionString(trial.getInteractionType()),
                trial.getDifficulty(),
                indexInTrials,
                trial.getTheoricDistance(),
                trial.getTotalDistance(),
                trial.getTotalTime(),
                trial.getSuccess()
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
