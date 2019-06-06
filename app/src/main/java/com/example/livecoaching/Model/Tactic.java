package com.example.livecoaching.Model;

import com.example.livecoaching.R;

public class Tactic {

    private String name;
    private int ID;
    private String sport;
    private String type;
    private int playersNeeded;
    private String creator;
    private int image;
    private int color;

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
            System.out.println("OffenseColor");
            this.color = R.color.tacticOffense;
        } else if (this.type.equals("Defense")){
            System.out.println("DefenseColor : " + R.color.tacticDefense);
            this.color = R.color.tacticDefense;
        } else {
            System.out.println("otherColor");
            this.color = R.color.tacticOther;
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

    public int getColor(){
        System.out.println("tactic : " + this.name + ", color : " + this.color);
        return this.color;
    }
}
