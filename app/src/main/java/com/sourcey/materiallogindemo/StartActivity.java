package com.sourcey.materiallogindemo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import butterknife.Bind;
import butterknife.ButterKnife;

import static android.os.SystemClock.sleep;

/**
 * Created by Isaac Kim on 2017-05-30.
 */

public class StartActivity extends AppCompatActivity {

    // Progress Dialog
    private ProgressDialog progressDialog;

    @Bind(R.id.btn_start) Button _startButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        getWindow().setWindowAnimations(android.R.style.Animation_Toast);
        ButterKnife.bind(this);

        _startButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new CreateNewProduct().execute();
            }
        });
    }

    class CreateNewProduct extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(StartActivity.this, R.style.Progress_Dialog);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("스트레스 측정 중...");
            progressDialog.show();
        }

        protected String doInBackground(String... args) {
            // 여기에 스트레스 측정하는 코드 넣어주세요 sleep은 빼고 성공할 경우 onStartSuccess 실행하도록
            sleep(3000);
            onStartSuccess();
            return null;
        }

        protected void onPostExecute(String file_url) {
            // dismiss the dialog once done
            progressDialog.dismiss();
        }
    }

    public void onStartSuccess() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        // 뒤로가기 버튼 막기
    }
}
