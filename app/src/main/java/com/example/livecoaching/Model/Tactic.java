package com.example.livecoaching.Model;

import com.example.livecoaching.R;

import java.util.ArrayList;

public class Tactic {

    private String name;
    private int ID;
    private String sport;
    private String type;
    private int playersNeeded;
    private String creator;
    private int image;
    private String color;
    private ArrayList finishZones;
    private ArrayList startZones;

    Tactic(String name, int id, String sport, String typ, int pNeeded, int img){
        this.name = name;
        this.ID = id;
        this.setSport(sport);
        this.setType(typ);
        this.playersNeeded = pNeeded;
        // for now
        this.creator = "TITI";
        this.image = img;
    }

    Tactic(String name, int id, String sport, String typ, int pNeeded, int img, ArrayList zones_start, ArrayList zones_finish){
        this.name = name;
        this.ID = id;
        this.setSport(sport);
        this.setType(typ);
        this.playersNeeded = pNeeded;
        // for now
        this.creator = "TITI";
        this.image = img;
        this.startZones = zones_start;
        this.finishZones = zones_finish;
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
        // set the color accordingly
        if (this.type.equals("Offense")){
            this.color = "#d13a58";
        } else if (this.type.equals("Defense")){
            this.color = "#84b3ed";
        } else {
            this.color = "#ed6b34";
        }
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

    public String getColor(){
        return this.color;
    }
}
