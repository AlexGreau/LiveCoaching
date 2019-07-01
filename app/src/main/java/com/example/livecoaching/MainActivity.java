package com.example.livecoaching;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.livecoaching.Model.ApplicationState;
import com.example.livecoaching.Model.Player;
import com.example.livecoaching.Model.Sequence;
import com.example.livecoaching.Model.Tactic;
import com.example.livecoaching.RenderEngine.TacticPanel;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private Tactic tactic;
    private Sequence sequence;
    private TacticPanel tacticPanel;

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        int index = intent.getIntExtra("tacticIndex", 0);
        tactic = ApplicationState.getInstance().getDisplayedList().get(index);

        setContentView(R.layout.activity_play);
        // test text
        TextView testText = findViewById(R.id.testPlay);
        testText.setText(tactic.getName());
        // panel
        this.tacticPanel = (TacticPanel) findViewById(R.id.tacticPanel);

        setupPlayToolbar();
        setupSequence();
    }

    public void setupPlayToolbar(){
        Toolbar toolbar = findViewById(R.id.toolbar_play);
        setSupportActionBar(toolbar);
        // play button
        Button playButton = findViewById(R.id.button_start);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sequence.start();
            }
        });

        // stop button
        Button finishButton = findViewById(R.id.button_finish);
        AlertDialog dialog = setupDialog();
        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });
        // reset Button
        Button resetButton = findViewById(R.id.button_reset);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetSequence();
            }
        });

    }

    public AlertDialog setupDialog(){
        // dialog
        AlertDialog.Builder builder  = new AlertDialog.Builder(this);
        builder.setMessage("Tactic complete ! what's next ?");
        builder.setPositiveButton(R.string.returnToMain, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked return button
                setResult(RESULT_OK);
                tacticPanel.stop();
                finish();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User wants to redo the play
                tacticPanel.stop();
                resetSequence();
            }
        });
        AlertDialog dialog = builder.create();
        return dialog;
    }

    public void resetSequence(){
        // cleanup then setup
        System.out.println("Resetting sequence");
        this.setupSequence();
    }

    public void setupSequence() {
        this.sequence = new Sequence(tactic, ApplicationState.getInstance().getPlayersConnected());
        // set background
        setBackground(this.sequence.getTactic());
        // set players (verify too)
        this.sequence.drawPLayers();
    }

    public void setBackground(Tactic t){
        if (t.getSport().equals("Football")){
            tacticPanel.setBackground(getDrawable(R.drawable.terrain_foot));
        }
        else if (t.getSport().equals("Ultimate")){
            tacticPanel.setBackground(getDrawable(R.drawable.terrain_ultimate));
        }
    }

    public void monitorPlayers(){
        // fetch positions
        ArrayList<Player> Players = ApplicationState.getInstance().getPlayersConnected();

        // difference to goal zones and way to go

    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "Destroying...");
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        Log.d(TAG, "Stopping...");
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        tacticPanel.stop();
        finish();
    }
}
