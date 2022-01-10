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
    Button highestScoresBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Intent intentMusic = new Intent(HomeActivity.this,MusicService.class);
        intentMusic.putExtra("class","main");
        startService(intentMusic);

        startGameBtn = findViewById(R.id.startGame);
        startGameBtn.setOnClickListener(this);

        settingsBtn = findViewById(R.id.settings);
        settingsBtn.setOnClickListener(this);

        highestScoresBtn = findViewById(R.id.highestScores);
        highestScoresBtn.setOnClickListener(this);

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Intent intentMusic = new Intent(HomeActivity.this,MusicService.class);
        intentMusic.putExtra("class","main");
        startService(intentMusic);
    }


    @Override
    public void onClick(View view) {
        int id = view.getId();

        if(id == R.id.startGame){
            Intent intent = new Intent(this, FetchImage.class);
            startActivity(intent);
        }

        else if(id == R.id.settings){
            Intent intent = new Intent(this, Settings.class);
            startActivity(intent);
        }

        else if(id == R.id.highestScores){
            Intent intent = new Intent(this, MyScore.class);
            startActivity(intent);
        }
    }
}
