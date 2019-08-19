package com.example.livecoaching.Model;

public class Trial {

    private int interactionType;
    private int trajectory;

    private Double totalTime;
    private Double totalDistanceParcourue;
    private Double totalDistanceTheorique;

    public Trial (){
        this.totalTime = 0.0;
        this.totalDistanceParcourue = 0.0;
        this.totalDistanceTheorique = 0.0;
    }
}
