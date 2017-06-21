package com.sourcey.materiallogindemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.telecom.Call;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by Isaac Kim on 2017-06-20.
 */

public class BoardActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_board);

        overridePendingTransition(0, 0);

        TextView txtTitle = (TextView) findViewById(R.id.title_label);
        TextView txtContent = (TextView) findViewById(R.id.content_label);
        ListView listView = (ListView) findViewById(R.id.usrinfo_list);
        View endLine = (View) findViewById(R.id.end_line);

        Intent i = getIntent();
        i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        // getting attached intent data
        String title = i.getStringExtra("title");
        // displaying selected product name
        txtTitle.setText(title);

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
            ListViewAdapter lviewAdapter = new ListViewAdapter(this, titles, contents);
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


    }

//    @Override
//    public void onPause() {
//        super.onPause();
//        overridePendingTransition(0, 0);
//
//
//    }
//
//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        overridePendingTransition(0, 0);
//    }
//
//    @Override public void finish()
//    {
//        super.finish();
//        overridePendingTransition(0, 0);
//    }
//
//    @Override
//    protected void onResume()
//    {
//        super.onResume();
//        overridePendingTransition(0, 0);
//    }
//
//    @Override
//    public void onStop(){
//        super.onStop();
//        overridePendingTransition(0, 0);
//    }
//
//    @Override
//    public void onDestroy(){
//        super.onDestroy();
//        overridePendingTransition(0, 0);
//    }
//
//    @Override
//    protected void onNewIntent(Intent intent) {
//        super.onNewIntent(intent);
//        overridePendingTransition(0, 0);
//    }

}
