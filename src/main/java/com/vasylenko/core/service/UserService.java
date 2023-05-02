package com.vasylenko.core.service;

import com.vasylenko.core.dto.UserDto;
import com.vasylenko.core.entity.User;

import java.util.List;

public interface UserService {
    void saveUser(UserDto userDto);
    UserDto updateUser(Long id, UserDto userDto);
    void deleteUserById(Long id);
    List<UserDto> getAllUsers();
    UserDto findUserByEmail(String email);
    UserDto findById(Long id);

}
