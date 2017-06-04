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

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.sourcey.materiallogindemo.MainActivity;
import com.sourcey.materiallogindemo.R;
import com.sourcey.materiallogindemo.StartActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.Chart;
import lecho.lib.hellocharts.view.LineChartView;


/**
 * A simple {@link Fragment} subclass.
 */





public class MainFragment extends Fragment {

    String nowValue;
    private SwipeRefreshLayout mSwipeRefresh;
//    private LineChart mChart;
    private LineChartView chart;
    private LineChartData data;
    private int numberOfLines = 1;
    private int maxNumberOfLines = 4;
    private int numberOfPoints = 12;

    float[][] randomNumbersTab = new float[maxNumberOfLines][numberOfPoints];

    private boolean hasAxes = true;
    private boolean hasAxesNames = false;
    private boolean hasLines = true;
    private boolean hasPoints = true;
    private ValueShape shape = ValueShape.CIRCLE;
    private boolean isFilled = true;
    private boolean hasLabels = false;
    private boolean isCubic = true;
    private boolean hasLabelForSelected = false;
    private boolean pointsHaveDifferentColor;
    private boolean hasGradientToTransparent = false;

    String currentTime;


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
        currentTime = DateFormat.getDateTimeInstance().format(new Date());

        _textUpdate.setText("마지막 업데이트. " + currentTime);



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

        chart = (LineChartView) rootView.findViewById(R.id.chart);

        generateValues();
        generateData();

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



    private void generateValues() {
        for (int i = 0; i < maxNumberOfLines; ++i) {
            for (int j = 0; j < numberOfPoints; ++j) {
                randomNumbersTab[i][j] = (float) Math.random() * 100f;
            }
        }
    }

    private void generateData() {

        List<Line> lines = new ArrayList<Line>();
        for (int i = 0; i < numberOfLines; ++i) {

            List<PointValue> values = new ArrayList<PointValue>();
            for (int j = 0; j < numberOfPoints; ++j) {
                values.add(new PointValue(j, randomNumbersTab[i][j]));
            }

            Line line = new Line(values);
            line.setColor(Color.WHITE);
            line.setShape(shape);
            line.setCubic(isCubic);
            line.setFilled(isFilled);
            line.setHasLabels(hasLabels);
            line.setHasLabelsOnlyForSelected(hasLabelForSelected);
            line.setHasLines(hasLines);
            line.setHasPoints(hasPoints);
            if (pointsHaveDifferentColor){
                line.setPointColor(ChartUtils.COLORS[(i + 1) % ChartUtils.COLORS.length]);
            }
            lines.add(line);
        }

        data = new LineChartData(lines);

        if (hasAxes) {
            Axis axisX = new Axis();
            Axis axisY = new Axis().setHasLines(true);

            List<AxisValue> axisYvalues = new ArrayList<>();
            for (int i = 0; i <= 100; i += 50) { //i represents value on a chart, for example Y value for vertical axis
                AxisValue axisValue = new AxisValue(i);
                axisYvalues.add(axisValue);
            }

            List<AxisValue> axisXvalues = new ArrayList<>();
            for (int i = 0; i <= numberOfPoints; i++) {
                Date currentDate = new Date(System.currentTimeMillis() - TimeUnit.HOURS.toMillis(numberOfPoints - i));
                SimpleDateFormat sdf = new SimpleDateFormat("a h");
                String hour = sdf.format(currentDate);
                AxisValue axisValue = new AxisValue(i);
                axisValue.setLabel (hour);
                if (i%2 == 1)
                    axisXvalues.add(axisValue);
            }
            axisX.setValues(axisXvalues);
            axisY.setValues(axisYvalues);
            axisX.setTextSize(8);
            axisX.setHasSeparationLine(true);
            axisX.setHasLines(true);
            axisY.setTextSize(8);


            if (hasAxesNames) {
                axisX.setName("Axis X");
                axisY.setName("Axis Y");
            }
            data.setAxisXTop(axisX);
            data.setAxisYLeft(axisY);
        } else {
            data.setAxisXBottom(null);
            data.setAxisYLeft(null);
        }

        data.setBaseValue(Float.NEGATIVE_INFINITY);

        chart.setLineChartData(data);
        chart.setZoomEnabled(false);

        final Viewport v = new Viewport(chart.getMaximumViewport());
        v.bottom = 0;
        v.top = 100;
        v.left = 0;
        v. right = 11;
        chart.setMaximumViewport(v);
        chart.setCurrentViewport(v);
    }



}
