package ru.practicum.shareit.user;

import jakarta.validation.ValidationException;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class UserRepositoryImpl implements UserRepository {
    private final Map<Long, User> users = new HashMap<Long, User>();

    @Override
    public List<UserDto> findAll() {
        return users.values().stream()
                .map(user -> UserMapper.toUserDto(user))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<User> findUser(Long id) {
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public UserDto save(UserDto userDto) {
        User user = UserMapper.toUser(userDto);
        String email = user.getEmail();
        users.values().stream()
                .filter(user1 -> user1.getEmail().equals(email))
                .findFirst()
                .ifPresent(existingUser -> {
                    throw new ValidationException("Пользователь с таким email уже существует");
                });
        Long id = getId();
        user.setId(id);
        users.put(id,user);
        return UserMapper.toUserDto(user);
    }

    @Override
    public UserDto update(UserDto userDto) {
        User newUser = UserMapper.toUser(userDto);
        String email = newUser.getEmail();
        if (newUser.getId() == null) {
            throw new ValidationException("Id должен быть указан");
        }
        Long id = newUser.getId();
        User oldUser = findUser(id)
                .orElseThrow(() -> new ValidationException("Пользователь с таким id не найден"));
        users.remove(oldUser);
        if (!oldUser.getEmail().equals(email)) {
            users.values().stream()
                    .filter(user1 -> user1.getEmail().equals(email))
                    .findFirst()
                    .ifPresent(existingUser -> {
                        throw new ValidationException("Пользователь с таким email уже существует");
                    });
        }
        if (newUser.getName() != null) {
            oldUser.setName(newUser.getName());
        }
        if (newUser.getEmail() != null) {
            oldUser.setEmail(newUser.getEmail());
        }
        users.put(id,oldUser);
        return UserMapper.toUserDto(oldUser);
    }

    @Override
    public void delete(Long id) {
        User user = findUser(id)
                .orElseThrow(() -> new ValidationException("Пользователь с таким id не найден"));
        users.remove(user);
    }

    private long getId() {
        long lastId = users.keySet().stream()
                .max(Long::compareTo)
                .orElse(0L);
        return lastId + 1;
    }
}
