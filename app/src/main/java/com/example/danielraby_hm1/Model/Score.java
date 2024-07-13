package com.example.danielraby_hm1.Model;


public class Score implements Comparable<Score> {
    private int distance;
    private int Score;
    private double lat;
    private double lng;

    public Score() {
        distance = 0;
        Score = 0;
        lat = 32.115514949769846;
        lng = 34.8181039;

    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public void incrementDistance() {
        this.distance++;
    }

    public int getScore() {
        return Score;
    }

    public Score setScore(int score) {
        Score = score;
        return this;
    }
    public void incrementScore(int Score) {
        this.Score += Score;
    }

    @Override
    public int compareTo(Score o) {
        return o.getScore() - this.getScore();
    }
}
