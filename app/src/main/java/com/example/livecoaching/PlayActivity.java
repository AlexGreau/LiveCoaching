package com.example.livecoaching;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
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

        // toolbar
        Toolbar toolbar = findViewById(R.id.toolbar_play);
        setSupportActionBar(toolbar);

        // toolbar buttons
            // play button
        Button playButton = findViewById(R.id.button_start);

            // stop button
        Button finishButton = findViewById(R.id.button_finish);
                // end of tactic dialog
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
                // TODO : reset sequence
            }
        });
        AlertDialog dialog = builder.create();
        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });

        // reset Button
        Button resetButton = findViewById(R.id.button_reset);
        // TODO : reset sequence
    }
}
