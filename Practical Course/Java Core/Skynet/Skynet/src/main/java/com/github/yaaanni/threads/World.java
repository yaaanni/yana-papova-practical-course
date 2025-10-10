package com.github.yaaanni.threads;

import java.util.concurrent.Phaser;

public class World extends Faction {
    public World(int days, Phaser phaser, Factory factory) {
        super(factory, phaser, days);
        setName("World ");
    }
}