package com;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main {

    int mapSize = 100, droneNum = 0;
    public int[][] map = new int[mapSize][mapSize];
    ArrayList<Drone> drones;
    final String[] ANSI = {"\u001B[0m","\u001B[31m","\u001B[32m","\u001B[33m","\u001B[34m","\u001B[35m","\u001B[36m"};

    public void initMap() {
        drones = new ArrayList<Drone>();
        drones.add(new Drone(0,0,"\u001B[36m"));
        droneNum++;
        for (int i=0; i<map.length; i++){
            for (int j=0; j<map[i].length; j++){
                map[i][j] = 0;
            }
        }
    }

    public void printMap() {
        for (int i=0; i<map.length; i++){
            for (int j=0; j<map[i].length; j++){
                if (map[j][i] != 0)
                    System.out.print(drones.get(map[j][i]).color+map[j][i]+" "+ANSI[0]);
                else
                    System.out.print("0 ");
            }
            System.out.println("");
        }
    }

    public void addDrone(Drone d) {
        drones.add(d);
        droneNum++;
    }

    public void setLocation(int xCoor, int yCoor, int droneId) {
        Drone d = drones.get(droneId);
        map[d.xCoor][d.yCoor] = 0;
        d.setLocation(xCoor,yCoor);
        map[xCoor][yCoor] = droneId;
    }

    public void refreshMap() {
        for (Drone d: drones) {
            if (d.droneId == 0)
                continue;
            int dir = d.dir;
            if (d.currAction.id == 1) {
                d.setGoTo(d.currAction.destX,d.currAction.destY);
            }
            if (dir == 0) { // Advance drone - Up
                if (d.yCoor-d.maxSpeed == -1)
                    System.out.println("drone "+d.droneId+" has exited the boundaries.");
                else
                    setLocation(d.xCoor,d.yCoor-d.maxSpeed, d.droneId);
            }
            else if (dir == 1) { // Advance drone - Down
                if (d.yCoor+d.maxSpeed == 100)
                    System.out.println("drone "+d.droneId+" has exited the boundaries.");
                else
                    setLocation(d.xCoor,d.yCoor+d.maxSpeed, d.droneId);
            }
            else if (dir == 2) { // Advance drone - Left
                if (d.xCoor-d.maxSpeed == -1)
                    System.out.println("drone "+d.droneId+" has exited the boundaries.");
                else
                    setLocation(d.xCoor-d.maxSpeed,d.yCoor, d.droneId);
            }
            else if (dir == 3) { // Advance drone - Right
                if (d.xCoor+d.maxSpeed == 100)
                    System.out.println("drone "+d.droneId+" has exited the boundaries.");
                else
                    setLocation(d.xCoor+d.maxSpeed,d.yCoor, d.droneId);
            }
            else if (dir == 4){} // do nothing - drone is in Stop mode
            else {
                System.out.println("ERROR: no such direction");
            }
        }
    }

    public int getDroneCount() {return droneNum;}

    public void setGoTo(int droneId, int xDest, int yDest) {
        Drone d = drones.get(droneId);
    }

    public static void main(String[] args) {
        Main m = new Main();
        Runnable refreshMapRunnable = new Runnable() {
            public void run() {
                m.refreshMap();
                m.printMap();
            }
        };
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(refreshMapRunnable, 0, 1, TimeUnit.SECONDS);
        m.initMap();

        m.addDrone(new Drone(m.getDroneCount(), 1,"\u001B[36m")); // first-drone is for testing, does not appear
        m.addDrone(new Hunter(m.getDroneCount(), 1));
        m.addDrone(new Wolverine(m.getDroneCount(), 1));

        m.setLocation(7,95,1);
        m.setLocation(3,99,2);
        m.setLocation(5,97,3);
        m.drones.get(3).setAction(new GoTo(0,99));
    }
}
