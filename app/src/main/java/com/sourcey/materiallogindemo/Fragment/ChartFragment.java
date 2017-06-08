package com.sourcey.materiallogindemo.Fragment;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RadioButton;

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



    RadioButton week_btn;
    RadioButton month_btn;

    public ChartFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootView = inflater.inflate(R.layout.fragment_chart, container, false);

        week_btn = (RadioButton) rootView.findViewById(R.id.week_btn);
        month_btn = (RadioButton) rootView.findViewById(R.id.month_btn);

        // ArrayAdapter를 통해 LIST로 표시할 오브젝트를 지정한다.
        // 여기서는 심플하게 그냥 String
        // 레이아웃 android.R.layout.simple_list_item_1 는 안드로이드가 기본적으로 제공하는 간단한 아이템 레이아웃
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1);

        // 아이템을 추가
        adapter.add("item1");
        adapter.add("item2");
        adapter.add("item3");

        // ListView 가져오기
        ListView listView = (ListView) rootView.findViewById(R.id.listView);

        // ListView에 각각의 아이템표시를 제어하는 Adapter를 설정
        listView.setAdapter(adapter);



        week_btn.setOnClickListener(optionOnClickListener);
        month_btn.setOnClickListener(optionOnClickListener);
        week_btn.setChecked(true);



        return rootView;
    }

    RadioButton.OnClickListener optionOnClickListener
            = new RadioButton.OnClickListener() {
        public void onClick(View v) {
            if(week_btn.isChecked()){
                week_btn.setTextColor(Color.BLACK);
                month_btn.setTextColor(Color.WHITE);
            } else {
                week_btn.setTextColor(Color.WHITE);
                month_btn.setTextColor(Color.BLACK);
            }

        }
    };

}
