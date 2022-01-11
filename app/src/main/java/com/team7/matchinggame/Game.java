package com.team7.matchinggame;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

public class Game extends AppCompatActivity implements View.OnClickListener{
    private static Context context;
	private ArrayList<ImageView> ImageViewList = new ArrayList<>();
	private String[] assignedImages = new String[12];
	private Boolean[] gridStatus = new Boolean[12];
	private File[] allImages = new File[6];
	private Integer seconds = 0;
	private Integer gameProgress = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
		context = this;
        retrieveImages();
        getAllImageViews();
        assignImagesToGrid();

		Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				hideImages();
				runStopwatch();
			}
		}, 3000);
    }

    protected void retrieveImages(){
		File dir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
		allImages = dir.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return (name.endsWith(".jpg"));
			}
		});
	}
	protected void getAllImageViews() {
		for (int i = 1; i <= 12; i++) {
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

	protected void assignImagesToGrid() {
    	int[] count = new int[allImages.length];
		Arrays.fill(count, 0);
		for (int i = 0; i < ImageViewList.size(); i++){
			int random = 0;
			do {
				random = (int) (Math.random() * allImages.length);
			}while (count[random] > 1);
			assignedImages[i] = allImages[random].getAbsolutePath();
			ImageViewList.get(i).setImageBitmap(BitmapFactory.decodeFile(allImages[random].getAbsolutePath()));
			count[random]+=1;
		}
		Arrays.fill(gridStatus, false);
	}

	protected void hideImages() {
		for (int i = 1; i <= 12; i++) {
			try {
				int imgViewId = R.id.class.getField("img" + i).getInt(null);
				ImageView imgView = findViewById(imgViewId);
				imgView.setImageResource(R.drawable.placeholder);
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (NoSuchFieldException e) {
				e.printStackTrace();
			}
		}
		TextView head = findViewById(R.id.gameHead);
		head.setText("Match!");
	}

	@Override
	public void onClick(View view) {
		int id = view.getId();
		toggleImage(view.getResources().getResourceName(id), view);
	}

	protected void toggleImage(String id, View view) {
    	Integer grid = Integer.parseInt(id.substring(29))-1;
    	//Integer grid = Integer.parseInt(String.valueOf(id.charAt(id.length()-1)));
		ImageView imageView = findViewById(view.getId());
    	if (gridStatus[grid]) {
			imageView.setImageResource(R.drawable.placeholder);
			gridStatus[grid] = false;

		} else {
			imageView.setImageBitmap(BitmapFactory.decodeFile(assignedImages[grid]));
			gridStatus[grid] = true;
		}
	}

	protected void runStopwatch() {
		TextView timeDisplay = findViewById(R.id.textGameTimer);
		Handler handler = new Handler();
		handler.post(new Runnable() {
			@Override
			public void run() {
				int hours = seconds / 3600;
				int minutes = (seconds % 3600) / 60;
				int secs = seconds % 60;
				String time = String.format(Locale.getDefault(),"%d:%02d:%02d", hours, minutes, secs);
				timeDisplay.setText(time);
				seconds++;
				handler.postDelayed(this, 1000);
			}
		});
	}

	protected void addScore() {
    	gameProgress++;
		TextView progress = findViewById(R.id.textGameProgress);
		progress.setText(gameProgress + " out of 6 Matches");
	}

	protected void endGame() {

	}
}