package com.example.cashflow;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class IntroActivity extends AppCompatActivity {

    public ViewPager screenPager;
    StartupViewPagerAdapter viewPagerAdapter;

    TextView prevBtn;
    Button nextBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // make Activity on Fullscreen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        if (restorePrefData()) {
            Intent loginActivity = new Intent(IntroActivity.this, Login.class);
            startActivity(loginActivity);
            finish();
        }

        setContentView(R.layout.activity_intro);


        prevBtn = findViewById(R.id.intro_btn_prev);
        nextBtn = findViewById(R.id.intro_btn_next);

        List<StartupScreenItem> mList = new ArrayList<>();
        mList.add(new StartupScreenItem("" + getText(R.string.welcome_one_heading), "" + getText(R.string.welcome_one_subtext), R.drawable.ic_savings)); // R.string.welcome_one_heading
        mList.add(new StartupScreenItem("" + getText(R.string.welcome_two_heading), "" + getText(R.string.welcome_two_subtext), R.drawable.ic_personal_finance));
        mList.add(new StartupScreenItem("" + getText(R.string.welcome_three_heading), "" + getText(R.string.welcome_three_subtext), R.drawable.ic_invest));
        screenPager = findViewById(R.id.screen_pager);

        viewPagerAdapter = new StartupViewPagerAdapter(this, mList);
        screenPager.setAdapter(viewPagerAdapter);

        screenPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                switch (position) {
                    case 0:
                        prevBtn.setVisibility(View.INVISIBLE);
                        nextBtn.setText(getText(R.string.next_text));
                        nextPage(nextBtn, 1);
                        break;
                    case 1:
                        nextBtn.setText(getText(R.string.next_text));
                        prevBtn.setVisibility(View.VISIBLE);
                        prevPage(prevBtn, 0);
                        nextPage(nextBtn, 2);
                        break;
                    case 2:
                        nextBtn.setText("Done");
                        nextBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
//                                Intent mainActivity = new Intent(IntroActivity.this, MainActivity.class);
//                                startActivity(mainActivity);
//                                savePrefsData();
                                Intent login = new Intent(IntroActivity.this, Login.class);
                                startActivity(login);
                                savePrefsData();
                            }
                        });
                        prevPage(prevBtn, 1);
                        break;
                }
            }

            @Override
            public void onPageSelected(int position) {
                Log.d("ViewPager Kali", "In onPageSelected Page");
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                Log.d("ViewPager Kali", "In onPageScrollStateChanged Page");
            }
        });

        Log.d("Current Item Kali", "" + screenPager.getCurrentItem());
    }

    private void prevPage(TextView btn, final int position) {
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                screenPager.setCurrentItem(position);
            }
        });
    }

    private void nextPage(Button btn, final int position) {
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                screenPager.setCurrentItem(position);
            }
        });
    }

    private void savePrefsData() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("myPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("isFirstTime", true);
        editor.commit();
    }

    private boolean restorePrefData() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("myPrefs", MODE_PRIVATE);
        return pref.getBoolean("isFirstTime", false);
    }
}
