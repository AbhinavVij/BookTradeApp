package com.example.booktradeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity {

    private SharedPref sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedPreferences = new SharedPref(this);

        //load theme preference
        if (sharedPreferences.loadNightModeState()) {
            setTheme(R.style.DarkTheme);
        } else {
            setTheme(R.style.LightTheme);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

    }

    public static class SettingsFragment extends PreferenceFragment {

        private SharedPref sharedPreferences;
        private CheckBoxPreference toggleTheme;

        private CheckBoxPreference toggle_sell;


        @Override
        public void onCreate(Bundle savedInstanceState){
            sharedPreferences = new SharedPref(getActivity().getApplicationContext());
            super.onCreate(savedInstanceState);
            // Load the preferences from an XML resource
            addPreferencesFromResource(R.xml.preferences);

            toggleTheme = (CheckBoxPreference) findPreference("dark");
            if (sharedPreferences.loadNightModeState()) {
                toggleTheme.isEnabled();
            }

            toggleTheme.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    boolean isChecked = (boolean) newValue;
                    if (isChecked) {
                        sharedPreferences.setNightModeState(true);
                    } else {
                        sharedPreferences.setNightModeState(false);
                    }
                    Toast.makeText(getActivity(),"Theme changed. Please Restart the App",Toast.LENGTH_LONG).show();
                    return true;
                }
            });

            toggle_sell = (CheckBoxPreference) findPreference("Seller_Page");
            if (sharedPreferences.loadDisplay()) {
                toggleTheme.isEnabled();
            }

            toggle_sell.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    boolean isChecked = (boolean) newValue;
                    if (isChecked) {
                        sharedPreferences.setDisplay(true);
                    } else {
                        sharedPreferences.setDisplay(false);
                    }
                   Toast.makeText(getActivity(),"Please Restart the App to apply the changes.",Toast.LENGTH_LONG).show();
                    return true;
                }
            });

            }

        }
        }



