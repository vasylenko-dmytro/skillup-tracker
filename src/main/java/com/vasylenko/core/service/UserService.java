package com.vasylenko.core.service;

import com.vasylenko.core.dto.UserDto;
import com.vasylenko.core.entity.User;

import java.util.List;

public interface UserService {
    void saveUser(UserDto userDto);
    User findUserByEmail(String email);
    List<UserDto> findAllUsers();
}
