package com.example.livecoaching;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.livecoaching.Communication.Server;
import com.example.livecoaching.Interfaces.Decoder;
import com.example.livecoaching.Interfaces.ExperimentVisualizer;
import com.example.livecoaching.Logs.Logger;
import com.example.livecoaching.Model.Experiment;
import com.example.livecoaching.Model.TestExperiment;
import com.example.livecoaching.Model.Trial;

import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.view.View.GONE;

public class MainActivity extends AppCompatActivity implements ExperimentVisualizer, Decoder {
    private final String TAG = MainActivity.class.getSimpleName();
    private Server server;

    // UI components
    protected Button startButton;
    protected Button startTestButton;
    protected Button finishButton;
    protected Button nextButton;
    protected AlertDialog startExpDialog;
    protected TextView directionText;
    protected TextView distanceText;
    protected TextView trialNumberText;
    protected TextView generalInfoText;
    protected TextView infoText;

    // logs
    protected Logger simpleLogger;
    private int interactionType;
    // exp
    protected Experiment experiment;
    protected String ID;
    // logic variables
    protected boolean isRunning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setFullScreen();
        // for now
        interactionType = 1;
        initUI();
        server = new Server(this);
    }

    // init
    protected void initLoggers() {
        simpleLogger = new Logger(this);
    }

    public void initUI() {
        setContentView(R.layout.activity_main);
        initToolbar();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        directionText = findViewById(R.id.direction_text);
        distanceText = findViewById(R.id.distanceTo);
        trialNumberText = findViewById(R.id.trialNumber);
        generalInfoText = findViewById(R.id.generalTrialInfo);
        infoText = findViewById(R.id.infoText);
    }

    public void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar_play);
        setSupportActionBar(toolbar);
        initStartTestButton();
        initStartButton();
        initFinishButton();
        initNextButton();
        changeRunningStateTo(false);
    }

    // Logic functions
    public void changeRunningStateTo(boolean bool) {
        this.isRunning = bool;
        this.startTestButton.setEnabled(!isRunning);
        this.startButton.setEnabled(!isRunning);
        this.finishButton.setEnabled(isRunning);
        this.nextButton.setEnabled(isRunning);
    }

    protected void startExp(String ID) {
        initLoggers();
        setupExperimentUI();
        resetPlayContent();
        experiment = new Experiment(ID, this.simpleLogger, this);
        server.setDecoder(experiment);
        changeRunningStateTo(true);
        // test
        directionText.setText(ID);
        experiment.run();
    }

    protected void finishExp() {
        changeRunningStateTo(false);
        experiment.endExp();
    }

    protected void startTest() {
        initLoggers();
        setupExperimentUI();
        resetPlayContent();
        experiment = new TestExperiment(this.simpleLogger, this);
        server.setDecoder(experiment);
        changeRunningStateTo(true);
        // test
        generalInfoText.setText("TestRun");
        experiment.run();
        distanceText.setVisibility(View.VISIBLE);
    }

    protected boolean isValid(String text) {
        Pattern pattern = Pattern.compile("\\w+?");
        Matcher matcher = pattern.matcher(text);
        return matcher.matches();
    }

    public String buildResumeTrial(Trial trial) {
        StringBuilder builder = new StringBuilder();
        String separator = "; ";
        builder.append(trial.getInteractionString(trial.getInteractionType()));
        builder.append(separator);
        builder.append(trial.getDifficulty());
        builder.append(separator);
        builder.append(trial.getTheoricDistance());
        builder.append(separator);
        builder.append(trial.getTotalDistance());
        builder.append(separator);
        builder.append(trial.getTotalTime());
        builder.append(separator);
        builder.append(trial.getSuccess());

        return builder.toString();
    }

    public String buildGeneralInfos(Trial trial) {
        String res = "";
        String difficulty = trial.getDifficultyString(trial.getDifficulty());
        String interaction = trial.getInteractionString(trial.getInteractionType());
        res = difficulty + ", " + interaction;
        return res;
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

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            setFullScreen();
        }
    }

    @Override
    public String decodeMessage(String rep) {
        return "";
    }

    @Override
    public void handleEndOfExperiment() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                directionText.setText(" \nEND of EXPERIMENT");
                distanceText.setVisibility(GONE);
                changeRunningStateTo(false);
            }
        });
    }

    @Override
    public void handleTrialPrinting(int index, Trial trial) {
        DecimalFormat df = new DecimalFormat("#.#");
        String distTo = df.format(trial.calculateDistanceToNextCP()) + " m";
        String bearTo = df.format(trial.calculateBearingToNextCP()) + "degrees";

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                generalInfoText.setText(buildGeneralInfos(trial));
                distanceText.setText(distTo);
                directionText.setText(bearTo);
            }
        });
    }

    public void resetPlayContent() {
        generalInfoText.setText("");
        distanceText.setText("");
        directionText.setText("");
        trialNumberText.setText("");
    }

    public void setupBlankUI() {
        findViewById(R.id.play_content).setVisibility(GONE);
        this.infoText.setVisibility(View.VISIBLE);
    }

    public void setupExperimentUI() {
        this.infoText.setVisibility(GONE);
        findViewById(R.id.play_content).setVisibility(View.VISIBLE);
    }


    // getters and setters
    public int getInteractionType() {
        return interactionType;
    }

    public Logger getSimpleLogger() {
        return simpleLogger;
    }


    // setup button functions
    public void initStartButton() {
        this.startButton = findViewById(R.id.button_start);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "startButton pressed");
                startExpDialog = buildStartExpDialog();
                startExpDialog.show();
            }
        });
    }

    public void initStartTestButton() {
        this.startTestButton = findViewById(R.id.button_test);
        startTestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "startTestButton pressed");
                startTest();
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

    public void initNextButton() {
        this.nextButton = findViewById(R.id.button_nextTrial);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "next button pressed");
                // TODO : function to build dialog an action
            }
        });
    }

    // dialog functions
    public AlertDialog buildStartExpDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_startexp, null);

        TextView explanation = (TextView) view.findViewById(R.id.dialogExplanation);
        explanation.setText("Please enter a participant ID below");
        builder.setTitle("Information needed");
        EditText id = (EditText) view.findViewById(R.id.IDparticipant);
        TextView errorText = view.findViewById(R.id.dialogErrorText);

        Button continueButton = view.findViewById(R.id.dialogOkButton);
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textId = id.getText().toString();
                Log.d(TAG, textId);
                if (isValid(textId)) {
                    ID = textId;
                    startExp(textId);
                    startExpDialog.dismiss();
                } else {
                    errorText.setTextColor(Color.RED);
                    errorText.setText("Invalid ID, please enter a single word without special characters");
                    errorText.setVisibility(View.VISIBLE);
                }
            }
        });
        builder.setView(view);
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
}
