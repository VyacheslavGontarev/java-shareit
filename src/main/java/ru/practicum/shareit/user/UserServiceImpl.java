package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;

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
        return repository.findUser(id);
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