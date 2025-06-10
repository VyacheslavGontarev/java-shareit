package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    @Qualifier("userRepository")
    private final UserRepository repository;

    @Override
    public List<UserDto> getAllUsers() {
        return repository.findAll()
                .stream().map(UserMapper::toUserDto)
                .toList();
    }

    @Override
    public UserDto getUserById(Long id) {
        User user =  repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Не удалось нйти пользователя с id:" + id));
        return UserMapper.toUserDto(user);
    }

    @Override
    public UserDto createUser(UserDto user) {
        User userEntity = UserMapper.toUser(user);
        return UserMapper.toUserDto(repository.save(userEntity));
    }

    @Override
    public UserDto updateUser(UserDto user) {
        User oldUser = repository.findById(user.getId())
                .orElseThrow(() -> new NotFoundException("Не удалось нйти пользователя с id:" + user.getId()));

        if (user.getEmail() != null && !oldUser.getEmail().equals(user.getEmail())) {
            log.trace("Изменение email пользователя");
            oldUser.setEmail(user.getEmail());
        }

        if (user.getName() != null && !oldUser.getName().equals(user.getName())) {
            log.trace("Изменение имя пользователя");
            oldUser.setName(user.getName());
        }

        return UserMapper.toUserDto(repository.save(oldUser));
    }

    @Override
    public void deleteUser(Long id) {
        User user = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Не удалось нйти пользователя с id:" + id));
        repository.delete(user);
    }
}