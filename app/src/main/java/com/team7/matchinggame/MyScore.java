package com.team7.matchinggame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MyScore extends AppCompatActivity implements View.OnClickListener {

    // (Eric's part not sure what is wrong)
    /*long timeElapsed; //in ms
    String name = "Lisa";
    SharedPreferences sharedPref = getSharedPreferences("scores", Context.MODE_PRIVATE);
    long time1st = sharedPref.getLong("time1st", 0);
    long time2nd = sharedPref.getLong("time2nd", 0);
    long time3rd = sharedPref.getLong("time3rd", 0);
    long time4th = sharedPref.getLong("time4th", 0);
    long time5th = sharedPref.getLong("time5th", 0);
    int minutes;
    int seconds;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_score);
        setupBtns();

        //Get time taken from Game activity
        //timeElapsed = getIntent().getLongExtra("timeElapsed", 0);

        //Set time taken as my Scores
        TextView textView = findViewById(R.id.myScore);
        textView.setText("1.30");

        // (Eric's part not sure what is wrong)
        //Update scoreboard if top 5
        /*if (timeElapsed < time5th || (time5th == 0)) {
            updateScore();
        }*/
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

    // (Eric's part not sure what is wrong)
    /*public void updateScore() {
        Context context = getApplicationContext();
        sharedPref = context.getSharedPreferences("scores",Context.MODE_PRIVATE);
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


    }*/

}