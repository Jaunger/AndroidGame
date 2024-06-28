package com.example.danielraby_hm1.Utilities;

import com.example.danielraby_hm1.Entities.Danger;
import com.example.danielraby_hm1.Entities.Player;
import com.example.danielraby_hm1.R;

import java.util.ArrayList;
import java.util.Random;


public class GameManager {

    private Danger[][] dangersMatrix;
    private ArrayList<Integer> enemies;
    private ArrayList<Integer> rewards;

    private ArrayList<Player> playerPos = new ArrayList<>();
    private int isHit;
    private boolean hasMoved;
    private int distance = 0;
    private int lives = 3;
    private int score = 0;

    public GameManager(int rows, int cols) {
        enemies = new ArrayList<>();
        enemies.add(R.drawable.ic_bluesnail);
        enemies.add(R.drawable.ic_orangemush);
        enemies.add(R.drawable.ic_slime);


        rewards  = new ArrayList<>();
        rewards.add(R.drawable.ic_icon);
        rewards.add(R.drawable.ic_roll);
        rewards.add(R.drawable.ic_sack);

        dangersMatrix = new Danger[rows][cols];
        for (int i = 0, active = (cols % 2) + 1; i < cols; i++) {
            playerPos.add(new Player().setImage(R.drawable.player0_idle).setActive(active == i));
        }
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {

                dangersMatrix[i][j] = new Danger()
                        .setImage(enemies.get(0)).setActive(false);
            }

        }
    }
    public int getDistance() { return distance;}


    public int getLives() {
        return lives;
    }

    public void setLives(int i) {lives = i;}

    public int getImage(int i, int j) {return dangersMatrix[i][j].getImage();}

   public void movePlayer(int i) {
        int cur = getPlayerPosition();
        if(cur+i > -1 && cur+i < dangersMatrix[0].length) {
            playerPos.get(cur).setActive(false);
            playerPos.get(cur + i).setActive(true);
            hasMoved = true;
            return;
        }
       hasMoved = false;
    }

    public int getPlayerPosition() {
        for (int i = 0; i < playerPos.size() ; i++){
            if (playerPos.get(i).isActive()){
                return i;
                }
        }
        return 1;
    }

    public void makeNewDanger() {
        Random randomColumnIndex = new Random();
        Random r = new Random();

        int check = r.nextInt(100);
        int newDanger = randomColumnIndex.nextInt(dangersMatrix[0].length);
        if(check < 70) {
            dangersMatrix[0][newDanger] = new Danger()
                    .setImage(enemies.get(r.nextInt(enemies.size()))).setActive(true);
        } else if (check < 85) {
            dangersMatrix[0][newDanger] = new Danger()
                    .setImage(rewards.get(0)).setActive(true);
        } else if (check < 95) {
            dangersMatrix[0][newDanger] = new Danger()
                    .setImage(rewards.get(1)).setActive(true);
        } else
            dangersMatrix[0][newDanger] = new Danger()
                    .setImage(rewards.get(2)).setActive(true);


    }

    public void MoveDangers() {
        isHit = 0;
        for (int i = dangersMatrix.length - 1; i > -1; i--) {
            for (int j = dangersMatrix[0].length - 1; j > -1; j--) {
                if (dangersMatrix[i][j].isActive()) {
                   if (i == dangersMatrix.length - 1) {
                       if (playerPos.get(j).isActive())
                           isHit = dangersMatrix[dangersMatrix.length - 1][j].getImage();
                   }
                  else {
                       dangersMatrix[i + 1][j].setActive(true);
                       dangersMatrix[i + 1][j].setImage(dangersMatrix[i][j].getImage());
                   }


                dangersMatrix[i][j].setActive(false);
                }
            }
        }
        distance++;
    }


    public int playerHit(){
        if (enemies.contains(isHit)){
            lives--;
            return 1;
        }
        else if(rewards.contains(isHit)){
            score += (rewards.indexOf(isHit)+1)*10;
            return 2;
        }
        return 0;
    }
    public boolean checkActive(int i, int j) {
        return dangersMatrix[i][j].isActive();
    }

    public void reset() {
        setLives(3);
        distance = 0;
        score = 0;
        for (int i = 0; i < dangersMatrix.length; i++) {
            for (int j = 0; j < dangersMatrix[0].length; j++) {
                dangersMatrix[i][j] = new Danger().setImage(R.drawable.img_bomb).setActive(false);
            }

        }
    }

    public void incrementScore() {
        score+= 10;
    }

    public int getScore() { return score;}


    public boolean getHasMoved() {
        return hasMoved;
    }

    public void setHasMoved(boolean hasMoved) {
        this.hasMoved = hasMoved;
    }


}
