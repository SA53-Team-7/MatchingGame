package com.team7.matchinggame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;


public class MainActivity extends AppCompatActivity {

    private static int SPLASH_SCREEN = 2000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //create splash screen
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        },SPLASH_SCREEN);

        //initialize background music
        Intent intentMusic = new Intent(MainActivity.this, MusicService.class);
        intentMusic.putExtra("class","main");
        startService(intentMusic);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Intent intentMusic = new Intent(MainActivity.this, MusicService.class);
        intentMusic.putExtra("class","main");
        startService(intentMusic);
    }
}



