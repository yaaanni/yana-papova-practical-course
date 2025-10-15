package com.github.yaaanni.beans;

import com.github.yaaanni.annotations.Component;
import com.github.yaaanni.annotations.Scope;

@Scope("prototype")
@Component
public class Food {
    private String food;

    public String getFood() {
        return food;
    }

    public void setFood(String food) {
        this.food = food;
    }

    @Override
    public String toString() {
        return "Food{" +
                "food='" + food + '\'' +
                '}';
    }
}
