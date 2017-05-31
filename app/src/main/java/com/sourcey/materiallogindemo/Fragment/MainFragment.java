package com.sourcey.materiallogindemo.Fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

import java.text.DateFormat;
import java.util.Date;

import butterknife.Bind;


/**
 * A simple {@link Fragment} subclass.
 */





public class MainFragment extends Fragment {

    String nowValue;


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
        GraphView graph = (GraphView) rootView.findViewById(R.id.graph);


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




        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[]{
                new DataPoint(0, 1),
                new DataPoint(1, 5),
                new DataPoint(2, 3),
                new DataPoint(3, 2),
                new DataPoint(4, 6)
        });
        graph.addSeries(series);


        return rootView;
        //return inflater.inflate(R.layout.fragment_main, container, false);
    }

}
