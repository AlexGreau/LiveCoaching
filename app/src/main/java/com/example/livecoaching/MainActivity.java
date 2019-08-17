package com.example.livecoaching;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.livecoaching.Communication.Server;
import com.example.livecoaching.Logs.Logger;
import com.example.livecoaching.Model.ApplicationState;

import java.net.ServerSocket;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    protected ServerSocket serverSocket;
    protected Server server;

    // logs
    protected Logger logger;
    private int interactionType;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        interactionType = 1;
        initLogger();
        server = new Server(this);
        initUI();
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
    }

    public void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar_play);
        setSupportActionBar(toolbar);
        // play button
        Button playButton = findViewById(R.id.button_start);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "startButton pressed");
            }
        });

        // stop button
        Button finishButton = findViewById(R.id.button_finish);
        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "finishButton pressed");
            }
        });
        // reset Button
        Button resetButton = findViewById(R.id.button_reset);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "resetButton pressed");
            }
        });
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
        finish();
    }

    // getters and setters

    public int getInteractionType() {
        return interactionType;
    }

    public ServerSocket getServerSocket() {
        return serverSocket;
    }

    public void setServerSocket(ServerSocket socket) {
        this.serverSocket = socket;
    }

    public Logger getLogger() {
        return logger;
    }
}
