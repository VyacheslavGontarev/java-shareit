package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

interface UserRepository {
    List<UserDto> findAll();
    UserDto findUser(Long id);
    UserDto save(UserDto userDto);
    UserDto update(UserDto userDto);
    void delete(Long id);
}
