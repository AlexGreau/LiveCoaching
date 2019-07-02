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

import com.example.livecoaching.Model.ApplicationState;
import com.example.livecoaching.Model.Sequence;
import com.example.livecoaching.Model.Tactic;
import com.example.livecoaching.RenderEngine.TacticPanel;

public class MainActivity extends AppCompatActivity {
    private Tactic tactic;
    private Sequence sequence;
    private TacticPanel tacticPanel;

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initBlankScreen();
    }

    public void initBlankScreen() {
        setContentView(R.layout.activity_main);
        this.tacticPanel = (TacticPanel) findViewById(R.id.tacticPanel);
        setupPlayToolbar();
        // display dialog to force choice
        AlertDialog dialog = setupBlankDialog();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    public void startChoosingActivity() {
        Intent intent = new Intent(MainActivity.this, ChoosingTacticActivity.class);
        startActivityForResult(intent, ApplicationState.PICK_A_TACTIC);
    }

    public void terminateThreads() {
        tacticPanel.stop();
    }

    public void initFilledScreen() {
        setContentView(R.layout.activity_main);
        this.tacticPanel = (TacticPanel) findViewById(R.id.tacticPanel);
        setupPlayToolbar();
        System.out.println("Tactic chosen is " + tactic.getName());
        setupSequence();
    }

    public void setupPlayToolbar() {
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
        AlertDialog dialog = setupStopDialog();
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

        // catalogue button
        Button catalogButton = findViewById(R.id.catalogue);
        catalogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                terminateThreads();
                startChoosingActivity();
            }
        });

    }

    public AlertDialog setupStopDialog() {
        // dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Tactic complete ! what's next ?");
        builder.setPositiveButton(R.string.returnToMain, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked return button
                terminateThreads();
                startChoosingActivity();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User wants to redo the play
                terminateThreads();
                resetSequence();
            }
        });
        AlertDialog dialog = builder.create();
        return dialog;
    }

    public AlertDialog setupBlankDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.blank_explanation);
        builder.setPositiveButton(R.string.blank_buttonText, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked return button
                terminateThreads();
                startChoosingActivity();
            }
        });

        AlertDialog dialog = builder.create();
        return dialog;
    }

    public void resetSequence() {
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

    public void setBackground(Tactic t) {
        if (t.getSport().equals("Football")) {
            tacticPanel.setBackground(getDrawable(R.drawable.terrain_foot));
        } else if (t.getSport().equals("Ultimate")) {
            tacticPanel.setBackground(getDrawable(R.drawable.terrain_ultimate));
        }
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
        terminateThreads();
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ApplicationState.PICK_A_TACTIC) {
            this.tactic = ApplicationState.getInstance().getDisplayedList().get(data.getIntExtra("tacticIndex", 0));
            initFilledScreen();
        }
    }
}
