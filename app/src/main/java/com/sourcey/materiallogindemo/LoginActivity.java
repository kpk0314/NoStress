package com.sourcey.materiallogindemo;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Bind;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;

    private static final String TAG_SUCCESS = "error_code";
    private static final String NONCE = "nonce";
    private static final String access_tocken = "access_tocken";
    int error_code ;

    // url to create new product
    private static String url_create_product = "http://test.huy.kr/api/v1/user/signin.json";
    JSONParser jsonParser = new JSONParser();

    @Bind(R.id.input_email) EditText _emailText;
    @Bind(R.id.input_password) EditText _passwordText;
    @Bind(R.id.btn_login) Button _loginButton;
    @Bind(R.id.find_password) TextView _findButton;
    
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
//
//        _findButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                password();
//            }
//        });

    }
//
//    public void password() {
//        Intent intent = new Intent(this, PasswordActivity.class);
//        startActivity(intent);
//        finish();
//    }


    public void login() {
        Log.d(TAG, "Login");
        if (!validate()) {
            onLoginFailed1();
            return;
        }

        new Loginpage1().execute();


//        _loginButton.setEnabled(false);
//
//        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this, R.style.AppTheme_Dark_Dialog);
//        progressDialog.setIndeterminate(true);
//        progressDialog.setMessage("사용자 인증 중...");
//        progressDialog.show();
//
//        String email = _emailText.getText().toString();
//        String password = _passwordText.getText().toString();
//
//        // TODO: Implement your own authentication logic here.
//
//        new android.os.Handler().postDelayed(
//                new Runnable() {
//                    public void run() {
//                        // On complete call either onLoginSuccess or onLoginFailed
//                        onLoginSuccess();
//                        // onLoginFailed();
//                        progressDialog.dismiss();
//                    }
//                }, 3000);
    }

    class Loginpage1 extends AsyncTask<String, String, Integer> {

        long L = System.currentTimeMillis() / 1000;
        String unixtimestamp = Long.toString(L);

        String hash = getSHA256(unixtimestamp);


        protected Integer doInBackground(String... args) {

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("hash", hash));

            // getting JSON Object
            // Note that create product url accepts POST method
            JSONObject json = jsonParser.makeHttpRequest(url_create_product, "GET", params);

            try {
                String nonce = json.getString("nonce");
                String realm = json.getString("realm");
                error_code = json.getInt("error_code");

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return error_code;
        }


        protected void onPostExecute(Integer error_code) {
            // dismiss the dialog once done
            //progressDialog.dismiss();

            if (error_code == 1000) {
                // successfully created product
                new Loginpage().execute();
            } else if (error_code == 1001) {
                Toast.makeText(getBaseContext(), "Parameter 에러", Toast.LENGTH_LONG).show();
            } else if (error_code == 1002) {
                Toast.makeText(getBaseContext(), "Unauthorization", Toast.LENGTH_LONG).show();
            } else if (error_code == 1003) {
                Toast.makeText(getBaseContext(), "DB 오류", Toast.LENGTH_LONG).show();
            }
        }
    }

    class Loginpage extends AsyncTask<String, String, Integer>{
        ProgressDialog progressDialog;
        protected void onPreExecute() {
            super.onPreExecute();

            _loginButton.setEnabled(false);

            progressDialog = new ProgressDialog(LoginActivity.this, R.style.AppTheme_Dark_Dialog);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("사용자 인증 중...");
            progressDialog.show();

        }
        protected Integer doInBackground(String... args) {

            String email = _emailText.getText().toString();
            String password = _passwordText.getText().toString();

            String ha1 = getSHA256(email+":"+"realm"+":"+password);
            String ha2 = getSHA256("POST"+":"+"/api/v1/user/signin.json");
            String hash = getSHA256(ha1+":"+NONCE+":"+ha2);

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("nonce", NONCE));
            params.add(new BasicNameValuePair("email", email));
            params.add(new BasicNameValuePair("uri", "api/v1/user/signin"));
            params.add(new BasicNameValuePair("hash", hash));

            // getting JSON Object
            // Note that create product url accepts POST method
            JSONObject json = jsonParser.makeHttpRequest(url_create_product, "GET", params);


            // check log cat fro response
            Log.d("Login Response", json.toString());

            try {
                error_code = json.getInt("error_code");
                //           String access_tocken = json.getString("access_tocken");

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return error_code;
        }

        protected void onPostExecute(Integer error_code) {
            // dismiss the dialog once done
            progressDialog.dismiss();
            if (error_code == 1000) {
                // successfully created product
                onLoginSuccess();
            } else if(error_code == 1001) {
                onLoginFailed1();
            }
            else if(error_code == 1002) {
                onLoginFailed2();
            }
            else if(error_code == 1003) {
                onLoginFailed3();
            }
        }
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
        intent.putExtra("access_tocken", access_tocken);
        startActivity(intent);
        finish();
    }

    public void onLoginFailed1() {
        Toast.makeText(getBaseContext(), "Login failed:1001", Toast.LENGTH_LONG).show();
        _loginButton.setEnabled(true);
    }

    public void onLoginFailed2() {
        Toast.makeText(getBaseContext(), "Login failed:1002", Toast.LENGTH_LONG).show();
        _loginButton.setEnabled(true);
    }

    public void onLoginFailed3() {
        Toast.makeText(getBaseContext(), "Login failed:1003", Toast.LENGTH_LONG).show();
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
