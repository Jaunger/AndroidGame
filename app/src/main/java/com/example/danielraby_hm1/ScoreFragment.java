package com.example.danielraby_hm1;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.danielraby_hm1.Model.Score;
import com.example.danielraby_hm1.Interfaces.ScoreCallBack;

import com.example.danielraby_hm1.Utilities.ScoreSharedPreferences;

import java.util.ArrayList;

/**
 * A fragment representing a list of Items.
 */
public class ScoreFragment extends Fragment {

    private RecyclerView main_LST_scores;
    private ScoreCallBack scoreCallBack;
    ArrayList<Score> scores;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_score_list, container, false);

        main_LST_scores = view.findViewById(R.id.main_LST_scores);
        initViews();

        return view;
    }

    private void initViews() {
        ScoreSharedPreferences scoreSharedPreferences = ScoreSharedPreferences.getInstance();
        scores = scoreSharedPreferences.readScore();
        ScoreAdapter adapter = new ScoreAdapter(scores);
        adapter.setScoreCallBack((score)->{
            if(scoreCallBack != null) {
                scoreCallBack.onScoreClick(score);
            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        main_LST_scores.setLayoutManager(linearLayoutManager);
        main_LST_scores.setAdapter(adapter);
    }

    public void setScoreCallBack(ScoreCallBack scoreCallBack){
        this.scoreCallBack = scoreCallBack;
    }

    public ArrayList<Score> getScores() {
        return scores;
    }


}