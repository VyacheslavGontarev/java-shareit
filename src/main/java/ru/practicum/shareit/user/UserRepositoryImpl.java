package ru.practicum.shareit.user;

import jakarta.validation.ValidationException;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserRepositoryImpl implements UserRepository {
    private final List<User> users = new ArrayList<>();

    @Override
    public List<UserDto> findAll() {
        return users.stream()
                .map(user -> UserMapper.toUserDto(user))
                .collect(Collectors.toList());
    }

    @Override
    public UserDto findUser(Long id) {
        return UserMapper.toUserDto(users.stream()
                .filter(user1 -> user1.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new ValidationException("Пользователь с таким id не найден")));
    }

    @Override
    public UserDto save(UserDto userDto) {
        User user = UserMapper.toUser(userDto);
        String email = user.getEmail();
        users.stream()
                .filter(user1 -> user1.getEmail().equals(email))
                .findFirst()
                .ifPresent(existingUser -> {
                    throw new ValidationException("Пользователь с таким email уже существует");
                });
        user.setId(getId());
        users.add(user);
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
        User oldUser = UserMapper.toUser(findUser(id));
        users.remove(oldUser);
        if (!oldUser.getEmail().equals(email)) {
            users.stream()
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
        users.add(oldUser);
        return UserMapper.toUserDto(oldUser);
    }

    @Override
    public void delete(Long id) {
        User user = UserMapper.toUser(findUser(id));
        users.remove(user);
    }

    private long getId() {
        long lastId = users.stream()
                .mapToLong(User::getId)
                .max()
                .orElse(0);
        return lastId + 1;
    }
}
