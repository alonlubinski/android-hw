package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;

public class GameOverActivity extends AppCompatActivity {

    private Button playAgainBtn, menuBtn;
    private int score, numOfLanes;
    private TextView gameScore;
    private boolean vib, tilt;
    private EditText nameEditText;
    private String name;
    private SharedPreferences sharedPreferences;
    private ArrayList<Player> highscoresList;
    private int PERMISSION_ID = 20;
    private FusedLocationProviderClient mFusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUIVisibility();
        setContentView(R.layout.activity_game_over);

        sharedPreferences = getSharedPreferences("shared_preferences", MODE_PRIVATE);
        highscoresList = new ArrayList<>();


        gameScore = findViewById(R.id.gameScore);
        playAgainBtn = findViewById(R.id.playAgainBtn);
        menuBtn = findViewById(R.id.menuBtn);
        nameEditText = findViewById(R.id.nameEditText);


        savedInstanceState = getIntent().getExtras();
        if (savedInstanceState != null) {
            score = savedInstanceState.getInt("score");
            numOfLanes = savedInstanceState.getInt("numOfLanes");
            vib = savedInstanceState.getBoolean("vib");
            tilt = savedInstanceState.getBoolean("tilt");
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
    public void startMainActivity() {
        name = nameEditText.getText().toString();
        saveHighscore();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    // Method that start the game activity.
    public void startGameScreenActivity() {
        name = nameEditText.getText().toString();
        saveHighscore();
        Intent intent;
        if (numOfLanes == 3) {
            intent = new Intent(this, ThreeLanesGameScreenActivity.class);
        } else {
            intent = new Intent(this, FiveLanesGameScreenActivity.class);
        }
        intent.putExtra("vib", vib);
        intent.putExtra("tilt", tilt);
        startActivity(intent);
        finish();
    }

    // Phone back key event.
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            startMainActivity();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void setUIVisibility() {
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
    }

    private void saveHighscore() {
        int highscore = score;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Player player = new Player("", 0, 0, 0);
        player.setName(name);
        player.setScore(highscore);

        String jsonString = sharedPreferences.getString("list", null);
        if (jsonString != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<Player>>() {}.getType();
            ArrayList<Player> listFromGson;
            listFromGson = gson.fromJson(jsonString, type);
            Collections.sort(listFromGson);
            if(listFromGson.size() == 10){
                if(listFromGson.get(9).getScore() < highscore){
                    listFromGson.remove(9);
                    listFromGson.add(player);
                }
            }
            else{
                listFromGson.add(player);
            }
            jsonString = gson.toJson(listFromGson);
            editor.putString("list", jsonString);
            editor.apply();
        } else {
            Gson gson = new Gson();
            highscoresList.add(player);
            jsonString = gson.toJson(highscoresList);
            editor.putString("list", jsonString);
            editor.apply();
        }

    }
/*
    public void getLastLocation(){
        if(checkPermission()){
            if(isLocationEnabled()){
                mFusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        Location location = task.getResult();
                        if(location == null){
                            requestNewLocationData();
                        }
                    }
                });
            } else {
                Toast.makeText(this, "Turn on location", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        } else {
            requestPermissions();
        }
    }

    public void requestNewLocationData(){
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(0);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
    }

    */
    @Override
    public void onResume() {
        super.onResume();
        setUIVisibility();
    }
}
