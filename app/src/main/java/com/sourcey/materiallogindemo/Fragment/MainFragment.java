package com.sourcey.materiallogindemo.Fragment;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
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

    String nowValue; // 현재 스트레스 지수
    private SwipeRefreshLayout mSwipeRefresh; // 당겨서 새로고침 위한 변수

    // 그래프 프로퍼티 변수
    private LineChartView chart;
    private LineChartData data;
    private int numberOfLines = 1;
    private int maxNumberOfLines = 4;
    private int numberOfPoints = 9;
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
    Cursor y_cursor, d_cursor;


    public MainFragment() {
        // Required empty public constructor

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        nowValue = ((MainActivity) getActivity()).stress;


        // 엘리먼트 아이디 받아오기
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        TextView _textUpdate = (TextView) rootView.findViewById(R.id.textUpdate);
        TextView _levelValue = (TextView) rootView.findViewById(R.id.levelValue);
        TextView _todayValue = (TextView) rootView.findViewById(R.id.todayValue);
        TextView _yesterdayValue = (TextView) rootView.findViewById(R.id.yesterdayValue);
        chart = (LineChartView) rootView.findViewById(R.id.chart);

        // 현재 시간 업데이트
        currentTime = DateFormat.getDateTimeInstance().format(new Date());


        _textUpdate.setText("마지막 업데이트. " + currentTime);

        // 스트레스 레벨, 글씨 색상 업데이트
        int intNow = Integer.parseInt(nowValue);
        if (intNow > 80) {
            _levelValue.setText("5");
            _levelValue.setTextColor(getResources().getColor(R.color.level5));
        } else if (intNow > 60) {
            _levelValue.setText("4");
            _levelValue.setTextColor(getResources().getColor(R.color.level4));
        } else if (intNow > 40) {
            _levelValue.setText("3");
            _levelValue.setTextColor(getResources().getColor(R.color.level3));
        } else if (intNow > 20) {
            _levelValue.setText("2");
            _levelValue.setTextColor(getResources().getColor(R.color.level2));
        } else {
            _levelValue.setText("1");
            _levelValue.setTextColor(getResources().getColor(R.color.level1));
        }

        // 현재 스트레스 지수 업데이트
        _todayValue.setText(nowValue);

        // 어제이시간 업데이트, 없을 경우 "-"
        y_cursor = ((MainActivity) getActivity()).y_cursor;
        if(y_cursor.moveToNext()){
            _yesterdayValue.setText(String.valueOf(y_cursor.getInt(0)));
        } else {
            _yesterdayValue.setText("-");
        }
        y_cursor.close();


        // 그래프 생성 함수
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
                ((MainActivity) getActivity()).refreshUI();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.detach(MainFragment.this).attach(MainFragment.this).commit();
            }
        });


        return rootView;
        //return inflater.inflate(R.layout.fragment_main, container, false);
    }

    // 그래프를 생성하기 위한 코드
    private void generateData() {

        List<Line> lines = new ArrayList<Line>();
        for (int i = 0; i < numberOfLines; ++i) {
            List<PointValue> values = new ArrayList<PointValue>();

            d_cursor = ((MainActivity) getActivity()).d_cursor;
            // db로부터 오늘 3시간 간격 데이터 받아오기
            while (d_cursor.moveToNext()) {
                values.add(new PointValue(d_cursor.getInt(0), d_cursor.getInt(1)));
            }
            d_cursor.close();

            // 그래프 프로퍼티 설정
            Line line = new Line(values);
            line.setColor(Color.WHITE);
            line.setShape(shape);
            line.setCubic(isCubic);
            line.setFilled(isFilled);
            line.setHasLabels(hasLabels);
            line.setHasLabelsOnlyForSelected(hasLabelForSelected);
            line.setHasLines(hasLines);
            line.setHasPoints(hasPoints);
            if (pointsHaveDifferentColor) {
                line.setPointColor(ChartUtils.COLORS[(i + 1) % ChartUtils.COLORS.length]);
            }
            lines.add(line);
        }

        data = new LineChartData(lines);

        if (hasAxes) {
            Axis axisX = new Axis();
            Axis axisY = new Axis().setHasLines(true);

            // Y축 단위를 지정하기 위한 코드
            List<AxisValue> axisYvalues = new ArrayList<>();
            for (int i = 0; i <= 100; i += 50) { //i represents value on a chart, for example Y value for vertical axis
                AxisValue axisValue = new AxisValue(i);
                axisYvalues.add(axisValue);
            }

            // X축 단위를 지정하기 위한 코드 - 지금으로부터 한시간 전부터 두시간 단위로 빼나간다
            List<AxisValue> axisXvalues = new ArrayList<>();
//            for (int i = 0; i <= numberOfPoints; i++) {
//                Date currentDate;
//                currentDate = new Date(System.currentTimeMillis() - TimeUnit.HOURS.toMillis(26 - i * 3));
//                SimpleDateFormat sdf = new SimpleDateFormat("a h");
//                String hour = sdf.format(currentDate);
//                AxisValue axisValue = new AxisValue(i);
//                axisValue.setLabel(hour);
//                axisXvalues.add(axisValue);
//            }

            // X축 단위를 지정하기 위한 코드 - 0, 3, 6 ... 고정
            for (int i = 0; i <= numberOfPoints; i++) {
                SimpleDateFormat sdf = new SimpleDateFormat("a h");
                String hour = sdf.format(new Date(TimeUnit.HOURS.toMillis(i * 3 - 9)));
                AxisValue axisValue = new AxisValue(i);
                axisValue.setLabel(hour);
                axisXvalues.add(axisValue);
            }


            // X Y축 프로퍼티 설정
            axisX.setValues(axisXvalues);
            axisY.setValues(axisYvalues);
            axisX.setTextSize(8);
            axisX.setHasSeparationLine(true);
            axisX.setHasLines(true);
            axisY.setTextSize(8);
            axisX.setTextColor(Color.parseColor("#ccffffff"));
            axisY.setTextColor(Color.parseColor("#ccffffff"));

            // 그래프 가로길이 확장을 위한 가짜 axis Z
            Axis axisZ = new Axis();
            axisZ.setTextSize(8);
            axisZ.setTextColor(Color.parseColor("#00000000"));

            if (hasAxesNames) {
                axisX.setName("Axis X");
                axisY.setName("Axis Y");
            }
            data.setAxisXTop(axisX);
            data.setAxisYLeft(axisY);
            data.setAxisYRight(axisZ); // 투명색 가짜 axis Z를 오른쪽에 삽입함으로써 가로길이를 조금 더 증가시킴
        } else {
            data.setAxisXBottom(null);
            data.setAxisYLeft(null);
        }

        data.setBaseValue(Float.NEGATIVE_INFINITY);

        chart.setLineChartData(data);
        chart.setZoomEnabled(false);

        final Viewport v = new Viewport(chart.getMaximumViewport());
        v.bottom = -5;
        v.top = 105;
        v.left = 0;
        v.right = 8;
        chart.setMaximumViewport(v);
        chart.setCurrentViewport(v);
    }


}
