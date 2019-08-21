package com.example.livecoaching.Interfaces;


import com.example.livecoaching.Model.Trial;

public interface ExperimentVisualizer {
    void handleEndOfExperiment();
    void handleEndOfTrial(int index, Trial trial);
}
