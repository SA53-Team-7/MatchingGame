package com.team7.matchinggame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

public class EndGame extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_game);

        Button btn = findViewById(R.id.next);
        if(btn!=null){
            btn.setOnClickListener(this);
        }

    }
    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.next) {
            Intent intent = new Intent(this, MyScore.class);
            startActivity(intent);
        }
    }
}