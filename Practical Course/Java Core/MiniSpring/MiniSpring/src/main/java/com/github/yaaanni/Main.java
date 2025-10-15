package com.github.yaaanni;

import com.github.yaaanni.beans.Animal;
import com.github.yaaanni.beans.Cat;
import com.github.yaaanni.beans.Food;
import com.github.yaaanni.beans.Person;
import com.github.yaaanni.context.MiniApplicationContext;

import java.lang.reflect.InvocationTargetException;

public class Main {

    public static void main(String[] args) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        MiniApplicationContext context = new MiniApplicationContext("com.github.yaaanni");
// Cat and food prototype, Cat implements InitializingBean
        Cat cat = context.getBean(Cat.class);
        cat.setCatName("Cat1");
        cat.getFood().setFood("Milk");
        Cat cat2 = context.getBean(Cat.class);
        cat2.setCatName("Cat2");
        cat2.getFood().setFood("Meat");
        cat.eat();
        cat2.eat();
// Only cat implement Animal -> MiniApplicationContext returns realization Cat
        Animal animal = context.getBean(Animal.class);
// Singleton Person
        Person person1 = context.getBean(Person.class);
        person1.setName("Masha");
        System.out.println(person1);
// Reuse person1
        Person person2 = context.getBean(Person.class);
        System.out.println(person2);
        System.out.println("person1 == person2: " + (person1 == person2)); // true
    }
}
