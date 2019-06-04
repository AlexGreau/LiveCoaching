package com.example.livecoaching.GameObjects;

public class Tactic {

    private String name;
    private String ID;
    private Sports sport;
    private TacticTypes type;
    private int playersNeeded;
    private String creator;
    // TODO : image


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public Sports getSport() {
        return sport;
    }

    public void setSport(Sports sport) {
        this.sport = sport;
    }

    public TacticTypes getType() {
        return type;
    }

    public void setType(TacticTypes type) {
        this.type = type;
    }

    public int getPlayersNeeded() {
        return playersNeeded;
    }

    public void setPlayersNeeded(int playersNeeded) {
        this.playersNeeded = playersNeeded;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }
}
