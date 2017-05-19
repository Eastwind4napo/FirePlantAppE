package com.example.kaumadi.fireplantapp.mFragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kaumadi.fireplantapp.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Niroshana on 5/1/2017.
 */

public class LightFragment extends Fragment {

    private String API_KEY = "21vCrRxRAFYrGxGQvgyxRAB34NdeW5";
    private String lightVarId = "58dd31da7625421122ffc60c";
    private LineChart lightChart;




    private static final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    public LightFragment() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.light_fragment,container,false);
        lightChart = (LineChart) v.findViewById(R.id.chartLight);

        initTempChart(lightChart);

        //Temperature


        ( new UbidotsClient() ).handleUbidots(lightVarId, API_KEY, new UbidotsClient.UbiListener() {
            @Override
            public void onDataReady(List<UbidotsClient.Value> result) {
                Log.d("Chart", "======== On data Ready ===========");
                List<Entry> entries = new ArrayList();
                List<String> labels = new ArrayList<String>();
                for (int i = 0; i < result.size(); i++) {

                    Entry be = new Entry(result.get(i).value, i);
                    entries.add(be);
                    Log.d("Chart", be.toString());
                    // Convert timestamp to date
                    Date d = new Date(result.get(i).timestamp);
                    // Create Labels
                    labels.add(sdf.format(d));



                }

                LineDataSet ld = new LineDataSet(entries, "Light");

                ld.setDrawHighlightIndicators(false);
                ld.setDrawValues(true);
                ld.setColor(Color.BLUE);
                ld.setCircleColor(Color.BLUE);
                ld.setLineWidth(1f);
                ld.setCircleSize(3f);
                ld.setDrawCircleHole(false);
                ld.setFillAlpha(65);
                ld.setFillColor(Color.BLUE);
                ld.setDrawCubic(true);
                ld.setDrawFilled(true);
                ld.setHighlightEnabled(true);



                LineData lineData = new LineData(labels, ld);
                lightChart.setData(lineData);


                if (getActivity() != null) {
                    Handler handler = new Handler(LightFragment.this.getActivity().getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            lightChart.invalidate();
                        }
                    });

                }
            }


        });

        return v;




    }






    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void initTempChart(LineChart chart) {
        chart.setTouchEnabled(true);
        chart.setDrawGridBackground(false);
        chart.getAxisRight().setEnabled(false);
        chart.setScaleMinima(10f,5f);
        chart.animateY(3000);
        chart.setNoDataText("Loading chart data...");


        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setAxisMaxValue(1000F);
        leftAxis.setAxisMinValue(10F);
        leftAxis.setStartAtZero(false);
        leftAxis.setAxisLineWidth(2);
        leftAxis.setDrawGridLines(true);



        // X-Axis
        XAxis xAxis = chart.getXAxis();
        xAxis.resetLabelsToSkip();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(true);




    }

}
