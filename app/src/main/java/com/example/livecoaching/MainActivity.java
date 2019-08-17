package com.example.livecoaching;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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
                AlertDialog dialog = buildStartExpDialog();
                dialog.show();
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
            }
        });
    }

    public void initFinishButton() {
        this.finishButton = findViewById(R.id.button_finish);
        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "finishButton pressed");
                AlertDialog dialog = buildFinnishDialog();
                dialog.show();
            }
        });
    }

    // dialog functions
    public AlertDialog buildStartExpDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("Start Experience")
                .setMessage("Please enter the participant's ID below before continuing")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        EditText ID = findViewById(R.id.IDparticipant);
                        String IDtext = ID.getText().toString();
                        if (!isValid(IDtext)) {
                            startExp(IDtext);
                        }
                    }
                })
                // A null listener allows the button to dismiss the dialog and take no further action.
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        setFullScreen();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setView(R.layout.startexp_dialog_layout);
        return builder.create();
    }

    public AlertDialog buildFinnishDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("Finish Experience")
                .setMessage("Are you sure you want to end this experience?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finishExp();
                    }
                })
                // A null listener allows the button to dismiss the dialog and take no further action.
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert);

        return builder.create();
    }

    public AlertDialog buildClearFileDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("Clear File")
                .setMessage("Are you sure you want to Clear the log file and loose all the data ?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Continue with delete operation
                        // TODO : clear file
                    }
                })

                // A null listener allows the button to dismiss the dialog and take no further action.
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert);

        return builder.create();
    }

    // full screen function
    protected void setFullScreen() {
        this.getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        // Set the content to appear under the system bars so that the
                        // content doesn't resize when the system bars hide and show.
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the nav bar and status bar
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    // Logic functions
    protected void startExp(String ID) {
        // create new object exp(ID)
        // exp.run()
        changeRunningStateTo(true);
        TextView test = (TextView) findViewById(R.id.testPlay);
        test.setText(ID);
    }

    protected void finishExp() {
        // get exp object
        // exp.stop()
        changeRunningStateTo(false);
    }

    protected void startTest() {
        // create testRun object
        // testRun.run()
    }

    protected boolean isValid(String text) {
        // TODO : REGEX for no ":" or no special char
        boolean isValid = true;


        return isValid;
    }
}
