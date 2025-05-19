package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

interface UserService {
    List<UserDto> getAllUsers();
    UserDto getUserById(Long id);
    UserDto saveUser(UserDto userDto);
    UserDto updateUser(UserDto userDto);
    void deleteUser(Long id);
}
