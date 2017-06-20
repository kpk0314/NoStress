package com.sourcey.materiallogindemo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.Bind;

import org.json.JSONException;
import org.json.JSONObject;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.sourcey.materiallogindemo.LoginActivity.getSHA256;

public class SignupActivity extends AppCompatActivity {
    private static final String TAG = "SignupActivity";

    public String email = null;
    public String password = null;
    public String name = null;


    JSONParser jsonParser = new JSONParser();
    // Progress Dialog
    private ProgressDialog progressDialog;

    // url to create new product
    private static String url_create_product = "http://test.huy.kr/api/v1/user/signup.json";

    // JSON Node names
    private static final String TAG_SUCCESS = "success";

    @Bind(R.id.input_email)
    EditText _emailText;
    @Bind(R.id.input_password)
    EditText _passwordText;
    @Bind(R.id.input_reEnterPassword)
    EditText _reEnterPasswordText;
    @Bind(R.id.input_name)
    EditText _nameText;

    @Bind(R.id.btn_signup)
    Button _signupButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        getWindow().setWindowAnimations(android.R.style.Animation_Toast);
        ButterKnife.bind(this);

        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                signup();

            }
        });
    }

    @Override
    public void onBackPressed() {
        overridePendingTransition(0, 0);
        Intent intent = new Intent(this, InitialActivity.class);
        startActivity(intent);
        finish();
        super.onBackPressed();
    }
    public void signup() {
        Log.d(TAG, "Signup");

        if (!validate()) {
            onSignupFailed1();
            return;
        }

        progressDialog = new ProgressDialog(SignupActivity.this, R.style.Progress_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("계정 생성 중...");
        progressDialog.show();

        email = _emailText.getText().toString();
        String passwd = _passwordText.getText().toString();
        String reEnterPassword = _reEnterPasswordText.getText().toString();
        name = _nameText.getText().toString();

        password = getSHA256(passwd);
        _signupButton.setEnabled(true);

        setResult(RESULT_OK, null);
        Intent intent = new Intent(this, PrivacyActivity.class);
        intent.putExtra("email", email);
        intent.putExtra("password", password);
        intent.putExtra("name", name);
        startActivity(intent);
        finish();

        // new CreateNewProduct().execute();
    }

    public void onSignupFailed1() {
        Toast.makeText(getBaseContext(), "Login failed:1001", Toast.LENGTH_LONG).show();
        _signupButton.setEnabled(true);
    }

    public void onSignupSuccess() {
        _signupButton.setEnabled(true);
        setResult(RESULT_OK, null);
        Intent intent = new Intent(this, PrivacyActivity.class);
        startActivity(intent);
        finish();
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();
        _signupButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String name = _nameText.getText().toString();
        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();
        String reEnterPassword = _reEnterPasswordText.getText().toString();

        if (name.isEmpty() || name.length() < 2) {
            _nameText.setError("정확한 이름을 입력하세요");
            valid = false;
        } else {
            _nameText.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        if (reEnterPassword.isEmpty() || reEnterPassword.length() < 4 || reEnterPassword.length() > 10 || !(reEnterPassword.equals(password))) {
            _reEnterPasswordText.setError("Password Do not match");
            valid = false;
        } else {
            _reEnterPasswordText.setError(null);
        }

        return valid;
    }
}