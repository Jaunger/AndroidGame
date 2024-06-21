package com.example.danielraby_hm1;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.VibrationEffect;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;


import com.google.android.material.button.MaterialButton;

import android.os.Vibrator;

import java.util.Random;


public class MainActivity extends AppCompatActivity {


    private AppCompatImageView[][] trivia_IMG_dangers;
    private AppCompatImageView[] trivia_IMG_hearts;
    private MaterialButton trivia_BTN_left;
    private MaterialButton trivia_BTN_right;
    private int playerLives[];
    private MediaPlayer mediaPlayer;
    private MediaPlayer background;
    private Context context;
    private int position;

    private GameManager gameManager;
    private final int DELAY = 1000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        findViews();

        gameManager = new GameManager(4, 3);
        mediaPlayer = MediaPlayer.create(this, R.raw.hit_sound);
        initiateMatrix(4,3);
        setUpControlls();

        handler.postDelayed(runnable, DELAY);



    }

    private void setUpControlls() {
        trivia_BTN_left.setOnClickListener(v -> movePlayer(-1));
        trivia_BTN_right.setOnClickListener(v -> movePlayer(1));
    }
    //TODO: maybe add stop and start when paused and resumed

    @Override
    protected void onResume() {
        super.onResume();
    }




    private void tick() {
        gameManager.MoveDangers();
        position = gameManager.makeNewDanger();
        if(gameManager.playerHit())
            mediaPlayer.start();
        updateDangersUI();
        updateLivesUI();
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

    private Runnable runnable = new Runnable() {
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
        trivia_IMG_dangers[trivia_IMG_dangers.length-1][pos - i].setVisibility(View.INVISIBLE);
        trivia_IMG_dangers[trivia_IMG_dangers.length-1][pos].setImageResource(playerLives[gameManager.getLives()-1]);
        trivia_IMG_dangers[trivia_IMG_dangers.length-1][pos].setVisibility(View.VISIBLE);

    }


    private void updateLivesUI() {
        int SZ = trivia_IMG_hearts.length;

        for (int i = 0; i < SZ; i++) {
            trivia_IMG_hearts[i].setVisibility(View.VISIBLE);
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
        Toast.makeText(this, "You lose, lets try that again", Toast.LENGTH_SHORT).show();
        vibrate();
        gameManager.reset();
        updateDangersUI();
    }

    private void initiateMatrix(int rows, int cols){
        trivia_IMG_dangers[trivia_IMG_dangers.length-1][0].setImageResource(playerLives[gameManager.getLives()-1]);
        trivia_IMG_dangers[trivia_IMG_dangers.length-1][1].setImageResource(playerLives[gameManager.getLives()-1]);
        trivia_IMG_dangers[trivia_IMG_dangers.length-1][2].setImageResource(playerLives[gameManager.getLives()-1]);

        for (int i = 0; i <= rows; i++) {
            for (int j = 0; j < cols; j++) {
                trivia_IMG_dangers[i][j].setVisibility(View.INVISIBLE);
            }
        }
       trivia_IMG_dangers[rows][cols%2].setVisibility(View.VISIBLE);
    }

    private void findViews() {
        trivia_BTN_left = findViewById(R.id.trivia_BTN_left);
        trivia_BTN_right = findViewById(R.id.trivia_BTN_right);

        trivia_IMG_hearts = new AppCompatImageView[] {
                findViewById(R.id.trivia_IMG_heart1),
                findViewById(R.id.trivia_IMG_heart2),
                findViewById(R.id.trivia_IMG_heart3),
        };


        trivia_IMG_dangers = new AppCompatImageView[][]{
                {findViewById(R.id.top1),findViewById(R.id.top2),findViewById(R.id.top3)},
                {findViewById(R.id.mid1),findViewById(R.id.mid2),findViewById(R.id.mid3)},
                {findViewById(R.id.mid4),findViewById(R.id.mid5),findViewById(R.id.mid6)},
                {findViewById(R.id.bot1),findViewById(R.id.bot2),findViewById(R.id.bot3)},
                {findViewById(R.id.player1),findViewById(R.id.player2),findViewById(R.id.player3)}


        };
        playerLives = new int[]{R.drawable.player0_idle,R.drawable.player1_idle,R.drawable.player2_idle} ;

    }

    private void vibrate(){
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
// Vibrate for 500 milliseconds
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            //deprecated in API 26
            v.vibrate(500);
        }
    }

}