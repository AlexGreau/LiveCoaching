package com.example.livecoaching;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AsyncPlayer;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.livecoaching.Adapter.TacticsAdapter;
import com.example.livecoaching.Model.ApplicationState;
import com.example.livecoaching.Model.Tactic;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements AdapterView.OnItemSelectedListener, TacticsAdapter.TacticClickListener {
    private String sport;
    private String tacticType;
    private TacticsAdapter tacticsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
        // types spinner
        Spinner tacticFilter = findViewById(R.id.spinner_types);
        ArrayAdapter<CharSequence> adapterTypes = ArrayAdapter.createFromResource(this,
                R.array.tacticTypes, android.R.layout.simple_spinner_item);
        adapterTypes.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tacticFilter.setOnItemSelectedListener(this);
        tacticFilter.setAdapter(adapterTypes);

        //sports spinner
        Spinner sportsSpinner = findViewById(R.id.spinner_sports);
        sportsSpinner.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> adapterSports = ArrayAdapter.createFromResource(this,
                R.array.sports, android.R.layout.simple_spinner_item);
        adapterSports.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sportsSpinner.setAdapter(adapterSports);

        // init
        this.tacticType = "All tactics";
        this.sport = "All sports";

        // Connect button
        // TODO : add a listener referring to another function later


        // Profile
        // TODO : Profile

        // RecyclerView for Tactics
        RecyclerView catalogue = findViewById(R.id.tactics_recyclerView);
        catalogue.setLayoutManager(new GridLayoutManager(this, 3));
        this.tacticsAdapter = new TacticsAdapter(this);
        catalogue.setAdapter(this.tacticsAdapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        if (parent.getId() == R.id.spinner_sports) {
            this.sport = (String) parent.getItemAtPosition(pos);
        } else if (parent.getId() == R.id.spinner_types) {
            this.tacticType = (String) parent.getItemAtPosition(pos);
        }
        tacticsAdapter.filterList(this.sport,this.tacticType);
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
        this.tacticType = "All tactics";
        this.sport = "All sports";
    }

    @Override
    public void onChooseClickListener(int clickedIndex) {
        if (ApplicationState.getInstance().getDisplayedList().get(clickedIndex).getPlayersNeeded() == ApplicationState.getInstance().getPlayersConnected().size()){
            Intent intent = new Intent(MainActivity.this, PlayActivity.class);
            System.out.println("making transition from main listener : " + ApplicationState.getInstance().getDisplayedList().get(clickedIndex).getName());
            intent.putExtra("tacticIndex", clickedIndex);
            startActivityForResult(intent, RESULT_OK);
        } else {
            Dialog dialog = getPlayerErrorDialog(ApplicationState.getInstance().getDisplayedList().get(clickedIndex));
            dialog.show();
        }
    }

    public AlertDialog getPlayerErrorDialog(Tactic tactic){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("The number of players connected does not match the requirements : " + ApplicationState.getInstance().getPlayersConnected().size() + " / " + tactic.getPlayersNeeded() + " needed");
        builder.setPositiveButton(R.string.goToConnect, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked go to connect button
                // TODO : transition to connect Activity
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        return dialog;
    }
}
