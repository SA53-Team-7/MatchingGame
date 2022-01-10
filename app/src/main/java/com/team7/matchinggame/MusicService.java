package com.team7.matchinggame;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;

public class MusicService extends Service {
    private final IBinder mBinder = new ServiceBinder();
    private int musicLength = 0;
    MediaPlayer player;


    public MusicService() {
    }

    // let other activities bind to MusicService
    public class ServiceBinder extends Binder{
        MusicService getService(){
            return MusicService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
       return mBinder;
    }

    public void onCreate() {
        player =MediaPlayer.create(this, R.raw.main_music);
        player.setLooping(true);

    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent.getStringExtra("class").equals("main")) {
            player.start();
            return Service.START_NOT_STICKY;
        }
        return Service.START_NOT_STICKY;
    }

    public void pauseMusic()
    {
        if(player.isPlaying())
        {
            player.pause();
            musicLength=player.getCurrentPosition();

        }
    }

    public void resumeMusic()
    {
        if(player.isPlaying()==false)
        {
            player.seekTo(musicLength);
            player.start();
        }
    }

    public void onDestroy() {
        super.onDestroy();
        if(player != null)
        {
            try{
                player.stop();
                player.release();
            }finally {
                player = null;
            }
        }
    }
}