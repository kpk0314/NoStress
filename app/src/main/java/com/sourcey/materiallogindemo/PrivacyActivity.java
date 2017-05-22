package com.sourcey.materiallogindemo;

import android.app.ActivityManager;
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

import butterknife.Bind;
import butterknife.ButterKnife;

public class PrivacyActivity extends AppCompatActivity {
    private static final String TAG = "PrivacyActivity";

    JSONParser jsonParser = new JSONParser();

    private ProgressDialog progressDialog;

    // url to create new product
    private static String url_create_product = "http://10.0.2.2/db/create_private.php";

    // JSON Node names
    private static final String TAG_SUCCESS = "success";

    @Bind(R.id.input_birth) EditText _birthText;
    @Bind(R.id.input_gender) EditText _genderText;
    @Bind(R.id.input_height) EditText _heightText;
    @Bind(R.id.input_weight) EditText _weightText;

    @Bind(R.id.btn_signup) Button _signupButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy);
        ButterKnife.bind(this);

        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });
    }
    /* @Override
     public void onBackPressed() {
         overridePendingTransition(0, 0);
         Intent intent = new Intent(this, LoginActivity.class);
         startActivity(intent);
         finish();
         super.onBackPressed();
     }

     @Override
     public boolean onKeyDown(int keyCode, KeyEvent event) {
         switch (keyCode) {
             case KeyEvent.KEYCODE_BACK:
                 return true;
         }
         return super.onKeyDown(keyCode, event);
     }
 */



    public void signup() {
        Log.d(TAG, "Signup");

       /* if (!validate()) {
            onSignupFailed();
            return;
        }*/
        new CreateNewProduct().execute();
    }

    class CreateNewProduct extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


            progressDialog = new ProgressDialog(PrivacyActivity.this, R.style.Progress_Dialog);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Creating Account...");
            progressDialog.show();

        }

        protected String doInBackground(String... args) {

            String birth = _birthText.getText().toString();
            String gender = _genderText.getText().toString();
            String height = _heightText.getText().toString();
            String weight = _weightText.getText().toString();

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("birth", birth));
            params.add(new BasicNameValuePair("gender", gender));
            params.add(new BasicNameValuePair("height", height));
            params.add(new BasicNameValuePair("weight", weight));

            // getting JSON Object
            // Note that create product url accepts POST method
            JSONObject json = jsonParser.makeHttpRequest(url_create_product, "POST", params);


            // check log cat fro response
            Log.d("Create Response", json.toString());

            // check for success tag
            try {
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    // successfully created product
                    onSignupSuccess();
                } else {
                    // failed to create product
                    onSignupFailed();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String file_url) {
            // dismiss the dialog once done
            progressDialog.dismiss();
        }




    }

    public void onSignupSuccess() {
        _signupButton.setEnabled(true);
        setResult(RESULT_OK, null);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "private failed", Toast.LENGTH_LONG).show();
        _signupButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String birth = _birthText.getText().toString();
        String gender = _genderText.getText().toString();
        String height = _heightText.getText().toString();
        String weight = _weightText.getText().toString();


        return valid;
    }



}

