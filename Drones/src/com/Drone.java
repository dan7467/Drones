package com;

public class Drone {

    int droneId, xCoor, yCoor;
    int dir; // 0=Up, 1=Down, 2=Left, 3=Right, 4=Stop
    int maxSpeed;
    String color;
    Action currAction;

    public Drone(int droneId, int maxSpeed, String color){
        this.droneId = droneId;
        this.maxSpeed = maxSpeed;
        this.currAction = new TakeOff();
        this.color = color;
        this.currAction = new TakeOff();
        dir = 4;
    }

    public boolean outOfBounds() {
        return xCoor < 0 || yCoor < 0 || xCoor > 99 || yCoor > 99;
    }

    public void setGoTo(int xDest, int yDest){
        if (xCoor < xDest)
            dir = 3;
        else if (xCoor > xDest)
            dir = 2;
        else if (yCoor < yDest)
            dir = 1;
        else if (yCoor > yDest)
            dir = 0;
        else{
            dir = 4;
            currAction = new Land();
        }
    }

    public int[] getLocation() {
        int[] ret = {xCoor, yCoor};
        return ret;
    }

    public void setLocation(int xCoor, int yCoor) {
        this.xCoor = xCoor;
        this.yCoor = yCoor;
    }

    public void setAction(Action newAction) {
        if (newAction.id == 1) {
            if (this.currAction.id == 1) {
                // do nothing. we want to finish the current GoTo Action.
            }
            else {
                this.currAction = newAction;
            }
        }
        else {
            this.currAction = newAction;
        }
    }

    public Drone getDrone() {
        return this;
    }

}
