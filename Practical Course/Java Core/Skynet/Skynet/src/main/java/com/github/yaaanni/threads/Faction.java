package com.github.yaaanni.threads;

import com.github.yaaanni.details.RequiredDetails;
import com.github.yaaanni.details.TypeDetail;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Phaser;

public abstract class Faction extends Thread{

    protected final Factory factory;
    protected int amountOfRobots;
    protected final Map<TypeDetail, Integer> details;
    protected int days;
    protected final Phaser phaser;

    public Faction(Factory factory,  Phaser phaser,  int days) {
        this.factory = factory;
        this.phaser = phaser;
        this.days = days;
        details = new ConcurrentHashMap<>();
    }

    public void run() {
        for (int i = 0; i < days; i++) {
            phaser.arriveAndAwaitAdvance();
            takeParts();
            phaser.arriveAndAwaitAdvance();
            if (RequiredDetails.ifEnough(details)) {
                buildRobot(details);
            }
        }

    }

    public void takeParts() {
        Map<TypeDetail, Integer> factoryDetails = factory.getDetails();
        synchronized (factoryDetails) {
            Random random = new Random();
            int taken = 0;
            int randomNumber = random.nextInt(6);
            for (TypeDetail type : TypeDetail.values()) {
                while (factoryDetails.getOrDefault(type, 0) > 0 && taken < randomNumber) {
                    factoryDetails.put(type, factoryDetails.get(type) - 1);
                    putDetail(type, 1);
                    taken++;
                }
            }
        }
    }


    public void putDetail(TypeDetail detail, int amount) {
        details.put(detail, details.getOrDefault(detail, 0) + amount);
    }

    public void buildRobot(Map<TypeDetail, Integer> details) {
        while (RequiredDetails.ifEnough(details)) {
            for (Map.Entry<TypeDetail, Integer> entry : RequiredDetails.getRequiredDetails().entrySet()) {
                TypeDetail detail = entry.getKey();
                int requiredCount = entry.getValue();
                int availableCount = details.getOrDefault(detail, 0);
                details.put(detail, availableCount - requiredCount);

            }
            amountOfRobots++;
        }
        System.out.println(this.getName() + amountOfRobots);
    }

}
