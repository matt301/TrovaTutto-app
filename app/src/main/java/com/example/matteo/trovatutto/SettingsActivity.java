package com.example.matteo.trovatutto;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.os.Handler;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;


import android.preference.SwitchPreference;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.Toast;

import com.pavelsikun.seekbarpreference.SeekBarPreference;

import java.util.prefs.Preferences;

public class SettingsActivity extends AppCompatPreferenceActivity{
    private static final String TAG = SettingsActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.home_drawer_setting);


        // load settings fragment
        getFragmentManager().beginTransaction().replace(android.R.id.content, new MainPreferenceFragment()).commit();
    }

    public static class MainPreferenceFragment extends PreferenceFragment {
        private SeekBarPreference radiusSeekbar;
        private CheckBoxPreference showAll;


        @Override
        public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);

            showAll = (CheckBoxPreference) findPreference("ch_radius_preference");
            radiusSeekbar = (SeekBarPreference) findPreference("seekbar_preference");
            if (showAll.isChecked()) {
                radiusSeekbar.setEnabled(false);
            }

            showAll.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {

                    if (showAll.isChecked()) {
                        radiusSeekbar.setEnabled(false);
                    }

                    else{
                        radiusSeekbar.setEnabled(true);
                    }

                    return true;
                }
            });



        }
    }


    private void goToHome() {

        Intent intent = new Intent(this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                goToHome();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }



    @Override
    public void onBackPressed() {
        Intent openHome = new Intent(SettingsActivity.this, HomeActivity.class);
        startActivity(openHome);
        this.finish();

    }




}