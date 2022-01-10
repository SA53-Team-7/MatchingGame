package com.team7.matchinggame;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Settings extends AppCompatActivity
        implements View.OnClickListener{
    Button musicOnBtn;
    Button musicOffBtn;
    Button volumeUpBtn;
    Button volumeDownBtn;
    Button homeBtn;
    MediaPlayer player;

    //bind Settings to MusicService
    private boolean mIsBound = false;
    private MusicService musicService;
    private ServiceConnection sConnection =new ServiceConnection(){

        public void onServiceConnected(ComponentName name, IBinder
                binder) {
            musicService =  ((MusicService.ServiceBinder)binder).getService();
        }

        public void onServiceDisconnected(ComponentName name) {
            musicService = null;
        }
    };

    void doBindService(){
        bindService(new Intent(this,MusicService.class),
                sConnection,Context.BIND_AUTO_CREATE);
        mIsBound = true;
    }

    void doUnbindService()
    {
        if(mIsBound)
        {
            unbindService(sConnection);
            mIsBound = false;
        }
    }

    @Override
    protected void onCreate (Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_settings);
        musicOnBtn = findViewById(R.id.musicOn);
        musicOffBtn = findViewById(R.id.musicOff);
        volumeUpBtn = findViewById(R.id.volumeUp);
        volumeDownBtn = findViewById(R.id.volumeDown);

        doBindService();

        AudioManager audioManager = (AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
        volumeUpBtn.setOnClickListener(v -> {
            audioManager.adjustVolume(AudioManager.ADJUST_RAISE, AudioManager.FLAG_PLAY_SOUND);
            Toast.makeText(this, "Volume up", Toast.LENGTH_SHORT).show();
        });
        volumeDownBtn.setOnClickListener(v -> {
            audioManager.adjustVolume(AudioManager.ADJUST_LOWER, AudioManager.FLAG_PLAY_SOUND);
            Toast.makeText(this, "Volume down", Toast.LENGTH_SHORT).show();
        });

        homeBtn = findViewById(R.id.settingHome);
        homeBtn.setOnClickListener(this);

    }

    public void turnOnMusic(View v) {
        if (musicService != null){
            musicService.resumeMusic();
            Toast.makeText(this, "Turn on music", Toast.LENGTH_SHORT).show();
        }
    }

    public void turnOffMusic(View v) {
        if (musicService != null){
            musicService.pauseMusic();
            Toast.makeText(this, "Turn off music", Toast.LENGTH_SHORT).show();
        }
    }

    public void onDestroy(){
        super.onDestroy();
        doUnbindService();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if(id == R.id.settingHome) {
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
        }
    }
}
