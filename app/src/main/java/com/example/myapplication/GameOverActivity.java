package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class GameOverActivity extends AppCompatActivity {

    private Button playAgainBtn, menuBtn;
    private int score;
    private TextView gameScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);

        gameScore = findViewById(R.id.gameScore);
        playAgainBtn = findViewById(R.id.playAgainBtn);
        menuBtn = findViewById(R.id.menuBtn);


        savedInstanceState = getIntent().getExtras();
        if(savedInstanceState != null){
            score = savedInstanceState.getInt("score");
        }
        gameScore.setText(" " + score);

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
        Intent intent = new Intent(this, GameScreenActivity.class);
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
}
