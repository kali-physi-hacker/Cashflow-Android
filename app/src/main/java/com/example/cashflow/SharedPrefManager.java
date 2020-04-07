package com.example.cashflow;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SharedPrefManager {

    public static final String SHARED_PREF_NAME = "cashFlowRegisterLogin";
    private static final String SHARED_KEY_USERNAME = "keyUsername";
    private static final String SHARED_KEY_PASSWORD = "keyPassword";
    private static final String SHARED_KEY_USER_TOKEN = "keyUserToken";
    private static final String SHARED_ACTIVE_SAVINGS_ACCOUNT = "keyActiveSavingsAccount";
    public static final String SHARED_BASE_SAVINGS_ACCOUNT = "keyBaseSavingsAccount";
    public static final String SHARED_SAVINGS_ACCOUNT_NUMBER = "keySavingsAccountNumber";
    private static SharedPrefManager mInstance;
    private static Context ctx;

    private SharedPrefManager(Context context) {
        ctx = context;
    }

    public static synchronized SharedPrefManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SharedPrefManager(context);
        }
        return mInstance;
    }

    // Storing the user information
    public void userLogin(User user, JSONObject savingsAccount) {
        SharedPreferences sharedPrefManager = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefManager.edit();
        editor.putString(SHARED_KEY_USERNAME, user.getUsername());
        // editor.putString(SHARED_KEY_PASSWORD, user.getPassword());
        editor.putString(SHARED_KEY_USER_TOKEN, user.getToken());

        // Savings Accounts
        editor.putInt(SHARED_SAVINGS_ACCOUNT_NUMBER, savingsAccount.length());
        for (int i=0; i<savingsAccount.length(); i++) {
            for (int j=0; j<2; j++) {
                String keyName = SHARED_BASE_SAVINGS_ACCOUNT + " NAME " + j;
                String keyUUID = SHARED_BASE_SAVINGS_ACCOUNT + " UUID " + j;
                try {
                    JSONObject account = savingsAccount.getJSONObject(j+"");
                    editor.putString(keyName, account.getString("name"));
                    editor.putString(keyUUID, account.getString("uuid"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        editor.apply();
    }

    // Checks whether user is already logged in or not
    public boolean isLoggedIn() {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(SHARED_KEY_USER_TOKEN, null) != null;
    }

    // Returns the logged in user
    public User getUser() {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return new User(
            sharedPreferences.getString(SHARED_KEY_USER_TOKEN, null),
            sharedPreferences.getString(SHARED_KEY_USERNAME, null)
            // sharedPreferences.getString(SHARED_KEY_PASSWORD, null)
        );
    }

    // Logs com.example.cashflow.User Out
    public void logout() {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        Intent intent = new Intent(ctx, Login.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        ctx.startActivity(intent);
        MainActivity.fa.finish();
    }

    public String getSharedActiveSavingsAccount() {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(SHARED_ACTIVE_SAVINGS_ACCOUNT, "");
    }

    public void setSharedActiveSavingsAccount(String savingsAccount) {
        SharedPreferences preferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(SHARED_ACTIVE_SAVINGS_ACCOUNT, savingsAccount);
        editor.apply();
    }

    public ArrayList<SavingsAccountModel> getSavingsAccounts(int size) {
        ArrayList<SavingsAccountModel> savingsAccounts = new ArrayList<>();

        SharedPreferences sharedPrefManager = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        for (int i=0; i<size; i++) {
            String keyName = SHARED_BASE_SAVINGS_ACCOUNT + " NAME " + i;
            String keyUUID = SHARED_BASE_SAVINGS_ACCOUNT + " UUID " + i;
            SavingsAccountModel accountModel = new SavingsAccountModel(sharedPrefManager.getString(keyName, null),
                    sharedPrefManager.getString(keyUUID,null));
            savingsAccounts.add(accountModel);
        }
        Log.d("Savings AccountMod", savingsAccounts.get(0).getName());
        return savingsAccounts;
    }
}
