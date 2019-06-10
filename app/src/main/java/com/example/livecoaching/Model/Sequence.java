package com.example.livecoaching.Model;

import java.util.ArrayList;

public class Sequence {
    private ArrayList<Player> players;
    private Tactic tactic;

    public Sequence(Tactic t, ArrayList<Player> tab){
        this.tactic = t;
        this.players = tab;
    }

    public void drawPLayers(){
        // TODO : get players coordinates and update their positions on the screen
        // For now, just draw them on their goal zones

    }

    public void setBackground(){
        // TODO : if tactic is football; load football field, else load sport's field

    }

    public void differenceToGoal(Player player){
        // TODO : difference between players coordinates and his goal zone
        // TODO : change return type of this function

    }

    public void differenceToStartZone( Player player){
        // TODO : function to guide players to starting points
        // TODO : change return type of this function

    }


}
