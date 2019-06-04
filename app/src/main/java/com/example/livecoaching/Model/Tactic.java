package com.example.livecoaching.Model;

public class Tactic {

    private String name;
    private int ID;
    private String sport;
    private String type;
    private int playersNeeded;
    private String creator;
    private int image;

    Tactic(String name, int id, String sport, String typ, int pNeeded){
        this.name = name;
        this.ID = id;
        this.sport = sport;
        this.type = typ;
        this.playersNeeded = pNeeded;
        // for now
        this.creator = "TITI";
        this.image = 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getSport() {
        return sport;
    }

    public void setSport(String sport) {
        this.sport = sport;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
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

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
