package com.github.yaaanni.threads;

import com.github.yaaanni.details.TypeDetail;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Phaser;

public class Factory extends Thread {
    private final ConcurrentHashMap<TypeDetail, Integer> details;
    private final int days;
    private final Phaser phaser;

    public Factory(int days, Phaser phaser) {
        details = new ConcurrentHashMap<>();
        this.days = days;
        this.phaser = phaser;
        this.setName("Factory");
    }

    public void run() {
        for (int i = 0; i < days; i++) {
            System.out.println("Day starts");
            makeDetails();
            System.out.println("Day ends");
            phaser.arriveAndAwaitAdvance();
            phaser.arriveAndAwaitAdvance();
        }
    }

    public ConcurrentHashMap<TypeDetail, Integer> getDetails() {
        return details;
    }

    public void makeDetails() {
        Random random = new Random();
        int randomNumber = random.nextInt(11);
        for (int i = 0; i < randomNumber; i++) {
            TypeDetail[] values = TypeDetail.values();
            TypeDetail randomDetail = values[random.nextInt(values.length)];
            details.put(randomDetail, details.getOrDefault(randomDetail, 0) + 1);
        }
        System.out.println("All details at the factory: " + details);
    }

    public synchronized ConcurrentHashMap<TypeDetail, Integer> takeDetails(int amount, String name) {
        ConcurrentHashMap<TypeDetail, Integer> addMap = new ConcurrentHashMap<>();
        Random random = new Random();
        while (amount > 0) {
            List<TypeDetail> availableTypes = details.entrySet().stream()
                    .filter(entry -> entry.getValue() > 0)
                    .map(Map.Entry::getKey)
                    .toList();
            if (availableTypes.isEmpty()) break;
            TypeDetail randomType = availableTypes.get(random.nextInt(availableTypes.size()));
            int available = details.get(randomType);
            int take = Math.min(available, amount);
            details.put(randomType, available - take);
            addMap.put(randomType, addMap.getOrDefault(randomType, 0) + take);
            amount -= take;
        }
        System.out.println(name + " I am picking " + addMap);
        return addMap;
    }


}