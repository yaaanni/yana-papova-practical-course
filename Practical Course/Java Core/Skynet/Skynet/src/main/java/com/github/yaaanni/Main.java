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
        Wednesday wednesday = new Wednesday(days, phaser, factory);
        World world = new World(days, phaser, factory);
        factory.start();
        wednesday.start();
        world.start();
    }
}
