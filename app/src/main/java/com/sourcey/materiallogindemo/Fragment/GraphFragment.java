package com.sourcey.materiallogindemo.Fragment;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.sourcey.materiallogindemo.R;

/**
 * Created by Isaac Kim on 2017-06-28.
 */

public class GraphFragment extends Fragment {

    View rootView;

    String position = "1";
    String city = "";
    String weather = "";
    String temperature = "";
    String windSpeed = "";
    String iconfile = "";
    ImageButton imgWeatherIcon;

    TextView tvcity;
    TextView tvtemp;
    TextView tvwindspeed;
    TextView tvCondition;

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

        // SettingFragment로 부터 title 변수 받아 Title로 적용
        String date = getArguments().getString("date");
        String day_of_week = getArguments().getString("day_of_week");

        date_label.setText(date);
        day_of_week_label.setText(day_of_week);

//
//        try {
//            //handle for the UI elements
//            imgWeatherIcon = (ImageButton) rootView.findViewById(R.id.imageButtonAlpha);
//            //Text fields
//            tvcity = (TextView) rootView.findViewById(R.id.textViewCity);
//            tvtemp = (TextView) rootView.findViewById(R.id.textViewTemperature);
//            tvwindspeed = (TextView) rootView.findViewById(R.id.textViewWindSpeed);
//            tvCondition = (TextView) rootView.findViewById(R.id.textViewCondition);
//
//            // Get position to display
//            Intent i = getActivity().getIntent();
//
//            this.position = i.getStringExtra("position");
//            this.city = i.getStringExtra("city");
//            this.weather = i.getStringExtra("weather");
//            this.temperature = i.getStringExtra("temperature");
//            this.windSpeed = i.getStringExtra("windspeed");
//            this.iconfile = i.getStringExtra("icon");
//
//            String uri = "drawable/" + "d" + iconfile;
//            int imageBtnResource = getResources().getIdentifier(uri, null, getActivity().getPackageName());
//            Drawable dimgbutton = getResources().getDrawable(imageBtnResource);
//
//
//            //text elements
//            tvcity.setText(city);
//            tvtemp.setText(temperature);
//            tvwindspeed.setText(windSpeed);
//            tvCondition.setText(weather);
//
//            //thumb_image.setImageDrawable(image);
//            imgWeatherIcon.setImageDrawable(dimgbutton);
//        } catch (Exception ex) {
//            Log.e("Error", "Loading exception");
//        }

        return rootView;
    }

}

