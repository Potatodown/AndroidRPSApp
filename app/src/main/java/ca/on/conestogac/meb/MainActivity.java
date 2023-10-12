package ca.on.conestogac.meb;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.text.DecimalFormat;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private Button buttonRock;
    private Button buttonPaper;
    private Button buttonScissors;
    private TextView textViewDetails;
    private ImageView imageComputerResult;
    private ImageView imagePlayerResult;
    private SharedPreferences sharedPref;
    private Timer timerEnticement  = null;
    private Boolean creatingActivity;
    private Boolean saveState;
    private Boolean theme;
    int playerResult = 0;
    int computerResult = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final AppCompatActivity SELF = this;

        creatingActivity = true;
        View.OnClickListener rockPaperScissorsListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String result;
                if (R.id.buttonRock == view.getId()){
                    playerResult =1;
                }
                else if (R.id.buttonPaper == view.getId()){
                    playerResult =2;
                }
                else {
                    playerResult =3;
                }
                Random ran = new Random();
                computerResult = ran.nextInt(4);
                rockPaperScissorsDecision(playerResult, computerResult);

                if (playerResult == computerResult){
                    result = "It's a tie!";
                    ((MERPSApplication) getApplication()).addTie(1, Math.round(System.currentTimeMillis() /1000));
                }
                else if (computerResult == 3 && playerResult == 2 || computerResult == 2 && playerResult == 1 || computerResult == 1 && playerResult == 3){
                    result = "The computer Wins";
                    ((MERPSApplication) getApplication()).addLose(1, Math.round(System.currentTimeMillis() /1000));
                }
                else{
                    result = "You Win!";
                    ((MERPSApplication) getApplication()).addWin(1, Math.round(System.currentTimeMillis() /1000));
                }
                imageComputerResult.setVisibility(View.VISIBLE);
                imagePlayerResult.setVisibility(View.VISIBLE);
                imagePlayerResult.setAlpha(0f);
                imageComputerResult.setAlpha(0f);
                imagePlayerResult.animate().alpha(1f).setDuration(2000);
                String finalResult = result;
                imageComputerResult.animate().alpha(1f).setDuration(2300).setListener(
                        new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                textViewDetails.setText(finalResult);
                                performEnticement();
                            }
                        }
                );
            }
        };

        PreferenceManager.setDefaultValues(this, R.xml.settings, false);

        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

        theme = sharedPref.getBoolean("menu_theme",false);
        refreshTheme(theme);

        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);

        buttonRock = findViewById(R.id.buttonRock);
        buttonPaper = findViewById(R.id.buttonPaper);
        buttonScissors = findViewById(R.id.buttonScissors);
        textViewDetails = findViewById(R.id.textViewDetails);
        imageComputerResult = findViewById(R.id.imageComputerResult);
        imagePlayerResult = findViewById(R.id.imagePlayerResult);

        buttonRock.setOnClickListener(rockPaperScissorsListener);
        buttonPaper.setOnClickListener(rockPaperScissorsListener);
        buttonScissors.setOnClickListener(rockPaperScissorsListener);




    }

    private void refreshTheme(boolean theme){
        setTheme(R.style.Theme_MeRockPaperScissors);
        if (theme)
            setTheme(R.style.Theme_MeRockPaperScissorsNight);
    }

    private void rockPaperScissorsDecision(int playerResult, int computerResult){
        if (playerResult == 1)
            imagePlayerResult.setImageResource(R.drawable.rock);
        else if (playerResult == 2)
            imagePlayerResult.setImageResource(R.drawable.paper);
        else if (playerResult == 3){
            imagePlayerResult.setImageResource(R.drawable.scissors);
        }
        if (computerResult == 1)
            imageComputerResult.setImageResource(R.drawable.rock);
        else if (computerResult == 2)
            imageComputerResult.setImageResource(R.drawable.paper);
        else if (computerResult == 3){
            imageComputerResult.setImageResource(R.drawable.scissors);
        }
        if(!(playerResult == 0 && computerResult == 0)) {
            imagePlayerResult.setVisibility(View.VISIBLE);
            imageComputerResult.setVisibility(View.VISIBLE);
        }
    }

    private void performEnticement(){
        if(timerEnticement !=  null)
            timerEnticement.cancel();

        timerEnticement = new Timer(true);
        timerEnticement.schedule(new TimerTask() {
            @Override
            public void run() {
                timerEnticement.cancel();
                timerEnticement = null;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Keep playing! You know you want to!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }, 3000);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.rockpaperscissors_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        boolean ret = true;
        switch (item.getItemId()){
            case R.id.menu_settings:
                startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
                break;
            case R.id.menu_statistics:
                startActivity(new Intent(getApplicationContext(), StatsActivity.class));
                break;
            default:
                ret = super.onOptionsItemSelected(item);
                break;
        }
        return ret;
    }

    @Override
    protected void onPause() {
        Editor ed = sharedPref.edit();
        ed.putString("details", textViewDetails.getText().toString());
        ed.putInt("playerResult", playerResult);
        ed.putInt("computerResult", computerResult);
        ed.commit();
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        saveState = sharedPref.getBoolean("menu_save", false);
        if(saveState || !creatingActivity){
            textViewDetails.setText(sharedPref.getString("details", ""));
            playerResult = sharedPref.getInt("playerResult", 0);
            computerResult = sharedPref.getInt("computerResult", 0);
            rockPaperScissorsDecision(playerResult, computerResult);
        }
            refreshTheme(sharedPref.getBoolean("menu_theme",false));
    }

    @Override
    protected void onStop() {
        startService(new Intent(getApplicationContext(), AppNotificationService.class));
        super.onStop();
    }
}