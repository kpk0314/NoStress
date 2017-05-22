package com.sourcey.materiallogindemo;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.sourcey.materiallogindemo.InitialFragment.FirstFragment;
import com.sourcey.materiallogindemo.InitialFragment.SecondFragment;
import com.sourcey.materiallogindemo.InitialFragment.ThirdFragment;

import butterknife.ButterKnife;


/**
 * Created by Isaac Kim on 2017-05-22.
 */


public class InitialActivity extends AppCompatActivity {

    //This is our viewPager
    private ViewPager viewPager;

    //Fragments
    FirstFragment firstFragment;
    SecondFragment secondFragment;
    ThirdFragment thirdFragment;
    MenuItem prevMenuItem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial);
        ButterKnife.bind(this);

        //Initializing viewPager
        viewPager = (ViewPager) findViewById(R.id.viewpager);

        Button btn_first = (Button)findViewById(R.id.btn_signup2);
        Button btn_second = (Button)findViewById(R.id.btn_login2);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (prevMenuItem != null) {
                    prevMenuItem.setChecked(false);
                }
                Log.d("page", "onPageSelected: " + position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        setupViewPager(viewPager);
        viewPager.setCurrentItem(0);

        btn_first.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PrivacyActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btn_second.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }


    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        firstFragment = new FirstFragment();
        secondFragment = new SecondFragment();
        thirdFragment = new ThirdFragment();
        adapter.addFragment(firstFragment);
        adapter.addFragment(secondFragment);
        adapter.addFragment(thirdFragment);
        viewPager.setAdapter(adapter);
    }

}
