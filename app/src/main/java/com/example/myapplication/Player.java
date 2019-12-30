package com.example.myapplication;

public class Player {
    private String name;
    private int score;
    private float longitude, latitude;

    public void Player(String name, int score, float longitude, float latitude){
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


}
