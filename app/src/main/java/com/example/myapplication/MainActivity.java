package com.example.myapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {
    private Button startBtn;
    private ImageButton setBtn, aboutBtn, exitBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startBtn = findViewById(R.id.startButton);
        setBtn = findViewById(R.id.settingsButton);
        aboutBtn = findViewById(R.id.aboutButton);
        exitBtn = findViewById(R.id.exitButton);

        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startGameScreenActivity();
            }
        });
        setBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startOptionsActivity();
            }
        });
        aboutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                aboutPopupDialog();
            }
        });
        exitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exitPopupDialog();
            }
        });
    }

    // Method that start the game activity.
    public void startGameScreenActivity(){
        Intent intent = new Intent(this, GameScreenActivity.class);
        startActivity(intent);
        finish();
    }

    // Method that start the options activity.
    public void startOptionsActivity() {
        Intent intent = new Intent(this, OptionsActivity.class);
        startActivity(intent);
        finish();
    }

    // Method that start the about pop up.
    public void aboutPopupDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("About");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        LayoutInflater layoutInflater = getLayoutInflater();
        View popupView = layoutInflater.inflate(R.layout.popup_about, null);
        builder.setView(popupView);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    // Method that start the exit pop up.
    public void exitPopupDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Exit");
        builder.setNegativeButton("Exit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
                System.exit(0);
            }
        });
        builder.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        LayoutInflater layoutInflater = getLayoutInflater();
        View popupView = layoutInflater.inflate(R.layout.popup_exit, null);
        builder.setView(popupView);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    // Phone back key event.
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0){
                exitPopupDialog();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


}
