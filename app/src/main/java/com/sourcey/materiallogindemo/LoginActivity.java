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
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.logging.Logger;

import butterknife.Bind;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;
    private static final String TAG_SUCCESS = "error_code";
    private static final String NONCE = "nonce";
    private static final String access_tocken = "access_tocken";

    // url to create new product
    private static String url_create_product = "http://test.huy.kr/api/v1/user/signin.json";

    JSONParser jsonParser = new JSONParser();


    @Bind(R.id.input_email) EditText _emailText;
    @Bind(R.id.input_password) EditText _passwordText;
    @Bind(R.id.btn_login) Button _loginButton;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().setWindowAnimations(android.R.style.Animation_Toast);
        ButterKnife.bind(this);
        
        _loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

    }

    public void login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed();
            return;
        }

        new Loginpage().execute();
    }
    class Loginpage extends AsyncTask<String, String, String>{

        protected void onPreExecute() {
            super.onPreExecute();

            _loginButton.setEnabled(false);

            final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this, R.style.AppTheme_Dark_Dialog);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("사용자 인증 중...");
            progressDialog.show();

        }

        long L = System.currentTimeMillis() / 1000;
        String unixtimestamp = Long.toString(L);

        String hash = getSHA256(unixtimestamp);

        protected String LoginFirst(String... args) {

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("hash", hash));

            // getting JSON Object
            // Note that create product url accepts POST method
            JSONObject json = jsonParser.makeHttpRequest(url_create_product, "GET", params);

            try {
                String nonce = json.getString("nonce");
                String realm = json.getString("realm");

                if (error_code == 1000) {
                    // successfully created product
                    onLoginSuccess();
                } else if(error_code == 1001)
                {
                    Toast.makeText(getBaseContext(), "Parameter 에러", Toast.LENGTH_LONG).show();
                }
                else if(error_code == 1002)
                {
                    Toast.makeText(getBaseContext(), "Unauthorization", Toast.LENGTH_LONG).show();
                }
                else if(error_code == 1003)
                {
                    Toast.makeText(getBaseContext(), "DB 오류", Toast.LENGTH_LONG).show();
                }
                //else {
                // failed to create product
                //  onLoginFailed();
                //}
            }
            catch (JSONException e) {
                e.printStackTrace();
            }

            return null ;
        }


        protected String doInBackground(String... args) {

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("nonce", NONCE));
            params.add(new BasicNameValuePair("email", email));
            params.add(new BasicNameValuePair("password", password));

            // getting JSON Object
            // Note that create product url accepts POST method
            JSONObject json = jsonParser.makeHttpRequest(url_create_product, "GET", params);


            // check log cat fro response
            Log.d("Login Response", json.toString());

            try {

                String access_tocken = json.getString("access_tocken");
                int error_code = json.getInt(TAG_SUCCESS);

                if (error_code == 1000) {
                    // successfully created product
                    onLoginSuccess();
                } else if(error_code == 1001)
                {
                    Toast.makeText(getBaseContext(), "Parameter 에러", Toast.LENGTH_LONG).show();
                }
                else if(error_code == 1002)
                {
                    Toast.makeText(getBaseContext(), "Unauthorization", Toast.LENGTH_LONG).show();
                }
                else if(error_code == 1003)
                {
                    Toast.makeText(getBaseContext(), "DB 오류", Toast.LENGTH_LONG).show();
                }
                //else {
                    // failed to create product
                  //  onLoginFailed();
                //}
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        // TODO: Implement your own authentication logic here.

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onLoginSuccess or onLoginFailed
                        onLoginSuccess();
                        // onLoginFailed();
                        progressDialog.dismiss();
                    }
                }, 3000);
    }

    public static String getSHA256(String str) {
        String rtnSHA = "";

        try{
            MessageDigest sh = MessageDigest.getInstance("SHA-256");
            sh.update(str.getBytes());
            byte byteData[] = sh.digest();
            StringBuffer sb = new StringBuffer();

            for(int i = 0 ; i < byteData.length ; i++){
                sb.append(Integer.toString((byteData[i]&0xff) + 0x100, 16).substring(1));
            }
            rtnSHA = sb.toString();

        }catch(NoSuchAlgorithmException e){
            e.printStackTrace();
            rtnSHA = null;
        }
        return rtnSHA;
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {

                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        overridePendingTransition(0, 0);
        Intent intent = new Intent(this, InitialActivity.class);
        startActivity(intent);
        finish();
        super.onBackPressed();
    }

    public void onLoginSuccess() {
        _loginButton.setEnabled(true);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        this.finish();
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        _loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

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

        return valid;
    }
}
