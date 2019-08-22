package com.example.livecoaching.Interfaces;


import com.example.livecoaching.Model.Trial;

public interface ExperimentVisualizer {
    void handleEndOfExperiment();
    void handleTrialPrinting(int index, Trial trial);
    void handleEndOfTrial(int index, Trial trial);
}
