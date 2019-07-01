package com.example.livecoaching;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.FeatureInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.example.livecoaching.Adapter.TacticsAdapter;
import com.example.livecoaching.Model.ApplicationState;
import com.example.livecoaching.Model.Tactic;

public class ChoosingTacticActivity extends AppCompatActivity
        implements AdapterView.OnItemSelectedListener, TacticsAdapter.TacticClickListener {
    private String sport;
    private String tacticType;
    private TacticsAdapter tacticsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    public void init() {
        setContentView(R.layout.activity_choosing);
        Toolbar toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
        this.tacticType = "All tactics";
        this.sport = "All sports";
        initConnect();
        initProfile();
        initSpinners();
        initTacticsView();
    }

    public void initProfile() {
        Button profileBtn = findViewById(R.id.profile);
        profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Accessing to profile");
            }
        });
    }

    public void initConnect() {
        Button connectButton = findViewById(R.id.connectDevicesButton);
        connectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChoosingTacticActivity.this, ConnectActivity.class);
                intent.putExtra("msg", sport);
                startActivity(intent);
            }
        });
    }

    public void initTacticsView() {
        RecyclerView catalogue = findViewById(R.id.tactics_recyclerView);
        catalogue.setLayoutManager(new GridLayoutManager(this, 3));
        this.tacticsAdapter = new TacticsAdapter(this);
        catalogue.setAdapter(this.tacticsAdapter);
    }

    public void initSportSpinner() {
        Spinner sportsSpinner = findViewById(R.id.spinner_sports);
        sportsSpinner.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> adapterSports = ArrayAdapter.createFromResource(this,
                R.array.sports, android.R.layout.simple_spinner_item);
        adapterSports.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sportsSpinner.setAdapter(adapterSports);
    }

    public void initTypeSpinner() {
        Spinner tacticFilter = findViewById(R.id.spinner_types);
        ArrayAdapter<CharSequence> adapterTypes = ArrayAdapter.createFromResource(this,
                R.array.tacticTypes, android.R.layout.simple_spinner_item);
        adapterTypes.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tacticFilter.setOnItemSelectedListener(this);
        tacticFilter.setAdapter(adapterTypes);
    }

    public void initSpinners() {
        initSportSpinner();
        initTypeSpinner();
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
        tacticsAdapter.filterList(this.sport, this.tacticType);
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
        this.tacticType = "All tactics";
        this.sport = "All sports";
    }

    @Override
    public void onChooseClickListener(int clickedIndex) {
        Intent intent = new Intent();
        intent.putExtra("tacticIndex", clickedIndex);
        setResult(RESULT_OK, intent);
        finish();
    }

    private boolean isWifiDirectSupported(Context ctx) {
        PackageManager pm = ctx.getPackageManager();
        FeatureInfo[] features = pm.getSystemAvailableFeatures();
        for (FeatureInfo info : features) {
            if (info != null && info.name != null && info.name.equalsIgnoreCase("android.hardware.wifi.direct")) {
                return true;
            }
        }
        return false;
    }
}
