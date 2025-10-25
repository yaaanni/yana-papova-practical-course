package com.example.demo.service.User;

import com.example.demo.dto.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
    UserDto createUser(UserDto dto);

    UserDto getUserById(Long id);

    Page<UserDto> getAllUsers(Pageable pageable);

    UserDto getUserByEmail(String email);

    UserDto updateUserById(Long id, UserDto dto);

    void deleteUserById(Long id);
}
