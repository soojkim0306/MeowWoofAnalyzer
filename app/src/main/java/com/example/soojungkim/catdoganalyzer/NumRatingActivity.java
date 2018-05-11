package com.example.soojungkim.catdoganalyzer;

import android.app.Activity;
import android.media.Rating;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.RatingBar;


public class NumRatingActivity extends AppCompatActivity {

    private float ratingVal = 0;
;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_num_rating);
        RatingBar ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                ratingVal = rating;
            }
        });
    }

    /* Sending the rating back to Main */
    public void onClick(View v) {
        Intent myIntent = new Intent();
        myIntent.putExtra("rating", ratingVal);
        setResult(Activity.RESULT_OK, myIntent);
        finish();
    }
}
