package com.example.danielraby_hm1;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.VibrationEffect;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;


import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;

import android.os.Vibrator;


public class MainActivity extends AppCompatActivity {


    private static final int ENEMY = 1;
    private static final int REWARD = 2;
    private AppCompatImageView[][] trivia_IMG_dangers;
    private AppCompatImageView[] trivia_IMG_hearts;
    private MaterialTextView trivia_LBL_score;
    private MaterialTextView trivia_LBL_DistanceScore;
    private MaterialButton trivia_BTN_left;
    private MaterialButton trivia_BTN_right;
    private int[] playerLives;
    private MediaPlayer[] mediaPlayer;
    private boolean isPaused = false;
    int rows,cols;
    private GameManager gameManager;
    private final int DELAY = 1000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        findViews();
        rows = 6;
        cols = 5;
        gameManager = new GameManager(rows, cols);
        mediaPlayer = new MediaPlayer[]{ MediaPlayer.create(this, R.raw.hit_sound),
                MediaPlayer.create(this, R.raw.lose_sound),
                MediaPlayer.create(this, R.raw.move),
                MediaPlayer.create(this, R.raw.reward_sound)};

        initiateMatrix(rows,cols);
        setUpControls();

        handler.postDelayed(runnable, DELAY);



    }

    private void setUpControls() {
        trivia_BTN_left.setOnClickListener(v -> movePlayerLeft());
        trivia_BTN_right.setOnClickListener(v -> movePlayerRight());
    }

    private void movePlayerRight() { movePlayer(1);}

    private void movePlayerLeft() { movePlayer(-1);}


    @Override
    protected void onResume() {
        super.onResume();
        isPaused = false;

    }




    private void tick() {
        if (!isPaused) {
            gameManager.incrementScore();
            gameManager.MoveDangers();
            gameManager.makeNewDanger();
            int isHit = gameManager.playerHit();
            if (isHit == ENEMY) {
                mediaPlayer[0].start();
                updateLivesUI();
            }
            else if(isHit == REWARD) //TODO: 2 rewards in a row dont make sound
                mediaPlayer[3].start();
            updateScoresUI();
            updateDangersUI();
        }
    }

    private void updateScoresUI() {
        trivia_LBL_DistanceScore.setText(String.valueOf(gameManager.getDistance()));
        trivia_LBL_score.setText(String.valueOf(gameManager.getScore()));
    }


    @Override
    protected void onPause() {
        super.onPause();
        isPaused = true;
    }



    private void updateDangersUI() {
        for (int i = 0; i < trivia_IMG_dangers.length-1; i++) {
            for (int j = 0; j < trivia_IMG_dangers[0].length; j++) {
                if(gameManager.checkActive(i,j)) {
                    trivia_IMG_dangers[i][j].setImageResource(gameManager.getImage(i,j));
                    trivia_IMG_dangers[i][j].setVisibility(View.VISIBLE);
                }else
                    trivia_IMG_dangers[i][j].setVisibility(View.INVISIBLE);

            }

        }
    }

    private final Handler handler = new Handler();

    private final Runnable runnable = new Runnable() {
        public void run() {
            tick();
            handler.postDelayed(runnable, DELAY);
        }
    };


    private void movePlayer(int i) {
        gameManager.movePlayer(i);
        updatePositionUI(i);
    }

    private void updatePositionUI(int i) {
        int pos = gameManager.getPlayerPosition();
        mediaPlayer[2].start();
        trivia_IMG_dangers[trivia_IMG_dangers.length-1][pos - i].setVisibility(View.INVISIBLE);
        trivia_IMG_dangers[trivia_IMG_dangers.length-1][pos].setImageResource(playerLives[gameManager.getLives()-1]);
        trivia_IMG_dangers[trivia_IMG_dangers.length-1][pos].setVisibility(View.VISIBLE);

    }


    private void updateLivesUI() {
        int SZ = trivia_IMG_hearts.length;

        for (AppCompatImageView triviaImgHeart : trivia_IMG_hearts) {
            triviaImgHeart.setVisibility(View.VISIBLE);
        }

        for (int i = 0; i < SZ - gameManager.getLives(); i++) {
            trivia_IMG_hearts[SZ - i - 1].setVisibility(View.INVISIBLE);
        }
        if (gameManager.getLives() == 0) {
            lose();
        }
        trivia_IMG_dangers[trivia_IMG_dangers.length-1][gameManager.getPlayerPosition()].setImageResource(playerLives[gameManager.getLives()-1]);


    }
    private void lose() {
        Toast.makeText(this, "You lose \n Score: " + gameManager.getScore() +
                ", Distance: " + gameManager.getDistance(), Toast.LENGTH_SHORT).show();
        vibrate();
        mediaPlayer[1].start();
        Runnable v = () -> {gameManager.reset(); isPaused=false;};
        isPaused = true;
        handler.postDelayed(v,mediaPlayer[1].getDuration());
        gameManager.reset();
        updateDangersUI();
        updateScoresUI();
    }

    private void initiateMatrix(int rows, int cols){

        for (int i = 0; i < cols; i++) {
            trivia_IMG_dangers[trivia_IMG_dangers.length-1][i].setImageResource(playerLives[gameManager.getLives()-1]);
        }


        for (int i = 0; i <= rows; i++) {
            for (int j = 0; j < cols; j++) {
                trivia_IMG_dangers[i][j].setVisibility(View.INVISIBLE);
            }
        }
       trivia_IMG_dangers[rows][(cols%2)+1].setVisibility(View.VISIBLE);
    }

    private void findViews() {
        trivia_BTN_left = findViewById(R.id.trivia_BTN_left);
        trivia_BTN_right = findViewById(R.id.trivia_BTN_right);
        trivia_LBL_score = findViewById(R.id.trivia_LBL_Score);
        trivia_LBL_DistanceScore = findViewById(R.id.trivia_LBL_DistanceScore);

        trivia_IMG_hearts = new AppCompatImageView[] {
                findViewById(R.id.trivia_IMG_heart1),
                findViewById(R.id.trivia_IMG_heart2),
                findViewById(R.id.trivia_IMG_heart3),
        };


        trivia_IMG_dangers = new AppCompatImageView[][]{
                {findViewById(R.id.top1),findViewById(R.id.top2),findViewById(R.id.top3),findViewById(R.id.top4),findViewById(R.id.top5)},
                {findViewById(R.id.top6),findViewById(R.id.top7),findViewById(R.id.top8),findViewById(R.id.top9),findViewById(R.id.top10)},
                {findViewById(R.id.mid1),findViewById(R.id.mid2),findViewById(R.id.mid3),findViewById(R.id.mid4),findViewById(R.id.mid5)},
                {findViewById(R.id.mid6),findViewById(R.id.mid7),findViewById(R.id.mid8),findViewById(R.id.mid9),findViewById(R.id.mid10)},
                {findViewById(R.id.bot1),findViewById(R.id.bot2),findViewById(R.id.bot3),findViewById(R.id.bot4),findViewById(R.id.bot5)},
                {findViewById(R.id.bot6),findViewById(R.id.bot7),findViewById(R.id.bot8),findViewById(R.id.bot9),findViewById(R.id.bot10)},
                {findViewById(R.id.player1),findViewById(R.id.player2),findViewById(R.id.player3),findViewById(R.id.player4),findViewById(R.id.player5)}


        };
        playerLives = new int[]{R.drawable.player0_idle,R.drawable.player1_idle,R.drawable.player2_idle} ;

    }

    private void vibrate(){
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
// Vibrate for 500 milliseconds
        v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
    }

}