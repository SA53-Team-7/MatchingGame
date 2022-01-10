package com.team7.matchinggame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

public class Scoreboard extends AppCompatActivity {

    SharedPreferences sharedPref;
    long timeElapsed; //in ms
    long time1st;
    long time2nd;
    long time3rd;
    long time4th;
    long time5th;
    int minutes;
    int seconds;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scoreboard);

        //Get time taken from Game activity
        timeElapsed = getIntent().getLongExtra("timeElapsed", 0);


        //Saving score via shared pref
        sharedPref = context.getSharedPreferences(Context.MODE_PRIVATE);
        time1st = sharedPref.getLong("time1st", 0);
        time2nd = sharedPref.getLong("time2nd", 0);
        time3rd = sharedPref.getLong("time3rd", 0);
        time4th = sharedPref.getLong("time4th", 0);
        time5th = sharedPref.getLong("time5th", 0);

        //Update scoreboard if top 5
        if (timeElapsed < time5th || (time5th == 0)) {
            updateScore();

        }



    }


    public void updateScore() {
        Context context = getApplicationContext();
        sharedPref = context.getSharedPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        //>1st become 1st and push the scoreboard downward
        if (timeElapsed < time1st || (time1st == 0)) {

            editor.putString("name2nd", sharedPref.getString("name1st", "PLayer2nd"));
            editor.putLong("time2nd", sharedPref.getLong("time1st", 0));
            editor.putString("name3rd", sharedPref.getString("name2nd", "Player3rd"));
            editor.putLong("time3rd", sharedPref.getLong("time2nd", 0));
            editor.putString("name4th", sharedPref.getString("name3rd", "Player4th"));
            editor.putLong("time4th", sharedPref.getLong("time3rd", 0));
            editor.putString("name5th", sharedPref.getString("name4th", "Player5th"));
            editor.putLong("time5th", sharedPref.getLong("time4th", 0));

            editor.putString("name1st", name);
            editor.putLong("time1st", timeElapsed);

        }
        else if (timeElapsed < time2nd || (time2nd == 0)) {

            editor.putString("name3rd", sharedPref.getString("name2nd", "Player3rd"));
            editor.putLong("time3rd", sharedPref.getLong("time2nd", 0));
            editor.putString("name4th", sharedPref.getString("name3rd", "Player4th"));
            editor.putLong("time4th", sharedPref.getLong("time3rd", 0));
            editor.putString("name5th", sharedPref.getString("name4th", "Player5th"));
            editor.putLong("time5th", sharedPref.getLong("time4th", 0));

            editor.putString("name2nd", name);
            editor.putLong("time2nd", timeElapsed);

        }

        else if (timeElapsed < time3rd || (time3rd == 0)) {
            editor.putString("name4th", sharedPref.getString("name3rd", "Player4th"));
            editor.putLong("time4th", sharedPref.getLong("time3rd", 0));
            editor.putString("name5th", sharedPref.getString("name4th", "Player5th"));
            editor.putLong("time5th", sharedPref.getLong("time4th", 0));

            editor.putString("name3rd", name);
            editor.putLong("time3rd", timeElapsed);
        }

        else if (timeElapsed < time4th || (time4th == 0)) {
            editor.putString("name5th", sharedPref.getString("name4th", "Player5th"));
            editor.putLong("time5th", sharedPref.getLong("time4th", 0));

            editor.putString("name4th", name);
            editor.putLong("time4th", timeElapsed);
        }

        else {
            editor.putString("name5th", name);
            editor.putLong("time5th", timeElapsed);

        }
        editor.commit();
    }


        }

    }












}