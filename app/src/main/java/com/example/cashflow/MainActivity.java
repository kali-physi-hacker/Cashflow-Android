package com.example.cashflow;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.cashflow.ui.savings.SavingsFragment;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private CalendarView savingsCalendar;
    private TextView userNameTextView;
    int size;

    public static Activity fa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fa = this;
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // FloatingActionButton fab = findViewById(R.id.action_button);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        userNameTextView = headerView.findViewById(R.id.username_text);
        userNameTextView.setText(SharedPrefManager.getInstance(getApplicationContext()).getUser().getUsername());

        // Log.d("Username Text", userNameTextView.getText().toString());
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_savings, R.id.nav_expense,
                R.id.nav_tools, R.id.nav_share, R.id.nav_send)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        // Check to see whether user has an active savings Account, If not, User should be prompted to select

        SharedPreferences preferences = getApplicationContext().getSharedPreferences(SharedPrefManager.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        size = preferences.getInt(SharedPrefManager.SHARED_SAVINGS_ACCOUNT_NUMBER, 0);

        String activeSavingsAccount = SharedPrefManager.getInstance(this).getSharedActiveSavingsAccount();

        if (activeSavingsAccount.isEmpty()) {
            showSelectDialog(MainActivity.this, size);
        } else {
            Log.d("Active Savings Account", "Active Account " + activeSavingsAccount);
        }
    }

    public void showSelectDialog(Activity activity, int savingsAccountSize) {
        final Dialog dialog = new Dialog(activity);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.select_savings_account_dialog);

        ArrayList<SavingsAccountModel> savingsAccounts = SharedPrefManager.getInstance(this).getSavingsAccounts(savingsAccountSize);
        Log.d("Savings Accounts List", ""+savingsAccountSize);
        ArrayList<String> savingsAccountNames = new ArrayList<>();
        final ArrayList<String> savingsAccountUuids = new ArrayList<>();

        for (int i=0; i<savingsAccounts.size(); i++) {
            savingsAccountNames.add(savingsAccounts.get(i).getName());
            savingsAccountUuids.add(savingsAccounts.get(i).getUuid());
        }

        ListView listView = dialog.findViewById(R.id.savings_account_list);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("Item", savingsAccountUuids.get(position));
                SharedPrefManager.getInstance(getApplicationContext()).setSharedActiveSavingsAccount(savingsAccountUuids.get(position));
                dialog.dismiss();
            }
        });


        ArrayAdapter arrayAdapter = new ArrayAdapter(this, R.layout.select_savings_account_list_item, R.id.savings_account_list_item_text_view, savingsAccountNames);
        // Log.d("Savings Account Names", savingsAccountNames.toString());
        listView.setAdapter(arrayAdapter);

        dialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_logout:
                SharedPrefManager.getInstance(getApplicationContext()).logout();
                return true;
            case R.id.action_change_savings_account:
                showSelectDialog(MainActivity.this, size);
                Log.d("Savings Account", "Changing Savings Account");
                return true;
            case R.id.action_reload_savings_account:
                Log.d("Reload", "Reload Savings Account");
                FragmentManager manager = getSupportFragmentManager();
                SavingsFragment fragment = new SavingsFragment();
                manager.beginTransaction().add(R.id.nav_host_fragment, fragment);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
         return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

}


