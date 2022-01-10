package com.team7.matchinggame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class FetchImage extends AppCompatActivity implements View.OnClickListener {
    private ArrayList<ImageView> ImageViewList = new ArrayList<>(); // contains all imageviews
    private HashMap<Integer, String> FileNameLists = new HashMap<>(); // contains imageview IDs and respective file name
    private ArrayList<String> selectedImages = new ArrayList(); // contains imageview IDs of selected cards
    private static Context context;
    private String webURL;
    private ArrayList<String> imgURLList = new ArrayList<String>();
    private int count;
    private TextView progressDesc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fetch_image);
        context = this;
        // removes old images in external folder
        cleanUpImages(2);

        // set up fetch button
        setupFetchBtn();
        progressDesc = findViewById(R.id.progressDesc);

        // setupProgressBarAndDesc();
    }

    protected void setupFetchBtn(){
        Button btn = findViewById(R.id.fetchBtn);
        if (btn != null){
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // removes old images from previous fetch
                    cleanUpImages(2);

                    // removes old grid selections from previous selection
                    resetGrids();

                    EditText newURL = findViewById(R.id.urlTextbox);
                    if (newURL != null) {
                        // webURL = newURL.getText().toString();
                        webURL = newURL.getText().toString().startsWith("https://")
                                ? newURL.getText().toString() : "https://" + newURL.getText().toString();
                    }

                    Thread t1 = new Thread(() -> {
                        Log.e("hello", "here");
                        imgURLList = FetchImageHelper.getImageURLList(webURL);
                    });
                    t1.start();
                    try {
                        t1.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    for (String s : imgURLList) {
                        System.out.println(s);
                    }

                    deletePreviousImage();
                    displayImg();
                }
            });
        }
    }

    // if fetch again, delete the stored image
    protected void deletePreviousImage() {
        File dir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        for (String file : FileNameLists.values()) {
            File f = new File(dir, file);
            if (f.exists()) {
                f.delete();
            }
        }
    }

    protected void displayImg(){
        count = 1;
        for(int i = 1; i <= imgURLList.size(); i++){
            Log.e("i", String.valueOf(i));
            final int num = i;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    DownloadImageHelper dhelper = new DownloadImageHelper(context);
                    File f = dhelper.downloadImgByURL(imgURLList.get(num-1));
                    if (f != null){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                Log.e("count value", String.valueOf(count));
                                Log.e("num value", String.valueOf(num));
                                int resId = 0;
                                try {
                                    resId = R.id.class.getField("img" + String.valueOf(count)).getInt(null);
                                } catch (IllegalAccessException e) {
                                    e.printStackTrace();
                                } catch (NoSuchFieldException e) {
                                    e.printStackTrace();
                                }

                                // Store ImageView ID and associated image with it
                                FileNameLists.put(resId, f.getName());

                                // load all images
                                ImageView imageView = findViewById(resId);
                                Bitmap bitmap = BitmapFactory.decodeFile(f.getAbsolutePath());
                                imageView.setImageBitmap(bitmap);

                                // progress bar and text view
                                ProgressBar progressBar = findViewById(R.id.progressBar);

                                progressBar.setMax(imgURLList.size());
                                progressBar.setProgress(count);
                                progressDesc.setText("Downloading " + String.valueOf(count) + " of " + String.valueOf(imgURLList.size()) + " images...");
                                count++;

                                // downloading finished, toast text
                                if (count == (imgURLList.size()+1)){
                                    // set up onclick listeners for image selection
                                    setupImageSelection();
                                    progressDesc.setText("Please select 6 images...");
                                    Toast finishDL = Toast.makeText(context, "Downloading finished!", Toast.LENGTH_LONG);
                                    finishDL.show();
                                }
                            }
                        });
                    }
                }
            }).start();
        }
    }


    protected void setupImageSelection() {
        for (int i = 1; i < 21; i++) {
            try {
                int imgViewId = R.id.class.getField("img" + i).getInt(null);
                ImageView imgView = findViewById(imgViewId);
                imgView.setOnClickListener(this);
                ImageViewList.add(imgView);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
    }

    protected void disableSelection() {
        for (ImageView imgview : ImageViewList) {
            imgview.setOnClickListener(null);
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (FileNameLists.containsKey(id)) {
            selectCard(Integer.toString(id));
        }
    }

    protected void selectCard(String id) {
        ImageView iv = findViewById(Integer.parseInt(id));
        if (selectedImages.contains(id)) {
            selectedImages.remove(id);
            iv.setAlpha(1.0f);
        } else {
            selectedImages.add(id);
            iv.setAlpha(0.3f);
        }

        progressDesc.setText(selectedImages.size() + " of 6 images selected");

        if (selectedImages.size() == 6) {
            disableSelection();
            if (cleanUpImages(1)) {
                // Go to games page
                Intent intent = new Intent(this, Game.class);
                startActivity(intent);
            };
        }
    }

    protected void resetGrids() {
        selectedImages.clear();
        for (ImageView i : ImageViewList) {
            i.setAlpha(1.0f);
        }
    }

    protected boolean cleanUpImages(int type) {
        File dir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        switch (type) {
            case 1:
                // Remove ImageView IDs that are needed in the next activity
                for (int i = 0; i < selectedImages.size(); i++) {
                    FileNameLists.remove(Integer.parseInt(selectedImages.get(i)));
                }

                try {
                    // Delete images that are not chosen
                    for (Object value : FileNameLists.values()) {
                        File file = new File(dir, value.toString());
                        if (file.exists()) {
                            file.delete();
                        }
                    }
                    return true;
                } catch (Exception e) {
                    return false;
                }
            case 2:
                try {
                    if (dir.exists()) {
                        String[] files = dir.list();
                        for (String file : files) {
                            File f = new File(dir, file);
                            if (f.exists()) {
                                f.delete();
                            }
                        }
                    }
                    return true;
                } catch (Exception e) {
                    return false;

                }
        }
        return false;
    }
}