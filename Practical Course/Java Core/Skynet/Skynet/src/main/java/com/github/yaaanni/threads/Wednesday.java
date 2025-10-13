package com.github.yaaanni.threads;

import java.util.concurrent.Phaser;

public class Wednesday extends Faction {

    public Wednesday(Factory factory, int days, Phaser phaser) {
        super(factory, days, phaser);
        this.setName("Wednesday");
    }
}