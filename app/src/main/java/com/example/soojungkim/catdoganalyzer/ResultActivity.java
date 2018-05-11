package com.example.soojungkim.catdoganalyzer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DecimalFormat;

public class ResultActivity extends AppCompatActivity {

    float dogFaceJoyAvg;
    float catFaceJoyAvg;
    float dogFaceSurpriseAvg;
    float catFaceSurpriseAvg;
    float dogRateAvg;
    float catRateAvg;
    int resultImgs[] = {R.drawable.dogperson, R.drawable.catperson, R.drawable.catanddog, R.drawable.dogsandcat, R.drawable.catwithdogs};
    ImageView resultPic;
    TextView resultText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        /* Receive Intent bundle from Main */
        Bundle b = this.getIntent().getExtras();
        float[] faceJoySMAArray = b.getFloatArray("faceJoySMAArray");
        float[] faceSurpriseSMAArray = b.getFloatArray("faceSurpriseSMAArray");
        float[] ratingArray = b.getFloatArray("ratingArray");
        int imgSize = b.getInt("numImage");

        /* Computing the average of values in each array */
        dogFaceJoyAvg = 0; catFaceJoyAvg = 0;
        dogFaceSurpriseAvg = 0; catFaceSurpriseAvg = 0;
        dogRateAvg = 0; catRateAvg = 0;
        int imageSizePer = imgSize / 2;

        for(int i = 0; i < 15; i++) {
            dogFaceJoyAvg += faceJoySMAArray[i];
            dogFaceSurpriseAvg += faceSurpriseSMAArray[i];
            dogRateAvg += ratingArray[i];
        }
        for(int i = 15; i < imgSize; i++) {
            catFaceJoyAvg += faceJoySMAArray[i];
            catFaceSurpriseAvg += faceSurpriseSMAArray[i];
            catRateAvg += ratingArray[i];
        }

        dogFaceJoyAvg = dogFaceJoyAvg / imageSizePer;
        dogRateAvg = dogRateAvg / imageSizePer;
        catFaceJoyAvg = catFaceJoyAvg / imageSizePer;
        catRateAvg = catRateAvg / imageSizePer;
        dogFaceSurpriseAvg = dogFaceSurpriseAvg / imageSizePer;
        catFaceSurpriseAvg = catFaceSurpriseAvg /imageSizePer;

        /* Debugging purpose only
        System.out.println("dogJoyFace: " + dogFaceJoyAvg + " catJoyFace: " + catFaceJoyAvg);
        System.out.println("dogSur: " + dogFaceSurpriseAvg+ " catSur: " + catFaceSurpriseAvg);
        System.out.println("dogRate: " + dogRateAvg + " catRate: " + catRateAvg);
        */

        resultPic = (ImageView) findViewById(R.id.result);
        DecimalFormat df = new DecimalFormat("#.##");
        /* Result Analysis Algorithm */
        if(dogRateAvg - catRateAvg > 0.25) {
            if(dogFaceJoyAvg > catFaceJoyAvg) {
                //displace DOGperson
                resultPic.setImageResource(resultImgs[0]);
                String result;
            } else {
                //both
                resultPic.setImageResource(resultImgs[3]);
            }
        } else if(catRateAvg - dogRateAvg > 0.25) {
            if(catFaceJoyAvg > dogFaceJoyAvg) {
                //cat person
                resultPic.setImageResource(resultImgs[1]);
            } else {
                //both
                resultPic.setImageResource(resultImgs[4]);
            }
        } else {
            //both
            resultPic.setImageResource(resultImgs[2]);
            resultText = (TextView) findViewById(R.id.resultText);
            if(dogFaceJoyAvg > catFaceJoyAvg) {
                double percent = dogFaceJoyAvg/(dogFaceJoyAvg+catFaceJoyAvg) * 100;
                String resultString = "but maybe you are a slightly more of a DOG person: " + df.format(percent) + "% dog person";
                resultText.setText(resultString);

            } else {
                double percent = catFaceJoyAvg/(dogFaceJoyAvg+catFaceJoyAvg) * 100;
                String resultString = "but maybe you are a slightly more of a CAT person: " + df.format(percent) + "% cat person";
                resultText.setText(resultString);
            }
        }
    }

    public void onClick(View v) {
        /* Sending bundle to Analyze Activity */
        Bundle b = new Bundle();
        b.putFloat("dogFaceJoyAvg", dogFaceJoyAvg);
        b.putFloat("dogFaceSurpriseAvg", dogFaceSurpriseAvg);
        b.putFloat("catFaceJoyAvg", catFaceJoyAvg);
        b.putFloat("catFaceSurpriseAvg", catFaceSurpriseAvg);
        b.putFloat("dogRateAvg", dogRateAvg);
        b.putFloat("catRateAvg", catRateAvg);

        Intent toAnalyze = new Intent(this, AnalyzeActivity.class);
        toAnalyze.putExtras(b);
        startActivity(toAnalyze);
    }
}
