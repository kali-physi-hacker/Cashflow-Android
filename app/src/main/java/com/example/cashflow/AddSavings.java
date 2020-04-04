package com.example.cashflow;

import androidx.annotation.ColorInt;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class AddSavings extends AppCompatActivity {

    private Button addMoneyBtn;

    private EditText amount;
    private TextView dateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_savings);

        amount = findViewById(R.id.amount_edit_text);
        dateTextView = findViewById(R.id.for_date_text_view);

        addMoneyBtn = findViewById(R.id.add_money_btn);

        dateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                final int mYear = c.get(Calendar.YEAR);
                final int mMonth = c.get(Calendar.MONTH);
                final int mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(AddSavings.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                String date = year+"-"+(month+1)+"-"+dayOfMonth;
                                dateTextView.setText(date);
                                dateTextView.setTextColor(getResources().getColor(R.color.white));
                            }
                        }, mYear, mMonth, mDay);
                // datePickerDialog.set
                datePickerDialog.show();
            }
        });

        addMoneyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addMoney();
            }
        });
    }

    private Boolean validate() {
        return amount.getText() != null;
    }

    private void addMoney() {
        if (validate()) {
            final int amnt = Integer.parseInt(amount.getText().toString());
            final String date = dateTextView.getText().toString();

            String fullUrl = CONSTANTS.URL + CONSTANTS.savings_account_uuid + "/";

            JSONObject postparams = new JSONObject();

            try {
                postparams.put("amount", amount);
                postparams.put("for_date", date);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, fullUrl, postparams,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d("Response", response.toString());
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("Error Response", error.toString());
                        }
                    }){
//                    @Override
//                    protected Map<String, String> getParams() {
//                        Map<String, String> params = new HashMap<String, String>();
//                        params.put("for_date", date);
//                        params.put("amount", ""+amnt);
//                        return params;
//                    }

                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        HashMap headers = new HashMap();
                        headers.put("Content-Type", "application/json");
                        headers.put("Authorization", "Token "+CONSTANTS.USER_TOKEN);
                        return headers;
                    }

            };

            RequestQueue queues = Volley.newRequestQueue(getApplicationContext());
            queues.add(postRequest);
        }
    }
}
