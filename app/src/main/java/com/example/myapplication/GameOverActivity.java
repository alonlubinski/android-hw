package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;

public class GameOverActivity extends AppCompatActivity {

    private Button playAgainBtn, menuBtn;
    private int score, numOfLanes;
    private TextView gameScore;
    private boolean vib, tilt;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUIVisibility();
        setContentView(R.layout.activity_game_over);

        sharedPreferences = getSharedPreferences("shared_preferences", MODE_PRIVATE);

        gameScore = findViewById(R.id.gameScore);
        playAgainBtn = findViewById(R.id.playAgainBtn);
        menuBtn = findViewById(R.id.menuBtn);


        savedInstanceState = getIntent().getExtras();
        if(savedInstanceState != null){
            score = savedInstanceState.getInt("score");
            numOfLanes = savedInstanceState.getInt("numOfLanes");
            vib = savedInstanceState.getBoolean("vib");
            tilt = savedInstanceState.getBoolean("tilt");
        }
        gameScore.setText(" " + score);

        saveHighscore();

        playAgainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startGameScreenActivity();
            }
        });

        menuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startMainActivity();
            }
        });

    }

    // Method that start the main activity.
    public void startMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    // Method that start the game activity.
    public void startGameScreenActivity(){
        Intent intent;
        if(numOfLanes == 3){
            intent = new Intent(this, ThreeLanesGameScreenActivity.class);
        }else{
            intent = new Intent(this, FiveLanesGameScreenActivity.class);
        }
        intent.putExtra("vib", vib);
        intent.putExtra("tilt", tilt);
        startActivity(intent);
        finish();
    }

    // Phone back key event.
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0){
            startMainActivity();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void setUIVisibility(){
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
    }

    private void saveHighscore(){
        int highscore = score;
        String name;

        SharedPreferences.Editor editor = sharedPreferences.edit();
        Player player = new Player();
        player.setScore(highscore);
        //player.setName(name);

        String jsonString;
        Gson gson = new Gson();
        jsonString = gson.toJson(player);
        editor.putString("player", jsonString);
        editor.apply();
    }

    @Override
    public void onResume(){
        super.onResume();
        setUIVisibility();
    }
}
