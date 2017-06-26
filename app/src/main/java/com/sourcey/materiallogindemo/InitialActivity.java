package com.sourcey.materiallogindemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.sourcey.materiallogindemo.InitialFragment.FirstFragment;
import com.sourcey.materiallogindemo.InitialFragment.SecondFragment;
import com.sourcey.materiallogindemo.InitialFragment.ThirdFragment;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.ButterKnife;


/**
 * Created by Isaac Kim on 2017-05-22.
 */


public class InitialActivity extends AppCompatActivity {

    static Timer timer = new Timer(); // 일정시간 지나면 화면 전환을 위한 time 변수

    // 뷰 페이져 변수
    private ViewPager viewPager;
    int i = 0;

    // 세 가지 프레그먼트
    FirstFragment firstFragment;
    SecondFragment secondFragment;
    ThirdFragment thirdFragment;
    MenuItem prevMenuItem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial);
        getWindow().setWindowAnimations(android.R.style.Animation_Toast);
        ButterKnife.bind(this);

        //뷰 페이져 초기화
        viewPager = (ViewPager) findViewById(R.id.viewpager);

        // 아이디 - 변수 매칭
        Button btn_first = (Button) findViewById(R.id.btn_signup2);
        Button btn_second = (Button) findViewById(R.id.btn_login2);

        // 뷰페이지 함수
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
        SwitchPage(5);

        // 회원가입 버튼 클릭 시
        btn_first.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // 로그인 버튼 클릭 시
        btn_second.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }


    // 세 가지 프레그먼트 -> 뷰 페이저
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        firstFragment = new FirstFragment();
        secondFragment = new SecondFragment();
        thirdFragment = new ThirdFragment();
        adapter.addFragment(firstFragment);
        adapter.addFragment(secondFragment);
        adapter.addFragment(thirdFragment);
        viewPager.setAdapter(adapter);

        viewPager.setPageTransformer(false, new ViewPager.PageTransformer() {
            @Override
            public void transformPage(View view, float position) {
                view.setTranslationX(view.getWidth() * -position);
                if (position <= -1.0F || position >= 1.0F) {
                    view.setAlpha(0.0F);
                } else if (position == 0.0F) {
                    view.setAlpha(1.0F);
                } else {
                    // position is between -1.0F & 0.0F OR 0.0F & 1.0F
                    view.setAlpha(1.0F - Math.abs(position));
                }
            }
        });
    }



    public void SwitchPage(int seconds) {
        if (timer != null) {
            timer.cancel();
        }
        timer = new Timer(); // At this line a new Thread will be created
        timer.schedule(new SwitchPageTask(),
                2000, seconds * 2000);
        // delay in milliseconds
    }



    class SwitchPageTask extends TimerTask {
        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                public void run() {
                    if (i < viewPager.getAdapter().getCount()) {
                        i++;
                        viewPager.setCurrentItem(i, true);
                    } else {
                        i = 0;
                    }
                }
            });
        }
    }


}
