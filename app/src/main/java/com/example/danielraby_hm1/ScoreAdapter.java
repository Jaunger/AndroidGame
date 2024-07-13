package com.example.danielraby_hm1;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.danielraby_hm1.Interfaces.ScoreCallBack;
import com.example.danielraby_hm1.Model.Score;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;


public class ScoreAdapter extends RecyclerView.Adapter<ScoreAdapter.ScoreViewHolder> {

    private final ArrayList<Score> scores;
    private ScoreCallBack scoreCallBack;

    public ScoreAdapter(ArrayList<Score> items) {scores = items;}

    public void setScoreCallBack(ScoreCallBack scoreCallBack){
        this.scoreCallBack = scoreCallBack;
    }

    @NonNull
    @Override
    public ScoreViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_score, parent, false);
        return new ScoreViewHolder(view);

    }

    @Override
    public void onBindViewHolder(final ScoreViewHolder holder, int position) {
        Score score = getItem(position);
        holder.score_LBL_score.setText(String.valueOf(score.getScore()));
        holder.score_LBL_distance.setText(String.valueOf(score.getDistance()));
        holder.score_img.setOnClickListener(v -> {
            if(scoreCallBack != null){
                scoreCallBack.onScoreClick(score);
            }
        });
    }

    @Override
    public int getItemCount() {return scores == null ? 0 : scores.size();}

    public Score getItem(int position) {return scores.get(position);}


    public static class ScoreViewHolder extends RecyclerView.ViewHolder {
        public final MaterialTextView score_LBL_score;
        public final MaterialTextView score_LBL_distance;
        public final MaterialButton score_img;

        public ScoreViewHolder(@NonNull View itemView) {
            super(itemView);
             score_LBL_score = itemView.findViewById(R.id.score_LBL_score);
             score_LBL_distance = itemView.findViewById(R.id.score_LBL_distance);
             score_img = itemView.findViewById(R.id.score_img);

        }


    }
}