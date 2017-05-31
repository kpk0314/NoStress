package com.sourcey.materiallogindemo.Fragment;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.sourcey.materiallogindemo.MainActivity;
import com.sourcey.materiallogindemo.R;
import com.sourcey.materiallogindemo.StartActivity;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;


/**
 * A simple {@link Fragment} subclass.
 */





public class MainFragment extends Fragment {

    String nowValue;
    private SwipeRefreshLayout mSwipeRefresh;
    private LineChart mChart;


    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        nowValue = ((MainActivity) getActivity()).stress;


        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        TextView _textUpdate = (TextView) rootView.findViewById(R.id.textUpdate);
        TextView _levelValue = (TextView) rootView.findViewById(R.id.levelValue);
        TextView _todayValue = (TextView) rootView.findViewById(R.id.todayValue);
        TextView _yesterdayValue = (TextView) rootView.findViewById(R.id.yesterdayValue);
//        GraphView graph = (GraphView) rootView.findViewById(R.id.graph);


        // 현재 시간 업데이트
        String currenTime = DateFormat.getDateTimeInstance().format(new Date());
        _textUpdate.setText("마지막 업데이트. " + currenTime);

        // 스트레스 레벨 업데이트
        int intNow = Integer.parseInt(nowValue);
        if(intNow > 80) _levelValue.setText("5");
        else if(intNow > 60) _levelValue.setText("4");
        else if(intNow > 40) _levelValue.setText("3");
        else if(intNow > 20) _levelValue.setText("2");
        else _levelValue.setText("1");

        // 현재 스트레스 지수 업데이트
        _todayValue.setText(nowValue);

        // 어제이시간 업데이트(일단 임의의 값으로)
        int randomNum = (int)(Math.random() * 100) + 1;
        _yesterdayValue.setText(String.valueOf(randomNum));
        if(randomNum > 80) _yesterdayValue.setTextColor(getResources().getColor(R.color.level5));
        else if(randomNum > 60) _yesterdayValue.setTextColor(getResources().getColor(R.color.level4));
        else if(randomNum > 40) _yesterdayValue.setTextColor(getResources().getColor(R.color.level3));
        else if(randomNum > 20) _yesterdayValue.setTextColor(getResources().getColor(R.color.level2));
        else _yesterdayValue.setTextColor(getResources().getColor(R.color.level1));


        int test[] = {30, 20, 15, 18, 60, 30, 10, 50, 60, 32, 24, 45};

        mChart = (LineChart) rootView.findViewById(R.id.chart1);
        mChart.setDrawGridBackground(false);
        // no description text
        mChart.getDescription().setEnabled(false);
        // enable touch gestures
        mChart.setTouchEnabled(true);
        // enable scaling and dragging
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);
        // mChart.setScaleXEnabled(true);
        // mChart.setScaleYEnabled(true);
        // if disabled, scaling can be done on x- and y-axis separately
        mChart.setPinchZoom(true);

        int[] dataObjects = {30, 20, 15, 18, 60, 30, 10, 50, 60, 32, 24, 45};

        List<Entry> entries = new ArrayList<Entry>();

        for(int i = 0; i < 12; i ++) {
            entries.add(new Entry(dataObjects[i], i));
        }

        LineDataSet setData = new LineDataSet(entries, "TEST 1");
        setData.setAxisDependency(YAxis.AxisDependency.LEFT);

        ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        dataSets.add(setData);

        ArrayList<String> xVals = new ArrayList<String>();
        xVals.add("PM 5");
        xVals.add("PM 8");
        xVals.add("PM 11");
        xVals.add("AM 2");
        xVals.add("AM 5");
        xVals.add("AM 8");
        xVals.add("AM 11");
        xVals.add("PM 2");
        xVals.add("PM 5");
        


//        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[]{
//                new DataPoint(0, test[0]),
//                new DataPoint(1, test[1]),
//                new DataPoint(2, test[2]),
//                new DataPoint(3, test[3]),
//                new DataPoint(4, test[4]),
//                new DataPoint(5, test[5]),
//                new DataPoint(6, test[6]),
//                new DataPoint(7, test[7]),
//                new DataPoint(8, test[8]),
//                new DataPoint(9, test[9]),
//                new DataPoint(10, test[10]),
//                new DataPoint(11, test[11]),
//        });
//        graph.addSeries(series);


        // 화면 아래로 당겨서 업데이트
        mSwipeRefresh = (SwipeRefreshLayout) rootView.findViewById(R.id.swiperefresh);
        // resource id로 색상을 변경하려면 setColorSchemeResources() 사용
        mSwipeRefresh.setColorSchemeResources(R.color.colorPrimary, R.color.colorAccent);
        // Color 객체는 setColorSchemeColors(...)를 사용
        mSwipeRefresh.setColorSchemeColors(Color.BLUE, Color.YELLOW, Color.BLUE, Color.YELLOW);
        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // animation을 멈추려면, fasle로 설정
                mSwipeRefresh.setRefreshing(true);
                ((MainActivity)getActivity()).refreshUI();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.detach(MainFragment.this).attach(MainFragment.this).commit();
            }
        });


        return rootView;
        //return inflater.inflate(R.layout.fragment_main, container, false);
    }



}
