package com.example.danielraby_hm1.Model;

import java.util.List;

public class Score implements Comparable<Score> {
    private int distance;
    private int Score;
    private double lat;
    private double lng;

    public Score() {
        distance = 0;
        Score = 0;
    }

    public double getLng() {
        return lng;
    }

    public Score setLng(double lng) {
        this.lng = lng;
        return this;
    }

    public double getLat() {
        return lat;
    }

    public Score setLat(double lat) {
        this.lat = lat;
        return this;
    }

    public int getDistance() {
        return distance;
    }

    public Score setDistance(int distance) {
        this.distance = distance;
        return this;
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
