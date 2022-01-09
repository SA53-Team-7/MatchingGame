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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fetch_image);
        context = this;
        // removes old images in external folder
        cleanUpImages(2);
        setupFetchBtn();

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
                        webURL = newURL.getText().toString();
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
                    count = 1;
                    displayImg();
                }
            });
        }
    }

    protected void displayImg(){
        for(int i = 1; i <= 20; i++){
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
                                int resId = 0;
                                try {
                                    resId = R.id.class.getField("img" + count).getInt(null);
                                } catch (IllegalAccessException e) {
                                    e.printStackTrace();
                                } catch (NoSuchFieldException e) {
                                    e.printStackTrace();
                                }

                                FileNameLists.put(resId, f.getName());
                                ImageView imageView = findViewById(resId);
                                Bitmap bitmap = BitmapFactory.decodeFile(f.getAbsolutePath());
                                imageView.setImageBitmap(bitmap);

                                ProgressBar progressBar = findViewById(R.id.progressBar);
                                TextView progressDesc = findViewById(R.id.progressDesc);

                                progressBar.setMax(20);
                                progressBar.setProgress(count);
                                progressDesc.setText("Downloading " + String.valueOf(count) + " of 20 images...");
                                count++;

                                if (count == 21){
                                    // set up onclick listeners for image selection
                                    setupImageSelection();
                                    Toast finishDL = Toast.makeText(context, "Downloading finished \nPlease select 6 images...", Toast.LENGTH_SHORT);
                                    finishDL.show();
                                }
                            }
                        });
                    }
                }
            }).start();
        }
    }
    // dantong:end


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

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (FileNameLists.containsKey(id)) {
            selectCard(Integer.toString(id));
        }
    }

    public void selectCard(String id) {
        ImageView iv = findViewById(Integer.parseInt(id));
        if (selectedImages.contains(id)) {
            selectedImages.remove(id);
            iv.setAlpha(1.0f);
        } else {
            selectedImages.add(id);
            iv.setAlpha(0.3f);
        }

        if (selectedImages.size() == 6) {
            if (cleanUpImages(1)) {
                // Go to games page
                System.out.println("Go to games page");
                // Intent intent = new Intent(this, );
                // startActivity(intent);
            };
        }
    }

    protected void resetGrids() {
        selectedImages.clear();
        for (ImageView i : ImageViewList) {
            i.setAlpha(1.0f);
        }
    }

    public boolean cleanUpImages(int type) {
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