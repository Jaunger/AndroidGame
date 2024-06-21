package com.example.danielraby_hm1;

public class Player {
    private int image;
    private boolean active;





    public int getImage() {
        return image;
    }

    public Player setImage(int image) {
        this.image = image;
        return this;
    }

    public boolean isActive() {
        return active;
    }

    public Player setActive(boolean active) {
        this.active = active;
        return this;
    }
}
