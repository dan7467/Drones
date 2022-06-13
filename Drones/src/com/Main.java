package com;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main {

    int mapSize = 100, droneNum = 0; /* map size (can be modified) and drone counter */
    public int[][] map = new int[mapSize][mapSize]; /* representation of the current map */
    ArrayList<Drone> drones;
    final String[] ANSI = {"\u001B[0m","\u001B[31m","\u001B[32m","\u001B[33m","\u001B[34m","\u001B[35m","\u001B[36m"};/* text colors */

    public int getDroneCount() {return droneNum;}

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
        printStatus();
    }

    public void printStatus() {
        String s,sub,act,subact;
        for (int i=1; i<droneNum; i++) {
            Drone d = drones.get(i);
            s = d.getClass().toString();
            sub = s.substring(10,s.length());
            act = d.currAction.toString();
            subact = act.substring(4,act.indexOf('@'));
            System.out.print(d.droneId+". "+sub+": ("+d.xCoor+","+d.yCoor+") Action: "+subact+"\t\t\t");
        }
        System.out.println("");
    }

    public void addDrone(Drone d) {
        drones.add(d);
        droneNum++;
    }

    public boolean outOfBounds(int xCoor, int yCoor){
        return xCoor < 0 || yCoor < 0 || xCoor > 99 || yCoor > 99;
    }

    public void setLocation(int xCoor, int yCoor, int droneId) {
        Drone d = drones.get(droneId);
        if (outOfBounds(xCoor,yCoor)) {
            d.dir = 4;
            return;
        }
        else {
            map[d.xCoor][d.yCoor] = 0;
            d.setLocation(xCoor,yCoor);
            map[xCoor][yCoor] = droneId;
        }
    }

    public void refreshMap() {
        printMap();
        for (Drone d: drones) {
            if (d.droneId == 0)
                continue;
            int dir = d.dir;
            if (d.currAction.id == 1) {
                d.setGoTo(d.currAction.destX,d.currAction.destY);
            }
            switch (dir) {
                case 0:
                    setLocation(d.xCoor,d.yCoor-d.maxSpeed, d.droneId);
                    break;
                case 1:
                    setLocation(d.xCoor,d.yCoor+d.maxSpeed, d.droneId);
                    break;
                case 2:
                    setLocation(d.xCoor-d.maxSpeed,d.yCoor, d.droneId);
                    break;
                case 3:
                    setLocation(d.xCoor+d.maxSpeed,d.yCoor, d.droneId);
                    break;
                case 4:
                    break;
            }
        }
    }

    public static void main(String[] args) {
        // 1. Initiate Thread Scheduler, map, drones
        Main m = new Main();
        Runnable refreshMapRunnable = new Runnable() {
            public void run() {
                m.refreshMap();
            }
        };
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(refreshMapRunnable, 0, 1, TimeUnit.SECONDS);
        m.initMap();

        // 2. Add drones to map
        m.addDrone(new Drone(m.getDroneCount(), 1,"\u001B[36m")); // first-drone is for testing, does not appear
        m.addDrone(new Hunter(m.getDroneCount(), 1));
        m.addDrone(new Wolverine(m.getDroneCount(), 1));

        // 3. Set drones' location
        m.setLocation(7,95,1);
        m.setLocation(3,99,2);
        m.setLocation(5,97,3);

        // 4. Set a GoTo action for 2 different drones
        m.drones.get(3).setAction(new GoTo(18,99));
        m.drones.get(1).setAction(new GoTo(0,99));
    }
}
