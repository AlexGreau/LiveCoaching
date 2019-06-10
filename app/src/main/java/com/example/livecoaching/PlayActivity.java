package com.example.livecoaching;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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

    }
}
