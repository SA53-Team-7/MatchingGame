package com.team7.matchinggame;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class FetchActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fetch);

        new Thread(new Runnable() {
            @Override
            public void run() {
                ArrayList<String> imgURLList = getImage();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (imgURLList != null){
                            Log.e("okok", String.valueOf(imgURLList.size()));
                            for(int i = 0; i < imgURLList.size(); i++){
                                Log.e("okok", imgURLList.get(i));
                            }
                        }
                        else
                            Log.e("cannot", "cannot");
                    }
                });
            }
        }).start();
    }

    protected ArrayList<String> getImage(){
        try{
            // temporary hardcode
            URL htmlURL = new URL("https://stocksnap.io");

            URLConnection conn = htmlURL.openConnection();
            InputStream in = conn.getInputStream();

            InputStreamReader inputStreamReader =
                    new InputStreamReader(in, "gbk");
            BufferedReader reader = new BufferedReader(inputStreamReader);
            ArrayList<String> imgURLList = new ArrayList<String>(){};


            String line;

            while((line = reader.readLine()) != null){
                if (line.startsWith("<img data-cfsrc=")){
                    if (line.contains("img src=\"https://")){
                        line = grabImg(line);
                        Log.e("line = ", line);

                        imgURLList.add(line);
                        Log.e("sb = ", String.valueOf(imgURLList.size()));
                        if (imgURLList.size() > 35){
                            break;
                        }
                    }
                }
            }
            Log.e("hey = ", String.valueOf(imgURLList.size()));
            return imgURLList;
        }
        catch (Exception e){
            e.getMessage();
        }
        return null;
    }

    protected String grabImg(String line){
        int startIdx, endIdx;
        String findStart = "img src=";
        String findEnd = ".jpg";
        startIdx = line.indexOf(findStart) + findStart.length() + 1;
        endIdx = line.lastIndexOf(".jpg") + findEnd.length();

        String imgURL = line.substring(startIdx, endIdx);

        return imgURL;
    }
}