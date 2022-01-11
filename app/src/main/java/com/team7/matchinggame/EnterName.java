package com.team7.matchinggame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EnterName extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_name);

        Button ok = findViewById(R.id.ok);
        if (ok != null) {
            ok.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View view) {
        EditText name = findViewById(R.id.enterName);
        if (name != null) {
            Intent intent = new Intent(this, MyScore.class);
            intent.putExtra("name", name.getText().toString());
            Intent previous = getIntent();
            int time = previous.getIntExtra("timeElapsed", 0);
            intent.putExtra("timeElapsed", time);
            startActivity(intent);
        }
    }

}