package com.team7.matchinggame;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Settings extends AppCompatActivity
        implements View.OnClickListener{
    TextView titleTv;
    TextView bkgMusicTv;
    Button musicOnBtn;
    Button musicOffBtn;
    Button volumeUpBtn;
    Button volumeDownBtn;
    Button homeBtn;
    MediaPlayer player;

    @Override
    protected void onCreate (Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_settings);
        titleTv = findViewById(R.id.settingsTitle);
        bkgMusicTv = findViewById(R.id.backgroundMusic);
        musicOnBtn = findViewById(R.id.musicOn);
        musicOffBtn = findViewById(R.id.musicOff);
        volumeUpBtn = findViewById(R.id.volumeUp);
        volumeDownBtn = findViewById(R.id.volumeDown);


        AudioManager audioManager = (AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
        volumeUpBtn.setOnClickListener(v -> {
            audioManager.adjustVolume(AudioManager.ADJUST_RAISE, AudioManager.FLAG_PLAY_SOUND);
        });
        volumeDownBtn.setOnClickListener(v -> {
            audioManager.adjustVolume(AudioManager.ADJUST_LOWER, AudioManager.FLAG_PLAY_SOUND);
        });

        homeBtn = findViewById(R.id.settingHome);
        homeBtn.setOnClickListener(this);

    }

    public void turnOnMusic(View v) {
        if (player == null) {
            player = MediaPlayer.create(this, R.raw.main_music);
            player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    stopPlayer();
                }
            });
        }

        player.setLooping(true);
        player.start();
    }

    public void turnOffMusic(View v) {
        stopPlayer();
    }

    private void stopPlayer() {
        if (player != null) {
            player.release();
            player = null;
        }
    }


    @Override
    public void onClick(View view) {
        int id = view.getId();

        if(id == R.id.settingHome) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }
}
