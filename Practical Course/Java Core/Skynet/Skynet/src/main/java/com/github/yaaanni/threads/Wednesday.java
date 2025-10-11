package com.github.yaaanni.threads;

import com.github.yaaanni.details.RequiredDetails;
import com.github.yaaanni.details.TypeDetail;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Phaser;

public class Wednesday extends Thread {

    public Wednesday(Factory factory, int days, Phaser phaser) {
        this.factory = factory;
        this.details = new ConcurrentHashMap<>();
        this.days = days;
        this.phaser = phaser;
        this.setName("Wednesday");
    }

    protected final Factory factory;
    protected int amountOfRobots;
    protected final ConcurrentHashMap<TypeDetail, Integer> details;
    protected final int days;
    protected final Phaser phaser;


    public void run() {
        for (int i = 0; i < days; i++) {
            phaser.arriveAndAwaitAdvance();
            System.out.println(this.getName() + " Night starts");
            takeParts();

            if (RequiredDetails.ifEnough(details)) {
                buildRobot(details);
            }
            System.out.println(this.getName() + " Night ends");
            phaser.arriveAndAwaitAdvance();
        }

    }

    public void takeParts() {
        Random random = new Random();
        int randomAmount = random.nextInt(6);
        ConcurrentHashMap<TypeDetail, Integer> taken = factory.takeDetails(randomAmount, this.getName());
        for (Map.Entry<TypeDetail, Integer> entry : taken.entrySet()) {
            TypeDetail type = entry.getKey();
            int amount = entry.getValue();
            details.put(type, details.getOrDefault(type, 0) + amount);
        }
    }


    public void buildRobot(ConcurrentHashMap<TypeDetail, Integer> details) {
        while (RequiredDetails.ifEnough(details)) {
            for (ConcurrentHashMap.Entry<TypeDetail, Integer> entry : RequiredDetails.getRequiredDetails().entrySet()) {
                TypeDetail detail = entry.getKey();
                int requiredCount = entry.getValue();
                int availableCount = details.getOrDefault(detail, 0);
                details.put(detail, availableCount - requiredCount);

            }
            amountOfRobots++;

            System.out.println(this.getName() + " make robot, all amount: " + amountOfRobots);
        }
    }

    public int getAmountOfRobots() {
        return amountOfRobots;
    }

}