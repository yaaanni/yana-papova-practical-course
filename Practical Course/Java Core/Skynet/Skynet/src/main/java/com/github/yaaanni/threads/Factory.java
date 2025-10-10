package com.github.yaaanni.threads;

import com.github.yaaanni.details.TypeDetail;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Phaser;

public class Factory extends Thread {
    private final Map<TypeDetail, Integer> details;
    private final int days;
    private final Phaser phaser;

    public Factory(int days, Phaser phaser) {
        details = new ConcurrentHashMap<>();
        this.days = days;
        this.phaser = phaser;
    }

    public void run() {
        for (int i = 0; i < days; i++) {
            makeDetails();
            phaser.arriveAndAwaitAdvance();
            phaser.arriveAndAwaitAdvance();
        }
    }

    public Map<TypeDetail, Integer> getDetails() {
        return details;
    }

    public void makeDetails() {
        Random random = new Random();
        int randomNumber = random.nextInt(11);
        for (int i = 0; i < randomNumber; i++) {
            TypeDetail[] values = TypeDetail.values();
            TypeDetail randomDetail = values[random.nextInt(values.length)];
            details.put(randomDetail, details.getOrDefault(randomDetail, 0) + 1);
            System.out.println(randomDetail);
        }
    }


}
