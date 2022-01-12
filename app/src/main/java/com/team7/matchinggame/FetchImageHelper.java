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
    private static final String[] suffix = new String[]{".jpg", ".png", ".jpeg"};

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

                if (line.contains("img src=\"https://") && !line.contains("smiley"))
                    for (String s: suffix)
                        if (line.contains(s)) {
                            line = grabImg(line);
                            imgURLList.add(line);
                            // if images number = 35, stop grab
                            if (imgURLList.size() > 34) {
                                break;
                            }
                        }
            }

            in.close();
            imgURLList = ramdomSelect20Img(imgURLList);
            return imgURLList;
        }
        catch (Exception e){
            e.getMessage();
        }
        return null;
    }

    protected static String grabImg(String line){
        int startIdx;
        String findStart = "img src=";
        startIdx = line.indexOf(findStart) + findStart.length() + 1;

        // only support 3 kinds of image types
        int endIdx = 0;
        for (String s: suffix){
            if (line.contains(s)) {
                endIdx = line.lastIndexOf(s) + s.length();
            }
        }

        String imgURL = line.substring(startIdx, endIdx);

        return imgURL;
    }

    protected static ArrayList<String> ramdomSelect20Img(ArrayList<String> allImgURL){
        Random r = new Random();
        ArrayList<Integer> randomNums = new ArrayList<Integer>(){};
        ArrayList<String> randomImgs = new ArrayList<String>(){};
        int num;
        int allURLsize = allImgURL.size();
        int targetImgNum = 20;
        // if image URL list >= 20, randomly select 20 images
        if (allURLsize > targetImgNum){
            while (randomNums.size() != targetImgNum){
                num = r.nextInt(allURLsize);
                if (!randomNums.contains(num)){
                    randomNums.add(num);
                }
            }
            for (Integer i: randomNums) {
                randomImgs.add(allImgURL.get(i));
            }
        }
        else
            randomImgs = allImgURL;
        return randomImgs;
    }
}
