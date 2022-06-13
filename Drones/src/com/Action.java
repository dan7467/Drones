package com;

public abstract class Action {

    public int id, destX, destY;

    public Action(int id, int destX, int destY) {
        if (destX<0 || destY<0 || destX>99 || destY>99)
            System.out.println("Out of bounds. ");
        this.id = id;
        this.destX = destX;
        this.destY = destY;
    }

}
