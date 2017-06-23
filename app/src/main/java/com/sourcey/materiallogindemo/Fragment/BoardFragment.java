package com.sourcey.materiallogindemo;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.telecom.Call;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by Isaac Kim on 2017-06-20.
 */

public class BoardFragment extends Fragment {

    View rootView;

    public BoardFragment() {
        // Required empty public constructor

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // 아이디 - 변수 매칭
        rootView = inflater.inflate(R.layout.setting_board, container, false);
        TextView txtTitle = (TextView) rootView.findViewById(R.id.title_label);
        TextView txtContent = (TextView) rootView.findViewById(R.id.content_label);
        ListView listView = (ListView) rootView.findViewById(R.id.usrinfo_list);
        View endLine = (View) rootView.findViewById(R.id.end_line);

        getActivity().overridePendingTransition(0, 0); // 화면 전환 애니메이션 효과 없애기

        // SettingFragment로 부터 title 변수 받아 Title로 적용
        String title = getArguments().getString("title");
        txtTitle.setText(title);

        // Title에 따라 다른 내용 출력
        if (title.matches("내 정보")) {

            // 내 정보 리스트에 들어갈 스트링 값 생성
            String[] titles = new String[]{
                    "계정",
                    "이름",
                    "생년월일",
                    "성별",
                    "키",
                    "몸무게",
            };
            String[] contents = new String[]{
                    "ephiker@naver.com",
                    "서안드레",
                    "1991년 4월 10일",
                    "남성",
                    "170cm",
                    "62kg",
            };

            // 리스트뷰 어댑터 클래스를 이용하여 두개 이상의 리스트뷰의 한 아이템에 두개 이상의 텍스트뷰가 들어갈 수 있도록 설정
            ListViewAdapter lviewAdapter = new ListViewAdapter(getActivity(), titles, contents);
            listView.setAdapter(lviewAdapter);

            // 내 정보의 경우에만 endline 뷰가 visible하게 변하도록
            endLine.setVisibility(View.VISIBLE);

            // txtcontent의 경우 숨김
            txtContent.setVisibility(View.INVISIBLE);
        } else if (title.matches("이용 약관")) {
            txtContent.setText("이용 약관");
        } else if (title.matches("개인 정보 취급 방침")) {
            txtContent.setText("개인 정보 취급 방침");
        } else if (title.matches("개발사 문의")) {
            txtContent.setText("개발사 문의");
        }

        return rootView;


    }



}
