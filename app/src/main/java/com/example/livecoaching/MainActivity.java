package com.example.livecoaching;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.example.livecoaching.Communication.Server;
import com.example.livecoaching.Logs.Logger;

public class MainActivity extends AppCompatActivity {
    private final String TAG = MainActivity.class.getSimpleName();

    protected Server server;

    // UI components
    protected Button startButton;
    protected Button startTestButton;
    protected Button finishButton;
    // logs
    protected Logger logger;
    private int interactionType;
    // logic variables
    protected boolean isRunning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setFullScreen();
        // for now
        interactionType = 1;
        initLogger();
        server = new Server(this);
        initUI();
    }

    // init
    protected void setFullScreen(){
        this.getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        // Set the content to appear under the system bars so that the
                        // content doesn't resize when the system bars hide and show.
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the nav bar and status bar
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    protected void initLogger() {
        logger = new Logger(this);
        String ID = getIntent().getStringExtra("ID");
        // for now
        String interactionType = getIntent().getStringExtra("interactionType");
        logger.initNewLog(ID, interactionType);
    }

    public void initUI() {
        setContentView(R.layout.activity_main);
        initToolbar();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    public void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar_play);
        setSupportActionBar(toolbar);
        initStartTestButton();
        initStartButton();
        initFinishButton();
        changeRunningStateTo(false);
    }

    // Logic functions
    public void changeRunningStateTo(boolean bool) {
        this.isRunning = bool;
        this.startTestButton.setEnabled(!isRunning);
        this.startButton.setEnabled(!isRunning);
        this.finishButton.setEnabled(isRunning);
    }

    // Overrides
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
        finish();
    }

    // getters and setters
    public int getInteractionType() {
        return interactionType;
    }

    public Logger getLogger() {
        return logger;
    }


    // setup button functions
    public void initStartButton() {
        this.startButton = findViewById(R.id.button_start);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "startButton pressed");
                changeRunningStateTo(true);
            }
        });
    }

    public void initStartTestButton() {
        this.startTestButton = findViewById(R.id.button_test);
        startTestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "startTestButton pressed");
                changeRunningStateTo(true);
            }
        });
    }

    public void initFinishButton() {
        this.finishButton = findViewById(R.id.button_finish);
        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "finishButton pressed");
                changeRunningStateTo(false);
            }
        });
    }
}
