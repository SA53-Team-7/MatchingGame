package com.team7.matchinggame;
import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

public class FetchImageHelper {
    protected static ArrayList<String> getImageURLList(String newURLStr){
        try{
            URL htmlURL = new URL(newURLStr);
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
                        imgURLList.add(line);
                        if (imgURLList.size() > 34){
                            break;
                        }
                    }
                }
            }

            in.close();
            Log.e("list size", String.valueOf(imgURLList.size()));
            imgURLList = ramdomSelect20Img(imgURLList);
            Log.e("list size", String.valueOf(imgURLList.size()));
            return imgURLList;
        }
        catch (Exception e){
            e.getMessage();
        }
        return null;
    }

    protected static String grabImg(String line){
        int startIdx, endIdx;
        String findStart = "img src=";
        String findEnd = ".jpg";
        startIdx = line.indexOf(findStart) + findStart.length() + 1;
        endIdx = line.lastIndexOf(".jpg") + findEnd.length();

        String imgURL = line.substring(startIdx, endIdx);

        return imgURL;
    }

    protected static ArrayList<String> ramdomSelect20Img(ArrayList<String> allImgURL){
        Random r = new Random();
        ArrayList<Integer> randomNums = new ArrayList<Integer>(){};
        ArrayList<String> randomImgs = new ArrayList<String>(){};
        int num;
        int fromWebImgNum = 35 - 1;
        int targetImgNum = 20;
        while (randomNums.size() != targetImgNum){
            num = r.nextInt(fromWebImgNum);
            Log.e("num", String.valueOf(num));
            if (!randomNums.contains(num)){
                randomNums.add(num);
            }
        }
        Log.e("nums", String.valueOf(randomNums));
        for (Integer i: randomNums) {
            randomImgs.add(allImgURL.get(i));
        }
        Log.e("list size", String.valueOf(randomImgs.size()));
        return randomImgs;
    }
}

