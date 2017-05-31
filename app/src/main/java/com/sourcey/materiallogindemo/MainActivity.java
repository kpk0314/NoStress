package com.sourcey.materiallogindemo;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;

import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.database.sqlite.SQLiteDatabase;
import android.database.Cursor;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sourcey.materiallogindemo.Fragment.ChartFragment;
import com.sourcey.materiallogindemo.Fragment.MainFragment;
import com.sourcey.materiallogindemo.Fragment.SettingFragment;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static android.os.SystemClock.sleep;


public class MainActivity extends AppCompatActivity {

//    Handler handler = new Handler();
//    AnimationDrawable anim;
//    RelativeLayout container;

    BottomNavigationView bottomNavigationView;

    //This is our viewPager
    private ViewPager viewPager;

    //Fragments

    MainFragment mainFragment;
    ChartFragment chartFragment;
    SettingFragment settingFragment;
    MenuItem prevMenuItem;

    public String testYesterday = null; // 어제 이 시간 값으로 사용하기 위한 테스트 데이터
    public String stress = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setWindowAnimations(android.R.style.Animation_Toast);

         final RelativeLayout container = (RelativeLayout) findViewById(R.id.activity_main);


        // SQLight DB  생성
        MyOpenHelper helper = new MyOpenHelper(this);

        //실제 스마트폰 단말기 내의 data/data/database경로에 파일이 만들어지게된다.
        SQLiteDatabase db = helper.getWritableDatabase();

        double randomValue = Math.random();
        int intValue = (int)(randomValue * 100) + 1;

        DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String date = df.format(Calendar.getInstance().getTime());

        db.execSQL("insert into member(t, h, r, x) values ('" + date + "', " + intValue + ", 0, '0/0/0');");

        //데이터데이스의 데이터를 그대로 메모리상에 올려놓은 객체자 Cursor이다.
        Cursor rs = db.rawQuery("select * from member;", null);
        while (rs.moveToNext()) {
            stress = String.valueOf(rs.getInt(1));
        }
        rs.close();
        db.close();

        // 스트레스 지수에 따라 배경화면 색상 변하기
        int intNow = Integer.parseInt(stress);
        if(intNow > 80) container.setBackgroundResource(R.drawable.color5);
        else if(intNow > 60) container.setBackgroundResource(R.drawable.color4);
        else if(intNow > 40) container.setBackgroundResource(R.drawable.color3);
        else if(intNow > 20) container.setBackgroundResource(R.drawable.color2);
        else container.setBackgroundResource(R.drawable.color1);

        // ViewPAager를 위한 코드
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_call:
                                viewPager.setCurrentItem(0);
                                break;
                            case R.id.action_chat:
                                viewPager.setCurrentItem(1);
                                break;
                            case R.id.action_contact:
                                viewPager.setCurrentItem(2);
                                break;
                        }
                        return false;
                    }
                });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(int position) {
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


    /**
     * Created by Isaac Kim on 2017-05-15.
     */

    public static class PrivacyActivity {
    }
}
