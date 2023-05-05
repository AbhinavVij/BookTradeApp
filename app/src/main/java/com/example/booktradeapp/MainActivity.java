package com.example.booktradeapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {


    private SharedPref sharedPreferences;
    FirebaseAuth mAuth;
    FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedPreferences = new SharedPref(this);
mAuth=FirebaseAuth.getInstance();

user=mAuth.getCurrentUser();
if(user==null)
{
    Intent intent =new Intent(getApplicationContext(),LoginActivity.class);
    startActivity(intent);
    finish();
}
        //load theme preference
        if (sharedPreferences.loadNightModeState()) {
            setTheme(R.style.DarkTheme);
        } else {
            setTheme(R.style.LightTheme);
        }
        if (sharedPreferences.loadDisplay()) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            Toolbar mytoolbar=findViewById(R.id.toolbar);
            setSupportActionBar(mytoolbar);
            PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
            Intent intent=new Intent(this,SellPage.class);
            startActivity(intent);
        } else {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            Toolbar mytoolbar=findViewById(R.id.toolbar);
            setSupportActionBar(mytoolbar);
            PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

        }



    }
    public void onSellButtonClicked(View view)
    {
        Intent intent=new Intent(this,SellPage.class);
        startActivity(intent);

    }

    public void onBuyButtonClicked(View view)
    {
        Intent intent2=new Intent(MainActivity.this,BuyPage.class);
        startActivity(intent2);


    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_home, menu);
        setTitle("Book Trade");

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                MainActivity.ConfirmDeleteDialog confirmDialog = new MainActivity.ConfirmDeleteDialog();
                confirmDialog.show(getSupportFragmentManager(), "logoutConfirmation");
                return true;
            case R.id.setting:
                SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                Intent it = new Intent(this, SettingsActivity.class);
                startActivity(it);

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public  static class ConfirmDeleteDialog extends DialogFragment {

        @Override
        public Dialog onCreateDialog(@NonNull Bundle savedInstanceState) {

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            builder.setTitle("Are you sure you want to logout ?")
                    .setMessage("You will have to login again to buy books")
                    .setPositiveButton("Logout",
                            (dialog,id) -> {
                                FirebaseAuth.getInstance().signOut();
                                Intent intent =new Intent(getContext(),LoginActivity.class);
                                startActivity(intent);
                                getActivity().finish();

                            })
                    .setNegativeButton("Cancel",
                            (dialog, id) -> {});
            return builder.create();
        }
    }
}