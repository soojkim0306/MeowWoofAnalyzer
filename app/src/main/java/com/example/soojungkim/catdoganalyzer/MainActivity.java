package com.example.soojungkim.catdoganalyzer;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.content.Intent;
import android.widget.ViewSwitcher;

import com.affectiva.android.affdex.sdk.Frame;
import com.affectiva.android.affdex.sdk.detector.CameraDetector;
import com.affectiva.android.affdex.sdk.detector.Face;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements CameraDetector.CameraEventListener, CameraDetector.ImageListener{

    static final int GET_RATING_REQUEST = 1;
    int imgIndex = 0;
    int imageNo[] = {R.drawable.dog1, R.drawable.dog2, R.drawable.dog3, R.drawable.dog4, R.drawable.dog5, R.drawable.dog6, R.drawable.dog7, R.drawable.dog8, R.drawable.dog9, R.drawable.dog10, R.drawable.dog11, R.drawable.dog12, R.drawable.dog13, R.drawable.dog14, R.drawable.dog15,
        R.drawable.cat1, R.drawable.cat2, R.drawable.cat3, R.drawable.cat4, R.drawable.cat5, R.drawable.cat6,R.drawable.cat7, R.drawable.cat8, R.drawable.cat9, R.drawable.cat10, R.drawable.cat11, R.drawable.cat12, R.drawable.cat13, R.drawable.cat14, R.drawable.cat15};

    //private ImageView mainImage;
    SurfaceView cameraDetectorSurfaceView;
    CameraDetector cameraDetector;
    int maxProcessingRate = 10;

    float joySMA;
    float joySMAsum;
    float surpriseSMA;
    float surpriseSMASum;
    LinkedList<Float> joySMAList;
    LinkedList<Float> surpriseSMAList;
    LinkedList<Float> smileList;
    LinkedList<Float> cheekRaiseList;
    LinkedList<Float> jawDropList;
    LinkedList<Float> mouthOpenList;
    float[] faceJoySMAArray;
    float[] faceSurpriseSMAArray;
    float rating;
    float[] ratingArray;
    float[] smileArray;
    float[] cheekRaiseArray;
    float[] jawDropArray;
    float[] mouthOpenArray;
    ArrayList<Integer> randOrder;

    @Override
    public void onCameraSizeSelected(int cameraHeight, int cameraWidth, Frame.ROTATE rotation) {
        ViewGroup.LayoutParams params = cameraDetectorSurfaceView.getLayoutParams();

        params.height = cameraHeight;
        params.width = cameraWidth;

        cameraDetectorSurfaceView.setLayoutParams(params);

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* Initializing arrays and lists */
        joySMAList = new LinkedList<>();
        surpriseSMAList = new LinkedList<>();
        smileList = new LinkedList<>();
        cheekRaiseList = new LinkedList<>();
        jawDropList = new LinkedList<>();
        mouthOpenList = new LinkedList<>();
        faceJoySMAArray = new float[imageNo.length];
        faceSurpriseSMAArray = new float[imageNo.length];
        ratingArray = new float[imageNo.length];
        smileArray = new float[imageNo.length];
        cheekRaiseArray = new float[imageNo.length];
        jawDropArray = new float[imageNo.length];
        mouthOpenArray = new float[imageNo.length];

        randOrder = new ArrayList<>();

        /* Randomize the picture order */
        Random rand = new Random();
        while(randOrder.size() < imageNo.length) {
            int ranNum = rand.nextInt(imageNo.length);
            if(!randOrder.contains(ranNum)) {
                randOrder.add(ranNum);
            }
        }

    }

    public void onResume() {
        super.onResume();
        setContentView(R.layout.activity_main);

        /* When all pictures are shown */
        if(imgIndex == imageNo.length) {
            String fileName = "result.csv";
            /* Write to a file */
            File file = new File(Environment.getExternalStorageDirectory(), fileName);
            try {
                FileWriter fw = new FileWriter(file, true);
                BufferedWriter bw = new BufferedWriter(fw);
                bw.write("Joy");
                writeArray(bw, faceJoySMAArray);

                bw.write("Surprise");
                writeArray(bw, faceSurpriseSMAArray);

                bw.write("Smile");
                writeArray(bw, smileArray);

                bw.write("jawDrop");
                writeArray(bw, jawDropArray);


                bw.write("CheekRaise");
                writeArray(bw, cheekRaiseArray);

                bw.write("mouthOpen");
                writeArray(bw, mouthOpenArray);


                bw.write("Rating");
                writeArray(bw, ratingArray);
                bw.close();
                fw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            /* And send arrays to Result Activity */
            Bundle b = new Bundle();
            b.putFloatArray("faceJoySMAArray", faceJoySMAArray);
            b.putFloatArray("faceSurpriseSMAArray", faceSurpriseSMAArray);
            b.putFloatArray("ratingArray", ratingArray);
            b.putInt("numImage", imageNo.length);
            Intent toResult = new Intent(this, ResultActivity.class);
            toResult.putExtras(b);
            startActivity(toResult);

        } else {
            final ImageSwitcher imageSwitcher = (ImageSwitcher) findViewById(R.id.imageChanger);

            imageSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
                @Override
                public View makeView() {
                    ImageView imageView = new ImageView(getApplicationContext());
                    imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                    imageView.setLayoutParams(new ImageSwitcher.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
                    return imageView;
                }
            });

            imageSwitcher.setImageResource(imageNo[randOrder.get(imgIndex)]);

            joySMA = 0; surpriseSMA = 0;
            joySMAsum = 0; surpriseSMASum = 0;

            /* clear the list for the new picture */
            joySMAList.clear();
            surpriseSMAList.clear();
            smileList.clear();
            cheekRaiseList.clear();
            mouthOpenList.clear();
            jawDropList.clear();

            cameraDetectorSurfaceView = (SurfaceView) findViewById(R.id.cameraDetectorSurfaceView);
            cameraDetectorSurfaceView.setAlpha(0);

            cameraDetector = new CameraDetector(this, CameraDetector.CameraType.CAMERA_FRONT, cameraDetectorSurfaceView);
            cameraDetector.setMaxProcessRate(maxProcessingRate);

            cameraDetector.setImageListener(this);
            cameraDetector.setOnCameraEventListener(this);

            cameraDetector.setDetectAllEmotions(true);
            cameraDetector.setDetectAllExpressions(true);
            cameraDetector.start();

        }

    }


    public void onClick(View v) {
        /* If not first picture, compute averages */
        if(!joySMAList.isEmpty()) {
            int idx = randOrder.get(imgIndex);
            faceJoySMAArray[idx] = computeAvg(joySMAList);
            faceSurpriseSMAArray[idx] = computeAvg(surpriseSMAList);
            cheekRaiseArray[idx] = computeAvg(cheekRaiseList);
            smileArray[idx] = computeAvg(smileList);
            jawDropArray[idx] = computeAvg(jawDropList);
            mouthOpenArray[idx] = computeAvg(mouthOpenList);
        }
        /* When the first picture, go to Rating - requiring the rate */
        Intent getRating = new Intent(this, NumRatingActivity.class);
        startActivityForResult(getRating, GET_RATING_REQUEST);
    }

    /* Coming back from Rating Activity */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GET_RATING_REQUEST) {
            if(resultCode == RESULT_OK) {
                rating = data.getFloatExtra("rating", 0);
                ratingArray[randOrder.get(imgIndex)] = rating;
                imgIndex++;
            }

        }
    }

    @Override
    public void onImageResults(List<Face> faces, Frame frame, float timeStamp) {

        if(faces == null)
            return; //frame was not processed

        if(faces.size() == 0)
            return; //no face found

        Face face = faces.get(0);

        float joy = face.emotions.getJoy();
        float surprise = face.emotions.getSurprise();


        joySMAList.addLast(joy);
        surpriseSMAList.addLast(surprise);

        cheekRaiseList.addLast(face.expressions.getCheekRaise());
        jawDropList.addLast(face.expressions.getJawDrop());
        mouthOpenList.addLast(face.expressions.getMouthOpen());
        smileList.addLast(face.expressions.getSmile());

    }

    /**
     * Compute the SMA for input list
     *
     * @param ll
     * @return
     */
    public float computeAvg(LinkedList<Float> ll) {
        float sum = 0;
        for(int i = 0; i < ll.size(); i++) {
            sum += ll.get(i);
        }
        return sum / ll.size();
    }

    /**
     * Write the array to the file
     * @param bw
     * @param arr
     */
    private void writeArray(BufferedWriter bw, float[] arr) {
        try {
            bw.newLine();
            for(int i = 0; i < arr.length; i++) {
                bw.write(String.valueOf(arr[i]));
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
