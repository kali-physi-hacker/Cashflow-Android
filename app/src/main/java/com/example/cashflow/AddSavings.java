package com.example.cashflow;

import androidx.annotation.ColorInt;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
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
    private EditText dateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_savings);

        amount = findViewById(R.id.amount_edit_text);
        dateTextView = findViewById(R.id.for_date_edit_text);

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
                                // dateTextView.setTextColor(getResources().getColor(R.color.colorPrimary));
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

            Map<String, String> params = new HashMap<>();
            params.put("amount", ""+amnt);
            params.put("for_date", date);

            JSONObject obj = new JSONObject(params);

            String fullUrl = CONSTANTS.SAVINGS_ENTRY_URL + SharedPrefManager.getInstance(getApplicationContext()).getSharedActiveSavingsAccount() + "/";

            StringRequest postRequest = new StringRequest(Request.Method.POST, fullUrl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("Response", response.toString());
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("Error Response", error.toString());
                            Log.d("Information: ", date + amnt);
                        }
                    }){
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<>();
                            params.put("for_date", date);
                            params.put("amount", ""+amnt);
                            return params;
                        }
                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            User user = SharedPrefManager.getInstance(getApplicationContext()).getUser();

                            Map<String, String> headers = new HashMap<>();
                            headers.put("Content-Type", "application/form-data");
                            headers.put("Authorization", "Token "+user.getToken());
                            return headers;
                        }
                    };

            // Volley.newRequestQueue(this).add(postRequest);

            VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(postRequest);
        }
    }
}

