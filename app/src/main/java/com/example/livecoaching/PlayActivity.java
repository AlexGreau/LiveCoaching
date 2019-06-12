package com.example.livecoaching;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.livecoaching.Model.ApplicationState;
import com.example.livecoaching.Model.Sequence;
import com.example.livecoaching.Model.Tactic;

public class PlayActivity extends AppCompatActivity {
    private Tactic tactic;
    private Sequence sequence;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        int index = intent.getIntExtra("tacticIndex", 0);
        tactic = ApplicationState.getInstance().getDisplayedList().get(index);
        TextView testText = (TextView) findViewById(R.id.testPlay);
        testText.setText(tactic.getName());

        setupPlayToolbar();
        setupSequence();
    }

    public void setupPlayToolbar(){
        Toolbar toolbar = findViewById(R.id.toolbar_play);
        setSupportActionBar(toolbar);
        // play button
        Button playButton = findViewById(R.id.button_start);
        // TODO : start monitoring players' positions and calculate feedback

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
                resetSequence(sequence);
            }
        });

    }

    public AlertDialog setupDialog(){
        // dialog
        AlertDialog.Builder builder  = new AlertDialog.Builder(this);
        builder.setMessage("zeuby ?");
        builder.setPositiveButton(R.string.returnToMain, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked return button
                Intent intent = new Intent(PlayActivity.this, MainActivity.class);
                setResult(RESULT_OK);
                finish();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User wants to redo the play
                resetSequence(sequence);
            }
        });
        AlertDialog dialog = builder.create();
        return dialog;
    }

    public void resetSequence(Sequence s){
        // cleanup then setup
        System.out.println("Resetting sequence");
        this.setupSequence();
    }

    public void setupSequence() {
        this.sequence = new Sequence(tactic);
        // set background
        setBackground(this.sequence.getTactic());
        // set players (verify too)
        setPlayers(this.sequence.getTactic());

    }

    public void setBackground(Tactic t){
        RelativeLayout content = findViewById(R.id.play_content);
        if (t.getSport().equals("Football")){
            content.setBackground(getDrawable(R.drawable.terrain_foot));
        }
        else if (t.getSport().equals("Ultimate")){
            content.setBackground(getDrawable(R.drawable.terrain_ultimate));
        }
    }

    public void setPlayers(Tactic t){
        // verify

        // place on the field
    }
}
