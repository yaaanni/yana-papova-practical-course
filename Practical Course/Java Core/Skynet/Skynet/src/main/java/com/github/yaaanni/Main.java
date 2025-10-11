package com.github.yaaanni;

import com.github.yaaanni.threads.Factory;
import com.github.yaaanni.threads.Wednesday;
import com.github.yaaanni.threads.World;

import java.util.concurrent.Phaser;

public class Main {
    public static void main(String[] args) {
        Phaser phaser = new Phaser(3);
        int days = 100;
        Factory factory = new Factory(days, phaser);
        Wednesday wednesday = new Wednesday(factory, days, phaser);
        World world = new World(factory, days, phaser);
        factory.start();
        wednesday.start();
        world.start();
        try {
            factory.join();
            world.join();
            wednesday.join();
        } catch (InterruptedException e) {
        }

        System.out.println("World built: " + world.getAmountOfRobots() + " robots.");
        System.out.println("Wednesday built: " + wednesday.getAmountOfRobots() + " robots.");

        if (world.getAmountOfRobots() > wednesday.getAmountOfRobots()) {
            System.out.println("Winner: World faction");
        } else if (world.getAmountOfRobots() < wednesday.getAmountOfRobots()) {
            System.out.println("Winner: Wednesday faction");
        } else {
            System.out.println("It's a draw");
        }
    }
}
