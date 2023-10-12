package ca.on.conestogac.meb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class StatsActivity extends AppCompatActivity {
    private SharedPreferences sharedPref;
    private Boolean theme;
    private TextView textAllTimeRecord;
    private TextView textLastMinRecord;
    private Button buttonResetStats;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        PreferenceManager.setDefaultValues(this, R.xml.settings, false);

        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

        theme = sharedPref.getBoolean("menu_theme",false);
        setTheme(R.style.Theme_MeRockPaperScissors);
        if (theme)
            setTheme(R.style.Theme_MeRockPaperScissorsNight);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);
        MERPSApplication application;
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        refreshStats();

        buttonResetStats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MERPSApplication) getApplication()).resetTableStats();
                refreshStats();
            }
        });
    }

    private void refreshStats(){

        MERPSApplication application;
        textLastMinRecord = findViewById(R.id.lastMinText);
        textAllTimeRecord = findViewById(R.id.allTimeRecordText);
        buttonResetStats = findViewById(R.id.resetbutton);

        application = (MERPSApplication) getApplication();
        ArrayList jeff = application.getTimes();
        textLastMinRecord.setText(""+ jeff.get(0) + "-"+jeff.get(1)+"-"+jeff.get(2));
        textAllTimeRecord.setText(""+application.getWins()
                +"-"+application.getLoses()
                +"-"+application.getTies());
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        boolean ret = true;
        switch (item.getItemId()){
            case android.R.id.home:
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                break;
            default:
                ret = super.onOptionsItemSelected(item);
                break;
        }
        return ret;
    }
}