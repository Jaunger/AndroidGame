package com.example.danielraby_hm1.Entities;

public class Player {
    private int image;
    private boolean active;




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
