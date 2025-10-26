package com.example.demo;

import com.example.demo.dto.UserDto;
import com.example.demo.dto.mapping.UserMapping;
import com.example.demo.entities.Card;
import com.example.demo.entities.User;
import com.example.demo.repository.JpaCardRepository;
import com.example.demo.repository.JpaUserRepository;
import com.example.demo.service.User.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

@SpringBootApplication
public class UserServiceApplication {
    @Autowired
    private JpaUserRepository jpaUserRepository;
    @Autowired
    private JpaCardRepository jpaCardRepository;
    @Autowired
    private UserMapping userMapping;
    @Autowired
    private UserServiceImpl userService;

    public static void main(String[] args) {

        SpringApplication.run(UserServiceApplication.class, args);

    }

    @EventListener(ApplicationReadyEvent.class)
    public void start() {
//        User user = new User();
//        user.setName("Olya");
//        user.setSurname("Lisichkina");
//        user.setBirthDay(LocalDate.of(2007, 02, 02));
//        user.setEmail("masha@gmail.com");
//        Card cardForOlya = new Card();
//        cardForOlya.setUser(user);
//        cardForOlya.setExpirationDate(LocalDate.of(2027, 9, 9));
//        cardForOlya.setHolder("Masha Lisichkina");
//        cardForOlya.setNumber(1111);
//        user.setCards(new ArrayList<>());
//        user.getCards().add(cardForOlya);
//        UserDto dto = userMapping.toDto(user);
//        userService.updateUserById(12L, dto);


//        Card cardForMasha = new Card();
//        User nastya = new User();
//        nastya.setId(7L);
//        nastya.setName("Anastasia");
//        nastya.setSurname("Krivchik");
//        nastya.setBirthDay(LocalDate.of(2006, 03, 04));
//        nastya.setEmail("nastya@gmail.com");
//        jpaUserRepository.save(nastya);
//        System.out.println("User saved with ID: " + nastya.getId());
//        User masha = new User();
//        masha.setName("Masha");
//        masha.setSurname("Lisichkina");
//        masha.setBirthDay(LocalDate.of(2007, 02, 02));
//        masha.setEmail("masha@gmail.com");
//        Optional<User> masha = jpaUserRepository.getUserById(12L);
//
//        cardForMasha.setUser(masha.get());
//        cardForMasha.setExpirationDate(LocalDate.of(2027, 9, 9));
//        cardForMasha.setHolder("Masha Lis");
//        cardForMasha.setNumber(1111);
//        masha.get().setCards(new ArrayList<>());
//        masha.get().getCards().add(cardForMasha);
//        jpaUserRepository.save(masha.get());


//        jpaUserRepository.save(masha);
//        System.out.println("User saved with ID: " + masha.getId());
//        jpaUserRepository.deleteById(11L);
    }

}
