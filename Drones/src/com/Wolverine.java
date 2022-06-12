package com;

public class Wolverine extends Drone{

    private int currWeight;

    public Wolverine(int droneId, int maxSpeed) {
        super(droneId, maxSpeed,"\u001B[31m");
        currWeight = 0;
    }

    public void setCurrWeight (int newCurrWeight) {
        this.currWeight = newCurrWeight;
    }

    public int getCurrWeight() {
        return currWeight;
    }

    public int getWeighedSpeed() {
        return (int)(maxSpeed - currWeight*0.3);
    }
}
