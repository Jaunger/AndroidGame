package com.example.danielraby_hm1.Activities;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.VibrationEffect;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;


import com.example.danielraby_hm1.Interfaces.MoveCallback;
import com.example.danielraby_hm1.R;
import com.example.danielraby_hm1.Utilities.GameManager;
import com.example.danielraby_hm1.Utilities.MoveDetector;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;

import android.os.Vibrator;


public class GameActivity extends AppCompatActivity {


    private static final int ENEMY = 1;
    private static final int REWARD = 2;
    private static final int SENSORS = 1;
    private static final int BUTTONS = 2;
    private AppCompatImageView[][] trivia_IMG_dangers;
    private AppCompatImageView[] trivia_IMG_hearts;
    private MaterialTextView trivia_LBL_score;
    private MaterialTextView trivia_LBL_DistanceScore;

    private MaterialButton trivia_BTN_left;
    private MaterialButton trivia_BTN_right;
    private int[] playerLives;
    private MediaPlayer[] mediaPlayer;
    private boolean isPaused = false;
    private boolean sensorActive = false;
    int rows,cols;
    private GameManager gameManager;
    private MoveDetector moveDetector;
    private int DELAY;


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
                MediaPlayer.create(this, R.raw.reward_soundv2)};
        Intent prev = getIntent();
        DELAY = prev.getExtras().getInt("delay");
        initiateMatrix(rows,cols);
        setUpControls(prev.getExtras().getInt("type"));

        handler.postDelayed(runnable, DELAY);



    }

    private void setUpControls(int i) {

        if( i == BUTTONS) {
            trivia_BTN_left.setOnClickListener(v -> updatePositionUI(-1));
            trivia_BTN_right.setOnClickListener(v -> updatePositionUI(1));
        }

        if(i == SENSORS) {
            trivia_BTN_left.setVisibility(View.INVISIBLE);
            trivia_BTN_right.setVisibility(View.INVISIBLE);
            sensorActive = true;
            moveDetector = new MoveDetector(this,
                    new MoveCallback() {
                        @Override
                        public void moveLeft() {
                            updatePositionUI(-1);
                        }

                        @Override
                        public void moveRight() {
                            updatePositionUI(1);
                        }
                    });

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
       if(sensorActive) moveDetector.start();
        isPaused = false;

    }


    @Override
    protected void onPause() {
        super.onPause();
        if(sensorActive)moveDetector.stop();
        isPaused = true;
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


    private void updatePositionUI(int move) {
        gameManager.movePlayer(move);
        for (int i = 0; i < cols; i++) {
            trivia_IMG_dangers[rows][i].setVisibility(i == gameManager.getPlayerPosition() ? View.VISIBLE : View.INVISIBLE);
        }
        if(gameManager.getHasMoved())
            mediaPlayer[2].start();
        gameManager.setHasMoved(false);
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
        for (int i = 0; i < 5; i++) {
            trivia_IMG_dangers[trivia_IMG_dangers.length-1][i].setImageResource(playerLives[Math.max(gameManager.getLives() - 1, 0)]);

        }


    }
    private void lose() {
        Intent intent = new Intent(this, MainActivity.class);
        Toast.makeText(this, "You lose \n Score: " + gameManager.getScore() +
                ", Distance: " + gameManager.getDistance(), Toast.LENGTH_SHORT).show();
        vibrate();

        mediaPlayer[1].start();
        gameManager.updateScoreboard(); //TODO: add location
        startActivity(intent);
        /*
        Runnable v = () -> {gameManager.reset(); isPaused=false;};
        isPaused = true;
        handler.postDelayed(v,mediaPlayer[1].getDuration());
        gameManager.reset();
        updateDangersUI();
        updateScoresUI();*/
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