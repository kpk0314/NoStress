package com.sourcey.materiallogindemo;

import android.content.Intent;

import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.database.sqlite.SQLiteDatabase;
import android.database.Cursor;

import com.sourcey.materiallogindemo.Fragment.ChartFragment;
import com.sourcey.materiallogindemo.Fragment.MainFragment;
import com.sourcey.materiallogindemo.Fragment.SettingFragment;


public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;



    //This is our viewPager
    private ViewPager viewPager;

    //Fragments

    MainFragment mainFragment;
    ChartFragment chartFragment;
    SettingFragment settingFragment;
    MenuItem prevMenuItem;

    public String stress = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // SQLight DB  생성
        MyOpenHelper helper = new MyOpenHelper(this);

        //실제 스마트폰 단말기 내의 data/data/database경로에 파일이 만들어지게된다.
        SQLiteDatabase db = helper.getWritableDatabase();

        db.execSQL("insert into member(t, h, r, x) values ('2017/05/13 13:24:33', 80, 0, '0/0/0');");

        //데이터데이스의 데이터를 그대로 메모리상에 올려놓은 객체자 Cursor이다.
        Cursor rs = db.rawQuery("select * from member;", null);

        while (rs.moveToNext()) {
            stress = String.valueOf(rs.getInt(1));
        }
        rs.close();
        db.close();

        //Initializing viewPager
        viewPager = (ViewPager) findViewById(R.id.viewpager);

        //Initializing the bottomNavigationView
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
        viewPager.setCurrentItem(1);

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
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





}
