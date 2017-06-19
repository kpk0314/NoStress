package com.sourcey.materiallogindemo;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sourcey.materiallogindemo.Fragment.ChartFragment;

public class BinderData extends BaseAdapter {

    // XML node keys
    static final String KEY_TAG = "listdata"; // parent node
    static final String KEY_ID = "id";
    static final String KEY_DAY_OF_WEEK = "day_of_week";
    static final String KEY_DATE = "date";
    static final String KEY_AVERAGE = "average";
    static final String KEY_MAXIMUM = "maximum";
    static final String KEY_MINIMUM = "minimum";

    LayoutInflater inflater;
    List<HashMap<String,String>> dataCollection;
    ViewHolder holder;
    public BinderData() {
        // TODO Auto-generated constructor stub
    }

    public BinderData(ChartFragment act, List<HashMap<String,String>> map) {

        this.dataCollection = map;

        inflater = (LayoutInflater) act.getActivity()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    public int getCount() {
        // TODO Auto-generated method stub
//		return idlist.size();
        return dataCollection.size();
    }

    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        View vi=convertView;
        if(convertView==null){

            vi = inflater.inflate(R.layout.list_row, null);
            holder = new ViewHolder();

            holder.day_of_week = (TextView)vi.findViewById(R.id.day_of_week); // city name
            holder.date = (TextView)vi.findViewById(R.id.date); // city weather overview
            holder.average =  (TextView)vi.findViewById(R.id.average); // city temperature
            holder.maximum =(TextView)vi.findViewById(R.id.maximum); // thumb image
            holder.minimum =(TextView)vi.findViewById(R.id.minimum); // thumb image

            vi.setTag(holder);
        }
        else{

            holder = (ViewHolder)vi.getTag();
        }

        // Setting all values in listview
        holder.day_of_week.setText(dataCollection.get(position).get(KEY_DAY_OF_WEEK));
        holder.date.setText(dataCollection.get(position).get(KEY_DATE));
        holder.average.setText(dataCollection.get(position).get(KEY_AVERAGE));
        holder.maximum.setText(dataCollection.get(position).get(KEY_MAXIMUM));
        holder.minimum.setText(dataCollection.get(position).get(KEY_MINIMUM));

        return vi;
    }

    static class ViewHolder{

        TextView day_of_week;
        TextView date;
        TextView average;
        TextView maximum;
        TextView minimum;
    }

}
