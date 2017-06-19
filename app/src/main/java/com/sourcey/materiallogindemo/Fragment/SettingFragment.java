package com.sourcey.materiallogindemo.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sourcey.materiallogindemo.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class SettingFragment extends Fragment {

    View rootView;


    public SettingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_setting, container, false);


        return rootView;
    }

}
