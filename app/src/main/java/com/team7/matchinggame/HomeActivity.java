package com.team7.matchinggame;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity
    implements View.OnClickListener{
    Button startGameBtn;
    Button settingsBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        startGameBtn = findViewById(R.id.startGame);
        startGameBtn.setOnClickListener(this);

        settingsBtn = findViewById(R.id.settings);
        settingsBtn.setOnClickListener(this);

    }



    @Override
    public void onClick(View view) {
        int id = view.getId();

        if(id == R.id.startGame){
            Intent intent = new Intent(this,FetchImage.class);
            startActivity(intent);
        }

        else if(id == R.id.settings){
            Intent intent = new Intent(this,Settings.class);
            startActivity(intent);
        }
    }
}
