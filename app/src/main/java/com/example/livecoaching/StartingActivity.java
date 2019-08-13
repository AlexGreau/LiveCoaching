package com.example.livecoaching;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
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
        // trajectory
        trajectory = findViewById(R.id.trajectories);
        ArrayAdapter<CharSequence> adapterTrajectories = ArrayAdapter.createFromResource(this,
                R.array.trajectories, android.R.layout.simple_spinner_item);
        adapterTrajectories.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        trajectory.setAdapter(adapterTrajectories);

        // interaction
        interaction = findViewById(R.id.interactionType);
        ArrayAdapter<CharSequence> adapterInteractions = ArrayAdapter.createFromResource(this,
                R.array.interactionTypes, android.R.layout.simple_spinner_item);
        adapterInteractions.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        interaction.setAdapter(adapterInteractions);

        // state
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
        startMainActivity();
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
