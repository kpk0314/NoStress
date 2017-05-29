package com.sourcey.materiallogindemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Isaac Kim on 2017-05-30.
 */

public class PasswordActivity extends AppCompatActivity {

    @Bind(R.id.input_email) EditText _emailText;
    @Bind(R.id.btn_send) Button _sendButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);
        getWindow().setWindowAnimations(android.R.style.Animation_Toast);
        ButterKnife.bind(this);

        _sendButton.setOnClickListener(new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            sendEmail();
        }
    });
}

    public void sendEmail() {

    }
}
