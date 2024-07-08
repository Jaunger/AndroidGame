package com.example.danielraby_hm1.Utilities;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.danielraby_hm1.Model.Score;

import java.util.ArrayList;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class ScoreSharedPreferences {

    private static ScoreSharedPreferences scoreSharedPreferences;
    private final SharedPreferences prefs;


    private ScoreSharedPreferences(Context context) {
        prefs = context.getSharedPreferences("MyPreference", MODE_PRIVATE);
    }

    public static void init(Context context) {
        if (scoreSharedPreferences == null) {
            scoreSharedPreferences = new ScoreSharedPreferences(context);
        }
    }

    public static ScoreSharedPreferences getInstance(){
        return scoreSharedPreferences;
    }
    public void saveScore(ArrayList<Score> Scores) {
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String scoresAsJson = gson.toJson(Scores);
        editor.putString("scores", scoresAsJson);
        editor.apply();
    }
    public ArrayList<Score> readScore(){
        Gson gson = new Gson();
        String scoresAsJson = prefs.getString("scores", null);
        if (scoresAsJson == null || scoresAsJson.isEmpty()) {
            return new ArrayList<>();
        }

        return gson.fromJson(scoresAsJson, new TypeToken<ArrayList<Score>>() {}.getType());
    }




}
