package com.sourcey.materiallogindemo.Fragment;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.sourcey.materiallogindemo.MainActivity;
import com.sourcey.materiallogindemo.R;
import com.sourcey.materiallogindemo.TestHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.LineChartView;

/**
 * Created by Isaac Kim on 2017-06-28.
 */

public class GraphFragment extends Fragment {

    View rootView;
    String date;
    String day_of_week;

    // 그래프 프로퍼티 변수
    private LineChartView chart;
    private LineChartData data;
    private int numberOfLines = 1;
    private int maxNumberOfLines = 4;
    private int numberOfPoints = 9;
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

    public GraphFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        // xml -> java 바인드
        rootView = inflater.inflate(R.layout.fragment_graph, container, false);
        TextView date_label = (TextView) rootView.findViewById(R.id.date_label);
        TextView day_of_week_label = (TextView) rootView.findViewById(R.id.day_of_week_label);
        chart = (LineChartView) rootView.findViewById(R.id.chart);

        // SettingFragment로 부터 title 변수 받아 Title로 적용
        date = getArguments().getString("date");
        day_of_week = getArguments().getString("day_of_week");
        date_label.setText(date.replace("-","."));
        day_of_week_label.setText(day_of_week);

        generateData();

        // 뒤에 있는 프레그먼트로 터치가 전달되지 않도록 막는 함수
        rootView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        return rootView;
    }

    // 그래프를 생성하기 위한 코드
    private void generateData() {

        List<Line> lines = new ArrayList<Line>();
        for (int i = 0; i < numberOfLines; ++i) {
            List<PointValue> values = new ArrayList<PointValue>();

            Cursor g_cursor = ((MainActivity) getActivity()).sendDataToGraphFragment(date);

            // db로부터 오늘 3시간 간격 데이터 받아오기
            while (g_cursor.moveToNext()) {
                values.add(new PointValue(g_cursor.getInt(0), g_cursor.getInt(1)));
            }
            g_cursor.close();

            // 그래프 프로퍼티 설정
            Line line = new Line(values);
            line.setColor(Color.parseColor("#252525"));
            line.setShape(shape);
            line.setCubic(isCubic);
            line.setFilled(isFilled);
            line.setHasLabels(hasLabels);
            line.setHasLabelsOnlyForSelected(hasLabelForSelected);
            line.setHasLines(hasLines);
            line.setHasPoints(hasPoints);
            line.setPointRadius(4);
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
            axisX.setTextColor(Color.parseColor("#888888"));
            axisY.setTextColor(Color.parseColor("#888888"));
            axisX.setLineColor(Color.parseColor("#d0d0d0"));
            axisY.setLineColor(Color.parseColor("#d0d0d0"));

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

