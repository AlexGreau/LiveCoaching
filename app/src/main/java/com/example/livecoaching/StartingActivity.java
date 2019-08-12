package com.example.livecoaching;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class StartingActivity extends AppCompatActivity {
    // components
    TextView welcomeText;
    EditText nameField;
    CheckBox checkBox;
    Spinner trajectory;
    Spinner interaction;
    Button continueButton;

    // values
    String sequenceString = "All";
    String interactionType = "All";
    String testerName = "";

    // TODO : "Enter name field" -> once entered make start button clickable
    // TODO : Spinners : Trajectory/interaction type
    // TODO : checkbox for complete test
    // TODO : launch server

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starting);
        init();
    }

    protected void init() {
        initText();
        initNameField();
        initCheckBox();
        initSpinners();
        initButton();
    }

    protected void initText() {
        // Welcome text
        welcomeText = findViewById(R.id.welcomeText);
        welcomeText.setText(R.string.welcome_text);
    }

    protected void initNameField() {
        nameField = findViewById(R.id.nameField);

    }

    protected void initCheckBox(){
        checkBox = findViewById(R.id.checkbox);
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSpinnersState(!checkBox.isChecked());
            }
        });
    }

    protected void initSpinners() {
        trajectory = findViewById(R.id.trajectories);
        interaction = findViewById(R.id.interactionType);
        setSpinnersState(!checkBox.isChecked());
    }

    protected void initButton() {
        continueButton = findViewById(R.id.continueButton);
        continueButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                testerName = nameField.getText().toString();
                if (!testerName.equals("")){ // TODO : REGEX
                    proceed();
                } else {
                    buildErrorDialog().show();
                }
            }
        });
    }

    protected void proceed(){
        // startMainActivity();
    }

    protected void setSpinnersState(boolean bool){
        interaction.setEnabled(bool);
        trajectory.setEnabled(bool);
    }

    protected AlertDialog buildErrorDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Please enter a correct name");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked return button
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        return dialog;
    }

    protected void startMainActivity() {
        Intent proceed = new Intent(this, MainActivity.class);
        proceed.putExtra("sequence", sequenceString);
        proceed.putExtra("interactionType", interactionType);
        startActivity(proceed);
    }

}
