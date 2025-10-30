package com.example.demo;

import com.example.demo.entities.Card;
import com.example.demo.entities.User;
import com.example.demo.repository.JpaCardRepository;
import com.example.demo.repository.JpaUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import java.time.LocalDate;
import java.util.ArrayList;

@SpringBootApplication
public class UserServiceApplication {
    @Autowired
    private JpaUserRepository jpaUserRepository;
    @Autowired
    private JpaCardRepository jpaCardRepository;

    public static void main(String[] args) {

        SpringApplication.run(UserServiceApplication.class, args);

    }

    @EventListener(ApplicationReadyEvent.class)
    public void start() {
        Card cardForMasha = new Card();
        User nastya = new User();
        nastya.setId(7L);
        nastya.setName("Anastasia");
        nastya.setSurname("Krivchik");
        nastya.setBirthDay(LocalDate.of(2006, 03, 04));
        nastya.setEmail("nastya@gmail.com");
        jpaUserRepository.save(nastya);
        System.out.println("User saved with ID: " + nastya.getId());
        User masha = new User();
        masha.setName("Masha");
        masha.setSurname("Lisichkina");
        masha.setBirthDay(LocalDate.of(2007, 02, 02));
        masha.setEmail("masha@gmail.com");
        cardForMasha.setUser(masha);
        cardForMasha.setExpirationDate(LocalDate.of(2027, 9, 9));
        cardForMasha.setHolder("Masha Lisichkina");
        cardForMasha.setNumber(1111);
        masha.setCards(new ArrayList<>());
        masha.getCards().add(cardForMasha);
        jpaUserRepository.save(masha);
        System.out.println("User saved with ID: " + masha.getId());
        jpaUserRepository.deleteById(11L);
    }

}
