package com.sourcey.materiallogindemo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.microsoft.band.BandClient;
import com.microsoft.band.BandClientManager;
import com.microsoft.band.BandException;
import com.microsoft.band.BandInfo;
import com.microsoft.band.ConnectionState;
import com.microsoft.band.UserConsent;
import com.microsoft.band.sensors.BandAccelerometerEvent;
import com.microsoft.band.sensors.BandAccelerometerEventListener;
import com.microsoft.band.sensors.BandHeartRateEvent;
import com.microsoft.band.sensors.BandHeartRateEventListener;
import com.microsoft.band.sensors.BandRRIntervalEvent;
import com.microsoft.band.sensors.BandRRIntervalEventListener;
import com.microsoft.band.sensors.HeartRateConsentListener;
import com.microsoft.band.sensors.SampleRate;

import java.lang.ref.WeakReference;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Isaac Kim on 2017-05-30.
 */

public class StartActivity extends AppCompatActivity {

    //--------MS밴드 관련해서 추가한 변수-------------------//
    private BandClient client = null;
    private Button btnStart, btnConsent;
    private TextView txtStatus;

    private Boolean HRPrinted = false;
    private Boolean RRPrinted = false;

    private Calendar c = Calendar.getInstance();

    int heartRate = 0;
    float acc_x, acc_y, acc_z;
    double rrInterval;
    String date;
    String A, B, C, D, E;

    public double HRVarianceValue;
    public int HRAverageValue;
    public double RRVarianceValue;
    public int RRAverageValue;

    String HRA ;
    String HRV ;
    String RRV ;
    String RRA ;


    // SQLight DB  생성
    MyOpenHelper helper;
    SQLiteDatabase db;
    final WeakReference<Activity> reference = new WeakReference<Activity>(this);

    private CountDownTimer countDownTimer;


    // Progress Dialog
    private ProgressDialog progressDialog;

    @Bind(R.id.btn_start) Button _startButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        getWindow().setWindowAnimations(android.R.style.Animation_Toast);
        ButterKnife.bind(this);

        helper = new MyOpenHelper(this);

        //실제 스마트폰 단말기 내의 data/data/database경로에 파일이 만들어지게된다.
        db = helper.getWritableDatabase();


        _startButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new CreateNewProduct().execute();

                new StartActivity.HeartRateConsentTask().execute(reference);
                countDownTimer();
                countDownTimer.start();

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

