package com.sourcey.materiallogindemo;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;


import butterknife.ButterKnife;
import butterknife.Bind;

public class PrivacyActivity extends AppCompatActivity implements NumberPicker.OnValueChangeListener{
    private static final String TAG = "PrivacyActivity";
    static Dialog d ;


    int mYear = 1990, mMonth = 1, mDay = 1;
    //Initializing a new string array with elements
    final String[] gender= {"남자","여자"};


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
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void signup() {
        Log.d(TAG, "Signup");

        if (!validate()) {
            onSignupFailed();
            return;
        }

        _signupButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(PrivacyActivity.this, R.style.Progress_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("계정 생성중...");
        progressDialog.show();

        String name = _birthText.getText().toString();
        String email = _genderText.getText().toString();
        String password = _heightText.getText().toString();
        String reEnterPassword = _weightText.getText().toString();

        // TODO: Implement your own signup logic here.

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onSignupSuccess or onSignupFailed
                        // depending on success
                        onSignupSuccess();
                        // onSignupFailed();
                        progressDialog.dismiss();
                    }
                }, 3000);
    }


    public void onSignupSuccess() {
        _signupButton.setEnabled(true);
        setResult(RESULT_OK, null);
        finish();
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        _signupButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String name = _birthText.getText().toString();
        String email = _genderText.getText().toString();
        String password = _heightText.getText().toString();
        String reEnterPassword = _weightText.getText().toString();


        return valid;
    }


    /* Date Picker Dialog */
    //날짜 대화상자 리스너 부분
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
        _birthText.setText(String.format("%d/%d/%d", mYear, mMonth + 1, mDay));
    }

    /* Gender Dialog */
    public void genderDialog()
    {
        final Dialog d = new Dialog(PrivacyActivity.this, R.style.Theme_AppCompat_Light_Dialog_Alert);
        d.setTitle("NumberPicker");
        d.setContentView(R.layout.dialog);
        Button b1 = (Button) d.findViewById(R.id.button1);
        Button b2 = (Button) d.findViewById(R.id.button2);
        final NumberPicker np = (NumberPicker) d.findViewById(R.id.numberPicker1);

        //Set min, max, wheel and populate.
        np.setMinValue(0);
        np.setMaxValue(gender.length-1);
        np.setWrapSelectorWheel(true);
        np.setDisplayedValues(gender);
        np.setOnValueChangedListener(this);

        b1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                _genderText.setText(String.valueOf(gender[np.getValue()]));
                d.dismiss();
            }
        });
        b2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                d.dismiss();
            }
        });
        d.show();
    }

    /* Hieght Dialog */
    public void heightDialog()
    {
        final Dialog d = new Dialog(PrivacyActivity.this, R.style.Theme_AppCompat_Light_Dialog_Alert);
        d.setTitle("NumberPicker");
        d.setContentView(R.layout.dialog);
        Button b1 = (Button) d.findViewById(R.id.button1);
        Button b2 = (Button) d.findViewById(R.id.button2);
        final NumberPicker np = (NumberPicker) d.findViewById(R.id.numberPicker1);

        //Set min, max, wheel and popunp.setValue(170);late.

        np.setMinValue(0);
        np.setMaxValue(200);
        np.setWrapSelectorWheel(true);
        np.setOnValueChangedListener(this);
        np.setValue(170);

        b1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                _heightText.setText(String.valueOf(np.getValue()) + " cm");
                d.dismiss();
            }
        });
        b2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                d.dismiss();
            }
        });
        d.show();

    }

    /* Wieght Dialog */
    public void weightDialog()
    {
        final Dialog d = new Dialog(PrivacyActivity.this, R.style.Theme_AppCompat_Light_Dialog_Alert);
        d.setTitle("NumberPicker");
        d.setContentView(R.layout.dialog);
        Button b1 = (Button) d.findViewById(R.id.button1);
        Button b2 = (Button) d.findViewById(R.id.button2);
        final NumberPicker np = (NumberPicker) d.findViewById(R.id.numberPicker1);

        //Set min, max, wheel and popunp.setValue(170);late.

        np.setMinValue(0);
        np.setMaxValue(120);
        np.setWrapSelectorWheel(true);
        np.setOnValueChangedListener(this);
        np.setValue(50);

        b1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                _weightText.setText(String.valueOf(np.getValue()) + " kg");
                d.dismiss();
            }
        });
        b2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                d.dismiss();
            }
        });
        d.show();

    }


}