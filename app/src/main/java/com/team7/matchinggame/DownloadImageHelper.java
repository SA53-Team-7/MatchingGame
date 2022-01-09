package com.team7.matchinggame;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.UUID;

public class DownloadImageHelper {

    // main method in this class
    private final Context context;

    public DownloadImageHelper(Context context){
        this.context = context;
    }

    protected File downloadImgByURL(String imgURL){
        try {
            File destFile = createFile();
            URL imgurl = new URL(imgURL);
            URLConnection conn = imgurl.openConnection();
            InputStream in = conn.getInputStream();
            FileOutputStream out = new FileOutputStream(destFile);

            byte[] buf = new byte[1024];
            int bytesRead = -1;
            while ((bytesRead = in.read(buf)) !=  -1){
                out.write(buf, 0, bytesRead);
            }
            out.close();
            in.close();
            return destFile;
        }
        catch (Exception e){
            e.getMessage();
            return null;
        }
    }

    protected File createFile(){
        String destFilename = UUID.randomUUID().toString() + ".jpg";
        File dir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File destFile = new File(dir, destFilename);
        Log.e("file", String.valueOf(destFile.exists()));
        return destFile;
    }
}