            return null;
        }

        protected void onPostExecute(String file_url) {
            // dismiss the dialog once done
            progressDialog.dismiss();
        }
    }

    public void countDownTimer() {
        countDownTimer = new CountDownTimer(30000, 1000) {
            @Override
                    public void onTick(long millisUntilFinished) {

                System.out.println("----------시작--------------");

                new StartActivity.Measurement().execute();

            }
            @Override
            public void onFinish(){
//countDownTimer.cancel();
                //표준 평균과 분산 구하기
String HRAverage="SELECT AVG(hr) FROM datareceived";
String HRVariance="SELECT(SUM(hr*hr) - SUM(hr) * SUM(hr) / COUNT(*)) / (COUNT(*)-1) FROM datareceived";
Cursor c=db.rawQuery(HRAverage,null);
Cursor s=db.rawQuery(HRVariance,null);
String RRAverage="SELECT AVG(rrInterval) FROM datareceived";
String RRVariance="SELECT (SUM(rrInterval * rrInterval) - SUM(rrInterval)* SUM(rrInterval) / COUNT(*)) / (COUNT(*)-1) FROM datareceived";
Cursor a=db.rawQuery(RRAverage,null);
Cursor b=db.rawQuery(RRVariance,null);
a.moveToFirst();
b.moveToFirst();
c.moveToFirst();
s.moveToFirst();
HRVarianceValue = s.getDouble(0);
HRAverageValue = c.getInt(0);
RRVarianceValue = b.getDouble(0);
RRAverageValue = a.getInt(0);

 HRA = Integer.toString(HRAverageValue);
 HRV = Double.toString(HRVarianceValue);
 RRV = Double.toString(RRVarianceValue);
 RRA = Integer.toString(RRAverageValue);
//구한 표준 평균과 분산을 데이터 베이스에 저장하기
db.execSQL("insert into STD (hrA, hrV, rrA, rrV) values ("+HRA+","+HRV+","+RRA+","+RRV+");");

                onStartSuccess();
            }
        };
    }



        public void onStartSuccess() {
        Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("HRVarianceValue",HRV);
intent.putExtra("HRAverageValue",HRA);
intent.putExtra("RRVarianceValue",RRV);
intent.putExtra("RRAverageValue",RRA);

            startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        // 뒤로가기 버튼 막기
    }

    //----------심박-------------------------------------------------------------------------------//
    private BandHeartRateEventListener mHeartRateEventListener = new BandHeartRateEventListener() {
        @Override
        public void onBandHeartRateChanged(final BandHeartRateEvent event) {
            if (event != null) {
                c = Calendar.getInstance();
                if (c.get(Calendar.SECOND) % 1 == 0) {
                    HRPrinted = true;

                    heartRate = event.getHeartRate();
                }
            }
        }
    };
    //----------심박-------------------------------------------------------------------------------//

    //----------알알 인터-벌-------------------------------------------------------------------------//
    private BandRRIntervalEventListener mRRIntervalEventListener = new BandRRIntervalEventListener() {
        @Override
        public void onBandRRIntervalChanged(final BandRRIntervalEvent event) {
            if (event != null && HRPrinted == true) {
                HRPrinted = false;
                RRPrinted = true;
                rrInterval = event.getInterval();
            }
        }
    };
    //----------알알 인터-벌-------------------------------------------------------------------------//

    //----------가속도 관련된 거 표시하기---------------------------------------------------------------//
    private BandAccelerometerEventListener mAccelerometerEventListener = new BandAccelerometerEventListener() {
        @Override
        public void onBandAccelerometerChanged(final BandAccelerometerEvent event) {
            if (event != null && RRPrinted == true) {
                RRPrinted = false;
                acc_x = event.getAccelerationX();
                acc_y = event.getAccelerationY();
                acc_z = event.getAccelerationZ();

                System.out.println("--------------------------------------------");
                System.out.println("date: " + date +
                        "\nhr: " + heartRate +
                        "\nxyz: " + acc_x + ", " + acc_y + " , " + acc_z +
                        "\nrr: " + rrInterval);
                System.out.println("--------------------------------------------");

                A = Integer.toString(heartRate);
                B = Double.toString(Math.round(rrInterval * 100000) / 100000.0);
                C = Double.toString(Math.round(acc_x * 100000) / 100000.0);
                D = Double.toString(Math.round(acc_y * 100000) / 100000.0);
                E = Double.toString(Math.round(acc_z * 100000) / 100000.0);

                DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                date = df.format(Calendar.getInstance().getTime());

                db.execSQL("insert into datareceived(d, hr, rrInterval, acc_x, acc_y, acc_z) values ('" +
                        date + "', " + A + ", " + B + ", " + C + ", " + D + ", " + E + ");");
            }
        }
    };
    //----------가속도 관련된 거 표시하기---------------------------------------------------------------//


    private class Measurement extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            try {
                if (getConnectedBandClient()) {
                    //가속도
                    client.getSensorManager().registerAccelerometerEventListener(mAccelerometerEventListener, SampleRate.MS128);
                    //심박
                    if (client.getSensorManager().getCurrentHeartRateConsent() == UserConsent.GRANTED) {
                        client.getSensorManager().registerHeartRateEventListener(mHeartRateEventListener);
                    } else {
                        appendToUI("You have not given this application consent to access heart rate data yet."
                                + " Please press the Heart Rate Consent button.\n");
                    }
                    //알-알
                    int hardwareVersion = Integer.parseInt(client.getHardwareVersion().await());
                    if (hardwareVersion >= 20) {
                        if (client.getSensorManager().getCurrentHeartRateConsent() == UserConsent.GRANTED) {
                            client.getSensorManager().registerRRIntervalEventListener(mRRIntervalEventListener);
                        } else {
                            appendToUI("You have not given this application consent to access heart rate data yet."
                                    + " Please press the Heart Rate Consent button.\n");
                        }
                    } else {
                        appendToUI("The RR Interval sensor is not supported with your Band version. Microsoft Band 2 is required.\n");
                    }
                } else {
                    appendToUI("Band isn't connected. Please make sure bluetooth is on and the band is in range.\n");
                }
            } catch (BandException e) {
                String exceptionMessage = "";
                switch (e.getErrorType()) {
                    case UNSUPPORTED_SDK_VERSION_ERROR:
                        exceptionMessage = "Microsoft Health BandService doesn't support your SDK Version. Please update to latest SDK.\n";
                        break;
                    case SERVICE_ERROR:
                        exceptionMessage = "Microsoft Health BandService is not available. Please make sure Microsoft Health is installed and that you have the correct permissions.\n";
                        break;
                    default:
                        exceptionMessage = "Unknown error occured: " + e.getMessage() + "\n";
                        break;
                }
                appendToUI(exceptionMessage);
            } catch (Exception e) {
                appendToUI(e.getMessage());
            }
            return null;
        }
    }

    private class HeartRateConsentTask extends AsyncTask<WeakReference<Activity>, Void, Void> {
        @Override
        protected Void doInBackground(WeakReference<Activity>... params) {
            try {
                if (getConnectedBandClient()) {
                    if (params[0].get() != null) {
                        client.getSensorManager().requestHeartRateConsent(params[0].get(), new HeartRateConsentListener() {
                            @Override
                            public void userAccepted(boolean consentGiven) {
                            }
                        });
                    }
                } else {
                    appendToUI("Band isn't connected. Please make sure bluetooth is on and the band is in range.\n");
                }
            } catch (BandException e) {
                String exceptionMessage = "";
                switch (e.getErrorType()) {
                    case UNSUPPORTED_SDK_VERSION_ERROR:
                        exceptionMessage = "Microsoft Health BandService doesn't support your SDK Version. Please update to latest SDK.\n";
                        break;
                    case SERVICE_ERROR:
                        exceptionMessage = "Microsoft Health BandService is not available. Please make sure Microsoft Health is installed and that you have the correct permissions.\n";
                        break;
                    default:
                        exceptionMessage = "Unknown error occured: " + e.getMessage() + "\n";
                        break;
                }
                appendToUI(exceptionMessage);
            } catch (Exception e) {
                appendToUI(e.getMessage());
            }
            return null;
        }
    }

    //원래 샘플 프로젝트 파일에서 연결 확인할 때 쓰던 것인데 이제는 안 씀
    private void appendToUI(final String string) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                System.out.println(string);
            }
        });
    }
    //원래 샘플 프로젝트 파일에서 연결 확인할 때 쓰던 것인데 이제는 안 씀

    private boolean getConnectedBandClient() throws InterruptedException, BandException {
        if (client == null) {
            //pair된 밴드의 목록을 불러온다
            BandInfo[] devices = BandClientManager.getInstance().getPairedBands();
            //pair된 밴드가 없으면 false 반환하고 메시지 출력
            if (devices.length == 0) {
                appendToUI("Band isn't paired with your phone.\n");
                return false;
            }
            client = BandClientManager.getInstance().create(getBaseContext(), devices[0]);
        } else if (ConnectionState.CONNECTED == client.getConnectionState()) {
            return true;
        }

        appendToUI("Band is connecting...\n");
        return ConnectionState.CONNECTED == client.connect().await();
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////

}

