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

    /*SharedPreferences sharedPref = getSharedPreferences("scores", Context.MODE_PRIVATE);
    long timeElapsed; //in ms
    String name1st = sharedPref.getString("name1st", "");
    String name2nd = sharedPref.getString("name2nd", "");
    String name3rd = sharedPref.getString("name3rd", "");
    String name4th = sharedPref.getString("name4th", "");
    String name5th = sharedPref.getString("name5th", "");
    String time1st = Long.toString(sharedPref.getLong("time1st", 0));
    String time2nd = Long.toString(sharedPref.getLong("time2nd", 0));
    String time3rd = Long.toString(sharedPref.getLong("time3rd", 0));
    String time4th = Long.toString(sharedPref.getLong("time4th", 0));
    String time5th = Long.toString(sharedPref.getLong("time5th", 0));*/
    private final Integer[] number = {1,2,3,4,5,};
    private final String[] names = {"name1st","name2nd","name3rd","name4th","name5th"};
    private final String[] timings = {"time1st","time2nd","time3rd","time4th","time5th"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scoreboard);

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