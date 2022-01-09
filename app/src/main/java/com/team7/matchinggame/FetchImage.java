package com.team7.matchinggame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class FetchImage extends AppCompatActivity implements View.OnClickListener {

    private Integer[] ImageViewIdList = { R.id.img1, R.id.img2, R.id.img3, R.id.img4, R.id.img5,
            R.id.img6, R.id.img7, R.id.img8, R.id.img9, R.id.img10,
            R.id.img11, R.id.img12, R.id.img13, R.id.img14, R.id.img15,
            R.id.img16, R.id.img17, R.id.img18, R.id.img19, R.id.img20 };

    private ArrayList<ImageView> ImageViewList = new ArrayList<>(); // contains all imageviews
    private HashMap<Integer, String> FileNameLists = new HashMap<>(); // contains imageview IDs and respective file name
    private ArrayList<String> selectedImages = new ArrayList(); // contains imageview IDs of selected cards

    private Button fetchBtn;
    private EditText url;

    // dantong:start
    private static Context context;
    private String webURL;
    private ArrayList<String> imgURLList = new ArrayList<String>();
    private int count = 1;
    // dantong:end


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fetch_image);

        // dantong:start
        context = this;
        setupBtn();
        // dantong:end

        setupImageSelection(); // set onclick listeners for imageviews
    }

    // dantong:start
    protected void setupBtn(){
        Button btn = findViewById(R.id.fetchBtn);
        if (btn != null){
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
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
                    displayImg();
                }
            });
        }
    }

//    @Override
//    public void onClick(View view) {
//        EditText newURL = findViewById(R.id.urlTextbox);
//        if (newURL != null) {
//            webURL = newURL.getText().toString();
//        }
//
//        Thread t1 = new Thread(() -> {
//            Log.e("hello", "here");
//            imgURLList = FetchImageHelper.getImageURLList(webURL);
//        });
//        t1.start();
//        try {
//            t1.join();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        displayImg();
//    }

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
                                count++;

                                ImageView imageView = findViewById(resId);
                                Bitmap bitmap = BitmapFactory.decodeFile(f.getAbsolutePath());
                                imageView.setImageBitmap(bitmap);
                            }
                        });
                    }

                }
            }).start();
        }
    }
    // dantong:end


    protected void setupImageSelection() {
        for (int i = 0; i < ImageViewIdList.length; i++) {
            ImageView imgView = findViewById(ImageViewIdList[i]);
            imgView.setOnClickListener(this);
            ImageViewList.add(imgView);
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.fetchBtn) {

        }else if (id == R.id.urlTextbox) {

        } else if (FileNameLists.containsKey(id)) {
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
            iv.setAlpha(0.5f);
        }

        if (selectedImages.size() == 6) {
            if (cleanUpImages(selectedImages)) {
                // Go to games page
            };
        }
    }

    public boolean cleanUpImages(ArrayList<String> selectedImages) {
        // Remove ImageView IDs that are needed in the next activity
        for (int i = 0; i < selectedImages.size(); i++) {
            FileNameLists.remove(Integer.parseInt(selectedImages.get(i)));
        }

        try {
            // Delete images that are not chosen
            File dir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

            for (Object value : FileNameLists.values()) {
                File file = new File(dir, value.toString() + ".jpg");
                if (file.exists()) {
                    file.delete();
                }
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}