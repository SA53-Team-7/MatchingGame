package com.team7.matchinggame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;


public class Scoreboard extends AppCompatActivity implements View.OnClickListener{

    /*private final Integer[] number = {1,2,3,4,5,};
    private final String[] names = {"name1st","name2nd","name3rd","name4th","name5th"};
    private final String[] timings = {"time1st","time2nd","time3rd","time4th","time5th"};*/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scoreboard);

        SharedPreferences pref = getSharedPreferences
                ("scores", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        String time1st = pref.getString("time1st", "0");
        String time2nd = pref.getString("time2nd", "0");
        String time3rd = pref.getString("time3rd", "0");
        String time4th = pref.getString("time4th", "0");
        String time5th = pref.getString("time5th", "0");

        String name1st = pref.getString("name1st", "0");
        String name2nd = pref.getString("name2nd", "0");
        String name3rd = pref.getString("name3rd", "0");
        String name4th = pref.getString("name4th", "0");
        String name5th = pref.getString("name5th", "0");

        Integer[] number = {1,2,3,4,5,};
        String[] names = {name1st,name2nd,name3rd,name4th,name5th};
        String[] timings = {time1st,time2nd,time3rd,time4th,time5th};

        ListView listView = findViewById(R.id.listView);
        if (listView != null) {
            listView.setAdapter(new ScoreboardAdapter(this, number,names, timings));
        }

        Button btn = findViewById(R.id.btnHome);
        if(btn!=null){
            btn.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.btnHome) {
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
        }
    }
}