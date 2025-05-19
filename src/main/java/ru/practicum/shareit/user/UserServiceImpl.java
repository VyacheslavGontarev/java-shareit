package ru.practicum.shareit.user;

import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;

import java.util.List;

@Service
@RequiredArgsConstructor
class UserServiceImpl implements UserService {
    private final UserRepository repository;

    @Override
    public List<UserDto> getAllUsers() {
        return repository.findAll();
    }

    @Override
    public UserDto getUserById(Long id) {
        return UserMapper.toUserDto(repository.findUser(id)
                .orElseThrow(() -> new ValidationException("Пользователь с таким id не найден")));
    }

    @Override
    public UserDto saveUser(UserDto userDto) {
        return repository.save(userDto);
    }

    @Override
    public UserDto updateUser(UserDto userDto) {
        return repository.update(userDto);
    }

    @Override
    public void deleteUser(Long id) {
        repository.delete(id);
    }
}