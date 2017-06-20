package com.sourcey.materiallogindemo.Fragment;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.sourcey.materiallogindemo.BoardActivity;
import com.sourcey.materiallogindemo.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class SettingFragment extends Fragment {

    View rootView;
    ListView listView ;

    public SettingFragment() {
        // Required empty public constructor

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        rootView = inflater.inflate(R.layout.fragment_setting, container, false);
        listView = (ListView) rootView.findViewById(R.id.setting_list);

        getActivity().overridePendingTransition(0, 0);

        // Defined Array values to show in ListView
        String[] values = new String[] {
                "내 정보",
                "이용 약관",
                "개인 정보 취급 방침",
                "버전 정보",
                "개발사 문의",
                "로그아웃",
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(rootView.getContext(),  R.layout.item_row , android.R.id.text1, values);

        // Assign adapter to ListView
        listView.setAdapter(adapter);

        // ListView Item Click Listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // selected item
                String title = ((TextView) view).getText().toString();

                if(title.matches("버전 정보")){
                Toast.makeText(rootView.getContext(),
                        "버전 정보: test version" , Toast.LENGTH_LONG)
                        .show();
                } else{
                    // Launching new Activity on selecting single List Item
                    Intent i = new Intent(getActivity().getApplicationContext(), BoardActivity.class);
                    // sending data to new activity
                    i.putExtra("title", title);
                    startActivity(i);
                }
            }

        });


        return rootView;
    }
}
