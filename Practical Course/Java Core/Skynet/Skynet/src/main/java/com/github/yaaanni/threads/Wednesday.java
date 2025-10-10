package com.github.yaaanni.threads;

import java.util.concurrent.Phaser;

public class Wednesday extends Faction {


    public Wednesday(int days, Phaser phaser, Factory factory) {
        super(factory, phaser, days);
        setName("Wednesday ");
    }

}