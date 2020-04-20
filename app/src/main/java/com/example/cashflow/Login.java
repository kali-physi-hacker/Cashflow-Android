package com.example.cashflow;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {

    private EditText username, password;
    ProgressBar progressBar;
    private Button loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (SharedPrefManager.getInstance(getApplicationContext()).isLoggedIn()) {
            startActivity(new Intent(Login.this, MainActivity.class));
            finish();
        }

        // Make Activity on FullScreen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_login);


        username = findViewById(R.id.username);
        password = findViewById(R.id.password);

        loginBtn = findViewById(R.id.login_btn);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String usernameText = username.getText().toString();
                final String passwordText = password.getText().toString();

                if (validate()) {
                    StringRequest loginRequest = new StringRequest(Request.Method.POST, CONSTANTS.LOGIN_URL,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    JSONObject obj = null;

                                    try {
                                        obj = new JSONObject(response);
                                        String token = null;
                                        String userName = null;
                                        JSONObject savingsAccounts = null;
                                        try {
                                            token = obj.getString("token");
                                            userName = obj.getString("name");
                                            savingsAccounts = obj.getJSONObject("savings_accounts");
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                        User user = new User(token, userName);
                                        Log.d("Savings Account", savingsAccounts.toString());

                                        // Store the user in the shared preferences
                                        SharedPrefManager.getInstance(getApplicationContext()).userLogin(user, savingsAccounts);

                                        // Starting The Main Activity
                                        finish();
                                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("Login Error", error.toString());
                            Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<>();
                            params.put("username", usernameText);
                            params.put("password", passwordText);
                            return params;
                        }
                    };

                    VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(loginRequest);
                }else {
                    Toast.makeText(getApplicationContext(), "Fill in the whole fields", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private Boolean validate() {
        return !(username.getText().toString().equals("") & password.getText().toString().equals(""));
    }
}
