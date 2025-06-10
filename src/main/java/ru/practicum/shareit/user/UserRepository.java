package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    List<UserDto> findAll();

    Optional<User> findUser(Long id);

    UserDto save(UserDto userDto);

    UserDto update(UserDto userDto);

    void delete(Long id);
}
