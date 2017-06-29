package com.sourcey.materiallogindemo;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
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
import com.sourcey.materiallogindemo.Fragment.ChartFragment;
import com.sourcey.materiallogindemo.Fragment.MainFragment;
import com.sourcey.materiallogindemo.Fragment.SettingFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.StringReader;
import java.lang.ref.WeakReference;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import static com.sourcey.materiallogindemo.InitialActivity.timer;


public class MainActivity extends AppCompatActivity {
    // url to create new product
    private static String url_create_product = "http://test.huy.kr/api/v1/user/signup.json";
    JSONParser jsonParser = new JSONParser();
    int error_code;
    public String access_tocken;

    boolean doubleBackToExitPressedOnce = false; // 뒤로가기 버튼을 한번 눌렀는지 체크하기 위한 변수

    BottomNavigationViewEx bottomNavigationView;
    RelativeLayout container;

    //This is our viewPager
    private ViewPager viewPager;

    //Fragments
    MainFragment mainFragment;
    ChartFragment chartFragment;
    SettingFragment settingFragment;
    MenuItem prevMenuItem;

    public String testYesterday = null; // 어제 이 시간 값으로 사용하기 위한 테스트 데이터
    public String stress;
    public Document week_doc;
    public Document month_doc;

    SQLiteDatabase db;

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


    int HRAverage ;
    double HRVariance, stdRRmean, stdRRvar, stdHRvar, stdHRmean;
    double RRAverage;
    double RRVariance;

    // SQLight DB  생성
    MyOpenHelper helper;

    // 임시 데이터를 추출하기 위한 헬퍼와 커서
    TestHelper testHelper;
    public Cursor y_cursor, d_cursor, w_cursor, m_cursor, g_cursor;
    SQLiteDatabase testDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        testHelper = new TestHelper(this); // 테스트 헬퍼

        container = (RelativeLayout) findViewById(R.id.activity_main);
        final View naviLine = (View) findViewById(R.id.navi_line);

        stress = String.valueOf(heartRate);


        helper = new MyOpenHelper(this);
        testDB = testHelper.getReadableDatabase(); // 테스트 디비
        getDataFromDB();
//실제 스마트폰 단말기 내의 data/data/database경로에 파일이 만들어지게된다.
        db = helper.getWritableDatabase();


        //디비에서 표준 HR, RR 평균 분산 가져오기

        String HRAverage1="SELECT AVG(hr) FROM datareceived";
        String HRVariance1="SELECT(SUM(hr*hr) - SUM(hr) * SUM(hr) / COUNT(*)) / (COUNT(*)-1) FROM datareceived";
        Cursor c=db.rawQuery(HRAverage1,null);
        Cursor s=db.rawQuery(HRVariance1,null);
        String RRAverage1="SELECT AVG(rrInterval) FROM datareceived";
        String RRVariance1="SELECT (SUM(rrInterval * rrInterval) - SUM(rrInterval)* SUM(rrInterval) / COUNT(*)) / (COUNT(*)-1) FROM datareceived";
        Cursor a=db.rawQuery(RRAverage1,null);
        Cursor b=db.rawQuery(RRVariance1,null);
        a.moveToFirst();
        b.moveToFirst();
        c.moveToFirst();
        s.moveToFirst();
        HRVariance = s.getDouble(0);
        HRAverage = c.getInt(0);
        RRVariance = b.getDouble(0);
        RRAverage = a.getDouble(0);


        // 스트레스 지수에 따라 배경화면 색상 변하기
        int intNow = Integer.parseInt(stress);
        if (intNow > 80) container.setBackgroundResource(R.drawable.color5);
        else if (intNow > 60) container.setBackgroundResource(R.drawable.color4);
        else if (intNow > 40) container.setBackgroundResource(R.drawable.color3);
        else if (intNow > 20) container.setBackgroundResource(R.drawable.color2);
        else container.setBackgroundResource(R.drawable.color1);

        // bottom navigation view에 property 설정
        bottomNavigationView = (BottomNavigationViewEx) findViewById(R.id.bottom_navigation);
        bottomNavigationView.enableAnimation(false);
        bottomNavigationView.setTextVisibility(false);
        bottomNavigationView.setIconSize(23.3f, 23.8f);
        bottomNavigationView.setItemIconTintList(getResources().getColorStateList(R.color.selector_option));
        naviLine.bringToFront();

