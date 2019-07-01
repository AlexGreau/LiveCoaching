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
        retrieveTactic();
        initBlankScreen();
    }

    public void initBlankScreen(){
        // replace the content view by a green screen with buttons to
        // force user to choose a tactic
        setContentView(R.layout.activity_main_blank);
        Button choose = findViewById(R.id.blank_chooseTactic);
        TextView text = findViewById(R.id.blank_text);

        text.setText(R.string.blank_explanation);
        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // starting choosing tactic activity
                startChoosingActivity();
            }
        });
    }

    public void startChoosingActivity(){
        Intent intent = new Intent(MainActivity.this, ChoosingTacticActivity.class);
        startActivityForResult(intent, ApplicationState.PICK_A_TACTIC);
    }

    public void initFilledScreen(){
        setContentView(R.layout.activity_main);
        this.tacticPanel = (TacticPanel) findViewById(R.id.tacticPanel);
        setupPlayToolbar();
        setupSequence();
    }

    public void retrieveTactic(){
        // get the intent and sets the tactic accordingly
        Intent intent = getIntent();
        int index = intent.getIntExtra("tacticIndex", 0);
        tactic = ApplicationState.getInstance().getDisplayedList().get(index);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ApplicationState.PICK_A_TACTIC){
            // just received tactic id
            tactic = ApplicationState.getInstance().getDisplayedList().get(data.getIntExtra("tacticIndex",0));
            initFilledScreen();
        }
    }
}
