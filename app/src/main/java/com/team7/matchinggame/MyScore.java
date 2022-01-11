package com.team7.matchinggame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MyScore extends AppCompatActivity implements View.OnClickListener {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_score);
        setupBtns();
        // For testing purpose only -> for real game uncomment the initScores()
        initScores();


        //Get time taken from Game activity
        Integer timeElapsed = getIntent().getIntExtra("timeElapsed", 0);

        //Get input name
        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        TextView scoreName = findViewById(R.id.myName);
        scoreName.setText(name);

        //Set time taken as my Scores
        TextView textView = findViewById(R.id.myScore);
        int hours = timeElapsed / 3600;
        int minutes = (timeElapsed % 3600) / 60;
        int secs = timeElapsed % 60;
        String time = String.format(Locale.getDefault(),"%d:%02d:%02d", hours, minutes, secs);
        textView.setText(time);

        // Parsing my time and time5th to time formate
        SharedPreferences pref = getSharedPreferences
            ("scores", MODE_PRIVATE);
        String time5th = pref.getString("time5th", "-");
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
        Date time5 = null;
        Date myTime = null;
        try {
            time5 = sdf.parse(time5th);
            myTime = sdf.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //Update scoreboard if top 5
        if (myTime.before(time5) || time5th =="-") {

            updateScore(time5, myTime, name, time);
        }
    }

    protected void setupBtns() {
        int[] ids = { R.id.btnHome, R.id.btnScoreboard };

        for (int i=0; i<ids.length; i++) {
            Button btn = findViewById(ids[i]);
            if (btn != null) {
                btn.setOnClickListener(this);
            }
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.btnHome) {
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.btnScoreboard) {
            Intent intent = new Intent(this, Scoreboard.class);
            startActivity(intent);
        }
    }

    public void initScores(){
        SharedPreferences pref = getSharedPreferences
                ("scores", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("name1st","Lisa");
        editor.putString("time1st","0:00:05");
        editor.putString("name2nd","Jennie");
        editor.putString("time2nd","0:00:10");
        editor.putString("name3rd","Rose");
        editor.putString("time3rd","0:00:15");
        editor.putString("name4th","Jisoo");
        editor.putString("time4th","0:00:20");
        editor.putString("name5th","GD");
        editor.putString("time5th","0:00:25");
        editor.commit();
    };


    public void updateScore(Date time5, Date myTime, String name, String time) {

        SharedPreferences pref = getSharedPreferences
                ("scores", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        String time1st = pref.getString("time1st", "-");
        String time2nd = pref.getString("time2nd", "-");
        String time3rd = pref.getString("time3rd", "-");
        String time4th = pref.getString("time4th", "-");
        String time5th = pref.getString("time5th", "-");

        String name1st = pref.getString("name1st", "-");
        String name2nd = pref.getString("name2nd", "-");
        String name3rd = pref.getString("name3rd", "-");
        String name4th = pref.getString("name4th", "-");
        String name5th = pref.getString("name5th", "-");

        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
        Date time1 = null;
        Date time2 = null;
        Date time3 = null;
        Date time4 = null;
        try {
            time1 = sdf.parse(time1st);
            time2 = sdf.parse(time2nd);
            time3 = sdf.parse(time3rd);
            time4 = sdf.parse(time4th);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        //>1st become 1st and push the scoreboard downward
        if (myTime.before(time1)|| time1st=="-" ) {

            editor.putString("name2nd", name1st);
            editor.putString("time2nd", time1st);
            editor.putString("name3rd", name2nd);
            editor.putString("time3rd", time2nd);
            editor.putString("name4th", name3rd);
            editor.putString("time4th", time3rd);
            editor.putString("name5th", name4th);
            editor.putString("time5th", time4th);

            editor.putString("name1st", name);
            editor.putString("time1st", time);

        }
        //>2nd become 2nd and push the scoreboard downward
        else if (myTime.before(time2)||time2nd=="-") {

            editor.putString("name3rd", name2nd);
            editor.putString("time3rd", time2nd);
            editor.putString("name4th", name3rd);
            editor.putString("time4th", time3rd);
            editor.putString("name5th", name4th);
            editor.putString("time5th", time4th);

            editor.putString("name2nd", name);
            editor.putString("time2nd", time);
        }

        //>3rd become 3rd and push the scoreboard downward
        else if (myTime.before(time3)||time3rd=="-") {
            editor.putString("name4th", name3rd);
            editor.putString("time4th", time3rd);
            editor.putString("name5th", name4th);
            editor.putString("time5th", time4th);

            editor.putString("name3rd", name);
            editor.putString("time3rd", time);
        }

        else if (myTime.before(time4)||time4th=="-") {
            editor.putString("name5th", name4th);
            editor.putString("time5th", time4th);

            editor.putString("name4th", name);
            editor.putString("time4th", time);
        }

        else {
            editor.putString("name5th", name);
            editor.putString("time5th", time);

        }
        editor.commit();


    }

}