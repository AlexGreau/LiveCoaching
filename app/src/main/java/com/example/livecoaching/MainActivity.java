package com.example.livecoaching;

import android.os.Bundle;
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
import com.example.livecoaching.Model.Tactic;

public class MainActivity extends AppCompatActivity
        implements AdapterView.OnItemSelectedListener {
    private String sport;
    private String tacticType;
    private TacticsAdapter tacticsAdapter;
    private Tactic[] tactics; // stored somewhere

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
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

        // Connect button
            // TODO : add a listener referring to another function later


        // Profile
            // TODO : Profile

        // ReccyclerView for Tactics
        RecyclerView catalogue = findViewById(R.id.tactics_recyclerView);
        catalogue.setLayoutManager(new GridLayoutManager(this, 3));
        tacticsAdapter = new TacticsAdapter();
        catalogue.setAdapter(tacticsAdapter);
    }

    @Override
    public void onBackPressed() {
            super.onBackPressed();
    }

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        if (parent.getId() == R.id.spinner_sports){
            this.sport = (String) parent.getItemAtPosition(pos);
            System.out.println(this.sport);
            // TODO : sort tactics accordingly
        }
        else if (parent.getId() == R.id.spinner_types){
            this.tacticType = (String) parent.getItemAtPosition(pos);
            System.out.println(this.tacticType);
            // TODO : sort tactics accordingly
        }
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }
}
