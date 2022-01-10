package com.team7.matchinggame;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

public class ScoreboardAdapter extends ArrayAdapter {
    private final Context context;

    protected Integer[] number;
    protected String[] timings;

    public ScoreboardAdapter(Context context, Integer[] number, String[] timings) {
        super(context, R.layout.scoreboard_row);
        this.context = context;
        this.number = number;
        this.timings = timings;

        addAll(new Object[number.length]);
    }

    @androidx.annotation.NonNull
    public View getView(int pos, View view, @NonNull ViewGroup parent) {
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(
                    Activity.LAYOUT_INFLATER_SERVICE);

            // if we are not responsible for adding the view to the parent,
            // then attachToRoot should be 'false' (which is in our case)
            view = inflater.inflate(R.layout.scoreboard_row, parent, false);
        }

        // set the text for number in TextView
        TextView num = view.findViewById(R.id.number);
        num.setText(number[pos]);

        // set the text for timings in TextView
        TextView time = view.findViewById(R.id.time);
        time.setText(timings[pos]);

        return view;
    }
}
