package com.example.livecoaching.Model;

import com.example.livecoaching.R;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static java.util.Collections.sort;

public class ApplicationState {
    private static final ApplicationState ourInstance = new ApplicationState();

    public static ApplicationState getInstance() {
        return ourInstance;
    }

    // Fields
    private UserProfile userProfile;
    private List<Tactic> offensiveTactics;
    private List<Tactic> defensiveTactics;
    private List<Tactic> allTactics;
    private List<Tactic> displayedList;

    private ApplicationState(){
        userProfile = new UserProfile();
        offensiveTactics = new ArrayList<>(Arrays.asList(
                new Tactic("Horizontal Stack",0,"Ultimate","Offense",7, R.drawable.stack_horizontal),
                new Tactic("Vertical Stack",1,"Ultimate","Offense",7,R.drawable.stack_v)
        ));

        defensiveTactics = new ArrayList<>(Arrays.asList(
                new Tactic("Junk",3,"Ultimate","Defense",7, R.drawable.junk),
                new Tactic("4-4-2",4,"Football","Defense",11,R.drawable.foot_442)
                ));

        allTactics = new ArrayList<Tactic>();
        displayedList = new ArrayList<Tactic>();
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

    public List<Tactic> getOffensiveTactics(){
        return this.offensiveTactics;
    }

    public List<Tactic> getDefensisveTactics(){
        return  this.defensiveTactics;
    }

    public List<Tactic> getAllTactics(){
        return this.allTactics;
    }

    public UserProfile getUserProfile(){
        return userProfile;
    }

    public List<Tactic> getDisplayedList(){
        return this.displayedList;
    }
    public List<Tactic> filterList(String sport, String type){
        this.displayedList.clear();
        for (Tactic t : ApplicationState.getInstance().getAllTactics()) {
            if (t.getType().equals(type) || type.startsWith("All")){
                if (t.getSport().equals(sport) || sport.startsWith("All")){
                    this.displayedList.add(t);
                }
            } else if (t.getSport().equals(sport) || sport.startsWith("All")){
                if (t.getType().equals(type) || type.startsWith("All")){
                    this.displayedList.add(t);
                }
            }
        }

        return this.displayedList;
    }
}
