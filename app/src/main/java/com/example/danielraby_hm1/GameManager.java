package com.example.danielraby_hm1;

import java.util.ArrayList;
import java.util.Random;


public class GameManager {

    private Danger[][] dangersMatrix;
    private int[] images;
    private ArrayList<Player> playerPos = new ArrayList<>();
    private boolean isHit = false;
    private int lives = 3;

        //TODO: change matrix to 0's and 1's
    public GameManager(int rows, int cols) {
        images = new int[]{R.drawable.ic_bluesnail,R.drawable.ic_orangemush,R.drawable.ic_slime};

        dangersMatrix = new Danger[rows][cols];
        playerPos.add(new Player().setImage(R.drawable.player0_idle).setActive(false));
        playerPos.add(new Player().setImage(R.drawable.player0_idle).setActive(true));
        playerPos.add(new Player().setImage(R.drawable.player0_idle).setActive(false));
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {

                dangersMatrix[i][j] = new Danger()
                        .setImage(images[0]).setActive(false);
            }

        }
    }

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
        }
    }

    public int getPlayerPosition() {
        for (int i = 0; i < playerPos.size() ; i++){
            if (playerPos.get(i).isActive()){
                return i;
                }
        }
        return 1;
    }

    public int makeNewDanger() {
        Random randomColumnIndex = new Random();
        Random r = new Random();
        r.nextInt(images.length);
        int newDanger = randomColumnIndex.nextInt(dangersMatrix[0].length);
        dangersMatrix[0][newDanger] = new Danger()
                .setImage(images[r.nextInt(images.length)]).setActive(true);
        return newDanger;
    }

    public void MoveDangers() {
        for (int i = dangersMatrix.length - 1; i > -1; i--) {
            for (int j = dangersMatrix[0].length - 1; j > -1; j--) {
                if (dangersMatrix[i][j].isActive()) {
                   if (i == dangersMatrix.length - 1) isHit = playerPos.get(j).isActive();
                  else {
                       dangersMatrix[i + 1][j].setActive(true);
                       dangersMatrix[i + 1][j].setImage(dangersMatrix[i][j].getImage());
                   }


                dangersMatrix[i][j].setActive(false);
                }
            }
        }
    }


    public boolean playerHit(){
        if (isHit){
            lives--;
            isHit = false;
            return true;
        }
        return isHit;
    }
    public boolean checkActive(int i, int j) {
        return dangersMatrix[i][j].isActive();
    }

    public void reset() {
        setLives(3);
        for (int i = 0; i < dangersMatrix.length; i++) {
            for (int j = 0; j < dangersMatrix[0].length; j++) {
                dangersMatrix[i][j] = new Danger().setImage(R.drawable.img_bomb).setActive(false);
            }

        }
    }
}
