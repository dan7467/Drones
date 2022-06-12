package com;

public abstract class Action {

    public int id, destX, destY;

    public Action(int id, int destX, int destY) {
        this.id = id;
        this.destX = destX;
        this.destY = destY;
    }

}
