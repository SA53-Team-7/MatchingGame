package com.team7.matchinggame;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Button;
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
	private Integer[] gridStatus = new Integer[12];
	private File[] allImages = new File[6];
	private Integer seconds = 0;
	private Integer gameProgress = 0;
	private boolean stopwatchStatus = true;
	private boolean isbusy = false;
	private String selected1;
	private View previousGrid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
		context = this;
        retrieveImages();
        getAllImageViews();
        assignImagesToGrid();
		isbusy = true;
		Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				hideImages();
				runStopwatch();
				isbusy = false;
			}
		}, 3000);

		//test stuff
		Button button = findViewById(R.id.testSkip);
		button.setOnClickListener(this);
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
		Arrays.fill(gridStatus, 0);
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

	protected void matchImage(View view) {
		int grid = getGridNumber(view);
		if (selected1 == null) {
			selected1 = assignedImages[grid];
			previousGrid = view;
			toggleImage(grid, view);
			return;
		}
		if (selected1 != null) {
			if (selected1 == assignedImages[grid]){
				selected1 = null;
				addScore();
				toggleImage(grid, view);
				gridStatus[grid] = 2;
				gridStatus[getGridNumber(previousGrid)] = 2;
				previousGrid.setAlpha(0.3f);
				view.setAlpha(0.3f);
			}
			else {
				isbusy = true;
				toggleImage(grid, view);
				Handler handler = new Handler();
				handler.postDelayed(new Runnable() {
					@Override
					public void run() {
						toggleImage(getGridNumber(previousGrid), previousGrid);
						toggleImage(grid, view);
						previousGrid = null;
						selected1 = null;
						isbusy = false;
					}
				}, 500);
			}
		}
	}

	@Override
	public void onClick(View view) {
    	if (isbusy) {
    		return;
		}
		else {
			int id = view.getId();
			if (id == R.id.testSkip) {
				addScore();
			}
			else{
				if (previousGrid != null) {
					if (id != previousGrid.getId()){
						matchImage(view);
						return;
					}
					else {
						return;
					}
				}
				else {
					matchImage(view);
					return;
				}
			}
		}
	}

	protected void toggleImage(int grid, View view) {
    	//Integer grid = Integer.parseInt(String.valueOf(id.charAt(id.length()-1)));
		ImageView imageView = findViewById(view.getId());
    	if (gridStatus[grid] == 1) {
			imageView.setImageResource(R.drawable.placeholder);
			gridStatus[grid] = 0;

		} else if (gridStatus[grid] == 0) {
			imageView.setImageBitmap(BitmapFactory.decodeFile(assignedImages[grid]));
			gridStatus[grid] = 1;
		} else {
    		return;
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
				if (stopwatchStatus == true) {
					seconds++;
				}
				handler.postDelayed(this, 1000);
			}
		});
	}

	protected void addScore() {
    	gameProgress++;
		TextView progress = findViewById(R.id.textGameProgress);
		progress.setText(gameProgress + " out of 6 Matches");
		if (gameProgress == 6) {
			stopwatchStatus = false;
			isbusy = true;
			Handler handler = new Handler();
			handler.postDelayed(new Runnable() {
				@Override
				public void run() {
					endGame();
				}
			}, 3000);
		}
	}

	protected void endGame() {
		Intent intent = new Intent(this, EndGame.class);
		intent.putExtra("timeElapsed", seconds);
		startActivity(intent);
	}

	protected Integer getGridNumber(View view) {
		String idString = view.getResources().getResourceName(view.getId());
		Integer grid = Integer.parseInt(idString.substring(29))-1;
		return grid;
	}
}