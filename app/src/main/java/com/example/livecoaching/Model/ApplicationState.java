package com.example.livecoaching.Model;

import com.example.livecoaching.R;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Arrays;
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

    private ApplicationState(){
        userProfile = new UserProfile();
        offensiveTactics = new ArrayList<>(Arrays.asList(
                new Tactic("Horizontal Stack",0,"Ultimate","Offense",7, R.drawable.stack_h),
                new Tactic("Vertical Stack",1,"Ultimate","Offense",7,R.drawable.stack_v)
        ));

        defensiveTactics = new ArrayList<>(Arrays.asList(
                new Tactic("junk",3,"Ultimate","Defense",7, R.drawable.junk),
                new Tactic("4-4-2",4,"Football","Defense",11,R.drawable.foot_442)
                ));

        // concat for alltactics
        allTactics = offensiveTactics;
        allTactics.addAll(defensiveTactics);

        // sorting lists
        /*
        sort(this.offensiveTactics, Collator.getInstance());
        sort(this.defensiveTactics, Collator.getInstance());
        sort(this.allTactics, Collator.getInstance());
        System.out.println(this.allTactics);
        */
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
}