        // ViewPAager를 위한 코드
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.chart_fragment:
                                viewPager.setCurrentItem(0);
                                break;
                            case R.id.main_fragment:
                                viewPager.setCurrentItem(1);
                                break;
                            case R.id.setting_fragment:
                                viewPager.setCurrentItem(2);
                                break;
                        }
                        return true;
                    }
                });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0 || position == 2) {
                    bottomNavigationView.setItemIconTintList(getResources().getColorStateList(R.color.selector_option2));
                    naviLine.setBackgroundColor(getResources().getColor(R.color.navi_color));
                } else {
                    bottomNavigationView.setItemIconTintList(getResources().getColorStateList(R.color.selector_option));
                    naviLine.setBackgroundColor(getResources().getColor(R.color.op40));
                }

                if (prevMenuItem != null) {
                    prevMenuItem.setChecked(false);
                } else {
                    bottomNavigationView.getMenu().getItem(0).setChecked(false);
                }
                Log.d("page", "onPageSelected: " + position);
                bottomNavigationView.getMenu().getItem(position).setChecked(true);
                prevMenuItem = bottomNavigationView.getMenu().getItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        setupViewPager(viewPager);
        viewPager.setCurrentItem(1); // 초기화면 설정
        final WeakReference<Activity> reference = new WeakReference<Activity>(this);
        new HeartRateConsentTask().execute(reference);
        SwitchPage(10);
        SwitchPage1(10);

    }

    private void getDataFromDB() {
        // 최근 24시
//        d_cursor = testDB.rawQuery(
//                "SELECT STRFTIME('%H', d) / 3 * 3 AS hour, AVG(s)\n" +
//                        "FROM rand \n" +
//                        "WHERE DATETIME(d) BETWEEN DATETIME('now','-25 hour', 'localtime') AND DATETIME('now', 'localtime')\n" +
//                        "GROUP BY DATE(d), hour"
//                , null);
        // 어제 이 시간
        y_cursor = testDB.rawQuery(
                "SELECT AVG(s)\n" +
                        "FROM rand\n" +
                        "WHERE DATETIME(d) BETWEEN  DATETIME('now', '-25 hour', 'localtime') AND DATETIME('now', '-24 hour', 'localtime')"
                ,null);

        // 오늘 0시부터 24시
        d_cursor = testDB.rawQuery(
                "SELECT STRFTIME('%H', d) / 3 AS hour, AVG(s)\n" +
                        "FROM rand \n" +
                        "WHERE DATE(d) IS DATE('now', 'localtime')\n" +
                        "GROUP BY hour", null);


        // 일주일 날짜, 평균, 최대, 최소, 요일
        w_cursor = testDB.rawQuery(
                "SELECT DATE(d), AVG(s), MAX(s), MIN(s),\n" +
                        "  case cast (strftime('%w', d) as integer)\n" +
                        "  when 0 then '일요일'\n" +
                        "  when 1 then '월요일'\n" +
                        "  when 2 then '화요일'\n" +
                        "  when 3 then '수요일'\n" +
                        "  when 4 then '목요일'\n" +
                        "  when 5 then '금요일'\n" +
                        "  else '토요일' end as dayofweek FROM rand\n" +
                        "WHERE DATE(d) BETWEEN DATE('now', '-7 day', 'localtime') AND DATE('now', '-1 day', 'localtime')\n" +
                        "GROUP BY DATE(d)", null);

        // 30일 날짜, 평균, 최대, 최소, 요일
        m_cursor = testDB.rawQuery(
                "SELECT DATE(d), AVG(s), MAX(s), MIN(s),\n" +
                        "  case cast (strftime('%w', d) as integer)\n" +
                        "  when 0 then '일요일'\n" +
                        "  when 1 then '월요일'\n" +
                        "  when 2 then '화요일'\n" +
                        "  when 3 then '수요일'\n" +
                        "  when 4 then '목요일'\n" +
                        "  when 5 then '금요일'\n" +
                        "  else '토요일' end as dayofweek FROM rand\n" +
                        "WHERE DATE(d) BETWEEN DATE('now', '-30 day', 'localtime') AND DATE('now', '-1 day', 'localtime')\n" +
                        "GROUP BY DATE(d)", null
        );

        // 일주일 데이터를 Jason Array에 저장
        JSONArray array = new JSONArray();
        if (w_cursor != null && w_cursor.getCount() > 0) {
            int id = 0;
            while (w_cursor.moveToNext()) {
                JSONObject object = new JSONObject();
                try {
                    object.put("id", ++id);
                    object.put("day_of_week", w_cursor.getString(4));
                    object.put("date", w_cursor.getString(0));
                    object.put("average", w_cursor.getInt(1));
                    object.put("maximum", w_cursor.getInt(2));
                    object.put("minimum", w_cursor.getInt(3));
                    array.put(object);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        w_cursor.close();

        // Jason Array를 String으로 저장
        String week_xml = "<Data>";
        try {
            week_xml += XML.toString(array, "listdata");
        } catch (JSONException je) {
        }
        week_xml += "</Data>";

        // 30일 데이터를 Jason Array에 저장
        array = new JSONArray();
        if (m_cursor != null && m_cursor.getCount() > 0) {
            int id = 0;
            while (m_cursor.moveToNext()) {
                JSONObject object = new JSONObject();
                try {
                    object.put("id", ++id);
                    object.put("day_of_week", m_cursor.getString(4));
                    object.put("date", m_cursor.getString(0));
                    object.put("average", m_cursor.getInt(1));
                    object.put("maximum", m_cursor.getInt(2));
                    object.put("minimum", m_cursor.getInt(3));
                    array.put(object);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        m_cursor.close();

        // Jason Array를 String으로 저장
        String month_xml = "<Data>";
        try {
            month_xml += XML.toString(array, "listdata");
        } catch (JSONException je) {
        }
        month_xml += "</Data>";

        // String을 Document로 저장
        try {
            week_doc = stringToDom(week_xml);
            month_doc = stringToDom(month_xml);
        } catch (SAXException e) {
        } catch (ParserConfigurationException e) {
        } catch (IOException e) {
        }


        // System.out.println(xml);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        chartFragment = new ChartFragment();
        mainFragment = new MainFragment();
        settingFragment = new SettingFragment();
        adapter.addFragment(chartFragment);
        adapter.addFragment(mainFragment);
        adapter.addFragment(settingFragment);
        viewPager.setAdapter(adapter);
    }


    // fragment에서 Mainactivity의 갱신된 스트레스 정보를 받아오기 위한 함수
    public void refreshUI() {
        stress = String.valueOf(heartRate);
        getDataFromDB();
        // 스트레스 지수에 따라 배경화면 색상 변하기
        int intNow = Integer.parseInt(stress);
        if (intNow > 80) container.setBackgroundResource(R.drawable.color5);
        else if (intNow > 60) container.setBackgroundResource(R.drawable.color4);
        else if (intNow > 40) container.setBackgroundResource(R.drawable.color3);
        else if (intNow > 20) container.setBackgroundResource(R.drawable.color2);
        else container.setBackgroundResource(R.drawable.color1);
    }

    // GraphFragment에서 요구하는 날짜의 24시간 데이터를 MainActivity의 DB로부터 받아오기 위한 함수
    public Cursor sendDataToGraphFragment(String date) {
//        g_cursor = testDB.rawQuery(
//                "SELECT STRFTIME('%H', d) / 3 AS hour, AVG(s)\n" +
//                        "FROM rand\n" +
//                        "WHERE DATE(d) IS DATE('"+ date +"', 'localtime')\n" +
//                        "GROUP BY hour"
//                , null);
        g_cursor = testDB.rawQuery(
                "SELECT STRFTIME('%H', d) / 3 AS hour, AVG(s)\n" +
                        "FROM rand\n" +
                        "WHERE DATE(d) IS DATE('"+ date +"', 'localtime')\n" +
                        "GROUP BY hour\n" +
                        "UNION\n" +
                        "SELECT '8' AS hour, AVG(s)\n" +
                        "FROM rand\n" +
                        "WHERE DATE(d) IS DATE('"+ date +"', '+1 day', 'localtime') AND STRFTIME('%H', d) / 3 IS 0\n" +
                        "GROUP BY hour"
                , null);
        return g_cursor;
    }

    // 뒤로가기를 두번 클릭 시 앱 종료
    @Override
    public void onBackPressed() {
        // BoardFragment의 container가 존재할 때는 그냥 백 버튼 실행
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment_byID = fm.findFragmentById(R.id.container);
        if (fragment_byID != null) {
            super.onBackPressed();
            return;
        }

        // 백 버튼을 두번 이상 누르면 종료
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }
        // 아니면 다음과 같은 알림창을 띄움
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "'뒤로'버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    private Fragment getCurrentFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        String fragmentTag = fragmentManager.getBackStackEntryAt(fragmentManager.getBackStackEntryCount() - 1).getName();
        Fragment currentFragment = fragmentManager.findFragmentByTag(fragmentTag);
        return currentFragment;
    }

    public void SwitchPage(int seconds){
        if(timer!=null){
            timer.cancel();
        }
        timer=new Timer();//AtthislineanewThreadwillbecreated
        timer.schedule(new MainActivity.MeasureTime(),
                0,seconds*1000);
        timer.schedule(new MainActivity.Getstress(),10000,seconds*1000);

    }

    public void SwitchPage1(int seconds){
        if(timer!=null){
            timer.cancel();
        }
        timer=new Timer();//AtthislineanewThreadwillbecreated
        timer.schedule(new MainActivity.Getstress(),10000,seconds*1000);

    }
    class Getstress extends TimerTask{
        @Override
        public void run(){
            String stdHRA="SELECT AVG(stdHR) FROM  STDdata WHERE REPLACE(d, '/', '-') BETWEEN DATETIME('now', '-10 second', 'localtime') AND DATETIME('now', 'localtime') ";
            String stdHRV="SELECT(SUM(stdHR*stdHR) - SUM(stdHR) * SUM(stdHR) / COUNT(*)) / (COUNT(*)-1) FROM  STDdata WHERE REPLACE(d, '/', '-') BETWEEN DATETIME('now', '-10 second', 'localtime') AND DATETIME('now', 'localtime') ";
            Cursor cc=db.rawQuery(stdHRA,null);
            Cursor ss=db.rawQuery(stdHRV,null);
            String stdRRA="SELECT AVG(stdRR) FROM  STDdata WHERE REPLACE(d, '/', '-') BETWEEN DATETIME('now', '-10 second', 'localtime') AND DATETIME('now', 'localtime') ";
            String stdRRV="SELECT (SUM(stdRR * stdRR) - SUM(stdRR)* SUM(stdRR) / COUNT(*)) / (COUNT(*)-1) FROM  STDdata WHERE REPLACE(d, '/', '-') BETWEEN DATETIME('now', '-10 second', 'localtime') AND DATETIME('now', 'localtime') ";
            Cursor aa=db.rawQuery(stdRRA,null);
            Cursor bb=db.rawQuery(stdRRV,null);
            aa.moveToFirst();
            bb.moveToFirst();
            cc.moveToFirst();
            ss.moveToFirst();
            stdHRvar = ss.getDouble(0);
            stdHRmean = cc.getDouble(0);
            stdRRvar = bb.getDouble(0);
            stdRRmean = aa.getDouble(0);

            double stressindex=-0.0802313290796385*stdHRmean+0.0281020720365838*stdHRvar-0.05540296813564*stdRRmean+0.0222774503382897*stdRRvar-0.0234010114494386+50;
            //    double stressindex=-0.0802313290796385*stdHRmean+0.281020720365838*1-0.5540296813564*stdRRmean+0.222774503382897*1-0.234010114494386;
            String stress=Double.toString(stressindex);
            db.execSQL("insert into STRESS(d,s)values('"+date+"','"+stress+"');");
        }
    }

    class MeasureTime extends TimerTask {
        @Override
                public void run(){


//-----------------측정실행은이Mesaurement().execute()로시작된다---------------------------//
            System.out.println("--------------------시작---------------------------------------");
            new Measurement().execute();

            System.out.println("--------------------멈춰!---------------------------------------");


//db.execSQL("insertintoHRSUM(sum)values('"+sum+"');");
//System.out.println("sum"+sum);
//System.out.println("저장완료");
//=======================================================================================//

                //sum=sum+heartRate;
        }
    }





    ////////////////////////////////////////////////////////////////////////////////////////////////
    /*
    * jeonyc93@gmail.com
    * 1. 아래 각각의 변수가 하나의 스레드로 실행됨
    * 2. 매 5초마다 측정하고 시간, 심박, rr인터벌, 가속도 순서대로 측정
    * 3. 각각 d, heartRate, rrInterval, acc_x/acc_y/acc_z에 저장
    * */

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

                //실제 스마트폰 단말기 내의 data/data/database경로에 파일이 만들어지게된다.
                db = helper.getWritableDatabase();


                DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                date = df.format(Calendar.getInstance().getTime());

//                db.execSQL("insert into datareceived(d, hr, rrInterval, acc_x, acc_y, acc_z) values ('" +
//                        date + "', " + A + ", " + B + ", " + C + ", " + D + ", " + E + ");");
//
                double stdHR=(heartRate-HRVariance)/HRAverage;
                double stdRR=(rrInterval-RRVariance)/RRAverage;

                db.execSQL("insert into STDdata(d, stdHR, stdRR) values ('" +
                        date + "', " + stdHR + ", " + stdRR + ");");
//


            }
        }
    };
    //----------가속도 관련된 거 표시하기---------------------------------------------------------------//

    public void onMainSuccess() {
//자동으로 서버에 넘김.
    }

    public void onMainFailed1() {
        Toast.makeText(getBaseContext(), "Login failed:1001", Toast.LENGTH_LONG).show();

    }

    public void onMainFailed2() {
        Toast.makeText(getBaseContext(), "Login failed:1002", Toast.LENGTH_LONG).show();

    }

    public void onMainFailed3() {
        Toast.makeText(getBaseContext(), "Login failed:1003", Toast.LENGTH_LONG).show();

    }

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

    // String 변수를 Document 타입 변수로 변환하기 위한 함수
    public static Document stringToDom(String xmlSource)
            throws SAXException, ParserConfigurationException, IOException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        return builder.parse(new InputSource(new StringReader(xmlSource)));
    }

}



