package com.sourcey.materiallogindemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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

        TextView txtTitle = (TextView) findViewById(R.id.title_label);
        TextView txtContent = (TextView) findViewById(R.id.content_label);
        ListView listView = (ListView) findViewById(R.id.usrinfo_list);

        Intent i = getIntent();
        // getting attached intent data
        String title = i.getStringExtra("title");
        // displaying selected product name
        txtTitle.setText(title);




        if(title.matches("내 정보")) {
            // Defined Array values to show in ListView
            String[] values = new String[] {
                    "계정",
                    "이름",
                    "생년월일",
                    "성별",
                    "키",
                    "몸무게",
            };
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,  R.layout.item_row, android.R.id.text1, values);
            // Assign adapter to ListView
            listView.setAdapter(adapter);
            txtContent.setText("");
        } else if(title.matches("이용 약관")) {
            txtContent.setText("이용 약관");
        } else if(title.matches("개인 정보 취급 방침")){
            txtContent.setText("개인 정보 취급 방침");
        } else if(title.matches("개발사 문의")){
            txtContent.setText("개발사 문의");
        } else if(title.matches("로그아웃")){

        }

    }
}
