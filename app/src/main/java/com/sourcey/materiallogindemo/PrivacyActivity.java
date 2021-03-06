package com.sourcey.materiallogindemo;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.SpannableStringBuilder;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Bind;

public class PrivacyActivity extends AppCompatActivity implements NumberPicker.OnValueChangeListener{
    private static final String TAG = "PrivacyActivity";
    static Dialog d ;
    int mYear = 1990, mMonth = 1, mDay = 1;
    //Initializing a new string array with elements
    final String[] genderArray= {"남자","여자"};
    String genderValue = null;

    JSONParser jsonParser = new JSONParser();

    private ProgressDialog progressDialog;

    // url to create new product
    private static String url_create_product = "http://203.252.111.149/api/v1/user/signup.json";



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
        getWindow().setWindowAnimations(android.R.style.Animation_Toast);

        ButterKnife.bind(this);

        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        _birthText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                new DatePickerDialog(PrivacyActivity.this, R.style.DialogTheme, mDateSetListener, mYear, mMonth, mDay).show();
            }
        });

        _genderText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                genderDialog();
            }
        });
        _heightText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                heightDialog();
            }
        });
        _weightText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                weightDialog();
            }
        });
    }

    @Override
    public void onBackPressed() {
        // 뒤로가기 버튼 막기(주석처리)
//        overridePendingTransition(0, 0);
//        Intent intent = new Intent(this, SignupActivity.class);
//        startActivity(intent);
//        finish();
//        super.onBackPressed();
    }

    public void signup() {
        Log.d(TAG, "Signup");

//        if (!validate()) {
//            onSignupFailed();
//            return;
//        }

        new CreateNewProduct().execute();
    }

    class CreateNewProduct extends AsyncTask<String, String, String> {

        // @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(PrivacyActivity.this, R.style.Progress_Dialog);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("정보 등록 중...");
            progressDialog.show();

        }

        protected String doInBackground(String... args) {

            Intent intent = getIntent();
            String email = intent.getStringExtra("email");
            String password = intent.getStringExtra("password");
            String name = intent.getStringExtra("name");

            String birth = _birthText.getText().toString();
            //  int gender = Integer.parseInt(_genderText.getText().toString());
            String gender = _genderText.getText().toString();
            String height = _heightText.getText().toString().split(" ")[0];
            String weight = _weightText.getText().toString().split(" ")[0];
            //  float height= Float.parseFloat(_heightText.getText().toString());
            //  float weight= Float.parseFloat(_weightText.getText().toString());
//            float height1 = Float.valueOf(height).floatValue();
//            float weight1 = Float.valueOf(weight).floatValue();

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("email", email));
            params.add(new BasicNameValuePair("password", password));
            params.add(new BasicNameValuePair("name", name));
            params.add(new BasicNameValuePair("birth", birth));
            //  params.add(new BasicNameValuePair("gender", Integer.toString(gender1)));
            if(gender.matches("여자")){
                params.add(new BasicNameValuePair("gender", "1"));
            } else {
                params.add(new BasicNameValuePair("gender", "1"));
            }
            params.add(new BasicNameValuePair("height", height));
            params.add(new BasicNameValuePair("weight", weight));

            // getting JSON Object
            // Note that create product url accepts POST method
            JSONObject json = jsonParser.makeHttpRequest(url_create_product, "POST", params);


            // check log cat fro response
            Log.d("Create Response", json.toString());

            // check for success tag
            try {
                int error_code = json.getInt("error_code");

                if (error_code == 1000) {
                    // successfully created product
                    onSignupSuccess();
                } else if(error_code == 1001) {
                    onSignupFailed1();
                }
                else if(error_code == 1002) {
                    onSignupFailed2();
                }
                else if(error_code == 1003) {
                    onSignupFailed3();
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
        Intent intent = new Intent(this, StartActivity.class);
        startActivity(intent);
        finish();
    }

    public void onSignupFailed1() {
        Toast.makeText(getBaseContext(), "Login failed:1001", Toast.LENGTH_LONG).show();
        _signupButton.setEnabled(true);
    }

    public void onSignupFailed2() {
        Toast.makeText(getBaseContext(), "Login failed:1002", Toast.LENGTH_LONG).show();
        _signupButton.setEnabled(true);
    }

    public void onSignupFailed3() {
        Toast.makeText(getBaseContext(), "Login failed:1003", Toast.LENGTH_LONG).show();
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


    /* Date Picker Dialog */
    DatePickerDialog.OnDateSetListener mDateSetListener =
            new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    // TODO Auto-generated method stub
                    //사용자가 입력한 값을 가져온뒤
                    mYear = year;
                    mMonth = monthOfYear;
                    mDay = dayOfMonth;
                    //텍스트뷰의 값을 업데이트함
                    UpdateNow();
                }
            };

    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
        Log.i("value is",""+newVal);
    }

    //텍스트뷰의 값을 업데이트 하는 메소드
    void UpdateNow(){
        _birthText.setText(String.format("%d%02d%02d", mYear, mMonth + 1, mDay));
    }

    /* Gender Dialog */
    public void genderDialog()
    {
        final Dialog d = new Dialog(PrivacyActivity.this, R.style.Theme_AppCompat_Light_Dialog_MinWidth);
        d.setContentView(R.layout.dialog);
        Button b1 = (Button) d.findViewById(R.id.finish);
        final NumberPicker np = (NumberPicker) d.findViewById(R.id.numberPicker1);

        // Dialog 사이즈 조절 하기
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(d.getWindow().getAttributes());
        lp.width =  (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 240, getResources().getDisplayMetrics());
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;


        //Set min, max, wheel and populate.
        np.setMinValue(0);
        np.setMaxValue(genderArray.length-1);
        np.setWrapSelectorWheel(true);
        np.setDisplayedValues(genderArray);
        np.setOnValueChangedListener(this);
        if (_genderText.getText().toString().matches("여자"))
            np.setValue(1);
        else
            np.setValue(0);
        b1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                _genderText.setText(String.valueOf(genderArray[np.getValue()]));
                d.dismiss();
            }
        });

        genderValue = String.valueOf(np.getValue());

        d.show();
        Window window = d.getWindow();
        window.setAttributes(lp);
    }

    /* Hieght Dialog */
    public void heightDialog()
    {
        final Dialog d = new Dialog(PrivacyActivity.this, R.style.Theme_AppCompat_Light_Dialog_Alert);
        d.setTitle("NumberPicker");
        d.setContentView(R.layout.dialog);
        Button b1 = (Button) d.findViewById(R.id.finish);
        final NumberPicker np = (NumberPicker) d.findViewById(R.id.numberPicker1);

        // Dialog 사이즈 조절 하기
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(d.getWindow().getAttributes());
        lp.width =  (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 240, getResources().getDisplayMetrics());
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        np.setMinValue(0);
        np.setMaxValue(200);
        np.setWrapSelectorWheel(true);
        np.setOnValueChangedListener(this);

        if (_heightText.getText().toString().matches(""))
            np.setValue(170);
        else
            np.setValue(Integer.parseInt(_heightText.getText().toString().split(" ")[0]));

        b1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                _heightText.setText(String.valueOf(np.getValue()) + " cm");
                d.dismiss();
            }
        });
        d.show();
        Window window = d.getWindow();
        window.setAttributes(lp);

    }

    /* Wieght Dialog */
    public void weightDialog()
    {
        final Dialog d = new Dialog(PrivacyActivity.this, R.style.Theme_AppCompat_Light_Dialog_Alert);
        d.setTitle("NumberPicker");
        d.setContentView(R.layout.dialog);
        Button b1 = (Button) d.findViewById(R.id.finish);
        final NumberPicker np = (NumberPicker) d.findViewById(R.id.numberPicker1);

        // Dialog 사이즈 조절 하기
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(d.getWindow().getAttributes());
        lp.width =  (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 240, getResources().getDisplayMetrics());
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        np.setMinValue(0);
        np.setMaxValue(120);
        np.setWrapSelectorWheel(true);
        np.setOnValueChangedListener(this);

        if (_weightText.getText().toString().matches(""))
            np.setValue(50);
        else
            np.setValue(Integer.parseInt(_weightText.getText().toString().split(" ")[0]));

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _weightText.setText(String.valueOf(np.getValue()) + " kg");
                d.dismiss();
            }
        });
        d.show();
        Window window = d.getWindow();
        window.setAttributes(lp);
    }
}