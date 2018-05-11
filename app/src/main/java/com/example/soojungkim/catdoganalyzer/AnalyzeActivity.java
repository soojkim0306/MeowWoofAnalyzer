package com.example.soojungkim.catdoganalyzer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class AnalyzeActivity extends AppCompatActivity {

    BarChart faceChart;
    BarChart rateChart;
    float dogFaceJoyAvg;
    float catFaceJoyAvg;
    float dogFaceSurpriseAvg;
    float catFaceSurpriseAvg;
    float dogRateAvg;
    float catRateAvg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analyze);

        /* Receive bundle from Result Activity */
        Bundle b = this.getIntent().getExtras();
        dogFaceJoyAvg = b.getFloat("dogFaceJoyAvg");
        catFaceJoyAvg = b.getFloat("catFaceJoyAvg");
        dogFaceSurpriseAvg = b.getFloat("dogFaceSurpriseAvg");
        catFaceSurpriseAvg = b.getFloat("catFaceSurpriseAvg");
        dogRateAvg = b.getFloat("dogRateAvg");
        catRateAvg = b.getFloat("catRateAvg");

        faceChart = (BarChart) findViewById(R.id.FaceBarGraph);
        rateChart = (BarChart) findViewById(R.id.RatingBarGraph);

        float[] joyFaceVals = new float[] {dogFaceJoyAvg, catFaceJoyAvg};
        float[] surpriseFaceVals = new float[] {dogFaceSurpriseAvg, catFaceSurpriseAvg};
        ArrayList<BarEntry> joyFaceEntries = new ArrayList<>();
        ArrayList<BarEntry> surpriseFaceEntries = new ArrayList<>();

        /* values for Joy and Surprise */
        for(int i = 0; i < joyFaceVals.length; i++) {
            joyFaceEntries.add(new BarEntry( i, joyFaceVals[i]));
            surpriseFaceEntries.add(new BarEntry(i, surpriseFaceVals[i]));
        }

        /* Create data sets for bar graphs */
        BarDataSet joySet = new BarDataSet(joyFaceEntries, "Joy level");
        BarDataSet surpriseSet = new BarDataSet(surpriseFaceEntries, "Surprise level");

        /* Bar graph formatting */
        joySet.setColor(ColorTemplate.COLORFUL_COLORS[0]);
        surpriseSet.setColor(ColorTemplate.COLORFUL_COLORS[2]);
        float groupSpace = 0.06f;
        float barSpace = 0.02f;
        float barWidth = 0.45f;

        ArrayList<BarEntry> rateBarEntries = new ArrayList<>();
        rateBarEntries.add(new BarEntry(0.5f, dogRateAvg));
        rateBarEntries.add(new BarEntry(1.5f, catRateAvg));
        BarDataSet rateDataSet = new BarDataSet(rateBarEntries, "Ratings Calculated");

        /* String labels for x-axis */
        String[] xLabels = new String[2];
        xLabels[0] = "DOG";
        xLabels[1] = "CAT";

        /* Create grouped bar graph */
        BarData faceData = new BarData(joySet, surpriseSet);
        faceData.setBarWidth(barWidth);

        XAxis faceX = faceChart.getXAxis();

        /* More graph formatting */
        faceX.setPosition(XAxis.XAxisPosition.BOTTOM);
        faceX.setAxisMinimum(0f);
        faceX.setAxisMaximum(2f);

        faceX.setGranularity(1f);
        faceChart.setData(faceData);
        faceChart.groupBars(0f, groupSpace, barSpace);

        faceX.setValueFormatter(new XAxisValueFormatter(xLabels));
        faceX.setCenterAxisLabels(true);

        faceChart.setTouchEnabled(true);
        faceChart.setScaleEnabled(true);
        faceChart.setFitBars(true);

        BarData rateData = new BarData(rateDataSet);
        rateData.setBarWidth(0.7f);

        XAxis rateX = rateChart.getXAxis();
        rateX.setAxisMinimum(0f);
        rateX.setAxisMaximum(2f);
        rateX.setValueFormatter(new XAxisValueFormatter(xLabels));
        rateX.setCenterAxisLabels(true);
        rateX.setGranularity(1f);
        YAxis rateY = rateChart.getAxisLeft();
        rateChart.getAxisRight().setEnabled(false);
        rateY.setAxisMinimum(0f);
        rateY.setAxisMaximum(5f);
        rateY.setGranularity(1f);
        rateX.setPosition(XAxis.XAxisPosition.BOTTOM);
        rateChart.setData(rateData);
        rateChart.setTouchEnabled(true);
        rateChart.setScaleEnabled(true);
        rateChart.setFitBars(true);
    }

    /* Formatter function for X axis */
    public class XAxisValueFormatter implements IAxisValueFormatter {
        private String[] axisVals;

        public XAxisValueFormatter(String[] axisVals) {
            this.axisVals = axisVals;
        }

        public String getFormattedValue(float val, AxisBase axis) {
            if(axisVals.length > (int) val && (int) val >= 0) {
                return axisVals[(int) val];
            } else {
                return null;
            }
        }
    }
}
