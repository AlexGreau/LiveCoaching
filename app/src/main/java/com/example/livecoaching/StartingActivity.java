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
    Button continueButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_starting);
        init();
    }

    protected void init() {
        initText();
        initButton();
    }

    protected void initText() {
        // Welcome text
        welcomeText = findViewById(R.id.welcomeText);
        welcomeText.setText(R.string.welcome_text);
    }

    protected void initButton() {
        continueButton = findViewById(R.id.continueButton);
        continueButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                proceed();

            }
        });
    }

    protected void proceed() {
        startMainActivity();
    }

    protected AlertDialog buildErrorDialog() {
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
        startActivity(proceed);
    }

}
