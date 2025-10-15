package com.github.yaaanni.beans;

import com.github.yaaanni.annotations.Autowired;
import com.github.yaaanni.annotations.Scope;
import com.github.yaaanni.lifecycle.InitializingBean;

@Scope("prototype")
public class Cat implements Animal, InitializingBean {
    private String catName;
    @Autowired
    private Food food;

    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

    public void setFood(Food food) {
        this.food = food;
    }

    public Food getFood() {
        return food;
    }

    @Override
    public void eat() {
        System.out.println(this + " eats: " + food);
    }

    @Override
    public String toString() {
        return "Cat{" +
                "catName='" + catName + '\'' +
                ", food=" + food +
                '}';
    }

    @Override
    public void afterPropertiesSet() {
        System.out.println("afterPropertiesSet from " + this.catName);
    }
}
