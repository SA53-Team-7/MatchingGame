package com.team7.matchinggame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ListView;

public class Scoreboard extends AppCompatActivity {

    SharedPreferences pref = getPreferences(Context. MODE_PRIVATE);
    String first = Long.toString(pref.getLong("timing1st",0));
    String second = Long.toString(pref.getLong("timing2nd",0));
    String third = Long.toString(pref.getLong("timing3rd",0));
    String fourth = Long.toString(pref.getLong("timing4th",0));
    String fifth = Long.toString(pref.getLong("timing5th",0)); 
    private final Integer[] number = {1,2,3,4,5,};
    private final String[] timings = {first, second, third, fourth, fifth};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scoreboard);

        ListView listView = findViewById(R.id.listView);
        if (listView != null) {
            listView.setAdapter(new ScoreboardAdapter(this, number, timings));
        }

    }

}