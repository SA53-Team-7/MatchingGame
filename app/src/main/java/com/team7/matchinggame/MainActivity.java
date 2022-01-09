package com.team7.matchinggame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity
    implements View.OnClickListener{
    Button btn1;
    Button btn2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn1 = findViewById(R.id.startGame);
        btn1.setOnClickListener(this);

        btn2 = findViewById(R.id.settings);
        btn2.setOnClickListener(this);

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