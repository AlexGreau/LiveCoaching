package com.example.livecoaching;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class StartingActivity extends AppCompatActivity {
    // starting activity includes Cool Name and start button
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starting);
        TextView textView = findViewById(R.id.StartText);
        Button button = findViewById(R.id.StartButton);
        button.setOnClickListener( new View.OnClickListener(){
            public void onClick(View v) {
                startMainActivity();
            }});
    }

    protected void startMainActivity(){
        Intent proceed = new Intent(this, MainActivity.class);
        startActivity(proceed);
    }

}
