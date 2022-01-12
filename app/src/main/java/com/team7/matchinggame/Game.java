package com.team7.matchinggame;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Button;
import android.widget.TextView;


import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

public class Game extends AppCompatActivity implements View.OnClickListener{
    private Context context;
	private final ArrayList<ImageButton> ImageButtonList = new ArrayList<>(); //contains all imagebuttons
	private final String[] assignedImages = new String[12];	//contains images randomly assigned to imagebuttons, in same index
	private Integer[] gridStatus = new Integer[12];		//contains status of the imagebuttons 0 = hidden, 1 = revealed, 2 = revealed, matched and locked
	private File[] allImages = new File[6];				//contains the 6 .jpg files selected by the user
	private Integer seconds = 0;						//stores the time taken by the user
	private Integer gameProgress = 0;					//stores the number of successful matches
	private boolean stopwatchStatus = true;				//flag for starting/stopping the stopwatch
	private boolean isBusy = false;						//flag for blocking UI inputs during delays
	private String selected1;							//stores the image file location of the first selection match
	private View previousGrid;							//stores the imagebutton of the first selection match

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
		context = this;
        retrieveImages();
        getAllImageButtons();
        assignImagesToGrid();
		isBusy = true;
		Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				hideImages();
				runStopwatch();
				isBusy = false;
			}
		}, 1500);

		//test stuff
		Button button = findViewById(R.id.testSkip);
		button.setOnClickListener(this);
    }

    protected void retrieveImages(){
		File dir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
		//retrieve all .jpg files in the SDcard pictures directory into allImages[]
		allImages = dir.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return (name.endsWith(".jpg"));
			}
		});
	}
	protected void getAllImageButtons() {
    	//retrieve all imagebuttons into ImageButtonList<>
		for (int i = 1; i <= 12; i++) {
			try {
				int imgViewId = R.id.class.getField("img" + i).getInt(null);
				ImageButton imgView = findViewById(imgViewId);
				imgView.setOnClickListener(this);
				ImageButtonList.add(imgView);
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (NoSuchFieldException e) {
				e.printStackTrace();
			}
		}
	}

	protected void assignImagesToGrid() {
    	//array to track which the number of times the images have been assigned
    	int[] count = new int[allImages.length];
		Arrays.fill(count, 0);
		int random;
		for (int i = 0; i < ImageButtonList.size(); i++){
			do {
				//pick a random image to assign
				random = (int) (Math.random() * allImages.length);
			}while (count[random] > 1);  //repeat until an image picked has been assigned less than 2 times
			assignedImages[i] = allImages[random].getAbsolutePath();
			//show assigned images on grid
			ImageButtonList.get(i).setImageBitmap(BitmapFactory.decodeFile(allImages[random].getAbsolutePath()));
			count[random]+=1;
		}
		Arrays.fill(gridStatus, 0);
	}

	protected void hideImages() {
    	//replace all shown images with question mark drawable
		for (ImageButton btn: ImageButtonList) {
			btn.setImageResource(R.drawable.question_mark);
		}
		TextView head = findViewById(R.id.gameHead);
		head.setText("Match!");
	}

	protected void matchImage(View view) {
		int grid = getGridNumber(view);
		//if nothing has previously been selected
		if (selected1 == null) {
			selected1 = assignedImages[grid];
			previousGrid = view;
			toggleImage(grid, view);
			return;
		}
		//if there is a previous selection for match
		if (selected1 != null) {
			//if the 2 selections match
			if (selected1.equals(assignedImages[grid])){
				selected1 = null;
				addScore();
				toggleImage(grid, view);
				gridStatus[grid] = 2;
				gridStatus[getGridNumber(previousGrid)] = 2;
				previousGrid.setAlpha(0.3f);
				view.setAlpha(0.3f);
				MediaPlayer matched = MediaPlayer.create(Game.this, R.raw.match);
				matched.start();
			}
			//if selections don't match
			else {
				isBusy = true;
				toggleImage(grid, view);
				Handler handler = new Handler();
				handler.postDelayed(new Runnable() {
					@Override
					public void run() {
						toggleImage(getGridNumber(previousGrid), previousGrid);
						toggleImage(grid, view);
						previousGrid = null;
						selected1 = null;
						isBusy = false;
					}
				}, 500);
			}
		}
	}

	@Override
	public void onClick(View view) {
    	//block inputs if isBusy flag is true
		if (!isBusy) {
			int id = view.getId();
			if (id == R.id.testSkip) {
				addScore();
			}
			else{
				//block input if the grid status is 2 (locked)
				if (gridStatus[getGridNumber(view)] != 2) {
					if (previousGrid != null) {
						//block clicking the already selected grid
						if (id != previousGrid.getId()){
							matchImage(view);
						}
					}
					else {
						matchImage(view);
					}
				}
			}
		}
	}

	protected void toggleImage(int grid, View view) {
		ImageButton imageButton = findViewById(view.getId());
		//hide image if status is 1 (revealed)
    	if (gridStatus[grid] == 1) {
			imageButton.setImageResource(R.drawable.question_mark);
			gridStatus[grid] = 0;
		//show image if status is 0 (hidden), ignore if status is 2 (locked)
		} else if (gridStatus[grid] == 0) {
			imageButton.setImageBitmap(BitmapFactory.decodeFile(assignedImages[grid]));
			gridStatus[grid] = 1;
		}
	}

	protected void runStopwatch() {
    	//run every second to add 1 to the int 'seconds' and update textView textGameTimer
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
				if (stopwatchStatus) {
					seconds++;
				}
				handler.postDelayed(this, 1000);
			}
		});
	}

	protected void addScore() {
    	//add 1 to the game progress for each match, ends calls endGame after all matches are complete (6)
    	gameProgress++;
		TextView progress = findViewById(R.id.textGameProgress);
		progress.setText(gameProgress + " out of 6 Matches");
		if (gameProgress == 6) {
			stopwatchStatus = false;
			isBusy = true;
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
    	//calls EndGame activity, passes the user's time
		Intent intent = new Intent(this, EndGame.class);
		intent.putExtra("timeElapsed", seconds);
		startActivity(intent);
	}


	protected Integer getGridNumber(View view) {
		//returns the grid number (1-12) from the view id
		String idString = view.getResources().getResourceName(view.getId());
		return Integer.parseInt(idString.substring(29))-1;
	}
}