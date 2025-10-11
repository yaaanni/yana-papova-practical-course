package com.github.yaaanni.threads;

import java.util.concurrent.Phaser;

public class World extends Faction {
    public World(Factory factory, int days, Phaser phaser) {
        super(factory, days, phaser);
        this.setName("World");
    }
}