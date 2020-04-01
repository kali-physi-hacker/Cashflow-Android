package com.example.cashflow.ui.savings;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.cashflow.R;
import com.example.cashflow.SavingsModel;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class SavingsFragment extends Fragment {

    private View root;
    // private static String URL = "http://192.168.100.50:8000/susu/savingsEntry/api/"; <----- Office Local Ip Address
     private static String URL = "http://192.168.43.188:8000/susu/savingsEntry/api/";
    SimpleDateFormat date = new SimpleDateFormat("dd-M-yyyy hh:mm:ss a", Locale.getDefault());

    private SavingsViewModel savingsViewModel;
    private CompactCalendarView savingsCalendarView;

    private JSONArray savings = new JSONArray();
    private ProgressDialog loading;

    private TextView daily, weekly, monthly;

    private String[] months = {
            "January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"
    };

    private Button savingsCalendarBtnOne, savingsCalendarBtnTwo, savingsCalendarBtnThree;

    TextView dateTextView;
    @RequiresApi(api = Build.VERSION_CODES.N)
    public View onCreateView(LayoutInflater inflater,
                             final ViewGroup container, Bundle savedInstanceState) {
        savingsViewModel =
                ViewModelProviders.of(this).get(SavingsViewModel.class);
        root = inflater.inflate(R.layout.fragment_savings, container, false);

        daily = root.findViewById(R.id.daily_total);
        weekly = root.findViewById(R.id.weekly_total);
        monthly = root.findViewById(R.id.monthly_total);

        savingsCalendarBtnOne = root.findViewById(R.id.savings_calendar_button_one);
        savingsCalendarBtnTwo = root.findViewById(R.id.savings_calendar_button_two);
        savingsCalendarBtnThree = root.findViewById(R.id.savings_calendar_button_three);

        // Loading Animation for Fetching Data for Savings Calendar
        loading = new ProgressDialog(getContext());
        loading.setTitle("Fetching Data");
        loading.setMessage("Loading ...");
        loading.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        loading.show();

        savingsCalendarView = root.findViewById(R.id.savings_calendar);
        savingsCalendarView.setUseThreeLetterAbbreviation(true);
        savingsCalendarView.setEventIndicatorStyle(1);
        dateTextView = root.findViewById(R.id.date_text_view);
        updateDisplayInformation(dateTextView);

        initializeBtns();

        savingsCalendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                Log.d("Date", dateClicked.toString());
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                String monthName = months[firstDayOfNewMonth.getMonth()];
                Calendar currCal = Calendar.getInstance();
                currCal.setTime(firstDayOfNewMonth);
                int year = currCal.get(Calendar.YEAR);
                String dateText = monthName + " " + year;
                dateTextView.setText(dateText);
            }
        });

        getSavingsData(1, 1);
        return root;

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void updateDisplayInformation(TextView textView) {
        int currentMonth = savingsCalendarView.getFirstDayOfCurrentMonth().getMonth();
        Calendar currCal = Calendar.getInstance();
        currCal.setTime(savingsCalendarView.getFirstDayOfCurrentMonth());

        String currentMonthName = months[currentMonth];
        String currentDate = currentMonthName + " " + currCal.get(Calendar.YEAR);
        textView.setText(currentDate);
        Log.d("Scrolled", currentDate);
    }

    private void updateCalendar(JSONArray savingsArr) {
        Log.d("Savings Array Content", "" + savingsArr.length());
        for (int i = 0; i < savingsArr.length(); i++) {
            try {
                String date = savingsArr.getJSONObject(i).getString("date");
                String amount = savingsArr.getJSONObject(i).getString("amount");
                Event event = new Event(Color.RED, getEpochTime(date), amount);
                savingsCalendarView.addEvent(event);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        savingsCalendarView.invalidate();
    }

    private long getEpochTime(String timestamp) {
        long epoch;
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy/dd/MM").parse(timestamp);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (date != null) {
            epoch = date.getTime();
        } else {
            epoch = -1;
        }

        return epoch;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void initializeBtns () {
        Calendar currCal = Calendar.getInstance();
        currCal.setTime(savingsCalendarView.getFirstDayOfCurrentMonth());
        int currentMonth = currCal.get(Calendar.MONTH);
        if ( currentMonth < 0) {
            currentMonth = months.length - (-1*currentMonth);
        }

        int prevMonth = currCal.get(Calendar.MONTH) - 1;
        if (prevMonth < 0) {
            prevMonth = months.length - (-1*prevMonth);
        }

        int nextMonth = currCal.get(Calendar.MONTH) + 1;
        if (nextMonth < 0) {
            nextMonth = months.length - (-1*nextMonth);
        }
        String currentMonthName = months[currentMonth];
        String prevMonthName = months[prevMonth];
        String nextMonthName = months[nextMonth];

        Log.d("Current Month", "Current Month Number: " + currCal.get(Calendar.MONTH));
        Log.d("Previous Month", "Previous Month Number: " + (currCal.get(Calendar.MONTH) - 1));


        savingsCalendarBtnOne.setText(prevMonthName);
        savingsCalendarBtnTwo.setText(currentMonthName);
        savingsCalendarBtnThree.setText(nextMonthName);
    }

    private void swapButtons(View view) {

    }

    private void getSavingsData(int savingsAccount, int userId) {
        String fullURl = URL + savingsAccount + '/' + userId + '/';

        JsonObjectRequest savingsRequest = new JsonObjectRequest(Request.Method.GET, fullURl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                loading.dismiss();
                String status = null;
                String weekly_total = null;
                String monthly_total = null;
                String yearly_total = null;

                try {

                    status = response.getString("STATUS");
                    weekly_total = response.getString("weekly_total");
                    monthly_total = response.getString("monthly_total");
                    yearly_total = response.getString("yearly_total");

                } catch (JSONException e) {

                    e.printStackTrace();
                }
                if (status != null && status.equals("OK")) {
                    try {
                        savings = response.getJSONArray("entries");
                        updateCalendar(savings);

                        daily.setText(weekly_total);
                        weekly.setText(monthly_total);
                        monthly.setText(yearly_total);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Events status", "Error occured");
                Log.d("Error Message", error.toString());

            }
        });

        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(savingsRequest);

    }


}