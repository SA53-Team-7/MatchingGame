package com.team7.matchinggame;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
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
    private Context context;
    private String webURL;
    private ArrayList<String> imgURLList = new ArrayList<String>();
    private int count;
    private TextView progressDesc;
    private Thread bgThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fetch_image);
        context = this;
        // removes old images in external folder
        cleanUpImages(2);

        // set up fetch button
        setupFetchBtn();

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

                    if (bgThread != null) {
                        bgThread.interrupt();
                    }

                    EditText newURL = findViewById(R.id.urlTextbox);
                    if (newURL != null) {
                        // webURL = newURL.getText().toString();
                        webURL = newURL.getText().toString().startsWith("https://")
                                ? newURL.getText().toString() : "https://" + newURL.getText().toString();
                    }

                    Thread t1 = new Thread(() -> {
                        imgURLList = FetchImageHelper.getImageURLList(webURL);
                    });
                    t1.start();
                    try {
                        t1.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    count = 1;
                    checkImageNum();
                    displayImg();
                }
            });
        }
    }

    protected void displayProgress(int number){
        // progress bar and text view
        ProgressBar progressBar = findViewById(R.id.progressBar);
        TextView progressDesc = findViewById(R.id.progressDesc);

        progressBar.setMax(imgURLList.size());
        progressBar.setProgress(count);
        progressDesc.setText("Downloading " + String.valueOf(number) + " of " + String.valueOf(imgURLList.size()) + " images...");
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

    protected void checkImageNum() {
        if (imgURLList.size() < 6 || imgURLList == null) {
            String title = "Notice";
            String msg = "Sorry, not enough images in the URL. \nPlease enter another URL.";

            AlertDialog.Builder builder = new AlertDialog.Builder(this)
                    .setTitle(title)
                    .setMessage(msg)
                    .setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            });
            builder.create().show();
        }
    }


    protected void displayImg(){
        bgThread = new Thread(new Runnable() {
            @Override
            public void run() {
                for(int i = 1; i <= imgURLList.size(); i++){
                    Log.e("i", String.valueOf(i));
                    final int num = i;

                DownloadImageHelper dhelper = new DownloadImageHelper(context);
                File f = dhelper.downloadImgByURL(imgURLList.get(num - 1));
                if (f != null) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            int resId = 0;
                            try {
                                resId = R.id.class.getField("img" + String.valueOf(num)).getInt(null);
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

                            displayProgress(num);

                            if (Thread.interrupted()) {
                                return;
                            }
                        }
                    });
                }
            }
            }
        });
        bgThread.start();
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

        progressDesc = findViewById(R.id.progressDesc);
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