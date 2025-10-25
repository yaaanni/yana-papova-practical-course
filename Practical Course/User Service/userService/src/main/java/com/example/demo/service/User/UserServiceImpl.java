package com.example.demo.service.User;

import com.example.demo.dto.UserDto;
import com.example.demo.dto.mapping.CardMapping;
import com.example.demo.dto.mapping.UserMapping;
import com.example.demo.entities.Card;
import com.example.demo.entities.User;
import com.example.demo.repository.JpaCardRepository;
import com.example.demo.repository.JpaUserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final JpaCardRepository jpaCardRepository;
    private final JpaUserRepository jpaUserRepository;
    private final UserMapping userMapping;
    private final CardMapping cardMapping;

    @Override
    public UserDto createUser(UserDto dto) {
        User user = jpaUserRepository.save(userMapping.toEntity(dto));
        return userMapping.toDto(user);
    }

    @Override
    public UserDto getUserById(Long id) {
        User user = jpaUserRepository.getUserById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        return userMapping.toDto(user);
    }

    @Override
    public Page<UserDto> getAllUsers(Pageable pageable) {
        Page<User> users = jpaUserRepository.findAll(pageable);
        return users
                .map(user -> userMapping.toDto(user));
    }

    @Override
    public UserDto getUserByEmail(String email) {
        User user = jpaUserRepository.findUserByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        return userMapping.toDto(user);
    }

    @Override
    @Transactional
    public UserDto updateUserById(Long id, UserDto dto) {
        User user = jpaUserRepository.getUserById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        user.setName(dto.getName());
        user.setSurname(dto.getSurname());
        user.setBirthDay(dto.getBirthDay());
        user.setEmail(dto.getEmail());
        if (dto.getCards() != null) {
            List<Card> newCards = dto.getCards().
                    stream()
                    .map(cardDto -> {
                        Card card = cardMapping.toEntity(cardDto);
                        card.setUser(user);
                        return card;
                    })
                    .collect(Collectors.toList());
            user.getCards().clear();
            user.getCards().addAll(newCards);
        }
        return userMapping.toDto(user);
    }

    @Override
    @Transactional
    public void deleteUserById(Long id) {
        User user = jpaUserRepository.getUserById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        jpaUserRepository.deleteById(id);
    }
}
