package com.example.myapplication;

import android.content.Intent;

public class Player implements Comparable{
    private String name;
    private int score;
    private float longitude, latitude;

    public Player(String name, int score, float longitude, float latitude){
        this.name = name;
        this.score = score;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    // Getters and setters
    public String getName(){
        return this.name;
    }

    public void setName(String name){
        this.name = name;
    }

    public int getScore(){
        return this.score;
    }

    public void setScore(int score){
        this.score = score;
    }

    public float getLongitude(){
        return this.longitude;
    }

    public void setLongitude(float longitude){
        this.longitude = longitude;
    }

    public float getLatitude(){
        return this.longitude;
    }

    public void setLatitude(float latitude){
        this.latitude = latitude;
    }


    @Override
    public int compareTo(Object o) {
        Player player = (Player) o;
        return Integer.compare(player.getScore(), this.getScore());
    }
}
