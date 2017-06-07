package com.sourcey.materiallogindemo.Fragment;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.CandleStickChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.CandleData;
import com.github.mikephil.charting.data.CandleDataSet;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.sourcey.materiallogindemo.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.PointValue;


/**
 * A simple {@link Fragment} subclass.
 */
public class ChartFragment extends Fragment {


    public ChartFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootView = inflater.inflate(R.layout.fragment_chart, container, false);

//        CandleStickChart candleStickChart = (CandleStickChart) rootView.findViewById(R.id.chart);
//
//        ArrayList<CandleEntry> entries = new ArrayList<>();
//        entries.add(new CandleEntry(0, 46.2f, 20.2f, 27.0f, 41.3f));
//        entries.add(new CandleEntry(1, 55.0f, 27.0f, 33.5f, 49.6f));
//        entries.add(new CandleEntry(2, 52.5f, 30.2f, 35.0f, 45.0f));
//        entries.add(new CandleEntry(3, 60.0f, 32.5f, 44.0f, 50.0f));
//        entries.add(new CandleEntry(4, 56.7f, 20.0f, 28.0f, 45.0f));
//        entries.add(new CandleEntry(5, 46.2f, 20.2f, 27.0f, 41.3f));
//        entries.add(new CandleEntry(6, 55.0f, 27.0f, 33.5f, 49.6f));
//
//        CandleDataSet dataset = new CandleDataSet(entries, "# of Calls");
//
//        ArrayList<String> labels = new ArrayList<String>();
//
//        for (int i = 1; i <= 7; i++) {
//            Date currentDate = new Date(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(7 - i));
//            SimpleDateFormat sdf = new SimpleDateFormat("E");
//            String day = sdf.format(currentDate);
//            labels.add(day);
//        }
//
//        CandleData data = new CandleData(labels, dataset);
//        candleStickChart.setData(data);
//        int[] black = {Color.BLACK, Color.BLACK};
//        dataset.setColors(black);
//        dataset.setValueTextSize(15);
//
//        // dataset.setColors(ColorTemplate.COLORFUL_COLORS); //
//        candleStickChart.setBackgroundColor(Color.TRANSPARENT); //set whatever color you prefer
//        candleStickChart.setDrawGridBackground(false);// this is a must
//
//        XAxis xAxis = candleStickChart.getXAxis();
//        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
//        xAxis.setDrawGridLines(false);
//        xAxis.setDrawAxisLine(true);
//        xAxis.setAxisLineColor(Color.BLACK);
//        xAxis.setTextSize(15);
//
//
//        candleStickChart.getLegend().setEnabled(false);
//        candleStickChart.setDescription("");
//        candleStickChart.getAxisLeft().setDrawLabels(false);
//        candleStickChart.getAxisRight().setDrawLabels(false);
//        candleStickChart.getAxisLeft().setDrawGridLines(false);
//        candleStickChart.getAxisLeft().setDrawAxisLine(false);
//        candleStickChart.getAxisLeft().setAxisMinValue(80f);
//        candleStickChart.getAxisRight().setAxisMinValue(80f);
//        candleStickChart.getAxisRight().setDrawGridLines(false);
//        candleStickChart.getAxisRight().setDrawAxisLine(false);
//        candleStickChart.setPinchZoom(false);
//        candleStickChart.setDoubleTapToZoomEnabled(false);
//        candleStickChart.setTouchEnabled(false);
//        candleStickChart.setDescriptionTextSize(15);
//
//        // candleStickChart.setAutoScaleMinMaxEnabled(true);
//        candleStickChart.invalidate();

        return rootView;
    }

}
