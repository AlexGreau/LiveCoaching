package com.example.livecoaching.Model;

import android.bluetooth.BluetoothDevice;

import com.example.livecoaching.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Filter;
import java.util.logging.LogRecord;

public class ApplicationState {
    private static final ApplicationState ourInstance = new ApplicationState();

    public static ApplicationState getInstance() {
        return ourInstance;
    }

    // Fields
    private UserProfile userProfile;
    private ArrayList<Tactic> offensiveTactics;
    private ArrayList<Tactic> defensiveTactics;
    private ArrayList<Tactic> allTactics;
    private ArrayList<Tactic> displayedList;
    private ArrayList<Player> playersConnected;
    private ArrayList<BluetoothDevice>connectedDevices;

    private ApplicationState(){
        userProfile = new UserProfile();
        allTactics = new ArrayList<Tactic>();
        displayedList = new ArrayList<Tactic>();
        playersConnected = new ArrayList<Player>();
        connectedDevices = new ArrayList<BluetoothDevice>();
        offensiveTactics = new ArrayList<>(Arrays.asList(
                new Tactic("Horizontal Stack",0,"Ultimate","Offense",7, R.drawable.stack_horizontal),
                new Tactic("Vertical Stack",1,"Ultimate","Offense",7,R.drawable.stack_v)
        ));

        defensiveTactics = new ArrayList<>(Arrays.asList(
                new Tactic("Junk",3,"Ultimate","Defense",7, R.drawable.junk),
                new Tactic("4-4-2",4,"Football","Defense",11,R.drawable.foot_442)
                ));

        // concat for alltactics
        allTactics.addAll(offensiveTactics);
        allTactics.addAll(defensiveTactics);

        // sorting lists
        Comparator<Tactic> tacticNamesComparator =  new Comparator<Tactic>() {
            @Override
            public int compare(Tactic o1, Tactic o2) {
                return o1.getName().compareTo(o2.getName());
            }
        };
        offensiveTactics.sort(tacticNamesComparator);
        defensiveTactics.sort(tacticNamesComparator);
        allTactics.sort(tacticNamesComparator);
        displayedList.addAll(this.allTactics);
    }

    public ArrayList<Tactic> getOffensiveTactics(){
        return this.offensiveTactics;
    }

    public ArrayList<Tactic> getDefensisveTactics(){
        return  this.defensiveTactics;
    }

    public ArrayList<Tactic> getAllTactics(){
        return this.allTactics;
    }

    public UserProfile getUserProfile(){
        return userProfile;
    }

    public ArrayList<Tactic> getDisplayedList(){
        return this.displayedList;
    }

    Filter filter = new Filter() {
        @Override
        public boolean isLoggable(LogRecord record) {
            return false;
        }
    };

    public ArrayList<Player> getPlayersConnected(){
        return this.playersConnected;
    }

    public void addPlayer(Player p){
        this.playersConnected.add(p);
    }

    public void resetPlayers(){
        this.playersConnected.clear();
    }

    public void setPlayersConnected(ArrayList<Player> listOfPlayers){
        this.playersConnected.clear();
        this.playersConnected.addAll(listOfPlayers);
    }

    public ArrayList<BluetoothDevice> getConnectedDevices(){
        return connectedDevices;
    }

    public void setConnectedDevices(ArrayList<BluetoothDevice> listOfDevices){
        this.connectedDevices.clear();
        this.connectedDevices.addAll(listOfDevices);
    }
}
